package com.zcode.zjw.security.controller;

import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampUserRole;
import com.zcode.zjw.security.service.GhdqsOampUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GhdqsOampUserRole的路由接口服务
 *
 * @author zhangjiwei
 */
@RestController
@Api(tags = "系统用户角色关联接口")
public class GhdqsOampUserRoleController {

    /**
     * GhdqsOampUserRoleService服务
     */
    @Autowired
    private GhdqsOampUserRoleService ghdqsOampUserRoleService;

    /**
     * 查询所有GhdqsOampUserRole数据的方法
     *
     * @param value
     * @return
     */
    @GetMapping(value = "/GhdqsOampUserRole", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("查询所有系统用户角色关联数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "系统用户角色关联实体", dataType = "GhdqsOampUserRole", paramType = "query", required = true)
    })
    public Result<?> find(GhdqsOampUserRole value) {
        return ghdqsOampUserRoleService.find(value);
    }


    /**
     * 插入GhdqsOampUserRole属性不为空的数据方法
     *
     * @param value
     * @return
     */
    @ApiOperation("新增系统用户角色关联数据")
    @PostMapping(value = "/GhdqsOampUserRole", produces = {"application/json;charset=UTF-8"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "系统用户角色关联实体", dataType = "GhdqsOampUserRole", paramType = "data", required = true)
    })
    public Result<?> save(GhdqsOampUserRole value) {
        return ghdqsOampUserRoleService.saveNotNull(value);
    }

}
