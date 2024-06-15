package com.zcode.zjw.auth.common;

import lombok.Getter;

import java.util.Objects;

/**
 * 用户角色枚举
 */
@Getter
public enum UserType {
    ADMIN(1, "管理员"),
    TEACHER(2, "教师"),
    STUDENT(3, "学生"),
    VISITOR(4, "游客");

    private final Integer value;
    private final String desc;

    UserType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 是否有权限
     *
     * @param userTypes
     * @param typeOfUser
     * @param logical
     * @return
     */
    public static boolean hasPermission(UserType[] userTypes, Integer typeOfUser, Logical logical) {
        Objects.requireNonNull(userTypes);
        Objects.requireNonNull(logical);

        // 1.1 要求单个角色权限，且当前用户刚好是这个角色
        if (userTypes.length == 1 && userTypes[0].getValue().equals(typeOfUser)) {
            return true;
        }
        // 1.2 要求多个角色权限
        if (userTypes.length > 1) {
            // AND：预留，当前设计其实不支持一个用户有多个角色
            if (Logical.AND.equals(logical)) {
                return false;
            }

            // OR：只要用户拥有其中一个角色即可
            if (Logical.OR.equals(logical)) {
                for (UserType type : userTypes) {
                    if (type.getValue().equals(typeOfUser)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}