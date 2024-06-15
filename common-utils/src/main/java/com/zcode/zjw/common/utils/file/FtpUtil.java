package com.zcode.zjw.common.utils.file;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: ftp工具类
 */
@Component
public class FtpUtil {

    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int DEFAULT_TIMEOUT = 60 * 1000;
    private static final String DAILY_FILE_PATH = "get";
    static FTPClient ftpClient;
    private volatile String ftpBasePath;


    /**
     * SFTP 服务器地址IP地址
     */
    static String host;
    /**
     * SFTP 登录用户名
     */
    static String username;
    /**
     * SFTP 登录密码
     */
    static String password;
    /**
     * SFTP 端口
     */
    static int port;

    public String getHost() {
        return host;
    }

    @Value("${ftp.hostname}")
    public void setHost(String host) {
        FtpUtil.host = host;
    }

    public String getUsername() {
        return username;
    }

    @Value("${ftp.username}")
    public void setUsername(String username1) {
        FtpUtil.username = username1;
    }

    public String getPassword() {
        return password;
    }

    @Value("${ftp.password}")
    public void setPassword(String password1) {
        FtpUtil.password = password1;
    }

    public int getPort() {
        return port;
    }

    @Value("${ftp.port}")
    public void setPort(int port) {
        FtpUtil.port = port;
    }

    public FtpUtil() {
    }

    private FtpUtil(String host, String username, String password) {
        this(host, 21, username, password, DEFAULT_CHARSET);
        setTimeout(DEFAULT_TIMEOUT, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT);
    }

    private FtpUtil(String host, int port, String username, String password, String charset) {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding(charset);
        FtpUtil.host = StringUtils.isEmpty(host) ? "localhost" : host;
        FtpUtil.port = (port <= 0) ? 21 : port;
        FtpUtil.username = StringUtils.isEmpty(username) ? "anonymous" : username;
        FtpUtil.password = password;
    }

    /**
     * @param host
     * @param username
     * @param password
     * @return
     * @TODO: 创建默认的ftp客户端
     */
    public static FtpUtil createFtpCli(String host, String username, String password) {
        return new FtpUtil(host, username, password);
    }

    /**
     * @param host
     * @param port
     * @param username
     * @param password
     * @param charset
     * @return
     * @TODO: 创建自定义属性的ftp客户端
     */
    public static FtpUtil createFtpCli(String host, int port, String username, String password, String charset) {
        return new FtpUtil(host, port, username, password, charset);
    }

    /**
     * @param defaultTimeout 超时时间
     * @param connectTimeout 超时时间
     * @param dataTimeout    超时时间
     * @TODO: 设置超时时间
     */
    public void setTimeout(int defaultTimeout, int connectTimeout, int dataTimeout) {
        ftpClient.setDefaultTimeout(defaultTimeout);
        ftpClient.setConnectTimeout(connectTimeout);
        ftpClient.setDataTimeout(dataTimeout);
    }

    /**
     * TODO ftp登录
     *
     * @return
     */
    public static Boolean login() {
        logger.info("username---" + username + "--host---" + host + "---port---" + port + "-----password-----" + password);
        boolean flag = false;
        try {
            ftpClient = new FTPClient();
            // 连接FTP服务器
            ftpClient.connect(host, port);
            // 登陆FTP服务器
            boolean login = ftpClient.login(username, password);
            // 中文支持
            ftpClient.setControlEncoding(DEFAULT_CHARSET);
            // 设置文件类型为二进制（如果从FTP下载或上传的文件是压缩文件的时候，不进行该设置可能会导致获取的压缩文件解压失败）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();

//			if (!ftpClient.login(username, password)) {
//				disconnect();
//				throw new IOException("不能连接到该服务 :" + host);
//			}
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.error("连接FTP失败，用户名或密码错误。");
                disconnect();
            } else {
                logger.info("FTP连接成功!");
                flag = true;
            }
        } catch (Exception e) {
            logger.info("sftp login异常查看报错信息", e);
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * @throws IOException
     * @TODO: 连接到ftp
     */
    public void connect() throws IOException {
        try {
            ftpClient.connect(host, port);
        } catch (UnknownHostException e) {
            throw new IOException("在FTP没有发现该端口:" + host);
        }

        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            disconnect();
            throw new IOException("不能连接该FTP :" + host);
        }

        if (!ftpClient.login(username, password)) {
            disconnect();
            throw new IOException("不能登录FTP :" + host);
        }

        // set data transfer mode.
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        // Use passive mode to pass firewalls.
        ftpClient.enterLocalPassiveMode();

        initFtpBasePath();
    }

