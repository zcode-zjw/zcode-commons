package com.zcode.zjw.common.file.ftp.util;

import com.jcraft.jsch.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * FTP和SFTP相关工具类
 *
 * @author zhangjiwei
 * @Date 2022-5-31
 */
public class FtpUtil {

    private static final String CHECK_FILE_SUFFIX = ".txt,.csv"; //文件验证（datax仅支持text和csv）

    /**
     * ftp创建连接
     *
     * @param host     主机IP
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws IOException
     */
    public static FTPClient connectByFtp(String host, int port, String username, String password) throws IOException {
        FTPClient ftp = new FTPClient();
        //防止中文乱码，不能在connect,login之后设置，查看源码得知
        //    FTPClient继承FTP，FTP继承SocketClient,
        //    所以ftpClient调用方法connect()时，会调用_connectAction_()方法，如果还没有没置编码，
        //    getControlEncoding()会默认使用ios-8859-1,
        //    所以必需在connect前完成编码设置
        ftp.setControlEncoding("GBK");
        // 设置ip和端口
        ftp.connect(host, port);
        // 设置用户名和密码
        ftp.login(username, password);
        // 设置文件类型（二进制传输模式）
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        // 设置被动模式
        ftp.enterLocalPassiveMode();
        if (ftp.getReplyCode() == 200) {
            return ftp;
        } else {
            System.out.println("ftp连接状态：" + ftp.getReplyCode());
            return null;
        }
    }


    /**
     * ftp获取全部目录路径
     *
     * @param host     主机IP
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws IOException
     */
    public static List<String> getFtpDir(String host, int port, String username, String password) throws IOException {
        List<String> fileDirList = new ArrayList<>();
        FTPClient ftpClient = connectByFtp(host, port, username, password);
        String dirHome = ftpClient.printWorkingDirectory();
        fileDirList.add(dirHome);
        getFtpDirListWhile(ftpClient, fileDirList, dirHome);
        System.out.println("ftp dir list:" + fileDirList);
        ftpClient.logout();
        ftpClient.disconnect();
        return fileDirList;
    }

    /**
     * ftp根据目录获取目录下全部文件
     *
     * @param dir      路径
     * @param host     主机IP
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws IOException
     */
    public static List<String> getFtpFileList(String dir, String host, int port, String username, String password) throws IOException {
        List<String> fileList = null;
        FTPClient ftpClient = connectByFtp(host, port, username, password);
        fileList = getFtpFileNameListBySuffix(ftpClient, dir);
        System.out.println("ftp file list:" + fileList);
        ftpClient.logout();
        ftpClient.disconnect();
        return fileList;
    }


