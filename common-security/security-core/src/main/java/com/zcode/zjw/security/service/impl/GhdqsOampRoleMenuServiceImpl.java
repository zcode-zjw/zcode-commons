package com.zcode.zjw.security.service.impl;

import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampRoleMenu;
import com.zcode.zjw.security.mapper.GhdqsOampRoleMenuMapper;
import com.zcode.zjw.security.service.GhdqsOampRoleMenuService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GhdqsOampRoleMenu的服务接口的实现类
 * 
 * @author zhangjiwei
 *
 */
@Service
public class GhdqsOampRoleMenuServiceImpl implements GhdqsOampRoleMenuService {
	private final Logger LOG = LogManager.getLogger(this.getClass());

	@Autowired
	private GhdqsOampRoleMenuMapper ghdqsOampRoleMenuMapper;

	@Override
	public Result<?> find(GhdqsOampRoleMenu value) {
		//TODO这里可以做通过Assist做添加查询
		List<GhdqsOampRoleMenu> result = ghdqsOampRoleMenuMapper.selectGhdqsOampRoleMenu(null);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行获取GhdqsOampRoleMenu数据集-->结果:", result);
		}
		return Result.success(result);
	}
	@Override
	public Result<?> findOne(Integer id) {
		if (id == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行通过GhdqsOampRoleMenu的id获得GhdqsOampRoleMenu对象-->失败:id不能为空");
			}
			return Result.error("失败:id不能为空");
		}
		GhdqsOampRoleMenu result = ghdqsOampRoleMenuMapper.selectGhdqsOampRoleMenuById(id);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行通过GhdqsOampRoleMenu的id获得GhdqsOampRoleMenu对象-->结果:", result);
		}
		return Result.success(result);
	}

	@Override
	public Result<?> saveNotNull(GhdqsOampRoleMenu value) {
		if (value == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行将GhdqsOampRoleMenu中属性值不为null的数据保存到数据库-->失败:对象不能为空");
			}
			return Result.error("失败:对象不能为空");
		}
		if(value.getRoleId() == null  ){
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行将GhdqsOampRoleMenu中属性值不为null的数据保存到数据库-->失败:存在不能为空的空值");
			}
			return Result.success("失败:存在不能为空的空值");
		}
		int result = ghdqsOampRoleMenuMapper.insertNotNullGhdqsOampRoleMenu(value);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行将GhdqsOampRoleMenu中属性值不为null的数据保存到数据库-->结果:", result);
		}
		return Result.success(result);
	}
	@Override
	public Result<?> updateNotNullById(GhdqsOampRoleMenu value) {
		if (value == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行通过GhdqsOampRoleMenu的id更新GhdqsOampRoleMenu中属性不为null的数据-->失败:对象为null");
			}
			return Result.error("失败:对象为null");
		}
		int result = ghdqsOampRoleMenuMapper.updateNotNullGhdqsOampRoleMenuById(value);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行通过GhdqsOampRoleMenu的id更新GhdqsOampRoleMenu中属性不为null的数据-->结果:", result);
		}
		return Result.success(result);
	}

	@Override
	public Result<?> deleteById(Integer id) {
		if (id == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行通过GhdqsOampRoleMenu的id删除GhdqsOampRoleMenu-->失败:id不能为空");
			}
			return Result.error("失败:id不能为空");
		}
		int result = ghdqsOampRoleMenuMapper.deleteGhdqsOampRoleMenuById(id);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行通过GhdqsOampRoleMenu的id删除GhdqsOampRoleMenu-->结果:", result);
		}
		return Result.success(result);
	}


}