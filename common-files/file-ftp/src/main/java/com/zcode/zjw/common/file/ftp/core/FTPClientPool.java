package com.zcode.zjw.common.file.ftp.core;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class FTPClientPool {

    private static Logger logger = LoggerFactory.getLogger(FTPClientPool.class);
    Timer timer = new Timer();

    //ftp客户端连接池
    private GenericObjectPool<FTPClient> pool;

    //ftp客户端工厂
    private FTPClientFactory clientFactory;

    /**
     * 构造函数中 注入一个bean
     *
     * @param clientFactory
     */
    public FTPClientPool(FTPClientFactory clientFactory) {
        this.clientFactory = clientFactory;
        pool = new GenericObjectPool<>(clientFactory, clientFactory.getFtpPoolConfig());
        pool.setTestWhileIdle(true); //启动空闲检测
        //30秒间隔的心跳检测
        long period = clientFactory.getFtpPoolConfig().getPoolEvictInterval();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    pool.evict();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000, period);

    }

    public FTPClientFactory getClientFactory() {
        return clientFactory;
    }

    public GenericObjectPool<FTPClient> getPool() {
        return pool;
    }

    /**
     * 从池子中借一个连接对象
     *
     * @return (借来的FTPClient对象)
     * @throws Exception (异常)
     */
    public FTPClient borrowObject() throws Exception {
        return pool.borrowObject();
    }

    /**
     * 归还一个连接对象到池子中
     *
     * @param ftpClient (归还的连接)
     */
    public void returnObject(FTPClient ftpClient) {
        if (ftpClient != null) {
            pool.returnObject(ftpClient);
        }
    }
}