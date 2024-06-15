package com.zcode.zjw.security.service;


import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampUserRole;

/**
 * GhdqsOampUserRole的服务接口
 * 
 * @author zhangjiwei
 *
 */
public interface GhdqsOampUserRoleService {
	/**
	 * 获得GhdqsOampUserRole数据集,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
	 * 
	 * @return
	 */
	Result<?> find(GhdqsOampUserRole value);
	
	// TODO 你的表中没有找到主键属性,你可以修改模板使用Assist来作为条件值做一些操作,比如用Assist来做删除与修改
	
	/**
	 * 将GhdqsOampUserRole中属性值不为null的数据到数据库
	 * 
	 * @param value
	 * @return
	 */
	Result<?> saveNotNull(GhdqsOampUserRole value);
	
}
