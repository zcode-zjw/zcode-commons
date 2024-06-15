package com.zcode.zjw.log.trace.domain.request.repository.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 接口请求记录
 *
 * @author zhangjiwei
 * @since 2023/8/9
 */
@Data
@TableName(value = "zcode_api_request_record", autoResultMap = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequestRecord implements Serializable {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 请求URL
     */
    private String url;

    /**
     * 请求头信息
     */
    @TableField(value = "headers", typeHandler = JacksonTypeHandler.class)
    private String headers;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求参数
     */
    @TableField(value = "params", typeHandler = JacksonTypeHandler.class)
    private String params;

    /**
     * 请求时间
     */
    private String requestDatetime;

    /**
     * 接口耗时
     */
    private String cost;

    /**
     * 远程请求地址
     */
    private String remoteAddress;

    /**
     * 被记录的Java类以及方法名称
     */
    private String recordClassMethodFlag;

    /**
     * 响应的结果
     */
    @TableField(value = "response_res", typeHandler = JacksonTypeHandler.class)
    private String responseRes;

    /**
     * 请求成功状态（成功：SUC， 失败：ERR）
     */
    private String status;

}
