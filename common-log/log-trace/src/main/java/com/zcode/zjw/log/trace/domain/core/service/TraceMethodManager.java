package com.zcode.zjw.log.trace.domain.core.service;

import com.zcode.zjw.log.trace.domain.core.entity.TraceMethodEntity;
import com.zcode.zjw.log.trace.domain.core.repository.TraceFileInfoRepository;
import com.zcode.zjw.log.trace.domain.core.repository.TraceFlowInfoRepository;
import com.zcode.zjw.log.trace.domain.core.repository.TraceLogInfoRepository;
import com.zcode.zjw.log.trace.domain.core.repository.po.TraceFileRecord;
import com.zcode.zjw.log.trace.domain.core.repository.po.TraceLogFlowInfo;
import com.zcode.zjw.log.trace.domain.core.subject.asm.*;
import com.zcode.zjw.log.trace.infrastructure.utils.FileUtils;
import com.zcode.zjw.web.common.Result;
import javassist.util.HotSwapper;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 方法追踪管理者
 *
 * @author zhangjiwei
 * @since 2023/8/3
 */
@Lazy
@Slf4j
@Service
public class TraceMethodManager {

    @Value("${trace.log.backupRootDir:/usr/local/trace/backup}")
    private String backupRootDir;

    private final List<String> moduleInnerUseClass = new ArrayList<String>() {{
        add("com/zcode/zjw/log/trace/infrastructure/utils/ParameterUtils");
    }};
    @Value("${trace.log.monitor.scan:com.zcode.zjw}")
    private String scanPackage;

    @Value("${trace.log.hotSwapper.port:8000}")
    private int hotSwapperPort;

    private HotSwapper swap;

    private final TraceLogInfoRepository traceLogInfoRepository;

    private final TraceFlowInfoRepository traceFlowInfoRepository;

    private final TraceFileInfoRepository traceFileInfoRepository;

    public TraceMethodManager(TraceLogInfoRepository traceLogInfoRepository, TraceFlowInfoRepository traceFlowInfoRepository, TraceFileInfoRepository traceFileInfoRepository) {
        this.traceLogInfoRepository = traceLogInfoRepository;
        this.traceFlowInfoRepository = traceFlowInfoRepository;
        this.traceFileInfoRepository = traceFileInfoRepository;
    }


    /**
     * 判断方法是否已经添加过追踪
     *
     * @param classPath  相对包的类路径
     * @param methodName 方法名称
     * @return 是/否
     */
    private boolean isRepeat(String classPath, String methodName) {
        classPath += ".class";
        String sourceFilePath = FileUtils.getFilePath(classPath);

        // 先查询该类，是否已经添加过追踪，避免重复添加
        List<TraceFileRecord> traceFileRecords = traceFileInfoRepository.findTracingFileRecord(sourceFilePath);
        for (TraceFileRecord traceFileRecord : traceFileRecords) {
            if (traceFileRecord.getSourcePath().equals(sourceFilePath) && traceFileRecord.getMethodName().equals(methodName)) { // 如果当前需要追踪的对象已经在追踪
                return true;
            }
        }

        return false;
    }

