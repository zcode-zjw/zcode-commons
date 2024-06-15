package com.zcode.zjw.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.auth.pojo.Role;
import com.zcode.zjw.auth.pojo.RoleFullInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    RoleFullInfoVO findRoleFullInfo(@Param("roleId") String roleId);

}