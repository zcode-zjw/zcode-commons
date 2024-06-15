package com.zcode.zjw.security.service.impl;

import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampRole;
import com.zcode.zjw.security.mapper.GhdqsOampRoleMapper;
import com.zcode.zjw.security.service.GhdqsOampRoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GhdqsOampRole的服务接口的实现类
 * 
 * @author zhangjiwei
 *
 */
@Service
public class GhdqsOampRoleServiceImpl implements GhdqsOampRoleService {
	private final Logger LOG = LogManager.getLogger(this.getClass());

	@Autowired
	private GhdqsOampRoleMapper ghdqsOampRoleMapper;

	@Override
	public Result<?> find(GhdqsOampRole value) {
		//TODO这里可以做通过Assist做添加查询
		List<GhdqsOampRole> result = ghdqsOampRoleMapper.selectGhdqsOampRole(null);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行获取GhdqsOampRole数据集-->结果:", result);
		}
		return Result.success(result);
	}
	@Override
	public Result<?> findOne(Integer id) {
		if (id == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行通过GhdqsOampRole的id获得GhdqsOampRole对象-->失败:id不能为空");
			}
			return Result.error("失败:id不能为空");
		}
		GhdqsOampRole result = ghdqsOampRoleMapper.selectGhdqsOampRoleById(id);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行通过GhdqsOampRole的id获得GhdqsOampRole对象-->结果:", result);
		}
		return Result.success(result);
	}

	@Override
	public Result<?> saveNotNull(GhdqsOampRole value) {
		if (value == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行将GhdqsOampRole中属性值不为null的数据保存到数据库-->失败:对象不能为空");
			}
			return Result.error("失败:对象不能为空");
		}
		int result = ghdqsOampRoleMapper.insertNotNullGhdqsOampRole(value);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行将GhdqsOampRole中属性值不为null的数据保存到数据库-->结果:", result);
		}
		return Result.success(result);
	}
	@Override
	public Result<?> updateNotNullById(GhdqsOampRole value) {
		if (value == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行通过GhdqsOampRole的id更新GhdqsOampRole中属性不为null的数据-->失败:对象为null");
			}
			return Result.error("失败:对象为null");
		}
		int result = ghdqsOampRoleMapper.updateNotNullGhdqsOampRoleById(value);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行通过GhdqsOampRole的id更新GhdqsOampRole中属性不为null的数据-->结果:", result);
		}
		return Result.success(result);
	}

	@Override
	public Result<?> deleteById(Integer id) {
		if (id == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行通过GhdqsOampRole的id删除GhdqsOampRole-->失败:id不能为空");
			}
			return Result.error("失败:id不能为空");
		}
		int result = ghdqsOampRoleMapper.deleteGhdqsOampRoleById(id);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行通过GhdqsOampRole的id删除GhdqsOampRole-->结果:", result);
		}
		return Result.success(result);
	}


}