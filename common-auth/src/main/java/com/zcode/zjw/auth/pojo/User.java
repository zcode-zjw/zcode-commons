package com.zcode.zjw.auth.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zcode.zjw.datasource.annotation.EncryptTransaction;
import com.zcode.zjw.datasource.annotation.SensitiveData;
import com.zcode.zjw.web.datamask.annotation.DataMasking;
import com.zcode.zjw.web.datamask.common.DataMaskingFunc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户实体
 *
 * @author zhangjiwei
 * @date 2023/5/24
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SensitiveData
@TableName("t_user")
public class User {

    private Long id;

    private String username;

    @DataMasking(maskFunc = DataMaskingFunc.ALL_MASK)
    @EncryptTransaction
    private String password;

    private Integer userType;

    private String createTime;

    private String updateTime;

    private Integer deleted;

    private String icon;

    private String nickname;
    private String email;

    private String phone;

    private String address;

    private String property1;
    private String property2;
    private String property3;
    private String property4;
    private String property5;
    private String property6;
    private String property7;
    private String property8;
    private String property9;
    private String property10;
    private String property11;
    private String property12;
    private String property13;
    private String property14;
    private String property15;
    private String property16;
    private String property17;
    private String property18;
    private String property19;
    private String property20;

}
