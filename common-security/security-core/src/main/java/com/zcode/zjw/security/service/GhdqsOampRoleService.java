package com.zcode.zjw.security.service;


import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampRole;

/**
 * GhdqsOampRole的服务接口
 * 
 * @author zhangjiwei
 *
 */
public interface GhdqsOampRoleService {
	/**
	 * 获得GhdqsOampRole数据集,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
	 * 
	 * @return
	 */
	Result<?> find(GhdqsOampRole value);
	
	/**
	 * 通过GhdqsOampRole的id获得GhdqsOampRole对象
	 * 
	 * @param id
	 * @return
	 */
	Result<?> findOne(Integer id);
	
	/**
	 * 将GhdqsOampRole中属性值不为null的数据到数据库
	 * 
	 * @param value
	 * @return
	 */
	Result<?> saveNotNull(GhdqsOampRole value);
	
	/**
	 * 通过GhdqsOampRole的id更新GhdqsOampRole中属性不为null的数据
	 * 
	 * @param enti
	 * @return
	 */
	Result<?> updateNotNullById(GhdqsOampRole enti);
	
	/**
	 * 通过GhdqsOampRole的id删除GhdqsOampRole
	 * 
	 * @param id
	 * @return
	 */
	Result<?> deleteById(Integer id);
}