    /**
     * 为方法添加追踪
     *
     * @param classPath 类路径
     * @param flowId    流程ID
     */
    private String addTrace(String classPath, String flowId, String methodName) {
        String sourceClassPath = String.valueOf(classPath);
        // 先查询该类，是否已经添加过追踪，避免重复添加
        if (isRepeat(classPath, methodName)) {
            return "该方法已经在追踪：" + classPath + "@" + methodName;
        }
        String res = null;

        classPath += ".class";
        boolean backupOkFlag = false;
        String sourceFilePath = FileUtils.getFilePath(classPath);
        byte[] sourceBytes = FileUtils.readBytes(sourceFilePath);
        ClassReader cr = new ClassReader(sourceBytes);

        // 先备份class文件
        String[] fullClassNameArray = cr.getClassName().split(File.separator + "");
        String className = fullClassNameArray[fullClassNameArray.length - 1];
        String backupDir = backupRootDir + File.separator + flowId + File.separator;
        String backupFilePath = backupDir + className + ".bak";
        try (FileInputStream inputStream = new FileInputStream(sourceFilePath)) {
            File targetDirectory = new File(backupDir);
            if (!targetDirectory.exists()) {
                if (!targetDirectory.mkdirs()) { // 创建目标目录
                    res = "备份目录生成失败：" + sourceClassPath + "@" + methodName;
                    throw new Exception("备份目录生成失败！");
                }
            }

            // 记录到数据库
            if (traceFileInfoRepository.insert(flowId, sourceFilePath, backupFilePath,
                    sourceClassPath, methodName, 1) < 1) {
                res = "记录数据库失败（备份文件）：" + sourceClassPath + "@" + methodName;
                throw new Exception("记录数据库失败（备份文件）");
            }

            File backFile = new File(backupFilePath);
            if (!backFile.createNewFile() && !backFile.exists()) {
                res = "备份文件生成失败：" + sourceClassPath + "@" + methodName;
                throw new Exception("备份文件生成失败：" + backupFilePath);
            }

            try (FileOutputStream outputStream = new FileOutputStream(backFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                backupOkFlag = true;
            }

            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            int api = Opcodes.ASM9;
            MethodLocalVariableCollector methodLocalVariableCollector = new MethodLocalVariableCollector();
            ClassVisitor cv = new MethodParameterVisitor(api, cw, methodLocalVariableCollector.collect(cr), flowId, methodName);

            int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
            cr.accept(cv, parsingOptions);

            byte[] targetBytes = cw.toByteArray();

            FileUtils.writeBytes(sourceFilePath, targetBytes);

            res = "方法添加成功：" + sourceClassPath + "@" + methodName;
        } catch (Exception e) {
            if (backupOkFlag) {
                log.info("开始回退原始文件");
                backup(flowId);
            } else {
                log.error("文件备份失败");
            }
            log.error("添加追踪失败", e);
        }

        return res;
    }

    /**
     * 回退
     *
     * @param flowId 流程ID
     * @return 是否成功
     */
    public boolean backup(String flowId) {
        boolean res = true;
        // 获取源文件路径
        List<TraceFileRecord> traceFileRecords = traceFileInfoRepository.findRecordByFlowId(flowId);
        if (traceFileRecords == null || traceFileRecords.isEmpty()) {
            throw new RuntimeException("找不到追踪文件记录（flowId：" + flowId + "）");
        }
        for (TraceFileRecord fileRecord : traceFileRecords) {
            if (fileRecord.getStatus() == 0) { // 如果已经回退过，就不要再次回退
                continue;
            }
            String sourceFilePath = fileRecord.getSourcePath();
            String backupFilePath = fileRecord.getBackupPath();
            File file = new File(backupFilePath);
            try (FileOutputStream outputStream = new FileOutputStream(sourceFilePath);
                 FileInputStream inputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                traceFileInfoRepository.updateStatus(sourceFilePath, backupFilePath);
                hotLoad(fileRecord.getClassPath()); // 重载
                log.info("回退成功：{}", sourceFilePath);
            } catch (Exception ex) {
                log.error("回退失败: {}", sourceFilePath, ex);
                res = false;
            }
        }

        return res;
    }

    /**
     * 开始追踪
     *
     * @param traceMethodEntity 追踪请求对象（请求参数）
     * @return 追踪结果
     */
    public List<String> startTrace(TraceMethodEntity traceMethodEntity) throws Exception {
        String classPath = traceMethodEntity.getClasspath();
        String methodName = traceMethodEntity.getMethodName();
        String basePackage = traceMethodEntity.getBasePackage();
        List<String> excludes = traceMethodEntity.getExcludeMethod();
        List<String> addResList = new ArrayList<>();

        if (isRepeat(classPath, methodName) && judgeExcludeMethod(excludes, classPath, methodName)) {
            addResList.add("该方法已经在追踪：" + classPath + "@" + methodName);
            return addResList;
        }

        // 获取方法内的所有调用方法
        MethodCallCollector methodCallCollector = new MethodCallCollector();
        List<AsmMethodPojo> callMethodList = methodCallCollector.collect(classPath, methodName, basePackage);
        // 获取方法的所有被调用方法
        MethodCalledCollector methodCalledCollector = new MethodCalledCollector();
        List<AsmMethodPojo> calledMethodList = methodCalledCollector.collect(classPath, methodName, basePackage);

        String initMethod = classPath.split("/")[classPath.split("/").length - 1];
        String flowId = getFlowId(initMethod);
        addResList.add(addTrace(classPath, flowId, methodName)); // 初始方法添加追踪
        hotLoad(classPath);

        // 如果需要查看方法内调用的方法链路
        Boolean excludeCallMethods = Optional.ofNullable(traceMethodEntity.getExcludeCallMethods()).orElse(false);
        if (excludeCallMethods) {
            // 为每一个调用的方法都添加追踪
            for (AsmMethodPojo asmMethodPojo : callMethodList) {
                // 排除模块内置方法（如：ParameterUtils）
                if (moduleInnerUseClass.contains(asmMethodPojo.getOwner())) {
                    continue;
                }
                addResList.add(addTrace(asmMethodPojo.getOwner(), flowId, asmMethodPojo.getName()));
                hotLoad(asmMethodPojo.getOwner()); // 重载
            }
        }

        // 如果需要查看方法内调用的方法链路
        Boolean excludeCalledMethods = Optional.ofNullable(traceMethodEntity.getExcludeCalledMethods()).orElse(false);
        if (excludeCalledMethods) {
            //moduleInnerUseClass.add(classPath); // 将初始方法排除，避免出现无限递归
            // 为每一个调用的方法都添加追踪
            for (AsmMethodPojo asmMethodPojo : calledMethodList) {
                // 排除模块内置方法（如：ParameterUtils）
                if (moduleInnerUseClass.contains(asmMethodPojo.getOwner())) {
                    continue;
                }
                addResList.add(addTrace(asmMethodPojo.getOwner(), flowId, asmMethodPojo.getName()));
                hotLoad(asmMethodPojo.getOwner()); // 重载
            }
        }

        return addResList;
    }

    /**
     * 重载
     *
     * @param classPath 类路径（相对项目）
     * @throws Exception
     */
    public void hotLoad(String classPath) throws Exception {
        String classify = classPath.replaceAll("\\/", ".");
        if (swap == null) {
            swap = new HotSwapper(hotSwapperPort);
        }

        String projectClasspath = TraceMethodManager.class.getResource("/").getPath();
        String sourcePath = projectClasspath + classPath + ".class";
        Class.forName(classify);

        byte[] bytecode = Files.readAllBytes(Paths.get(sourcePath));
        swap.reload(classify, bytecode);
        log.info("已重载：{}", classify);
    }

    private String getFlowId(String initMethod) {
        try {
            String flowId = UUID.randomUUID().toString();
            // 记录到数据库
            TraceLogFlowInfo traceLogFlowInfo = new TraceLogFlowInfo();
            traceLogFlowInfo.setFlowId(flowId);
            traceLogFlowInfo.setStatus(1);
            traceLogFlowInfo.setStartMethod(initMethod);
            return traceFlowInfoRepository.getTraceLogInfoFlowMapper().insert(traceLogFlowInfo) > 0 ? flowId : null;
        } catch (Exception e) {
            log.error("生成流程ID或记录数据库失败", e);
            return null;
        }
    }

    /**
     * 获取指定方法调用的所有方法
     * @param traceMethodEntity 追踪方法实体
     * @return
     */
    public Result<Object> getCallMethod(TraceMethodEntity traceMethodEntity) {
        try {
            MethodCallCollector methodCallCollector = new MethodCallCollector();
            List<AsmMethodPojo> callMethodList = methodCallCollector.collect(traceMethodEntity.getClasspath(),
                    traceMethodEntity.getMethodName(), traceMethodEntity.getBasePackage());
            return Result.success(callMethodList.stream().map(obj -> {
                Map<String, String> tmpList = new HashMap<>();
                tmpList.put("classpath", obj.getOwner());
                tmpList.put("methodName", obj.getName());
                return tmpList;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("获取调用方法列表失败", e);
            return Result.error("获取调用方法列表失败");
        }
    }

    /**
     * 获取指定方法被调用的所有方法
     * @param traceMethodEntity 追踪方法实体
     * @return
     */
    public Result<Object> getCalledMethod(TraceMethodEntity traceMethodEntity) {
        try {
            MethodCalledCollector methodCalledCollector = new MethodCalledCollector();
            List<AsmMethodPojo> callMethodList = methodCalledCollector.collect(traceMethodEntity.getClasspath(),
                    traceMethodEntity.getMethodName(), traceMethodEntity.getBasePackage());
            return Result.success(callMethodList.stream().map(obj -> {
                Map<String, String> tmpList = new HashMap<>();
                tmpList.put("classpath", obj.getOwner());
                tmpList.put("methodName", obj.getName());
                return tmpList;
            }).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("获取被调用方法列表失败", e);
            return Result.error("获取被调用方法列表失败");
        }
    }

    /**
     * 判断是否排除追踪方法
     *
     * @param excludes   排除名单
     * @param classpath  相对包类路径
     * @param methodName 方法名称
     * @return 是/否
     */
    private boolean judgeExcludeMethod(List<String> excludes, String classpath, String methodName) {
        if (excludes == null) {
            return false;
        }
        if (excludes.isEmpty()) {
            return false;
        }
        return excludes.contains(classpath + "@" + methodName);
    }

}