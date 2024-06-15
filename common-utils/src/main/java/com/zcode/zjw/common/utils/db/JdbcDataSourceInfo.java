package com.zcode.zjw.common.utils.db;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * jdbc数据源信息实体
 *
 * @author zhangjiwei
 * @since 2023/7/27
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName(value = "etl_jdbc_datasource_info", autoResultMap = true)
public class JdbcDataSourceInfo implements Serializable {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String url;

    private String host;

    private Integer port;

    private String dbName;

    private String name;

    private String username;

    private String password;

    private String schemaName;

    private String sourceType;

    private Integer status;

    @TableField(value = "attr_params", typeHandler = JacksonTypeHandler.class)
    private JSONObject jsonParams;

}
