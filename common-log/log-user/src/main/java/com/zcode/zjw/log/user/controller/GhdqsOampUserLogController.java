package com.zcode.zjw.log.user.controller;

import com.zcode.zjw.log.api.annotation.ApiLog;
import com.zcode.zjw.log.user.common.Result;
import com.zcode.zjw.log.user.entity.GhdqsOampUserLog;
import com.zcode.zjw.log.user.service.GhdqsOampUserLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * GhdqsOampUserLog的路由接口服务
 *
 * @author zhangjiwei
 */
@RestController
@ApiLog
@Api(tags = "用户操作日志接口")
public class GhdqsOampUserLogController {

    /**
     * GhdqsOampUserLogService服务
     */
    @Autowired
    private GhdqsOampUserLogService ghdqsOampUserLogService;

    /**
     * 查询所有GhdqsOampUserLog数据的方法
     *
     * @param value
     * @return
     */
    @GetMapping(value = "/GhdqsOampUserLog", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("查询用户操作日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "用户操作日志实体", dataType = "GhdqsOampUserLog", paramType = "data", required = false)
    })
    public Result<?> find(GhdqsOampUserLog value) {
        return ghdqsOampUserLogService.find(value);
    }

    /**
     * 通过id查询GhdqsOampUserLog数据的方法
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/GhdqsOampUserLog/{id}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("根据ID查询用户操作日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", dataType = "Integer", paramType = "data", required = true)
    })
    public Result<?> findOne(@PathVariable(name = "id") Integer id) {
        return ghdqsOampUserLogService.findOne(id);
    }

    /**
     * 插入GhdqsOampUserLog属性不为空的数据方法
     *
     * @param value
     * @return
     */
    @PostMapping(value = "/GhdqsOampUserLog", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("新增用户操作日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "用户操作日志实体", dataType = "GhdqsOampUserLog", paramType = "data", required = true)
    })
    public Result<?> save(GhdqsOampUserLog value) {
        return ghdqsOampUserLogService.saveNotNull(value);
    }

    /**
     * 更新GhdqsOampUserLog属性不为空的数据方法
     *
     * @param value
     * @return
     */
    @PutMapping(value = "/GhdqsOampUserLog", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("更新用户操作日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "用户操作日志实体", dataType = "GhdqsOampUserLog", paramType = "data", required = true)
    })
    public Result<?> update(GhdqsOampUserLog value) {
        return ghdqsOampUserLogService.updateNotNullById(value);
    }

    /**
     * 通过id删除GhdqsOampUserLog数据方法
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/GhdqsOampUserLog/{id}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation("删除用户操作日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "用户操作日志实体", dataType = "GhdqsOampUserLog", paramType = "data", required = true)
    })
    public Result<?> delete(@PathVariable(name = "id") Integer id) {
        return ghdqsOampUserLogService.deleteById(id);
    }
}
