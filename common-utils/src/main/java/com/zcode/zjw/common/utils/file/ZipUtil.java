package com.zcode.zjw.common.utils.file;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * 压缩/解压缩工具
 *
 * @author zhangjiwei
 * @date 2023/6/30
 */
public class ZipUtil {

    /**
     * 解压缩zipFile
     *
     * @param filepath  要解压的zip文件路径
     * @param outputDir 要解压到某个指定的目录下
     * @throws IOException
     */
    public static void unzip(String filepath, String outputDir) throws IOException {
        ZipFile zipFile = null;
        File file = new File(filepath);
        try {
            Charset CP866 = Charset.forName("CP866");  //specifying alternative (non UTF-8) charset
            zipFile = new ZipFile(file, CP866);
            createDirectory(outputDir, null);//创建输出目录

            Enumeration<?> enums = zipFile.entries();
            while (enums.hasMoreElements()) {

                ZipEntry entry = (ZipEntry) enums.nextElement();
                System.out.println("解压." + entry.getName());

                if (entry.isDirectory()) {//是目录
                    createDirectory(outputDir, entry.getName());//创建空目录
                } else {//是文件
                    File tmpFile = new File(outputDir + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/", null);//创建输出目录

                    try (InputStream in = zipFile.getInputStream(entry); OutputStream out = Files.newOutputStream(tmpFile.toPath())) {
                        int length = 0;

                        byte[] b = new byte[2048];
                        while ((length = in.read(b)) != -1) {
                            out.write(b, 0, length);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new IOException("解压缩文件出现异常", e);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException ex) {
                throw new IOException("关闭zipFile出现异常", ex);
            }
        }
    }

    public static void zip(String sourcePath, String targetFile) {
        File sourceFile = new File(sourcePath);
        try (
                FileOutputStream fos = new FileOutputStream(targetFile);
                ZipOutputStream zipOut = new ZipOutputStream(fos)
        ) {
            compressFile(sourceFile, sourceFile.getName(), zipOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compressFile(File file, String baseDir, ZipOutputStream zipOut) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    String entryName = baseDir + "/" + childFile.getName();
                    compressFile(childFile, entryName, zipOut);
                }
            }
        } else {
            try (FileInputStream fis = new FileInputStream(file)) {
                ZipEntry zipEntry = new ZipEntry(baseDir);
                zipOut.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    zipOut.write(buffer, 0, bytesRead);
                }
                zipOut.closeEntry();
            }
        }
    }

    /**
     * 构建目录
     *
     * @param outputDir
     * @param subDir
     */
    public static void createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + "/" + subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            file.mkdirs();
        }
    }

    //------------------------------------------------------------------------------------------------------


