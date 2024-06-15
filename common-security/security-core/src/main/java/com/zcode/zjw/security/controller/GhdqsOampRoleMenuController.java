package com.zcode.zjw.security.controller;

import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampRoleMenu;
import com.zcode.zjw.security.service.GhdqsOampRoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * GhdqsOampRoleMenu的路由接口服务
 *
 * @author zhangjiwei
 */
@RestController
@Api(tags = "系统角色菜单关联接口")
public class GhdqsOampRoleMenuController {

    /**
     * GhdqsOampRoleMenuService服务
     */
    @Autowired
    private GhdqsOampRoleMenuService ghdqsOampRoleMenuService;

    /**
     * 查询所有GhdqsOampRoleMenu数据的方法
     *
     * @param value
     * @return
     */
    @GetMapping(value = "/GhdqsOampRoleMenu", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("查询所有系统角色菜单关联信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "系统角色菜单关联", dataType = "GhdqsOampRoleMenu", paramType = "data", required = true)
    })
    public Result<?> find(GhdqsOampRoleMenu value) {
        return ghdqsOampRoleMenuService.find(value);
    }

    /**
     * 通过id查询GhdqsOampRoleMenu数据的方法
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/GhdqsOampRoleMenu/{id}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("通过ID查询系统角色菜单关联信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "统角色菜单关联ID", dataType = "Integer", paramType = "query", required = true)
    })
    public Result<?> findOne(@PathVariable(name = "id") Integer id) {
        return ghdqsOampRoleMenuService.findOne(id);
    }

    /**
     * 插入GhdqsOampRoleMenu属性不为空的数据方法
     *
     * @param value
     * @return
     */
    @PostMapping(value = "/GhdqsOampRoleMenu", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("新增系统角色菜单关联信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "系统角色菜单关联", dataType = "GhdqsOampRoleMenu", paramType = "data", required = true)
    })
    public Result<?> save(GhdqsOampRoleMenu value) {
        return ghdqsOampRoleMenuService.saveNotNull(value);
    }

    /**
     * 更新GhdqsOampRoleMenu属性不为空的数据方法
     *
     * @param value
     * @return
     */
    @ApiOperation("更新系统角色菜单关联信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "系统角色菜单关联", dataType = "GhdqsOampRoleMenu", paramType = "data", required = true)
    })
    @PutMapping(value = "/GhdqsOampRoleMenu", produces = {"application/json;charset=UTF-8"})
    public Result<?> update(GhdqsOampRoleMenu value) {
        return ghdqsOampRoleMenuService.updateNotNullById(value);
    }

    /**
     * 通过id删除GhdqsOampRoleMenu数据方法
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/GhdqsOampRoleMenu/{id}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("删除系统角色菜单关联信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "系统角色菜单关联ID", dataType = "Integer", paramType = "data", required = true)
    })
    public Result<?> delete(@PathVariable(name = "id") Integer id) {
        return ghdqsOampRoleMenuService.deleteById(id);
    }
}
