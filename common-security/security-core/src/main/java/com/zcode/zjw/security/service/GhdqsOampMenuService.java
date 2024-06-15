package com.zcode.zjw.security.service;


import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampMenu;

/**
 * GhdqsOampMenu的服务接口
 * 
 * @author zhangjiwei
 *
 */
public interface GhdqsOampMenuService {
	/**
	 * 获得GhdqsOampMenu数据集,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
	 * 
	 * @return
	 */
	Result<?> find(GhdqsOampMenu value);
	
	/**
	 * 通过GhdqsOampMenu的id获得GhdqsOampMenu对象
	 * 
	 * @param id
	 * @return
	 */
	Result<?> findOne(Integer id);
	
	/**
	 * 将GhdqsOampMenu中属性值不为null的数据到数据库
	 * 
	 * @param value
	 * @return
	 */
	Result<?> saveNotNull(GhdqsOampMenu value);
	
	/**
	 * 通过GhdqsOampMenu的id更新GhdqsOampMenu中属性不为null的数据
	 * 
	 * @param enti
	 * @return
	 */
	Result<?> updateNotNullById(GhdqsOampMenu enti);
	
	/**
	 * 通过GhdqsOampMenu的id删除GhdqsOampMenu
	 * 
	 * @param id
	 * @return
	 */
	Result<?> deleteById(Integer id);


}
