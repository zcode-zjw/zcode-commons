package com.zcode.zjw.common.file.upload.chuck;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author zhangjiwei
 * @since 2023/8/30
 */
@SpringBootApplication
@MapperScan({"com.zcode.zjw.**.mapper"})
public class ChuckFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChuckFileApplication.class, args);
    }

}
