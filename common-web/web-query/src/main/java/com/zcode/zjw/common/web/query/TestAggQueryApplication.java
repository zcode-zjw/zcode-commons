package com.zcode.zjw.common.web.query;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/8/11
 */
@SpringBootApplication
@MapperScan({"com.zcode.zjw.**.mapper"})
public class TestAggQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestAggQueryApplication.class, args);
    }

}
