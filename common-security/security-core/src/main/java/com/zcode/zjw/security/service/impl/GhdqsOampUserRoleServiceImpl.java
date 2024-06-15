package com.zcode.zjw.security.service.impl;

import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampUserRole;
import com.zcode.zjw.security.mapper.GhdqsOampUserRoleMapper;
import com.zcode.zjw.security.service.GhdqsOampUserRoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GhdqsOampUserRole的服务接口的实现类
 * 
 * @author zhangjiwei
 *
 */
@Service
public class GhdqsOampUserRoleServiceImpl implements GhdqsOampUserRoleService {
	private final Logger LOG = LogManager.getLogger(this.getClass());

	@Autowired
	private GhdqsOampUserRoleMapper ghdqsOampUserRoleMapper;

	@Override
	public Result<?> find(GhdqsOampUserRole value) {
		//TODO这里可以做通过Assist做添加查询
		List<GhdqsOampUserRole> result = ghdqsOampUserRoleMapper.selectGhdqsOampUserRole(null);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行获取GhdqsOampUserRole数据集-->结果:", result);
		}
		return Result.success(result);
	}

	@Override
	public Result<?> saveNotNull(GhdqsOampUserRole value) {
		if (value == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行将GhdqsOampUserRole中属性值不为null的数据保存到数据库-->失败:对象不能为空");
			}
			return Result.error("失败:对象不能为空");
		}
		if(value.getUserId() == null  ){
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行将GhdqsOampUserRole中属性值不为null的数据保存到数据库-->失败:存在不能为空的空值");
			}
			return Result.success("失败:存在不能为空的空值");
		}
		int result = ghdqsOampUserRoleMapper.insertNotNullGhdqsOampUserRole(value);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行将GhdqsOampUserRole中属性值不为null的数据保存到数据库-->结果:", result);
		}
		return Result.success(result);
	}


}