package com.zcode.zjw.auth.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class UserLogDTO {

    private Long id;

    private String moduleCode;

    private Integer type;

    private String title;

    private Long operatorId;

    private Date operateTime;

    private String content;

}