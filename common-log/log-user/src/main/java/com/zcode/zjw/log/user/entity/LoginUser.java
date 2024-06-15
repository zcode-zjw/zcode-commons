package com.zcode.zjw.log.user.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhangjiwei
 * @since 2022-11-13 17:11
 * 登录用户对应实体
 */
@Data
@NoArgsConstructor
public class LoginUser {

    private static final long serialVersionUID = 1545148487501999023L;

    @ApiModelProperty(value = "用户", dataType = "GhdqsOampUser", required = true)
    private GhdqsOampUser user;

    //存储权限信息
    @ApiModelProperty(value = "存储权限信息", dataType = "List<String>", required = true)
    private List<String> permissions;


    public LoginUser(GhdqsOampUser user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }
}