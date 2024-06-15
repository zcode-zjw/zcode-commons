package com.zcode.zjw.log.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangjiwei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLogDTO implements Serializable {

    private Long id;

    private String moduleCode;

    private Integer type;

    private String title;

    private Integer operatorId;

    private Date operateTime;

    private String content;

    private String username;

}