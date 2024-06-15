package com.zcode.zjw.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.security.entity.GhdqsOampRole;
import com.zcode.zjw.common.utils.db.Assist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * GhdqsOampRole的Dao接口
 * 
 * @author zhangjiwei
 *
 */
public interface GhdqsOampRoleMapper extends BaseMapper<GhdqsOampRole> {

	/**
	 * 获得GhdqsOampRole数据的总行数,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
	 * 
	 * @param assist
	 * @return
	 */
	long getGhdqsOampRoleRowCount(Assist assist);
	
	/**
	 * 获得GhdqsOampRole数据集合,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
	 * 
	 * @param assist
	 * @return
	 */
	List<GhdqsOampRole> selectGhdqsOampRole(Assist assist);
	/**
	 * 通过GhdqsOampRole的id获得GhdqsOampRole对象
	 * 
	 * @param id
	 * @return
	 */
	GhdqsOampRole selectGhdqsOampRoleById(Integer id);
	
	/**
	 * 获得一个GhdqsOampRole对象,以参数GhdqsOampRole对象中不为空的属性作为条件进行查询,返回符合条件的第一条
	 * 
	 * @param obj
	 * @return
	 */
	GhdqsOampRole selectGhdqsOampRoleObjSingle(GhdqsOampRole obj);
	
	/**
	 * 获得一个GhdqsOampRole对象,以参数GhdqsOampRole对象中不为空的属性作为条件进行查询
	 * 
	 * @param obj
	 * @return
	 */
	List<GhdqsOampRole> selectGhdqsOampRoleByObj(GhdqsOampRole obj);

	/**
	 * 插入GhdqsOampRole到数据库,包括null值
	 * 
	 * @param value
	 * @return
	 */
	int insertGhdqsOampRole(GhdqsOampRole value);
	
	/**
	 * 插入GhdqsOampRole中属性值不为null的数据到数据库
	 * 
	 * @param value
	 * @return
	 */
	int insertNotNullGhdqsOampRole(GhdqsOampRole value);
	
	/**
	 * 批量插入GhdqsOampRole到数据库,包括null值
	 * 
	 * @param value
	 * @return
	 */
	int insertGhdqsOampRoleByBatch(List<GhdqsOampRole> value);
	/**
	 * 通过GhdqsOampRole的id删除GhdqsOampRole
	 * 
	 * @param id
	 * @return
	 */
	int deleteGhdqsOampRoleById(Integer id);
	
	/**
	 * 通过辅助工具Assist的条件删除GhdqsOampRole
	 * 
	 * @param assist
	 * @return
	 */
	int deleteGhdqsOampRoleByAssist(Assist assist);
	
	/**
	 * 通过GhdqsOampRole的id更新GhdqsOampRole中的数据,包括null值
	 * 
	 * @param enti
	 * @return
	 */
	int updateGhdqsOampRoleById(GhdqsOampRole enti);
	
	/**
	 * 通过GhdqsOampRole的id更新GhdqsOampRole中属性不为null的数据
	 * 
	 * @param enti
	 * @return
	 */
	int updateNotNullGhdqsOampRoleById(GhdqsOampRole enti);
	
	/**
	 * 通过辅助工具Assist的条件更新GhdqsOampRole中的数据,包括null值
	 * 
	 * @param value
	 * @param assist
	 * @return
	 */
	int updateGhdqsOampRole(@Param("enti") GhdqsOampRole value, @Param("assist") Assist assist);
	
	/**
	 * 通过辅助工具Assist的条件更新GhdqsOampRole中属性不为null的数据
	 * 
	 * @param value
	 * @param assist
	 * @return
	 */
	int updateNotNullGhdqsOampRole(@Param("enti") GhdqsOampRole value, @Param("assist") Assist assist);
}