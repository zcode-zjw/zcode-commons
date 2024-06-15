package com.zcode.zjw.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.auth.pojo.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统菜单数据访问层对象
 *
 * @author zhangjiwei
 * @since 2023/7/23
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    @Select("SELECT m.*\n" +
            "FROM t_role r\n" +
            "LEFT JOIN etl_sys_menu_role_rel mr ON r.id = mr.role_id\n" +
            "LEFT JOIN etl_sys_menu m ON mr.menu_id = m.id\n" +
            "WHERE role_id = (\n" +
            "\tSELECT role_id FROM t_user u LEFT JOIN t_user_role ur ON u.id = ur.user_id WHERE user_id = #{userId}\n" +
            ")")
    List<SysMenu> findAllMenuForUser(@Param("userId") String userId);

    @Select("SELECT *\n" +
            "FROM etl_sys_menu r\n" +
            "WHERE id = #{menuId}")
    SysMenu findAllMenu(@Param("menuId") String menuId);

    @Select("SELECT m.*\n" +
            "FROM t_role r\n" +
            "LEFT JOIN etl_sys_menu_role_rel mr ON r.id = mr.role_id\n" +
            "LEFT JOIN etl_sys_menu m ON mr.menu_id = m.id\n" +
            "WHERE role_id = #{roleId}")
    List<SysMenu> findAllMenuByRoleId(@Param("roleId") String roleId);

}
