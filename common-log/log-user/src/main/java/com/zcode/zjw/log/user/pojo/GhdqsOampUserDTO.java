package com.zcode.zjw.log.user.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * GHDQS_OAMP_USER 数据转换对象
 *
 * @author zhangjiwei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GhdqsOampUserDTO implements Serializable {


    private static final long serialVersionUID = 6057908459279389484L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", dataType = "Integer", required = true)
    private Integer id;



    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", dataType = "String", required = true)
    private String userName;



    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", dataType = "String", required = true)
    private String nickName;



    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", dataType = "String", required = true)
    private String password;



    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", dataType = "String", required = true)
    private String status;



    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", dataType = "String", required = true)
    private String email;



    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", dataType = "String", required = true)
    private String phoneNumber;



    /**
     * 性别
     */
    @ApiModelProperty(value = "性别", dataType = "String", required = true)
    private String sex;



    /**
     * 头像
     */
    @ApiModelProperty(value = "头像", dataType = "String", required = true)
    private String avatar;



    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型", dataType = "String", required = true)
    private String userType;



    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者", dataType = "Integer", required = true)
    private Integer createBy;



    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", dataType = "Date", required = true)
    private Date createTime;



    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者", dataType = "Integer", required = true)
    private Integer updateBy;



    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", dataType = "Date", required = true)
    private Date updateTime;



    /**
     * 删除标识
     */
    @ApiModelProperty(value = "删除标识", dataType = "Integer", required = true)
    private Integer delFlag;



    /**
     * 角色
     */
    @ApiModelProperty(value = "角色", dataType = "GhdqsOampRoleDTO", required = true)
    private GhdqsOampRoleDTO role;



    /**
     * 旧密码
     */
    @ApiModelProperty(value = "旧密码", dataType = "String", required = true)
    private String oldPwd;



    /**
     * 是否自动登录
     */
    @ApiModelProperty(value = "是否自动登录", dataType = "Boolean", required = true)
    private Boolean autoLoginFlag;



}
