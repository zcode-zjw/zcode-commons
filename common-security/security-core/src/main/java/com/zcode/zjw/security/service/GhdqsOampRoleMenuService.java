package com.zcode.zjw.security.service;


import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampRoleMenu;

/**
 * GhdqsOampRoleMenu的服务接口
 * 
 * @author zhangjiwei
 *
 */
public interface GhdqsOampRoleMenuService {
	/**
	 * 获得GhdqsOampRoleMenu数据集,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
	 * 
	 * @return
	 */
	Result<?> find(GhdqsOampRoleMenu value);
	
	/**
	 * 通过GhdqsOampRoleMenu的id获得GhdqsOampRoleMenu对象
	 * 
	 * @param id
	 * @return
	 */
	Result<?> findOne(Integer id);
	
	/**
	 * 将GhdqsOampRoleMenu中属性值不为null的数据到数据库
	 * 
	 * @param value
	 * @return
	 */
	Result<?> saveNotNull(GhdqsOampRoleMenu value);
	
	/**
	 * 通过GhdqsOampRoleMenu的id更新GhdqsOampRoleMenu中属性不为null的数据
	 * 
	 * @param enti
	 * @return
	 */
	Result<?> updateNotNullById(GhdqsOampRoleMenu enti);
	
	/**
	 * 通过GhdqsOampRoleMenu的id删除GhdqsOampRoleMenu
	 * 
	 * @param id
	 * @return
	 */
	Result<?> deleteById(Integer id);
}
