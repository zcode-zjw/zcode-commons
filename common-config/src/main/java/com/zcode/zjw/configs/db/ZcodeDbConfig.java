package com.zcode.zjw.configs.db;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcode.zjw.common.utils.bean.SpringContextUtils;
import com.zcode.zjw.configs.common.ZcodeConfig;
import com.zcode.zjw.configs.mapper.ZcodeConfigParamsMapper;
import com.zcode.zjw.configs.properties.FtpFileProperties;
import com.zcode.zjw.configs.properties.SSHProperties;
import com.zcode.zjw.configs.properties.SwaggerProperties;
import org.springframework.stereotype.Component;

/**
 * 描述
 *
 * @author zhangjiwei
 * @date 2023/6/3
 */
@Component
public class ZcodeDbConfig implements ZcodeConfig {

    @Override
    public Boolean getPrintSql() {
        QueryWrapper<ZcodeConfigParams> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("pkey", "print_sql");
        ZcodeConfigParamsMapper zcodeConfigParamsMapper = (ZcodeConfigParamsMapper) SpringContextUtils.getBean("zcodeConfigParamsMapper");
        String value = zcodeConfigParamsMapper.selectOne(objectQueryWrapper).getPval();
        return value.equals("true");
    }

    @Override
    public String getApiWhiteList() {
        QueryWrapper<ZcodeConfigParams> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("pkey", "api_white_list");
        ZcodeConfigParamsMapper zcodeConfigParamsMapper = (ZcodeConfigParamsMapper) SpringContextUtils.getBean("zcodeConfigParamsMapper");
        return zcodeConfigParamsMapper.selectOne(objectQueryWrapper).getPval();
    }

    @Override
    public String getWebIgnored() {
        QueryWrapper<ZcodeConfigParams> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("pkey", "web_ignored");
        ZcodeConfigParamsMapper zcodeConfigParamsMapper = (ZcodeConfigParamsMapper) SpringContextUtils.getBean("zcodeConfigParamsMapper");
        return zcodeConfigParamsMapper.selectOne(objectQueryWrapper).getPval();
    }

    @Override
    public String getOwnSiteDir() {
        QueryWrapper<ZcodeConfigParams> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("pkey", "own_site_dir");
        ZcodeConfigParamsMapper zcodeConfigParamsMapper = (ZcodeConfigParamsMapper) SpringContextUtils.getBean("zcodeConfigParamsMapper");
        return zcodeConfigParamsMapper.selectOne(objectQueryWrapper).getPval();
    }

    @Override
    public String getNodeType() {
        QueryWrapper<ZcodeConfigParams> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("pkey", "node_type");
        ZcodeConfigParamsMapper zcodeConfigParamsMapper = (ZcodeConfigParamsMapper) SpringContextUtils.getBean("zcodeConfigParamsMapper");
        return zcodeConfigParamsMapper.selectOne(objectQueryWrapper).getPval();
    }

    @Override
    public SwaggerProperties getSwagger() {
        QueryWrapper<ZcodeConfigParams> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("pkey", "swagger");
        ZcodeConfigParamsMapper zcodeConfigParamsMapper = (ZcodeConfigParamsMapper) SpringContextUtils.getBean("zcodeConfigParamsMapper");
        String value = zcodeConfigParamsMapper.selectOne(objectQueryWrapper).getPval();
        JSONObject jsonObject = JSONUtil.parseObj(value);
        SwaggerProperties swaggerProperties = new SwaggerProperties();
        swaggerProperties.setTitle((String) jsonObject.get("title"));
        swaggerProperties.setVersion((String) jsonObject.get("version"));
        swaggerProperties.setDescription((String) jsonObject.get("description"));
        return swaggerProperties;
    }

    @Override
    public FtpFileProperties getFtp() {
        QueryWrapper<ZcodeConfigParams> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("pkey", "ftp");
        ZcodeConfigParamsMapper zcodeConfigParamsMapper = (ZcodeConfigParamsMapper) SpringContextUtils.getBean("zcodeConfigParamsMapper");
        String value = zcodeConfigParamsMapper.selectOne(objectQueryWrapper).getPval();
        JSONObject jsonObject = JSONUtil.parseObj(value);
        FtpFileProperties ftpFileProperties = new FtpFileProperties();
        ftpFileProperties.setHost((String) jsonObject.get("host"));
        if (jsonObject.get("port") != null) {
            ftpFileProperties.setPort((Integer) jsonObject.get("port"));
        }
        if (jsonObject.get("username") != null) {
            ftpFileProperties.setUsername((String) jsonObject.get("username"));
        }
        if (jsonObject.get("password") != null) {
            ftpFileProperties.setPassword((String) jsonObject.get("password"));
        }
        if (jsonObject.get("poolEvictInterval") != null) {
            ftpFileProperties.setPoolEvictInterval((Integer) jsonObject.get("poolEvictInterval"));
        }
        return ftpFileProperties;
    }

    @Override
    public SSHProperties getSsh() {
        ZcodeConfigParamsMapper zcodeConfigParamsMapper = (ZcodeConfigParamsMapper) SpringContextUtils.getBean("zcodeConfigParamsMapper");
        QueryWrapper<ZcodeConfigParams> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("pkey", "ssh");
        String value = zcodeConfigParamsMapper.selectOne(objectQueryWrapper).getPval();
        JSONObject jsonObject = JSONUtil.parseObj(value);
        SSHProperties sshProperties = new SSHProperties();
        sshProperties.setHost((String) jsonObject.get("host"));
        if (jsonObject.get("port") != null) {
            sshProperties.setPort((Integer) jsonObject.get("port"));
        }
        if (jsonObject.get("username") != null) {
            sshProperties.setUsername((String) jsonObject.get("username"));
        }
        if (jsonObject.get("password") != null) {
            sshProperties.setPassword((String) jsonObject.get("password"));
        }
        if (jsonObject.get("maxIdle") != null) {
            sshProperties.setMaxIdle((Integer) jsonObject.get("maxIdle"));
        }
        if (jsonObject.get("incrementalConnections") != null) {
            sshProperties.setIncrementalConnections((Integer) jsonObject.get("incrementalConnections"));
        }
        if (jsonObject.get("minIdel") != null) {
            sshProperties.setMinIdel((Integer) jsonObject.get("minIdel"));
        }
        if (jsonObject.get("maxConnections") != null) {
            sshProperties.setMaxConnections((Integer) jsonObject.get("maxConnections"));
        }
        return sshProperties;
    }
}
