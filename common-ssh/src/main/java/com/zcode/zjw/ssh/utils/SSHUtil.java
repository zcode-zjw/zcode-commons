package com.zcode.zjw.ssh.utils;

import com.jcraft.jsch.*;
import com.zcode.zjw.ssh.config.ConnectionPool;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class SSHUtil {

    /**
     * ip地址
     */
    protected String ip;

    /**
     * 端口号
     */
    protected Integer port;

    /**
     * 用户名
     */
    protected String username;

    /**
     * 密码
     */
    protected String password;

    /**
     * 秘钥地址
     */
    protected String privateKey;

    private Session session;

    private Channel channel;

    private ChannelExec channelExec;

    private ChannelSftp channelSftp;

    private ChannelShell channelShell;

    private ConnectionPool connectionPool;

    public abstract void initSSHInfo();

    /**
     * 初始化
     *
     * @param ip       远程主机IP地址
     * @param port     远程主机端口
     * @param username 远程主机登陆用户名
     * @param password 远程主机登陆密码
     */
    public void init(String ip, Integer port, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public void init(String ip, String username, String password) {
        this.ip = ip;
        this.username = username;
        this.password = password;
    }

    private void getSession() throws JSchException {
        if (username == null || (password == null && privateKey == null)) {
            log.warn("无法创建ssh连接，正在使用本地环境！");
            return;
        }
        if (privateKey == null || "".equals(privateKey.trim())) {
            session = connectionPool.getConnection(ip, port, username, password);
        } else {
            session = connectionPool.getConnectionByPrivateKey(ip, port, username, privateKey);
        }
        if (Objects.isNull(session)) {
            connectionPool.refreshConnections();
            session = connectionPool.getConnection(ip, port, username, password);
            if (Objects.isNull(session)) {
                throw new RuntimeException("无可用连接");
            }
        }
    }

    /**
     * 连接多次执行命令，执行命令完毕后需要执行close()方法
     *
     * @param command 需要执行的指令
     * @return 执行结果
     * @throws Exception 没有执行初始化
     */
    public String execCmd(String command) throws Exception {
        initChannelExec();
        channelExec.setCommand(command);
        channel.setInputStream(null);
        channelExec.setErrStream(System.err);
        channel.connect();
        StringBuilder sb = new StringBuilder(16);
        try (InputStream in = channelExec.getInputStream();
             InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                sb.append("\n").append(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();

    }


    /**
     * 执行命令关闭连接
     *
     * @param command 需要执行的指令
     * @return 执行结果
     * @throws Exception 没有执行初始化
     */
    public String execCmdAndClose(String command) {
        String result = null;
        try {
            result = execCmd(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return result;
    }

    /**
     * 执行复杂shell命令
     *
     * @param commands 多条命令
     * @return 执行结果
     * @throws Exception 连接异常
     */
    public String execCmdByShell(String... commands) throws Exception {
        List<String> cmdList = Arrays.asList(commands);
        cmdList.add(0, "source /etc/profile && source ~/.bash_profile && ");
        return execCmdByShell(cmdList);
    }

    /**
     * 执行复杂shell命令
     *
     * @param cmds 多条命令
     * @return 执行结果
     * @throws Exception 连接异常
     */
    public String execCmdByShell(List<String> cmds) throws Exception {
        String result = "";
        initChannelShell();
        InputStream inputStream = channelShell.getInputStream();
        channelShell.setPty(true);
        channelShell.connect();

        OutputStream outputStream = channelShell.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        cmds.add(0, "source /etc/profile && source ~/.bash_profile && ");
        for (String cmd : cmds) {
            printWriter.println(cmd);
        }
        printWriter.flush();

        byte[] tmp = new byte[1024];
        while (true) {
            while (inputStream.available() > 0) {
                int i = inputStream.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                String s = new String(tmp, 0, i);
                if (s.contains("--More--")) {
                    outputStream.write((" ").getBytes());
                    outputStream.flush();
                }
            }
            if (channelShell.isClosed()) {
                log.info("exit-status:" + channelShell.getExitStatus());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        outputStream.close();
        inputStream.close();
        close();
        return result;
    }

    /**
     * 删除指定目录文件
     *
     * @param path 删除路径
     * @throws Exception 远程主机连接异常
     */
    public void deleteFile(String path) throws Exception {
        initChannelSftp();
        channelSftp.rm(path);
        log.info("Delete File {}", path);
        close();
    }

    /**
     * 删除指定目录
     *
     * @param path 删除路径
     * @throws Exception 远程主机连接异常
     */
    public void deleteDir(String path) throws Exception {
        initChannelSftp();
        channelSftp.rmdir(path);
        log.info("Delete Dir {} ", path);
        close();
    }

    /**
     * 释放资源
     */
    public void close() {
        connectionPool.returnConnection(session);
    }

    private void initChannelSftp() throws Exception {
        getSession();
        channel = session.openChannel("sftp");
        channel.connect(); // 建立SFTP通道的连接
        channelSftp = (ChannelSftp) channel;
        if (session == null || channel == null || channelSftp == null) {
            log.error("请先执行init()");
            throw new Exception("请先执行init()");
        }
    }

    private void initChannelExec() throws Exception {
        getSession();
        // 打开执行shell指令的通道
        channel = session.openChannel("exec");
        channelExec = (ChannelExec) channel;
        if (session == null || channel == null || channelExec == null) {
            log.error("请先执行init()");
            throw new Exception("请先执行init()");
        }
    }

    private void initChannelShell() throws Exception {
        getSession();
        // 打开执行shell指令的通道
        channel = session.openChannel("shell");
        channelShell = (ChannelShell) channel;
        if (session == null || channel == null || channelShell == null) {
            log.error("请先执行init()");
            throw new Exception("请先执行init()");
        }
    }


}