    /**
     * 压缩文件
     *
     * @param sourceFolder 指定打包的源目录
     * @param tarGzPath    指定目标 tar 包的位置
     */
    public static void tarGzip(String sourceFolder, String tarGzPath) {

        TarArchiveOutputStream tarOs = null;
        try {
            // 创建一个 FileOutputStream 到输出文件（.tar.gz）
            FileOutputStream fos = new FileOutputStream(tarGzPath);
            // 创建一个 GZIPOutputStream，用来包装 FileOutputStream 对象
            GZIPOutputStream gos = new GZIPOutputStream(new BufferedOutputStream(fos));
            // 创建一个 TarArchiveOutputStream，用来包装 GZIPOutputStream 对象
            tarOs = new TarArchiveOutputStream(gos);
            // 使文件名支持超过 100 个字节
            tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
            File sourceFile = new File(sourceFolder);
            //遍历源目录的文件，将所有文件迁移到新的目录tarGzPath下
            File[] sources = sourceFile.listFiles();
            for (File oneFile : sources) {
                addFilesToTarGZ(oneFile.getPath(), "", tarOs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (tarOs != null) {
                    tarOs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param sourcePath 源文件
     * @param parent     源目录
     * @param tarArchive 压缩输出流
     * @throws IOException
     */
    private static void addFilesToTarGZ(String sourcePath, String parent, TarArchiveOutputStream tarArchive) throws IOException {
        File sourceFile = new File(sourcePath);
        // 获取新目录下的文件名称
        String fileName = parent.concat(sourceFile.getName());
        //打包压缩该文件
        tarArchive.putArchiveEntry(new TarArchiveEntry(sourceFile, fileName));
        if (sourceFile.isFile()) {
            FileInputStream fis = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            // 写入文件
            IOUtils.copy(bis, tarArchive);
            tarArchive.closeArchiveEntry();
            bis.close();
        } else if (sourceFile.isDirectory()) {
            // 因为是个文件夹，无需写入内容，关闭即可
            tarArchive.closeArchiveEntry();
            // 遍历文件夹下的文件
            for (File f : sourceFile.listFiles()) {
                // 递归遍历文件目录树
                addFilesToTarGZ(f.getAbsolutePath(), fileName + File.separator, tarArchive);
            }
        }
    }

    public static void changeFileName(String oldFileName, String newFileName) throws IOException {
        // 旧的文件或目录
        File oldName = new File(oldFileName);
        // 新的文件或目录
        File newName = new File(newFileName);
        if (newName.exists()) {  //  确保新的文件名不存在
            throw new java.io.IOException("file exists");
        }
        if (oldName.renameTo(newName)) {
            System.out.println("已重命名");
        } else {
            System.out.println("Error");
        }
    }

    /**
     * 解压.tar.gz文件
     *
     * @param sourceFile 需解压文件
     * @param outputDir  输出目录
     * @throws IOException
     */
    public static void unTarGzip(String sourceFile, String outputDir) throws IOException {
        TarInputStream tarIn = null;
        File file = new File(sourceFile);
        try {
            tarIn = new TarInputStream(new GZIPInputStream(
                    new BufferedInputStream(Files.newInputStream(file.toPath()))),
                    1024 * 2);

            createDirectory(outputDir, null);//创建输出目录

            TarEntry entry;
            while ((entry = tarIn.getNextEntry()) != null) {

                if (entry.isDirectory()) {//是目录
                    entry.getName();
                    createDirectory(outputDir, entry.getName());//创建空目录
                } else {//是文件
                    File tmpFile = new File(outputDir + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/", null);//创建输出目录
                    try (OutputStream out = Files.newOutputStream(tmpFile.toPath())) {
                        int length;

                        byte[] b = new byte[2048];

                        while ((length = tarIn.read(b)) != -1) {
                            out.write(b, 0, length);
                        }

                    }
                }
            }
        } catch (IOException ex) {
            throw new IOException("解压归档文件出现异常", ex);
        } finally {
            try {
                if (tarIn != null) {
                    tarIn.close();
                }
            } catch (IOException ex) {
                throw new IOException("关闭tarFile出现异常", ex);
            }
        }
    }


    /**
     * 解压Rar文件
     *
     * @param rarFile 文件目录
     */
    public static File unRarZip(File rarFile, String outDir) throws Exception {
        File outFileDir = new File(outDir);
        if (!outFileDir.exists()) {
            boolean isMakDir = outFileDir.mkdirs();
            if (isMakDir) {
                System.out.println("创建压缩目录成功");
            }
        }
        System.out.println(rarFile.getName());
        Archive archive = new Archive(Files.newInputStream(rarFile.toPath()));
        FileHeader fileHeader = archive.nextFileHeader();
        File out = null;
        while (fileHeader != null) {
            if (fileHeader.isDirectory()) {
                fileHeader = archive.nextFileHeader();
                continue;
            }
            out = new File(outDir + fileHeader.getFileNameString());
            if (!out.exists()) {
                if (!out.getParentFile().exists()) {
                    out.getParentFile().mkdirs();
                }
                out.createNewFile();
            }
            FileOutputStream os = new FileOutputStream(out);
            archive.extractFile(fileHeader, os);

            os.close();
            break;
        }
        archive.close();
        return out;
    }


    private static void compressDirectory(String sourceDirectory, String targetGzFile) {
        try (
                FileOutputStream fos = new FileOutputStream(targetGzFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                GZIPOutputStream gzOut = new GZIPOutputStream(bos)
        ) {
            Path directory = Paths.get(sourceDirectory);
            compressFiles(directory, directory.getFileName().toString(), gzOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compressFiles(Path path, String baseDir, GZIPOutputStream gzOut) throws IOException {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path entry : stream) {
                    String entryName = baseDir + "/" + entry.getFileName().toString();
                    compressFiles(entry, entryName, gzOut);
                }
            }
        } else {
            try (InputStream is = Files.newInputStream(path)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    gzOut.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    /**
     * @param sourcePath 要压缩的文件路径
     * @param format     生成的格式（zip、rar）d
     */
    public static void zipOrRarZip(String sourcePath, String outputPath, String format) throws Exception {
        File file = new File(sourcePath);
        // 压缩文件的路径不存在
        if (!file.exists()) {
            throw new Exception("路径 " + sourcePath + " 不存在文件，无法进行压缩...");
        }
        // 用于存放压缩文件的文件夹
        String generateFile = file.getParent() + File.separator + "CompressFile";
        File compress = new File(generateFile);
        // 如果文件夹不存在，进行创建
        if (!compress.exists()) {
            compress.mkdirs();
        }

        // 目的压缩文件
        String generateFileName = outputPath + File.separator + file.getName() + "." + format;

        // 输入流 表示从一个源读取数据
        // 输出流 表示向一个目标写入数据

        // 输出流
        FileOutputStream outputStream = new FileOutputStream(generateFileName);
        // 压缩输出流
        org.apache.tools.zip.ZipOutputStream zipOutputStream = new org.apache.tools.zip.ZipOutputStream(new BufferedOutputStream(outputStream));

        zipOrRarZip(zipOutputStream, file, "");

        System.out.println("源文件位置：" + file.getAbsolutePath() + "，目的压缩文件生成位置：" + generateFileName);
        // 关闭 输出流
        zipOutputStream.close();
    }

    /**
     * @param out  输出流
     * @param file 目标文件
     * @param dir  文件夹
     * @throws Exception
     */
    private static void zipOrRarZip(org.apache.tools.zip.ZipOutputStream out, File file, String dir) throws Exception {
        // 当前的是文件夹，则进行一步处理
        if (file.isDirectory()) {
            //得到文件列表信息
            File[] files = file.listFiles();

            //将文件夹添加到下一级打包目录
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(dir + "/"));
            dir = dir.length() == 0 ? "" : dir + "/";
            //循环将文件夹中的文件打包
            for (File value : files) {
                zipOrRarZip(out, value, dir + value.getName());
            }
        } else { // 当前是文件
            // 输入流
            FileInputStream inputStream = new FileInputStream(file);
            // 标记要打包的条目
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(dir));
            // 进行写操作
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
            // 关闭输入流
            inputStream.close();
        }

    }


    public static void main(String[] args) throws Exception {
        //ZipUtil.compressDirectory("/Users/mac/Desktop/test-dir/zipTest/", "/Users/mac/Desktop/test.gz");
        //ZipUtil.unTarGzip("/Users/mac/Desktop/test.gz", "/Users/mac/Desktop/gzipTest/");
        //UnZipFileUtil.unZip("/Users/mac/Desktop/test.zip", "/Users/mac/Desktop/", true);
        Date date = new Date();
        for (int i = 0; i < 12; i++) {
            DateTime dateTime = DateUtil.offsetMonth(date, -i);
            int month = dateTime.month();
            int year = dateTime.year();
            System.out.println(year + "" + month);
        }

    }

}
