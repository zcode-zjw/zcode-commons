package com.zcode.zjw.auth.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

@Data
@TableName("t_permission")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Permission {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 对应role中的permissionId
     */
    private Long permissionId;

    private String method;

    private String module;

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

    public Permission(String method, String module, String name) {
        this.method = method;
        this.module = module;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(method, that.method) && Objects.equals(module, that.module) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, module, name);
    }

}