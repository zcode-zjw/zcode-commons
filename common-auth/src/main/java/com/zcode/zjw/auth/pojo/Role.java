package com.zcode.zjw.auth.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_role")
public class Role {
    private Long id;

    private String role;

    private String name;

    private Date createTime;

    private Date updateTime;

    private Boolean deleted;

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