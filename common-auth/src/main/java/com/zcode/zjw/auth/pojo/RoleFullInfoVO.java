package com.zcode.zjw.auth.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 角色所属全信息响应对象
 *
 * @author zhangjiwei
 * @since 2023/7/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoleFullInfoVO extends Role implements Serializable {

    private List<SysMenu> menus;


}
