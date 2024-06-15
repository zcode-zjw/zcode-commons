package com.zcode.zjw.common.file.ftp.service.impl;

import com.zcode.zjw.common.file.ftp.config.FtpPoolConfig;
import com.zcode.zjw.common.file.ftp.core.FTPClientFactory;
import com.zcode.zjw.common.file.ftp.core.FTPClientPool;
import com.zcode.zjw.common.file.ftp.service.FtpFileService;
import com.zcode.zjw.common.file.ftp.util.FtpClientUtils;
import com.zcode.zjw.configs.common.ZcodeConfig;
import com.zcode.zjw.configs.config.ZcodeConfigurationSelector;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Optional;

/**
 * Ftp服务实现
 *
 * @author zhangjiwei
 * @date 2023/6/2
 */
@Service
public class FtpFileServiceImpl implements FtpFileService {

    private ZcodeConfig zcodeConfig;

    @Resource
    private ZcodeConfigurationSelector zcodeConfigurationSelector;

    public static void main(String[] args) throws Exception {
        //配置信息
        FtpPoolConfig cfg = new FtpPoolConfig();
        cfg.setHost("127.0.0.1");
        cfg.setPort(21);
        cfg.setUsername("demo");
        cfg.setPassword("123123");
        cfg.setPoolEvictInterval(30000);
        FTPClientFactory factory = new FTPClientFactory(cfg);//对象工厂
        FTPClientPool pool = new FTPClientPool(factory);//连接池对象
        //创建工具对象
        FtpClientUtils ftp = new FtpClientUtils(pool);

        int t1 = ftp.mkdirs("/data/imgs"); //在FTP的工作目录下创建多层目录
        InputStream in = new FileInputStream("D:/001.jpg"); //读取一个本地文件
        int t2 = ftp.store(in, "/data/imgs/", "main.jpg");//上传到FTP服务器
        int t3 = ftp.retrieve("/data/imgs/main.jpg", new FileOutputStream("D:/002.jpg"));//从FTP服务器取回文件
        ftp.delete("/data/imgs/2018/09/29/main.jpg"); //删除FTP服务器中的文件

        System.out.println("目录耗时：" + t1);
        System.out.println("上传耗时：" + t2);
        System.out.println("下载耗时：" + t3);

        //FTPClient c = pool.borrowObject();//从池子中借一个FTPClient对象
        //pool.returnObject(c);//把对象归还给池子
    }

    @Override
    public FtpClientUtils getFtpClient() {
        zcodeConfig = zcodeConfigurationSelector.getZcodeConfig();
        //配置信息
        FtpPoolConfig cfg = new FtpPoolConfig();
        cfg.setHost(Optional.ofNullable(zcodeConfig.getFtp().getHost()).orElse("127.0.0.1"));
        cfg.setPort(Optional.ofNullable(zcodeConfig.getFtp().getPort()).orElse(21));
        cfg.setUsername(Optional.ofNullable(zcodeConfig.getFtp().getUsername()).orElse("admin"));
        cfg.setPassword(Optional.ofNullable(zcodeConfig.getFtp().getPassword()).orElse("admin"));
        cfg.setPoolEvictInterval(Optional.ofNullable(zcodeConfig.getFtp().getPoolEvictInterval()).orElse(30000));
        FTPClientFactory factory = new FTPClientFactory(cfg); // 对象工厂
        FTPClientPool pool = new FTPClientPool(factory); // 连接池对象
        // 创建工具对象
        return new FtpClientUtils(pool);
    }
}