    /**
     * ftp递归获取目录列表
     *
     * @param fileList 文件结合
     * @param dir      路径
     */
    public static void getFtpDirListWhile(FTPClient ftpClient, List<String> fileList, String dir) {
        try {
            FTPFile[] files = ftpClient.listFiles(dir);
            if (files != null && files.length > 0) {
                for (FTPFile file : files) {
                    if (file.isDirectory()) {
                        String fileDir = dir.concat("/").concat(file.getName());
                        fileList.add(fileDir);
                        getFtpDirListWhile(ftpClient, fileList, fileDir);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ftp获取目录下的文件
     *
     * @param dir 路径
     * @throws IOException
     */
    public static List<String> getFtpFileNameListBySuffix(FTPClient ftpClient, String dir) throws IOException {
        List<String> fileList = new ArrayList<>();
        try {
            FTPFile[] files = ftpClient.listFiles(dir);
            if (files != null && files.length > 0) {
                for (FTPFile file : files) {
                    String fileName = file.getName();
                    if (!".".equals(fileName) && !"..".equals(fileName) && !file.isDirectory()) {
                        String fileNameSuffix = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf("."), fileName.length()) : fileName;
                        if (CHECK_FILE_SUFFIX.contains(fileNameSuffix)) {
                            fileList.add(fileName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }


    /**
     * 获取ftp文件的内容，返回创建表的列集合
     *
     * @param dir      路径
     * @param fileName 文件
     * @param splitStr 分隔符
     * @param host     主机IP
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws IOException
     */
    public static List<String> getFtpFileContentColumn(String dir, String fileName, String splitStr, boolean isNewCol, String host, int port, String username, String password) throws IOException {
        List<String> fileList = null;
        FTPClient ftpClient = connectByFtp(host, port, username, password);
        try {
            InputStream inputStream = ftpClient.retrieveFileStream(dir + "/" + fileName);
            if (inputStream != null) {
                fileList = getReadFileColumn(inputStream, splitStr, isNewCol);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return fileList;
        }
    }


    /**
     * 创建SFTP连接
     *
     * @param host     主机ip
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     */
    public static ChannelSftp connectBySftp(String host, int port, String username, String password) throws JSchException {
        ChannelSftp sftp = null;
        Session sshSession = null;
        JSch jsch = new JSch();
        sshSession = jsch.getSession(username, host, port);
        sshSession.setPassword(password);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        sshSession.connect();
        Channel channel = sshSession.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;
        return sftp;
    }

    /**
     * 获取sftp目录
     *
     * @param host     主机ip
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws SftpException
     */
    public static List<String> getSftpDirList(String host, int port, String username, String password) throws SftpException, JSchException {

        ChannelSftp channelSftp = connectBySftp(host, port, username, password);
        String sftpHome = channelSftp.getHome();
        List<String> fileDirList = new ArrayList<>();
        fileDirList.add(sftpHome);
        getFileDirWhile(fileDirList, channelSftp, sftpHome);
        System.out.println("home path:" + fileDirList);
        channelSftp.disconnect();
        return fileDirList;
    }


    /**
     * sftp递归获取路径
     *
     * @param fileList    文件集合
     * @param channelSftp sftp对象
     * @param dir         路径
     * @throws SftpException
     */
    public static void getFileDirWhile(List<String> fileList, ChannelSftp channelSftp, String dir) throws SftpException {
        Vector<ChannelSftp.LsEntry> vector = channelSftp.ls(dir);
        vector.forEach(file -> {
            String fileName = file.getFilename();
            if (!(".".equals(fileName)) && !("..".equals(fileName)) && file.getAttrs().isDir()) {
                String fileDir = dir.concat("/").concat(fileName);
                fileList.add(fileDir);
                try {
                    getFileDirWhile(fileList, channelSftp, fileDir);
                } catch (SftpException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取sftp目录下所有文件
     *
     * @param dir      路径
     * @param host     主机ip
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws SftpException
     */
    public static List<String> getSftpFileList(String dir, String host, int port, String username, String password) throws SftpException, JSchException {
        ChannelSftp channelSftp = connectBySftp(host, port, username, password);
        List<String> fileList = getSftpFileNameBySuffix(channelSftp, dir);
        channelSftp.disconnect();
        return fileList;
    }


    /**
     * sftp根据目录获取全部过滤文件名文件名
     *
     * @param channelSftp sftp对象
     * @param dir         路径
     * @return
     * @throws SftpException
     */
    public static List<String> getSftpFileNameBySuffix(ChannelSftp channelSftp, String dir) throws SftpException {
        List<String> list = new ArrayList<>();
        //ls命令获取文件名列表
        Vector<ChannelSftp.LsEntry> vector = channelSftp.ls(dir);
        vector.forEach(file -> {
            //文件名称
            String fileName = file.getFilename();
            if (!".".equals(fileName) && !"..".equals(fileName) && !file.getAttrs().isDir()) {
                String fileNameSuffix = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf("."), fileName.length()) : fileName;
                if (CHECK_FILE_SUFFIX.contains(fileNameSuffix)) {
                    list.add(fileName);
                }
            }
        });
        return list;
    }

    /**
     * 读取Sftp内容
     *
     * @param host     主机ip
     * @param username 用户名
     * @param password 密码
     * @param dir      路径
     * @param splitStr 分隔符
     * @param fileName 文件名
     * @throws SftpException
     * @throws JSchException
     */
    public static List<String> getSftpFileContentColumn(String host, int port, String username, String password, String dir, String fileName, String splitStr, boolean isNewCol) throws SftpException, JSchException {
        List<String> list = null;
        ChannelSftp channelSftp = connectBySftp(host, port, username, password);
        try {
            InputStream inputStream = channelSftp.get(dir + "/" + fileName);
            if (inputStream != null) {
                list = getReadFileColumn(inputStream, splitStr, isNewCol);
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    /**
     * 读取文件第一行，确定多少列，生成建表列
     * 读取csv、txt通用
     *
     * @param inputStream 文件流
     * @param splitStr    分隔符
     * @return
     */
    public static List<String> getReadFileColumn(InputStream inputStream, String splitStr, boolean isNewCol) {
        List<String> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));//GBK
            String line = null;
            if ((line = reader.readLine()) != null) {
                String[] colList = line.split(splitStr);
                int colIndex = 1;
                //根据列数直接生成目标端需要建表的列名,或使用第一行作为列名
                for (String col : colList) {
                    if (!col.trim().equals("")) {
                        if (isNewCol) {
                            list.add("column" + colIndex);
                            colIndex++;
                        } else {
                            list.add(col.trim().replaceAll("\"", ""));
                        }
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }


    public static void main(String[] args) throws SftpException, IOException, JSchException {
        String host = "172.168.234.115";
        int port = 21;
        String username = "asfkldp";
        String password = "sk12p12#131";
        String sftpHost = "247.135.145.228";
        int sftpPort = 22;
        String sftpUsername = "rafogot";
        String sftpPassword = "ka1sf29a3";
        //ftp获取目录和文件
        List<String> list = getSftpDirList(sftpHost, sftpPort, sftpUsername, sftpPassword);
        System.out.println(list);
        list.forEach(dir -> {
            try {
                System.out.println(getSftpFileList(dir, sftpHost, sftpPort, sftpUsername, sftpPassword));
            } catch (SftpException | JSchException e) {
                e.printStackTrace();
            }
        });

        //ftp获取目录和文件
        List<String> listFtp = getFtpDir(host, port, username, password);
        listFtp.forEach(dir -> {
            try {
                getFtpFileList(dir, host, port, username, password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}