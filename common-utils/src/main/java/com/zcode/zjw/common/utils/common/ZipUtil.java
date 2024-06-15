package com.zcode.zjw.common.utils.common;

import cn.hutool.core.io.FileUtil;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangjiwei
 * @description zip 压缩单文件/多文件
 * @since 2022/8/3 11:52
 */
public class ZipUtil {

    /**
     * 功能描述： 单文件/多文件压缩
     *
     * @param input  单/多文件路径        例如："E:\\Testzip\\test\\22.pdf|E:\\Testzip\\11.pdf"
     * @param output 压缩包文件路径       例如："E:\\Test.zip"
     * @author
     * @date 2022/8/3 11:54
     */
    public static void zip(String input, String output) throws Exception {
        final boolean exist = FileUtil.exist(output);
        if (!exist) {
            FileUtil.newFile(output);
        }
        byte[] buffer = new byte[1024];
        final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(output));
        final List<File> fileList = Arrays.stream(input.split("\\|")).map(File::new).collect(Collectors.toList());

        for (File file : fileList) {
            final FileInputStream fileInputStream = new FileInputStream(file);
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            int len;
            while ((len = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }
            zipOutputStream.closeEntry();
            fileInputStream.close();
        }
        zipOutputStream.close();
    }

    /**
     * 功能描述：解压
     *
     * @param zipFile 压缩包路径               例如："C:\\Users\\Desktop\\zipTest.zip";
     * @param outDir  解压到的目录(支持创建)     例如："C:\\Users\\Desktop\11"
     * @return void
     * @author
     * @date 2022/8/3 17:28
     */
    public static void unzip(String zipFile, String outDir) throws Exception {
        if (!FileUtil.exist(zipFile)) {
            throw new FileNotFoundException();
        }
        final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry entry = null;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                final File file = new File(outDir, entry.getName());
                if (!file.exists()) {
                    final boolean mkdirs = file.getParentFile().mkdirs();
                }

                final FileOutputStream fileOutputStream = new FileOutputStream(file);
                final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                int len = -1;
                final byte[] bytes = new byte[1024];
                while ((len = zipInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, len);
                }
                bufferedOutputStream.close();
                fileOutputStream.close();
            }
            zipInputStream.closeEntry();
        }
        zipInputStream.close();
    }


    /**
     * 功能描述：单文件/多文件压缩 利用 hutool的ZipUtil
     *
     * @param input  单/多文件路径(支持文件夹)         例如："E:\\Testzip\\test\\ytt.html|E:\\Testzip\\uugg.html"
     * @param output 压缩包文件路径                  例如："E:\\Test.zip"
     * @return void
     * @author
     * @date 2022/8/3 17:22
     */
    public static void zip_hutool(String input, String output) {
        final File[] files = Arrays.stream(input.split("\\|")).map(File::new).toArray(File[]::new);
        cn.hutool.core.util.ZipUtil.zip(FileUtil.file(output), true, files);
    }


    /**
     * 功能描述：解压 （利用hutool）
     *
     * @param zipFile 压缩包路径               例如："C:\\Users\\Desktop\\zipTest.zip";
     * @param outDir  解压到的目录(支持创建)     例如："C:\Users\\Desktop\11"
     * @return void
     * @author
     * @date 2022/8/3 17:28
     */
    public static void unzipByHuTool(String zipFile, String outDir) {
        cn.hutool.core.util.ZipUtil.unzip(zipFile, outDir);
    }


}

