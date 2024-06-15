package com.zcode.zjw.log.user.service;


import com.zcode.zjw.log.user.common.Result;
import com.zcode.zjw.log.user.entity.GhdqsOampUserLog;
import com.zcode.zjw.log.user.pojo.UserLogDTO;

/**
 * GhdqsOampUserLog的服务接口
 * 
 * @author zhangjiwei
 *
 */
public interface GhdqsOampUserLogService {

	/**
	 * 插入用户操作日志
	 *
	 * @param userLogDTO
	 * @return
	 */
	Boolean addSysLog(UserLogDTO userLogDTO);

	/**
	 * 获得GhdqsOampUserLog数据集,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
	 * 
	 * @return
	 */
	Result<?> find(GhdqsOampUserLog value);
	
	/**
	 * 通过GhdqsOampUserLog的id获得GhdqsOampUserLog对象
	 * 
	 * @param id
	 * @return
	 */
	Result<?> findOne(Integer id);
	
	/**
	 * 将GhdqsOampUserLog中属性值不为null的数据到数据库
	 * 
	 * @param value
	 * @return
	 */
	Result<?> saveNotNull(GhdqsOampUserLog value);
	
	/**
	 * 通过GhdqsOampUserLog的id更新GhdqsOampUserLog中属性不为null的数据
	 * 
	 * @param enti
	 * @return
	 */
	Result<?> updateNotNullById(GhdqsOampUserLog enti);
	
	/**
	 * 通过GhdqsOampUserLog的id删除GhdqsOampUserLog
	 * 
	 * @param id
	 * @return
	 */
	Result<?> deleteById(Integer id);
}
