package com.zcode.zjw.timejob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/7/18
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TestTimeJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestTimeJobApplication.class, args);
    }

}
