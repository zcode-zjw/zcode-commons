package com.zcode.zjw.common.file.ftp.util;

import com.zcode.zjw.common.file.ftp.core.FTPClientPool;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * FTPClient工具类，提供上传、下载、删除、创建多层目录等功能
 */
public class FtpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(FtpClientUtils.class);

    private FTPClientPool pool;

    public FtpClientUtils(FTPClientPool pool) {
        this.pool = pool;
    }

    public void setPool(FTPClientPool pool) {
        this.pool = pool;
    }

    /**
     * 在FTP的工作目录下创建多层目录
     *
     * @param path (路径分隔符使用"/"，且以"/"开头，例如"/data/photo/2018")
     * @return (耗时多少毫秒)
     * @throws Exception (异常)
     */
    public int mkdirs(String path) throws Exception {
        FTPClient client = null;
        String workDirectory = null;
        try {
            client = pool.borrowObject();
            long start = System.currentTimeMillis();
            checkPath(path);
            workDirectory = client.printWorkingDirectory();
            File f = new File(workDirectory + path);
            List<String> names = new LinkedList<String>();
            while (f != null && f.toString().length() > 0) {
                names.add(0, f.toString().replaceAll("\\\\", "/"));
                f = f.getParentFile();
            }
            for (String name : names) {
                client.makeDirectory(name);
            }
            return (int) (System.currentTimeMillis() - start);
        } catch (Exception e) {
            throw e;
        } finally {
            if (client != null) {
                client.changeWorkingDirectory(workDirectory);
                pool.returnObject(client);
            }
        }

    }

    /**
     * 上传文件到FTP工作目录
     *
     * @param localFile (上传的文件)
     * @param path      (在工作目录中的路径，示例"/data/2018")
     * @param filename  (文件名，示例"default.jpg")
     * @return (耗时多少毫秒)
     * @throws Exception (异常)
     */
    public int store(File localFile, String path, String filename) throws Exception {
        InputStream in = new FileInputStream(localFile);
        return store(in, path, filename);
    }

    /**
     * 上传文件到FTP工作目录，path示例"/data/2018"，filename示例"default.jpg"
     *
     * @param in       (要上传的输入流)
     * @param path     (在工作目录中的路径，示例"/data/2018")
     * @param filename (文件名，示例"default.jpg")
     * @return (耗时多少毫秒)
     * @throws Exception (异常)
     */
    public int store(InputStream in, String path, String filename) throws Exception {
        FTPClient client = null;
        String workDirectory = null;
        try {
            client = pool.borrowObject();
            workDirectory = client.printWorkingDirectory();
            checkPath(path);
            long start = System.currentTimeMillis();
            synchronized (client) {
                mkdirs(path);
                client.changeWorkingDirectory(workDirectory + path);
                client.setFileType(FTP.BINARY_FILE_TYPE);
                client.storeFile(filename, in);
            }
            return (int) (System.currentTimeMillis() - start);
        } catch (IOException e) {
            throw e;
        } finally {
            if (client != null) {
                client.changeWorkingDirectory(workDirectory);
                pool.returnObject(client);
            }
        }

    }

    /**
     * 删除FTP工作目录中的指定文件
     *
     * @param pathname (文件路径，示例"/data/2018/default.jpg")
     * @return (删除成功返回true ， 删除失败返回false)
     * @throws Exception (IO异常)
     */
    public boolean delete(String pathname) throws Exception {
        FTPClient client = null;
        try {
            client = pool.borrowObject();
            return client.deleteFile(pathname);
        } catch (Exception e) {
            logger.error("删除文件失败", e);
            throw e;
        } finally {
            if (client != null) pool.returnObject(client);
        }
    }

    /**
     * 从FTP工作目录下载remote文件
     *
     * @param remote (FTP文件路径，示例"/data/2018/default.jpg")
     * @param local  (保存到本地的位置)
     * @return (耗时多少毫秒)
     * @throws Exception (异常)
     */
    public int retrieve(String remote, File local) throws Exception {
        return retrieve(remote, new FileOutputStream(local));
    }

    /**
     * 从FTP工作目录下载remote文件
     *
     * @param remote (文件路径，示例"/data/2018/default.jpg")
     * @param out    (输出流)
     * @return (耗时多少毫秒)
     * @throws Exception (异常)
     */
    public int retrieve(String remote, OutputStream out) throws Exception {
        InputStream in = null;
        FTPClient client = null;
        try {
            client = pool.borrowObject();
            long start = System.currentTimeMillis();
            in = client.retrieveFileStream(remote);
            if (in != null) {
                byte[] buffer = new byte[1024];
                for (int len; (len = in.read(buffer)) != -1; ) {
                    out.write(buffer, 0, len);
                }
                return (int) (System.currentTimeMillis() - start);
            } else {
                throw new RuntimeException("FTP Client retrieve Faild.");
            }
        } catch (Exception e) {
            logger.error("获取ftp下载流异常", e);
            throw e;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e2) {
            }
            try {
                if (client != null) pool.returnObject(client);
            } catch (Exception e2) {
            }
        }
    }

    private static void checkPath(String path) {
        if (path.contains("\\")) {
            throw new RuntimeException("'\\' is not allowed in the path,please use '/'");
        }
        if (!path.startsWith("/")) {
            throw new RuntimeException("Please start with '/'");
        }
        if (!path.endsWith("/")) {
            throw new RuntimeException("Don't end with '/'");
        }
    }

}