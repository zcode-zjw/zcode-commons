package com.zcode.zjw.log.user.service.impl;

import com.zcode.zjw.log.user.common.Result;
import com.zcode.zjw.log.user.entity.GhdqsOampUserLog;
import com.zcode.zjw.log.user.mapper.GhdqsOampUserLogMapper;
import com.zcode.zjw.log.user.pojo.UserLogDTO;
import com.zcode.zjw.log.user.service.GhdqsOampUserLogService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * GhdqsOampUserLog的服务接口的实现类
 * 
 * @author zhangjiwei
 *
 */
@Service
public class GhdqsOampUserLogServiceImpl implements GhdqsOampUserLogService {

	private final Logger LOG = LogManager.getLogger(this.getClass());

	@Autowired
	private GhdqsOampUserLogMapper ghdqsOampUserLogMapper;

	@Override
	public Boolean addSysLog(UserLogDTO userLogDTO) {
		GhdqsOampUserLog userLogDO = new GhdqsOampUserLog();
		userLogDO.setModuleCode(userLogDTO.getModuleCode());
		userLogDO.setType(userLogDTO.getType());
		userLogDO.setTitle(userLogDTO.getTitle());
		userLogDO.setOperatorId(Optional.ofNullable(userLogDTO.getOperatorId()).orElse(-1));
		userLogDO.setOperateTime(userLogDTO.getOperateTime());
		userLogDO.setContent(Optional.ofNullable(userLogDTO.getContent()).orElse("无"));

		return ghdqsOampUserLogMapper.insert(userLogDO) > 0;
	}

	@Override
	public Result<?> find(GhdqsOampUserLog value) {
		//TODO这里可以做通过Assist做添加查询
		List<GhdqsOampUserLog> result = ghdqsOampUserLogMapper.selectList(null);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行获取GhdqsOampUserLog数据集-->结果:", result);
		}
		return Result.success(result);
	}
	@Override
	public Result<?> findOne(Integer id) {
		if (id == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行通过GhdqsOampUserLog的id获得GhdqsOampUserLog对象-->失败: id不能为空");
			}
			return Result.error("id不能为空");
		}
		GhdqsOampUserLog result = ghdqsOampUserLogMapper.selectById(id);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行通过GhdqsOampUserLog的id获得GhdqsOampUserLog对象-->结果:", result);
		}
		return Result.success(result);
	}

	@Override
	public Result<?> saveNotNull(GhdqsOampUserLog value) {
		if (value == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行将GhdqsOampUserLog中属性值不为null的数据保存到数据库-->失败:对象不能为空");
			}
			return Result.error("对象不能为空");
		}
		if(value.getType() == null || value.getOperatorId() == null || value.getOperateTime() == null  ){
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行将GhdqsOampUserLog中属性值不为null的数据保存到数据库-->失败:存在不能为空的空值");
			}
			return Result.success("存在不能为空的空值");
		}
		int result = ghdqsOampUserLogMapper.insert(value);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行将GhdqsOampUserLog中属性值不为null的数据保存到数据库-->结果:", result);
		}
		return Result.success(result);
	}
	@Override
	public Result<?> updateNotNullById(GhdqsOampUserLog value) {
		if (value == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行通过GhdqsOampUserLog的id更新GhdqsOampUserLog中属性不为null的数据-->失败:对象为null");
			}
			return Result.error("失败:对象为null");
		}
		int result = ghdqsOampUserLogMapper.updateById(value);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行通过GhdqsOampUserLog的id更新GhdqsOampUserLog中属性不为null的数据-->结果:", result);
		}
		return Result.success(result);
	}

	@Override
	public Result<?> deleteById(Integer id) {
		if (id == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行通过GhdqsOampUserLog的id删除GhdqsOampUserLog-->失败:id不能为空");
			}
			return Result.error("失败:id不能为空");
		}
		int result = ghdqsOampUserLogMapper.deleteById(id);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行通过GhdqsOampUserLog的id删除GhdqsOampUserLog-->结果:", result);
		}
		return Result.success(result);
	}


}