package com.zcode.zjw.auth.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 角色权限实体
 *
 * @author zhangjiwei
 * @date 2023/5/24
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_role_permission")
public class RolePermission {

    private Long id;
    private Long roleId;
    private Long permissionId;

}
