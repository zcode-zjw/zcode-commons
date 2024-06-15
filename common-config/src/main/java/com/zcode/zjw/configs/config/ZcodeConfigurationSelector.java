package com.zcode.zjw.configs.config;

import com.zcode.zjw.configs.common.ZcodeConfig;
import com.zcode.zjw.configs.db.ZcodeDbConfig;
import com.zcode.zjw.configs.properties.ZcodeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Zcode配置类
 *
 * @author zhangjiwei
 * @date 2023/6/3
 */
@Component
public class ZcodeConfigurationSelector {

    @Value("${zcode-config.type:file}")
    private String type;

    @Autowired
    private ZcodeProperties zcodeProperties;

    @Autowired
    private ZcodeDbConfig zcodeDbConfig;

    public ZcodeConfig getZcodeConfig() {
        if (type.equals("db")) {
            return zcodeDbConfig;
        } else {
            return zcodeProperties;
        }
    }

}
