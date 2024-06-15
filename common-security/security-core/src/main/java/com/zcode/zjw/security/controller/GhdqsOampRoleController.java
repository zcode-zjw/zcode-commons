package com.zcode.zjw.security.controller;

import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampRole;
import com.zcode.zjw.security.service.GhdqsOampRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * GhdqsOampRole的路由接口服务
 * 
 * @author zhangjiwei
 *
 */
@RestController
@Api(tags = "系统角色接口")
public class GhdqsOampRoleController {

	/** GhdqsOampRoleService服务 */
	@Autowired
	private GhdqsOampRoleService ghdqsOampRoleService;
	
	/**
	 * 查询所有GhdqsOampRole数据的方法
	 * @param value
	 * @return
	 */
	@GetMapping(value = "/GhdqsOampRole", produces = {"application/json;charset=UTF-8"})
	@ApiOperation("查询系统角色信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "value", value = "系统角色信息", dataType = "GhdqsOampRole", paramType = "query", required = true)
	})
	public Result<?> find(GhdqsOampRole value) {
		return ghdqsOampRoleService.find(value);
	}
	
	/**
	 * 通过id查询GhdqsOampRole数据的方法
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/GhdqsOampRole/{id}", produces = {"application/json;charset=UTF-8"})
	@ApiOperation("通过ID查询系统角色信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "value", value = "系统角色信息", dataType = "GhdqsOampRole", paramType = "query", required = true)
	})
	public Result<?> findOne(@PathVariable(name="id") Integer id) {
		return ghdqsOampRoleService.findOne(id);
	}
	
	/**
	 * 插入GhdqsOampRole属性不为空的数据方法
	 * @param value
	 * @return
	 */
	@PostMapping(value = "/GhdqsOampRole", produces = {"application/json;charset=UTF-8"})
	@ApiOperation("新增系统角色信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "value", value = "系统角色信息", dataType = "GhdqsOampRole", paramType = "data", required = true)
	})
	public Result<?> save(GhdqsOampRole value) {
		return ghdqsOampRoleService.saveNotNull(value);
	}
	
	/**
	 * 更新GhdqsOampRole属性不为空的数据方法
	 * @param value
	 * @return
	 */
	@PutMapping(value = "/GhdqsOampRole", produces = {"application/json;charset=UTF-8"})
	@ApiOperation("更新系统角色信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "value", value = "系统角色信息", dataType = "GhdqsOampRole", paramType = "data", required = true)
	})
	public Result<?> update(GhdqsOampRole value) {
		return ghdqsOampRoleService.updateNotNullById(value);
	}

	/**
	 * 通过id删除GhdqsOampRole数据方法
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/GhdqsOampRole/{id}", produces = {"application/json;charset=UTF-8"})
	@ApiOperation("删除系统角色信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "value", value = "系统角色信息", dataType = "GhdqsOampRole", paramType = "data", required = true)
	})
	public Result<?> delete(@PathVariable(name="id") Integer id) {
		return ghdqsOampRoleService.deleteById(id);
	}
}
