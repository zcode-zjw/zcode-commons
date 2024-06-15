package com.zcode.zjw.log.trace.domain.core.subject.assist;

import cn.hutool.core.io.FileUtil;
import com.zcode.zjw.common.utils.common.StringUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 源代码加载器
 *
 * @author zhangjiwei
 * @since 2023-02-22 19:49
 */
public class SourceCodeLoader {

    /**
     * 获取类中的所有方法（字符串）
     *
     * @param targetClass
     * @return
     * @throws Exception
     */
    public Map<String, String> getAllMethod(Class<?> targetClass) throws Exception {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath().replace("target/classes/", "src/main/java/");
        String classPath = path + (targetClass.getName().replace(".", "/")) + ".java";
        Method[] declaredMethods = targetClass.getDeclaredMethods();
        Map<String, String> res = new HashMap<>();
        for (Method declaredMethod : declaredMethods) {
            res.put(declaredMethod.getName(), getMethodCode(classPath, declaredMethod.getName()));
        }
        return res;
    }

    public String getMethodCode(String filePath, String methodName) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new NoSuchFileException("文件不存在，请检查你的路径或文件！（文件路径：" + filePath + "）");
        }
        if (file.isDirectory()) {
            throw new Exception("不是文件：" + filePath);
        }
        StringBuilder res = new StringBuilder();
        Map<String, Integer> kuoHaoMap = new HashMap<String, Integer>(2) {{
            put("{", 0);
            put("}", 0);
        }};
        boolean startFlag = false;
        // 读取源代码
        String fileEncoding = "UTF-8";
        List<String> sourceCodeList = FileUtil.readLines(file, fileEncoding);
        for (String currentPcCode : sourceCodeList) {
            // 处理方法开头行
            if (!StringUtil.contains(currentPcCode, "class")
                    && StringUtil.contains(currentPcCode, methodName)) {
                startFlag = true;
                storeData(res, kuoHaoMap, currentPcCode);
                continue;
            }
            // 处理方法剩余部分
            if (startFlag) {
                storeData(res, kuoHaoMap, currentPcCode);
                // 如果括号闭合，退出循环
                Object[] numArray = kuoHaoMap.values().stream().distinct().toArray();
                if (numArray.length == 1 && (int) numArray[0] != 0) {
                    break;
                }
            }
        }
        return res.toString();
    }

    private void storeData(StringBuilder res, Map<String, Integer> kuoHaoMap, String currentPcCode) {
        res.append(currentPcCode);
        for (String kuoHao : kuoHaoMap.keySet()) {
            if (StringUtil.contains(currentPcCode, kuoHao)) {
                kuoHaoMap.put(kuoHao, kuoHaoMap.get(kuoHao) + 1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SourceCodeLoader sourceCodeLoader = new SourceCodeLoader();
//        System.out.println(sourceCodeLoader.getMethodCode(
//                "/Users/mac/Desktop/items/ghdqsoamp-master/zcode-commons/common-log/log-trace/src/main/java/com/zcode/zjw/log/trace/assist/test/AppMonitorServiceImplTest.java",
//                "getAppStatus"));
        System.out.println(sourceCodeLoader.getAllMethod(SourceCodeLoader.class));
    }

}