    /**
     * @throws IOException
     * @TODO: 连接ftp时保存刚登陆ftp时的路径
     */
    private void initFtpBasePath() throws IOException {
        if (StringUtils.isEmpty(ftpBasePath)) {
            synchronized (this) {
                if (StringUtils.isEmpty(ftpBasePath)) {
                    ftpBasePath = ftpClient.printWorkingDirectory();
                }
            }
        }
    }

    /**
     * @return
     * @TODO: ftp是否处于连接状态，是连接状态返回<tt>true</tt>
     */
    public boolean isConnected() {
        return ftpClient.isConnected();
    }

    /**
     * @param fileName    文件名
     * @param inputStream 文件输入流
     * @return
     * @throws IOException
     * @TODO: 上传文件到对应日期文件下，     * 如当前时间是2022-09-19，则上传到[ftpBasePath]/[DAILY_FILE_PATH]/2022/08/19/下]
     */
    public String uploadFileToDailyDir(String fileName, InputStream inputStream) throws IOException {
        changeWorkingDirectory(ftpBasePath);
        SimpleDateFormat dateFormat = new SimpleDateFormat("/yyyy/MM/dd");
        String formatDatePath = dateFormat.format(new Date());
        String uploadDir = DAILY_FILE_PATH + formatDatePath;
        makeDirs(uploadDir);
        storeFile(fileName, inputStream);
        return formatDatePath + "/" + fileName;
    }

    /**
     * @param dailyDirFilePath 方法uploadFileToDailyDir返回的路径
     * @param outputStream     输出流
     * @throws IOException
     * @TODO: 根据uploadFileToDailyDir返回的路径，从ftp下载文件到指定输出流中
     */
    public void downloadFileFromDailyDir(String dailyDirFilePath, OutputStream outputStream) throws IOException {
        changeWorkingDirectory(ftpBasePath);
        String ftpRealFilePath = DAILY_FILE_PATH + dailyDirFilePath;
        ftpClient.retrieveFile(ftpRealFilePath, outputStream);
    }

    /**
     * @param ftpFileName 文件在ftp上的路径  如绝对路径 /home/ftpuser/123.txt 或者相对路径 123.txt
     * @param out         输出流
     * @throws IOException
     * @TODO: 获取ftp上指定文件名到输出流中
     */
    public void retrieveFile(String ftpFileName, OutputStream out) throws IOException {
        try {
            FTPFile[] fileInfoArray = ftpClient.listFiles(ftpFileName);
            if (fileInfoArray == null || fileInfoArray.length == 0) {
                throw new FileNotFoundException("在FTP上未找到‘" + ftpFileName + "’文件");
            }

            FTPFile fileInfo = fileInfoArray[0];
            if (fileInfo.getSize() > Integer.MAX_VALUE) {
                throw new IOException("文件 " + ftpFileName + " 太大了");
            }

            if (!ftpClient.retrieveFile(ftpFileName, out)) {
                throw new IOException("从FTP服务器上加载文件错误‘ " + ftpFileName + "’。请检查FTP权限与路径。");
            }
            out.flush();
        } finally {
            closeStream(out);
        }
    }

    /**
     * @param ftpFileName 文件在ftp上的路径 如绝对路径 /home/ftpuser/123.txt 或者相对路径 123.txt
     * @param in          输入流
     * @throws IOException
     * @TODO: 将输入流存储到指定的ftp路径下
     */
    public void storeFile(String ftpFileName, InputStream in) throws IOException {
        try {
            if (!ftpClient.storeFile(ftpFileName, in)) {
                throw new IOException("该‘" + ftpFileName + "’上传至FTP失败。请检查FTP权限或文件路径。");
            }
        } finally {
            closeStream(in);
        }
    }

