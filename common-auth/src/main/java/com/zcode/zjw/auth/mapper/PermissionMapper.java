package com.zcode.zjw.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.auth.pojo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> selectBatchIds(@Param("permissionIds") List<Long> permissionIds);

}