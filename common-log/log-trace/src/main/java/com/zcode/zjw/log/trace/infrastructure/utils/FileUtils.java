package com.zcode.zjw.log.trace.infrastructure.utils;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static String getFilePath(String relativePath) {
        String dir = FileUtils.class.getResource("/").getPath();
        return dir + relativePath;
    }

    public static String getFilePath(Class<?> clazz, String className) {
        String path = clazz.getResource("/").getPath();
        return String.format("%s%s.class", path, className.replace('.', File.separatorChar));
    }

    public static byte[] readBytes(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File Not Exist: " + filepath);
        }

        InputStream in = null;

        try {
            in = Files.newInputStream(file.toPath());
            in = new BufferedInputStream(in);

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            IOUtils.copy(in, bao);

            return bao.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(in);
        }

        throw new RuntimeException("Can not read file: " + filepath);
    }

    public static void writeBytes(String filepath, byte[] bytes) {
        File file = new File(filepath);
        File dirFile = file.getParentFile();
        mkdirs(dirFile);

        try (OutputStream out = Files.newOutputStream(Paths.get(filepath));
             BufferedOutputStream buff = new BufferedOutputStream(out)) {
            buff.write(bytes);
            buff.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (FileTraceLogConst.DEBUG) System.out.println("file://" + filepath);
    }

    public static List<String> readLines(String filepath) {
        return readLines(filepath, "UTF8");
    }

    public static List<String> readLines(String filepath, String charsetName) {
        File file = new File(filepath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File Not Exist: " + filepath);
        }

        InputStream in = null;
        Reader reader = null;
        BufferedReader bufferReader = null;

        try {
            in = Files.newInputStream(file.toPath());
            reader = new InputStreamReader(in, charsetName);
            bufferReader = new BufferedReader(reader);

            List<String> list = new ArrayList<>();
            String line;
            while ((line = bufferReader.readLine()) != null) {
                list.add(line);
            }
            return list;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(bufferReader);
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(in);
        }

        assert !FileTraceLogConst.DEBUG : "bytes is null";

        return null;
    }

    public static void writeLines(String filepath, List<String> lines) {
        if (lines == null || lines.size() < 1) return;

        File file = new File(filepath);
        File dirFile = file.getParentFile();
        mkdirs(dirFile);

        OutputStream out = null;
        Writer writer = null;
        BufferedWriter bufferedWriter = null;

        try {
            out = Files.newOutputStream(file.toPath());
            writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bufferedWriter = new BufferedWriter(writer);

            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(bufferedWriter);
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(out);
        }
    }

    public static void mkdirs(File dirFile) {
        boolean file_exists = dirFile.exists();

        if (file_exists && dirFile.isDirectory()) {
            return;
        }

        if (file_exists && dirFile.isFile()) {
            throw new RuntimeException("Not A Directory: " + dirFile);
        }

        if (!file_exists) {
            boolean flag = dirFile.mkdirs();
            assert !FileTraceLogConst.DEBUG || flag : "Create Directory Failed: " + dirFile.getAbsolutePath();
        }
    }

    public static void clear(File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    delete(f);
                }
            }
        }
        else {
            delete(file);
        }
    }

    public static void delete(File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isFile()) {
            boolean flag = file.delete();
            assert !FileTraceLogConst.DEBUG || flag : "[Warning] delete file failed: " + file.getAbsolutePath();
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    delete(f);
                }
            }

            boolean flag = file.delete();
            assert !FileTraceLogConst.DEBUG || flag : "[Warning] delete file failed: " + file.getAbsolutePath();
        }
    }

    public static byte[] readStream(final InputStream in, final boolean close) {
        if (in == null) {
            throw new IllegalArgumentException("inputStream is null!!!");
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            return out.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (close) {
                IOUtils.closeQuietly(in);
            }
        }
        return null;
    }

    public static InputStream getInputStream(String className) {
        return ClassLoader.getSystemResourceAsStream(className.replace('.', '/') + ".class");
    }
}
