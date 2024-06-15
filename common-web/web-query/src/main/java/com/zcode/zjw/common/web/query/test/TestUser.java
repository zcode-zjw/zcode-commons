package com.zcode.zjw.common.web.query.test;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zcode.zjw.common.web.query.annotation.AggregateQueryField;
import com.zcode.zjw.common.web.query.annotation.AggregateQueryRegexMode;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/8/11
 */
@TableName("t_user")
@Data
@AggregateQueryField
public class TestUser implements Serializable {

    @AggregateQueryField
    private Long id;

    @AggregateQueryField(regexMode = AggregateQueryRegexMode.FUZZY)
    private String username;

    private String password;

    @AggregateQueryField(condition = true)
    private Integer userType;

    private String createTime;

    private String updateTime;

    private Integer deleted;

    private String icon;

    private String nickname;
    private String email;

    private String phone;

    private String address;

}