    /**
     * @param ftpFileName
     * @throws IOException
     * @TODO: 根据文件ftp路径名称删除文件
     */
    public void deleteFile(String ftpFileName) throws IOException {
        if (!ftpClient.deleteFile(ftpFileName)) {
            throw new IOException("从FTP上删除‘" + ftpFileName + "’失败");
        }
    }

    /**
     * @param ftpFileName 上传到ftp文件路径名称
     * @param localFile   本地文件路径名称
     * @throws IOException
     * @TODO: 上传文件到ftp
     */
    public void upload(String ftpFileName, File localFile) throws IOException {
        if (!localFile.exists()) {
            throw new IOException("该文件‘" + localFile.getAbsolutePath() + "’上传失败。 这个文件不存在。");
        }

        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(localFile));
            if (!ftpClient.storeFile(ftpFileName, in)) {
                throw new IOException("该‘" + ftpFileName + "’上传至FTP失败。请检查FTP权限或文件路径。");
            }
        } finally {
            closeStream(in);
        }
    }

    /**
     * @param remotePath ftp上文件夹路径名称
     * @param localPath  本地上传的文件夹路径名称
     * @throws IOException
     * @TODO: 上传文件夹到ftp上
     */
    public void uploadDir(String remotePath, String localPath) throws IOException {
        localPath = localPath.replace("\\\\", "/");
        File file = new File(localPath);
        if (file.exists()) {
            if (!ftpClient.changeWorkingDirectory(remotePath)) {
                ftpClient.makeDirectory(remotePath);
                ftpClient.changeWorkingDirectory(remotePath);
            }
            File[] files = file.listFiles();
            if (null != files) {
                for (File f : files) {
                    if (f.isDirectory() && !".".equals(f.getName()) && !"..".equals(f.getName())) {
                        uploadDir(remotePath + "/" + f.getName(), f.getPath());
                    } else if (f.isFile()) {
                        upload(remotePath + "/" + f.getName(), f);
                    }
                }
            }
        }
    }

    /**
     * @param ftpFileName ftp文件路径名称
     * @param localFile   本地文件路径名称
     * @throws IOException
     * @TODO: 下载ftp文件到本地上
     */
    public void download(String ftpFileName, File localFile) throws IOException {
        OutputStream out = null;
        try {
            FTPFile[] fileInfoArray = ftpClient.listFiles(ftpFileName);
            if (fileInfoArray == null || fileInfoArray.length == 0) {
                throw new FileNotFoundException("在FTP上未找到‘" + ftpFileName + "’文件");
            }

            FTPFile fileInfo = fileInfoArray[0];
            if (fileInfo.getSize() > Integer.MAX_VALUE) {
                throw new IOException("文件 " + ftpFileName + " 太大了");
            }

            out = new BufferedOutputStream(new FileOutputStream(localFile));
            if (!ftpClient.retrieveFile(ftpFileName, out)) {
                throw new IOException("从FTP服务器上加载文件错误‘ " + ftpFileName + "’。请检查FTP权限与路径。");
            }
            out.flush();
        } finally {
            closeStream(out);
        }
    }

    /**
     * @param dir ftp服务器上目录
     * @return boolean 改变成功返回true
     * @TODO: 改变工作目录
     */
    public static boolean changeWorkingDirectory(String dir) {
        if (ftpClient == null) {
            return false;
        }
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            return ftpClient.changeWorkingDirectory(dir);
        } catch (IOException e) {
        }

        return false;
    }

    /**
     * @param remotePath ftp上文件夹路径名称
     * @param localPath  本地上传的文件夹路径名称
     * @throws IOException
     * @TODO: 下载ftp服务器下文件夹到本地
     */
    public void downloadDir(String remotePath, String localPath) throws IOException {
        localPath = localPath.replace("\\\\", "/");
        File file = new File(localPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FTPFile[] ftpFiles = ftpClient.listFiles(remotePath);
        for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
            FTPFile ftpFile = ftpFiles[i];
            if (ftpFile.isDirectory() && !(".").equals(ftpFile.getName()) && !"..".equals(ftpFile.getName())) {
                downloadDir(remotePath + "/" + ftpFile.getName(), localPath + "/" + ftpFile.getName());
            } else {
                download(remotePath + "/" + ftpFile.getName(), new File(localPath + "/" + ftpFile.getName()));
            }
        }
    }

    /**
     * @param filePath ftp上文件目录
     * @return java.util.List<java.lang.String>
     * @throws IOException
     * @TODO: 列出ftp上文件目录下的文件
     */
    public List<String> listFileNames(String filePath) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles(filePath);
        List<String> fileList = new ArrayList<>();
        if (ftpFiles != null) {
            for (int i = 0; i < ftpFiles.length; i++) {
                FTPFile ftpFile = ftpFiles[i];
                if (ftpFile.isFile()) {
                    fileList.add(ftpFile.getName());
                }
            }
        }

        return fileList;
    }

    /**
     * @param args ftp命令
     * @throws IOException
     * @TODO: 发送ftp命令到ftp服务器中
     */
    public void sendSiteCommand(String args) throws IOException {
        if (!ftpClient.isConnected()) {
            ftpClient.sendSiteCommand(args);
        }
    }

    /**
     * @return java.lang.String 当前所处的工作目录
     * @TODO: 获取当前所处的工作目录
     */
    public String printWorkingDirectory() {
        if (!ftpClient.isConnected()) {
            return "";
        }

        try {
            return ftpClient.printWorkingDirectory();
        } catch (IOException e) {
            // do nothing
            e.printStackTrace();
        }

        return "";
    }

    /**
     * @return boolean 切换成功返回true
     * @TODO: 切换到当前工作目录的父目录下
     */
    public boolean changeToParentDirectory() {

        if (!ftpClient.isConnected()) {
            return false;
        }

        try {
            return ftpClient.changeToParentDirectory();
        } catch (IOException e) {
            // do nothing
            e.printStackTrace();
        }

        return false;
    }

    /**
     * @return java.lang.String 当前工作目录的父目录
     * @TODO: 返回当前工作目录的上一级目录
     */
    public String printParentDirectory() {
        if (!ftpClient.isConnected()) {
            return "";
        }

        String w = printWorkingDirectory();
        changeToParentDirectory();
        String p = printWorkingDirectory();
        changeWorkingDirectory(w);

        return p;
    }

    /**
     * @param pathname 路径名
     * @return 创建成功返回true
     * @throws IOException
     * @TODO: 创建目录
     */
    public static boolean makeDirectory(String pathname) throws IOException {
        return ftpClient.makeDirectory(pathname);
    }

    /**
     * @param pathname 路径名
     * @throws IOException
     * @TODO: 创建多个目录
     */
    public static void makeDirs(String pathname) throws IOException {
        pathname = pathname.replace("\\\\", "/");
        String[] pathnameArray = pathname.split("/");
        for (String each : pathnameArray) {
            if (!StringUtils.isEmpty(each)) {
                boolean flag = ftpClient.makeDirectory(each);
                ftpClient.changeWorkingDirectory(each);
                if (!flag) {
                    logger.info("创建ftp目录失败，卡住的位置：" + each);
                }
            }
        }
    }

    /**
     * @param stream 流
     * @TODO: 关闭流
     */
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ex) {
                // do nothing
                ex.printStackTrace();
            }
        }
    }

    /**
     * @TODO: 关闭ftp连接
     */
    public static void disconnect() {
        if (null != ftpClient && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException ex) {
                // do nothing
                ex.printStackTrace();
            }
        }
    }

    /********************************* 新上传/下载方法 *******************************/
    /**
     * 从FTP下载文件到本地
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @throws IOException
     */
    public static Boolean downloadFile(String directory, String downloadFile, String saveFile) {
        //文件下载成功标识
        boolean flag = false;
        logger.info("开始文件下载---------->>>>>ftp目录：" + directory + "------下载到本地目录：" + saveFile + "------------文件名：" + downloadFile);
        if (!saveFile.endsWith("/")) {
            saveFile = saveFile + "/";
        }
        try {
            Boolean loginFlag = login();
            if (!loginFlag) {
                logger.error("ftp登录失败");
                return flag;
            }
            //进入ftp目录
            boolean cdFlag = changeWorkingDirectory(directory);
            if (!cdFlag) {
                logger.error("进入ftp目录失败，该目录不存在！！！");
                return flag;
            }
            //若本地没有目录，则直接创建
            File fileDir = new File(saveFile);
            logger.info("创建文件时------------------------------------------------->>>>>" + downloadFile);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            //判断文件是否存在
            FTPFile[] files = ftpClient.listFiles(downloadFile);
            if (files == null || files.length <= 0) {
                logger.info("该文件不存在！！！");
                return flag;
            }

            // 获取ftp上的文件
            boolean loadFlag = ftpClient.retrieveFile(downloadFile, new FileOutputStream(new File(saveFile + downloadFile)));
            if (loadFlag) {
                logger.info("文件下载成功---------->>>>>文件名：" + downloadFile);
                logger.info("FTP文件下载成功！");
                flag = true;
            } else {
                logger.info("文件下载失败---------->>>>>文件名：" + downloadFile);
            }
            ftpClient.completePendingCommand();
        } catch (Exception e) {
            logger.error("FTP文件下载失败！", e);
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return flag;
    }

    /**
     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
     *
     * @param directory    上传到该目录
     * @param sftpFileName sftp端文件名
     * @param input        输入流
     */
    public static void uploadFile(String basePath, String directory, String sftpFileName, InputStream input) {

        //上传到该ftp目录下
        String ftpPath = directory;
        logger.info("开始文件上传---------->>>>>ftp目录：" + ftpPath + "------------文件名：" + sftpFileName);
        try {
            Boolean loginFlag = login();
            if (!loginFlag) {
                logger.error("ftp登录失败");
            }
            //进入ftp(基础)目录
            boolean cdBaseFlag = changeWorkingDirectory(basePath);
            if (!cdBaseFlag) {
                logger.error("该目录(基础目录)不存在！！！");
                logger.info("");
                return;
            }
            //进入ftp(具体)目录
            boolean cdDirFlag = changeWorkingDirectory(ftpPath);
            if (!cdDirFlag) {
                logger.error("该目录(具体目录)不存在，开始创建目录！！！");
                makeDirs(ftpPath);
            }
            //获取ftp上的文件
            boolean storeFlag = ftpClient.storeFile(sftpFileName, input);
            if (storeFlag) {
                logger.info("文件上传成功---------->>>>>文件名：" + sftpFileName);
                logger.info("FTP文件上传成功！");
            } else {
                logger.info("文件上传失败---------->>>>>文件名：" + sftpFileName);
            }
        } catch (Exception e) {
            logger.error("FTP文件上传失败！", e);
        } finally {
            disconnect();
        }
    }

    /**
     * 创建文件夹（若存在则不做操作）（因创建目录较多，这里不执行登录及断开连接操作）
     *
     * @param dir 文件夹目录（ftp登录后目录）
     * @date: 2019-08-14 11:26:41
     */
    public static void createFolder(String dir) {
        logger.info("开始文件夹创建---------->>>>>需要创建的目录：" + dir);
        try {
            Boolean loginFlag = login();
            if (!loginFlag) {
                logger.error("ftp登录失败");
            }
            //进入ftp(具体)目录
            boolean cdDirFlag = changeWorkingDirectory(dir);
            if (!cdDirFlag) {
                logger.info("该目录(具体目录)不存在，开始创建目录！！！");
                makeDirs(dir);
            } else {
                logger.info("该目录已存在，无需创建！！！");
            }
        } catch (Exception e) {
            logger.error("FTP文件目录创建失败！", e);
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public static void main(String[] args) throws IOException {
//		makeDirectory("test");
        FtpUtil ftpUtil = new FtpUtil("192.168.1.120", "test", "test");
        FileInputStream fileInputStream = new FileInputStream("D:\\data\\AFIS_QUALITY_CCRW_2021_07_21_14_11_37.txt");
        uploadFile("test/", "test", "test.txt", fileInputStream);
//		downloadFile("test\\text","test.txt","G:\\ftpTest");
    }
}
