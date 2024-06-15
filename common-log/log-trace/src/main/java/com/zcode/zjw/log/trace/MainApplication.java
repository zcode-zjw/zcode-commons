package com.zcode.zjw.log.trace;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@SpringBootApplication
@MapperScan({"com.zcode.zjw.**.mapper"})
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class);
    }

}
