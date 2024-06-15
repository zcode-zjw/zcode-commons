package com.zcode.zjw.common.file.ftp.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Ftp连接池
 *
 * @author zhangjiwei
 * @date 2023/6/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FtpPoolConfig extends GenericObjectPoolConfig<FTPClient> {


    private String host;// 主机名
    private int port = 21;// 端口
    private String username;// 用户名
    private String password;// 密码
    private int connectTimeOut = 5000;// ftp 连接超时时间 毫秒
    private String controlEncoding = "utf-8";
    private int bufferSize = 1024;// 缓冲区大小
    private int fileType = 2;// 传输数据格式 2表binary二进制数据
    private int dataTimeout = 120000;
    private boolean useEPSVwithIPv4 = false;
    private boolean passiveMode = true;// 是否启用被动模式
    private long poolEvictInterval = 30000; //连接池空闲检测周期


}
