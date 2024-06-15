package com.zcode.zjw.security.pojo;

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
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态
     */
    private String status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 性别
     */
    private String sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 创建者
     */
    private Integer createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private Integer updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标识
     */
    private Integer delFlag;

    /**
     * 角色
     */
    private GhdqsOampRoleDTO role;

    /**
     * 旧密码
     */
    private String oldPwd;

    /**
     * 是否自动登录
     */
    private Boolean autoLoginFlag;

}
