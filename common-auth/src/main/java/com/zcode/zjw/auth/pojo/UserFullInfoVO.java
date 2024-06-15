package com.zcode.zjw.auth.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 用户所属全信息响应对象
 *
 * @author zhangjiwei
 * @since 2023/7/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserFullInfoVO extends User implements Serializable {

    private List<RoleFullInfoVO> roles;

    private String token;

}
