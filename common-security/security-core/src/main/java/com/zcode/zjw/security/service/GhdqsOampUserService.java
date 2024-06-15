package com.zcode.zjw.security.service;


import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampUser;
import com.zcode.zjw.security.pojo.GhdqsOampUserDTO;

/**
 * GhdqsOampUser的服务接口
 * 
 * @author zhangjiwei
 *
 */
public interface GhdqsOampUserService {
	/**
	 * 获得GhdqsOampUser数据集,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
	 * 
	 * @return
	 */
	Result<?> find(GhdqsOampUser value);
	
	/**
	 * 通过GhdqsOampUser的id获得GhdqsOampUser对象
	 * 
	 * @param id
	 * @return
	 */
	Result<?> findOne(Integer id);
	
	/**
	 * 将GhdqsOampUser中属性值不为null的数据到数据库
	 * 
	 * @param value
	 * @return
	 */
	Result<?> saveNotNull(GhdqsOampUser value);
	
	/**
	 * 通过GhdqsOampUser的id更新GhdqsOampUser中属性不为null的数据
	 * 
	 * @param enti
	 * @return
	 */
	Result<?> updateNotNullById(GhdqsOampUserDTO enti);
	
	/**
	 * 通过GhdqsOampUser的id删除GhdqsOampUser
	 * 
	 * @param id
	 * @return
	 */
	Result<?> deleteById(Integer id);
}
