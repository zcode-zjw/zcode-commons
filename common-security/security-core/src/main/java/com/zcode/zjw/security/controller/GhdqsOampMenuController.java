package com.zcode.zjw.security.controller;

import com.zcode.zjw.log.api.annotation.ApiLog;
import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampMenu;
import com.zcode.zjw.security.service.GhdqsOampMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * GhdqsOampMenu的路由接口服务
 *
 * @author zhangjiwei
 */
@RestController
@ApiLog
@Api(tags = "系统菜单接口")
public class GhdqsOampMenuController {

    /**
     * GhdqsOampMenuService服务
     */
    @Autowired
    private GhdqsOampMenuService ghdqsOampMenuService;

    /**
     * 查询所有GhdqsOampMenu数据的方法
     *
     * @param value
     * @return
     */
    @GetMapping(value = "/GhdqsOampMenu", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("查询系统菜单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "系统菜单实体", dataType = "GhdqsOampMenu", paramType = "query", required = false)
    })
    public Result<?> find(GhdqsOampMenu value) {
        return ghdqsOampMenuService.find(value);
    }

    /**
     * 通过id查询GhdqsOampMenu数据的方法
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/GhdqsOampMenu/{id}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("根据ID查询系统菜单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", dataType = "GhdqsOampMenu", paramType = "query", required = true)
    })
    public Result<?> findOne(@PathVariable(name = "id") Integer id) {
        return ghdqsOampMenuService.findOne(id);
    }

    /**
     * 插入GhdqsOampMenu属性不为空的数据方法
     *
     * @param
     * @return
     */
    @PostMapping(value = "/GhdqsOampMenu", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("新增系统菜单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "系统菜单实体", dataType = "GhdqsOampMenu", paramType = "data", required = true)
    })
    public Result<?> save(GhdqsOampMenu value) {
        return ghdqsOampMenuService.saveNotNull(value);
    }

    /**
     * 更新GhdqsOampMenu属性不为空的数据方法
     *
     * @param
     * @return
     */
    @PutMapping(value = "/GhdqsOampMenu", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("更新系统菜单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "系统菜单实体", dataType = "GhdqsOampMenu", paramType = "data", required = true)
    })
    public Result<?> update(GhdqsOampMenu value) {
        return ghdqsOampMenuService.updateNotNullById(value);
    }

    /**
     * 通过id删除GhdqsOampMenu数据方法
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/GhdqsOampMenu/{id}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("删除系统菜单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", dataType = "Integer", paramType = "data", required = true)
    })
    public Result<?> delete(@PathVariable(name = "id") Integer id) {
        return ghdqsOampMenuService.deleteById(id);
    }
}
