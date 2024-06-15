package com.zcode.zjw.log.trace.domain.core.subject.asm;

import com.zcode.zjw.common.utils.file.JavaFileUtil;
import com.zcode.zjw.log.trace.infrastructure.utils.FileUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.objectweb.asm.Opcodes.ASM9;

/**
 * 方法被调用收集者
 *
 * @author zhangjiwei
 * @since 2023/8/3
 */
@Slf4j
public class MethodCalledCollector {

    private void findMethod(String targetClassIfy, String methodDesc, String targetMethod, String basePackagePath, List<AsmMethodPojo> res) {
        String filePath;
        byte[] sourceBytes;
        ClassReader classReader;
        List<AsmMethodPojo> dp = new ArrayList<>();
        List<JavaFileInfo> javaFileInfoList = getJavaFileInfoList(basePackagePath);
        for (JavaFileInfo javaFileInfo : javaFileInfoList) {
            String owner = javaFileInfo.getPackagePath()
                    .replaceAll("\\.", "/") + File.separator + javaFileInfo.getClassName();
            filePath = FileUtils.getFilePath(owner + ".class");
            sourceBytes = FileUtils.readBytes(filePath);
            classReader = new ClassReader(sourceBytes);
            ClassVisitor cv = new MethodFindRefVisitor(ASM9, null, owner, targetClassIfy, targetMethod,
                    methodDesc, dp);
            int option = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
            classReader.accept(cv, option);
        }
        res.addAll(dp);
    }

    public List<AsmMethodPojo> collect(String classPath, String methodName, String basePackage) throws Exception {
        Class<?> clazz = Class.forName(classPath.replaceAll("/", "."));
        Method method = null;
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if (declaredMethod.getName().equals(methodName)) {
                method = declaredMethod;
            }
        }
        StringBuilder descriptorBuilder = new StringBuilder();
        descriptorBuilder.append("(");

        if (method == null) {
            throw new Exception("找不到指定的方法（" + classPath + File.separator + methodName + "）");
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> paramType : parameterTypes) {
            descriptorBuilder.append(getTypeDescriptor(paramType));
        }

        descriptorBuilder.append(")");
        descriptorBuilder.append(getTypeDescriptor(method.getReturnType()));

        String methodDescriptor = descriptorBuilder.toString();
        List<AsmMethodPojo> dp = new ArrayList<>();
        findMethod(classPath, methodDescriptor, methodName, basePackage, dp);
        return dp.stream().distinct().collect(Collectors.toList());
    }

    private List<JavaFileInfo> getJavaFileInfoList(String basePackage) {
        List<JavaFileInfo> res = new ArrayList<>();
        List<File> javaFiles = JavaFileUtil.getJavaFilesInPackage(basePackage);

        for (File javaFile : javaFiles) {
            String packagePath = JavaFileUtil.getPackagePath(javaFile);
            List<String> methodNames = JavaFileUtil.getMethodNames(javaFile);
            JavaFileInfo javaFileInfo = new JavaFileInfo();
            javaFileInfo.setPackagePath(packagePath);
            javaFileInfo.setMethods(methodNames);
            javaFileInfo.setClassName(javaFile.getName().replaceAll(".java", ""));
            javaFileInfo.setClassAbsPath(javaFile.getPath());
            res.add(javaFileInfo);
        }

        return res;
    }

    @Data
    static class JavaFileInfo {
        private String packagePath;

        private List<String> methods;

        private String className;

        private String classAbsPath;
    }

    private static String getTypeDescriptor(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == int.class) {
                return "I";
            } else if (type == double.class) {
                return "D";
            } // 添加其他基本数据类型的判断
            else {
                return null;
            }
        } else if (type.isArray()) {
            return "[" + getTypeDescriptor(type.getComponentType());
        } else {
            String className = type.getName();
            return "L" + className.replace(".", "/") + ";";
        }
    }


    public static void main(String[] args) throws Exception {
        //String classPath = "com/zcode/zjw/log/trace/test/TestClass.class";
        //String filePath = FileUtils.getFilePath(classPath);
        //byte[] sourceBytes = FileUtils.readBytes(filePath);
        ////（1）构建ClassReader
        //ClassReader cr = new ClassReader(sourceBytes);
        //List<AsmMethodPojo> dp = new ArrayList<>();
        //ClassVisitor cv = new MethodFindRefVisitor(ASM9, null, "testMethod",
        //        "(I)Ljava/lang/String;", "com/zcode/zjw/log/trace", dp);
        //int option = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        //cr.accept(cv, option);

        //singleCollect(cr, "com/zcode/zjw/log/trace", "(I)Ljava/lang/String;", "testMethod", dp);
        //List<AsmMethodPojo> dp = new ArrayList<>();
        //findMethod("com/zcode/zjw/log/trace/core/TestClass", "(I)Ljava/lang/String;", "testMethod",
        //        "com/zcode/zjw/log/trace", dp);
        //System.out.println(dp.stream().distinct().collect(Collectors.toList()));

        //System.out.println(collect("com/zcode/zjw/log/trace/test/TestClass",
        //        "testMethod", "com/zcode/zjw/log/trace"));

    }

}
