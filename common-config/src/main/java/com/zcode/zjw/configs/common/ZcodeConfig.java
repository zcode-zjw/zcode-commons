package com.zcode.zjw.configs.common;

import com.zcode.zjw.configs.properties.FtpFileProperties;
import com.zcode.zjw.configs.properties.SSHProperties;
import com.zcode.zjw.configs.properties.SwaggerProperties;

/**
 * Zcode配置接口
 *
 * @author zhangjiwei
 * @date 2023/6/3
 */
public interface ZcodeConfig {

     Boolean getPrintSql();

     String getApiWhiteList();

     String getWebIgnored();

     String getOwnSiteDir();

     String getNodeType();

     SwaggerProperties getSwagger();

     FtpFileProperties getFtp();

     SSHProperties getSsh();

}
