package com.zcode.zjw.common.utils.file;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java文件工具类
 *
 * @author zhangjiwei
 * @since 2023/8/8
 */
public class JavaFileUtil {

    /**
     * 获取包下的所有Java文件
     *
     * @param packageName
     * @return
     */
    public static List<File> getJavaFilesInPackage(String packageName) {
        String packagePath = packageName.replace(".", File.separator);
        String basePath = System.getProperty("user.dir"); // 获取当前工作目录

        File packageDir = new File(basePath + File.separator + "common-log/log-trace/src/main/java" + File.separator + packagePath);

        List<File> javaFiles = new ArrayList<>();
        try (Stream<File> files = Files.walk(packageDir.toPath())
                .filter(path -> path.toFile().getName().endsWith(".java"))
                .map(path -> path.toFile())) {
            javaFiles = files.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return javaFiles;
    }

    /**
     * 获取Java文件相对包路径
     *
     * @param javaFile
     * @return
     */
    public static String getPackagePath(File javaFile) {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(javaFile);
            PackageDeclaration packageDeclaration = compilationUnit.getPackageDeclaration().get();
            return packageDeclaration.getNameAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取Java文件所有方法
     *
     * @param javaFile
     * @return
     */
    public static List<String> getMethodNames(File javaFile) {
        List<String> methodNames = new ArrayList<>();

        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(javaFile);
            compilationUnit.findAll(MethodDeclaration.class)
                    .forEach(method -> methodNames.add(method.getNameAsString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return methodNames;
    }

    public static void main(String[] args) {
        String basePackage = "com.zcode.zjw.log.trace"; // 替换成你的包名
        List<File> javaFiles = getJavaFilesInPackage(basePackage);

        for (File javaFile : javaFiles) {
            String packageName = getPackagePath(javaFile);
            List<String> methodNames = getMethodNames(javaFile);
            System.out.println("File: " + javaFile.getName());
            System.out.println("Methods: " + methodNames);
            System.out.println("packageName: " + packageName);
        }
    }

}
