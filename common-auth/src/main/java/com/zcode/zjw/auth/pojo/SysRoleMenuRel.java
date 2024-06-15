package com.zcode.zjw.auth.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 系统角色菜单关系表实体
 *
 * @author zhangjiwei
 * @since 2023/7/23
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("etl_sys_role_menu_rel")
public class SysRoleMenuRel implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long menuId;

    private Long roleId;


}
