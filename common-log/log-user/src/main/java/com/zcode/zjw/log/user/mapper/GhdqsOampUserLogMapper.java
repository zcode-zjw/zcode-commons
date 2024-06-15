package com.zcode.zjw.log.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.common.utils.db.Assist;
import com.zcode.zjw.log.user.entity.GhdqsOampUserLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * GhdqsOampUserLog的Dao接口
 * 
 * @author zhangjiwei
 *
 */
@Repository
public interface GhdqsOampUserLogMapper extends BaseMapper<GhdqsOampUserLog> {

	/**
	 * 获得GhdqsOampUserLog数据的总行数,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
	 * 
	 * @param assist
	 * @return
	 */
	long getGhdqsOampUserLogRowCount(Assist assist);
	
	/**
	 * 获得GhdqsOampUserLog数据集合,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
	 * 
	 * @param assist
	 * @return
	 */
	List<GhdqsOampUserLog> selectGhdqsOampUserLog(Assist assist);
	/**
	 * 通过GhdqsOampUserLog的id获得GhdqsOampUserLog对象
	 * 
	 * @param id
	 * @return
	 */
	GhdqsOampUserLog selectGhdqsOampUserLogById(Integer id);
	
	/**
	 * 获得一个GhdqsOampUserLog对象,以参数GhdqsOampUserLog对象中不为空的属性作为条件进行查询,返回符合条件的第一条
	 * 
	 * @param obj
	 * @return
	 */
	GhdqsOampUserLog selectGhdqsOampUserLogObjSingle(GhdqsOampUserLog obj);
	
	/**
	 * 获得一个GhdqsOampUserLog对象,以参数GhdqsOampUserLog对象中不为空的属性作为条件进行查询
	 * 
	 * @param obj
	 * @return
	 */
	List<GhdqsOampUserLog> selectGhdqsOampUserLogByObj(GhdqsOampUserLog obj);

	/**
	 * 插入GhdqsOampUserLog到数据库,包括null值
	 * 
	 * @param value
	 * @return
	 */
	int insertGhdqsOampUserLog(GhdqsOampUserLog value);
	
	/**
	 * 插入GhdqsOampUserLog中属性值不为null的数据到数据库
	 * 
	 * @param value
	 * @return
	 */
	int insertNotNullGhdqsOampUserLog(GhdqsOampUserLog value);
	
	/**
	 * 批量插入GhdqsOampUserLog到数据库,包括null值
	 * 
	 * @param value
	 * @return
	 */
	int insertGhdqsOampUserLogByBatch(List<GhdqsOampUserLog> value);
	/**
	 * 通过GhdqsOampUserLog的id删除GhdqsOampUserLog
	 * 
	 * @param id
	 * @return
	 */
	int deleteGhdqsOampUserLogById(Integer id);
	
	/**
	 * 通过辅助工具Assist的条件删除GhdqsOampUserLog
	 * 
	 * @param assist
	 * @return
	 */
	int deleteGhdqsOampUserLogByAssist(Assist assist);
	
	/**
	 * 通过GhdqsOampUserLog的id更新GhdqsOampUserLog中的数据,包括null值
	 * 
	 * @param enti
	 * @return
	 */
	int updateGhdqsOampUserLogById(GhdqsOampUserLog enti);
	
	/**
	 * 通过GhdqsOampUserLog的id更新GhdqsOampUserLog中属性不为null的数据
	 * 
	 * @param enti
	 * @return
	 */
	int updateNotNullGhdqsOampUserLogById(GhdqsOampUserLog enti);
	
	/**
	 * 通过辅助工具Assist的条件更新GhdqsOampUserLog中的数据,包括null值
	 * 
	 * @param value
	 * @param assist
	 * @return
	 */
	int updateGhdqsOampUserLog(@Param("enti") GhdqsOampUserLog value, @Param("assist") Assist assist);
	
	/**
	 * 通过辅助工具Assist的条件更新GhdqsOampUserLog中属性不为null的数据
	 * 
	 * @param value
	 * @param assist
	 * @return
	 */
	int updateNotNullGhdqsOampUserLog(@Param("enti") GhdqsOampUserLog value, @Param("assist") Assist assist);
}