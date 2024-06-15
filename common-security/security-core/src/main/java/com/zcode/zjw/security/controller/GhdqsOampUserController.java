package com.zcode.zjw.security.controller;

import com.zcode.zjw.log.api.annotation.ApiLog;
import com.zcode.zjw.log.user.annotation.UserLog;
import com.zcode.zjw.log.user.common.ModuleEnum;
import com.zcode.zjw.log.user.common.OperationEnum;
import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampUser;
import com.zcode.zjw.security.pojo.GhdqsOampUserDTO;
import com.zcode.zjw.security.service.GhdqsOampUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * GhdqsOampUser的路由接口服务
 *
 * @author zhangjiwei
 */
@RestController
@ApiLog
@RequestMapping("/ghdqs/user/")
@Api(tags = "系统用户接口")
public class GhdqsOampUserController {

    /**
     * GhdqsOampUserService服务
     */
    @Autowired
    private GhdqsOampUserService ghdqsOampUserService;

    /**
     * 查询所有GhdqsOampUser数据的方法
     *
     * @param value
     * @return
     */
    @GetMapping(value = "/GhdqsOampUser", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("查询所有系统用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "系统用户实体", dataType = "GhdqsOampUser", paramType = "query", required = true)
    })
    public Result<?> find(GhdqsOampUser value) {
        return ghdqsOampUserService.find(value);
    }

    /**
     * 通过id查询GhdqsOampUser数据的方法
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/GhdqsOampUser/{id}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("更新系统用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "Integer", paramType = "query", required = true)
    })
    public Result<?> findOne(@PathVariable(name = "id") Integer id) {
        return ghdqsOampUserService.findOne(id);
    }

    /**
     * 插入GhdqsOampUser属性不为空的数据方法
     *
     * @param value
     * @return
     */
    @UserLog(module = ModuleEnum.USER_SETTING, title = "用户新增", type = OperationEnum.ADD)
    @PostMapping(value = "/GhdqsOampUser", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("新增系统用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "系统用户实体", dataType = "GhdqsOampUser", paramType = "data", required = true)
    })
    public Result<?> save(GhdqsOampUser value) {
        return ghdqsOampUserService.saveNotNull(value);
    }

    /**
     * 更新GhdqsOampUser属性不为空的数据方法
     *
     * @param value
     * @return
     */
    @PostMapping(value = "/update")
    @UserLog(module = ModuleEnum.USER_SETTING, title = "用户更新", type = OperationEnum.MODIFY)
    @ResponseBody
    @ApiOperation("更新系统用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "系统用户DTO", dataType = "GhdqsOampUserDTO", paramType = "data", required = true)
    })
    public Result<?> update(@RequestBody GhdqsOampUserDTO value) {
        return ghdqsOampUserService.updateNotNullById(value);
    }

    /**
     * 通过id删除GhdqsOampUser数据方法
     *
     * @param id
     * @return
     */
    @UserLog(module = ModuleEnum.USER_SETTING, title = "用户删除", type = OperationEnum.DELETE)
    @DeleteMapping(value = "/GhdqsOampUser/{id}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("通过id删除系统用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "Integer", paramType = "data", required = true)
    })
    public Result<?> delete(@PathVariable(name = "id") Integer id) {
        return ghdqsOampUserService.deleteById(id);
    }
}
