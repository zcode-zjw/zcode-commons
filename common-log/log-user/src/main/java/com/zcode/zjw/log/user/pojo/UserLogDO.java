package com.zcode.zjw.log.user.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zhangjiwei
 */
@Data
@TableName("ghdqs_oamp_user_log")
public class UserLogDO {

    private Long id;

    /**
     * 本次操作的系统模块
     */
    @ApiModelProperty(value = "本次操作的系统模块", dataType = "String", required = true)
    private String moduleCode;


    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型", dataType = "Integer", required = true)
    private Integer type;


    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", dataType = "String", required = true)
    private String title;


    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人", dataType = "Long", required = true)
    private Long operatorId;


    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间", dataType = "Date", required = true)
    private Date operateTime;


    /**
     * 操作内容
     */
    @ApiModelProperty(value = "操作内容", dataType = "String", required = true)
    private String content;



}