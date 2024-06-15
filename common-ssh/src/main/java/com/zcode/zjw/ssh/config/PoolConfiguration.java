package com.zcode.zjw.ssh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PoolConfiguration {

    @Value("${ssh.strictHostKeyChecking:no}")
    private String strictHostKeyChecking;

    @Value("${ssh.timeout:30000}")
    private Integer timeout;

    @Value("${ssh.incrementalConnections:2}")
    private Integer incrementalConnections;

    @Value("${ssh.maxConnections:10}")
    private Integer maxConnections;

    @Bean
    public ConnectionPool connectionPool(){
        return new ConnectionPool(strictHostKeyChecking, timeout,incrementalConnections,maxConnections);
    }

}


