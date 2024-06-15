package com.zcode.zjw.ssh.utils;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author zhangjiwei
 * @description SSH连接工具类
 * @date 2022/11/14 下午8:09
 */
public class SSHClientUtil {

    /**
     * Server Host IP Address，default value is localhost
     */
    private String host = "";

    /**
     * Server SSH Port，default value is 22
     */
    private Integer port = 22;

    /**
     * SSH Login Username
     */
    private String username = "";

    /**
     * SSH Login Password
     */
    private String password = "";

    /**
     * SSH Login pubKeyPath，这里如果要用密钥登录的话，是你本机的或者项目所在服务器的私钥地址
     */

    private String pubKeyPath = "";

    /**
     * JSch
     */
    private JSch jsch = null;

    /**
     * ssh session
     */
    private Session session = null;

    /**
     * ssh channel
     */
    private Channel channel = null;

    /**
     * timeout for session connection
     */
    private final Integer SESSION_TIMEOUT = 60000;

    /**
     * timeout for channel connection
     */
    private final Integer CHANNEL_TIMEOUT = 60000;

    /**
     * the interval for acquiring ret
     */
    private final Integer CYCLE_TIME = 100;

    public SSHClientUtil() throws JSchException {
        // initialize
        jsch = new JSch();
    }

    public SSHClientUtil setHost(String host) {
        this.host = host;
        return this;
    }

    public SSHClientUtil setPort(Integer port) {
        this.port = port;
        return this;
    }

    public SSHClientUtil setUsername(String username) {
        this.username = username;
        return this;
    }

    public SSHClientUtil setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) throws JSchException {
        this.pubKeyPath = pubKeyPath;
        jsch.addIdentity(pubKeyPath);
    }

    public Session getSession() {
        return this.session;
    }

    public Channel getChannel() {
        return this.channel;
    }

    /**
     * login to server
     */
    public void login(String username, String host, Integer port, String password) {
        this.username = username;
        this.host = host;
        this.port = port;
        //如果是免密登录，不用密码
        this.password = password;

        try {
            if (null == session) {
                session = jsch.getSession(this.username, this.host, this.port);
                session.setConfig("StrictHostKeyChecking", "no");
            }
            session.connect(SESSION_TIMEOUT);
        } catch (JSchException e) {
            this.logout();
        }
    }

    /**
     * login to server
     */
    public void login() {
        this.login(this.username, this.host, this.port, this.password);
    }

    /**
     * logout of server
     */
    public void logout() {
        this.session.disconnect();
    }

    /**
     * send command through the ssh session,return the ret of the channel
     *
     * @return
     */
    public synchronized String sendCmd(String command) {

        // judge whether the session or channel is connected
        if (!session.isConnected()) {
            this.login();
        }
        if (this.session == null)
            return null;
        Channel channel = null;
        //InputStream input = null;
        BufferedReader bufferedReader = null;
        String resp = "";
        try {
            channel = this.session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            channel.connect();

            bufferedReader = new BufferedReader(new InputStreamReader(channel.getInputStream()));

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                resp += line + "\n";
            }
            if (resp != null && !resp.equals("")) {
                resp = resp.substring(0, resp.length() - 1);
            }
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
        return resp;
    }

    //写个main方法测试一下效果
    public static void main(String[] args) throws JSchException {
        SSHClientUtil sshClient = new SSHClientUtil();
        sshClient.setHost("192.168.1.186").setPort(22).setUsername("hdqs");
        sshClient.login();
        String command = "ls";
        String res = sshClient.sendCmd(command).trim();
        System.out.println("******************************");
        System.out.println(res);
        System.out.println("******************************");
        sshClient.logout();
    }
}
