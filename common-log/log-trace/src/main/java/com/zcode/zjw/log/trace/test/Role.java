package com.zcode.zjw.log.trace.test;

import com.zcode.zjw.log.trace.test.User;
import lombok.Data;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@Data
public class Role {

    private User user;

    private Long id;

    private String name;

}
