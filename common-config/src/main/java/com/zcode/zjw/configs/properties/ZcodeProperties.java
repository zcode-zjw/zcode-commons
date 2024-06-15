package com.zcode.zjw.configs.properties;

import com.zcode.zjw.configs.common.ZcodeConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhangjiwei
 * @description Zcode-common 配置文件对象
 * @date 2022/11/14 下午8:09
 */
@Configuration
@ConfigurationProperties(prefix = "zcode", ignoreUnknownFields = false)
@PropertySource(value = "${zcode-config.file.path:classpath:zcode.properties}", encoding = "UTF-8")
//@PropertySource("file:./config/zcode.properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZcodeProperties implements ZcodeConfig {

    /**
     * 是否打印SQl
     */
    private Boolean printSql;

    /**
     * Api白名单
     */
    private String apiWhiteList;

    private String ownSiteDir;

    private String nodeType;

    private SwaggerProperties swagger;

    private FtpFileProperties ftp;

    private SSHProperties ssh;

    private String webIgnored;
}