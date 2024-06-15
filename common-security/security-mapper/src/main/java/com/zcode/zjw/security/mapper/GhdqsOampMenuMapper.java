package com.zcode.zjw.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.security.entity.GhdqsOampMenu;
import com.zcode.zjw.common.utils.db.Assist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * GhdqsOampMenu的Dao接口
 * 
 * @author zhangjiwei
 *
 */
public interface GhdqsOampMenuMapper  extends BaseMapper<GhdqsOampMenu> {

	/**
	 * 获得GhdqsOampMenu数据的总行数,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
	 * 
	 * @param assist
	 * @return
	 */
	long getGhdqsOampMenuRowCount(Assist assist);
	
	/**
	 * 获得GhdqsOampMenu数据集合,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
	 * 
	 * @param assist
	 * @return
	 */
	List<GhdqsOampMenu> selectGhdqsOampMenu(Assist assist);
	/**
	 * 通过GhdqsOampMenu的id获得GhdqsOampMenu对象
	 * 
	 * @param id
	 * @return
	 */
	GhdqsOampMenu selectGhdqsOampMenuById(Integer id);
	
	/**
	 * 获得一个GhdqsOampMenu对象,以参数GhdqsOampMenu对象中不为空的属性作为条件进行查询,返回符合条件的第一条
	 * 
	 * @param obj
	 * @return
	 */
	GhdqsOampMenu selectGhdqsOampMenuObjSingle(GhdqsOampMenu obj);
	
	/**
	 * 获得一个GhdqsOampMenu对象,以参数GhdqsOampMenu对象中不为空的属性作为条件进行查询
	 * 
	 * @param obj
	 * @return
	 */
	List<GhdqsOampMenu> selectGhdqsOampMenuByObj(GhdqsOampMenu obj);

	/**
	 * 插入GhdqsOampMenu到数据库,包括null值
	 * 
	 * @param value
	 * @return
	 */
	int insertGhdqsOampMenu(GhdqsOampMenu value);
	
	/**
	 * 插入GhdqsOampMenu中属性值不为null的数据到数据库
	 * 
	 * @param value
	 * @return
	 */
	int insertNotNullGhdqsOampMenu(GhdqsOampMenu value);
	
	/**
	 * 批量插入GhdqsOampMenu到数据库,包括null值
	 * 
	 * @param value
	 * @return
	 */
	int insertGhdqsOampMenuByBatch(List<GhdqsOampMenu> value);
	/**
	 * 通过GhdqsOampMenu的id删除GhdqsOampMenu
	 * 
	 * @param id
	 * @return
	 */
	int deleteGhdqsOampMenuById(Integer id);
	
	/**
	 * 通过辅助工具Assist的条件删除GhdqsOampMenu
	 * 
	 * @param assist
	 * @return
	 */
	int deleteGhdqsOampMenuByAssist(Assist assist);
	
	/**
	 * 通过GhdqsOampMenu的id更新GhdqsOampMenu中的数据,包括null值
	 * 
	 * @param enti
	 * @return
	 */
	int updateGhdqsOampMenuById(GhdqsOampMenu enti);
	
	/**
	 * 通过GhdqsOampMenu的id更新GhdqsOampMenu中属性不为null的数据
	 * 
	 * @param enti
	 * @return
	 */
	int updateNotNullGhdqsOampMenuById(GhdqsOampMenu enti);
	
	/**
	 * 通过辅助工具Assist的条件更新GhdqsOampMenu中的数据,包括null值
	 * 
	 * @param value
	 * @param assist
	 * @return
	 */
	int updateGhdqsOampMenu(@Param("enti") GhdqsOampMenu value, @Param("assist") Assist assist);
	
	/**
	 * 通过辅助工具Assist的条件更新GhdqsOampMenu中属性不为null的数据
	 * 
	 * @param value
	 * @param assist
	 * @return
	 */
	int updateNotNullGhdqsOampMenu(@Param("enti") GhdqsOampMenu value, @Param("assist") Assist assist);

	/**
	 * 通过用户Id获取权限
	 * @param id
	 * @return
	 */
	List<String> selectPermsByUserId(Integer id);

}