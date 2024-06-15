package com.zcode.zjw.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.security.entity.GhdqsOampUserRole;
import com.zcode.zjw.common.utils.db.Assist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * GhdqsOampUserRole的Dao接口
 *
 * @author zhangjiwei
 */
public interface GhdqsOampUserRoleMapper extends BaseMapper<GhdqsOampUserRole> {

    /**
     * 获得GhdqsOampUserRole数据的总行数,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
     *
     * @param assist
     * @return
     */
    long getGhdqsOampUserRoleRowCount(Assist assist);

    /**
     * 获得GhdqsOampUserRole数据集合,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
     *
     * @param assist
     * @return
     */
    List<GhdqsOampUserRole> selectGhdqsOampUserRole(Assist assist);

    /**
     * 获得一个GhdqsOampUserRole对象,以参数GhdqsOampUserRole对象中不为空的属性作为条件进行查询,返回符合条件的第一条
     *
     * @param obj
     * @return
     */
    GhdqsOampUserRole selectGhdqsOampUserRoleObjSingle(GhdqsOampUserRole obj);

    /**
     * 获得一个GhdqsOampUserRole对象,以参数GhdqsOampUserRole对象中不为空的属性作为条件进行查询
     *
     * @param obj
     * @return
     */
    List<GhdqsOampUserRole> selectGhdqsOampUserRoleByObj(GhdqsOampUserRole obj);

    /**
     * 插入GhdqsOampUserRole到数据库,包括null值
     *
     * @param value
     * @return
     */
    int insertGhdqsOampUserRole(GhdqsOampUserRole value);

    /**
     * 插入GhdqsOampUserRole中属性值不为null的数据到数据库
     *
     * @param value
     * @return
     */
    int insertNotNullGhdqsOampUserRole(GhdqsOampUserRole value);

    /**
     * 批量插入GhdqsOampUserRole到数据库,包括null值
     *
     * @param value
     * @return
     */
    int insertGhdqsOampUserRoleByBatch(List<GhdqsOampUserRole> value);

    /**
     * 通过辅助工具Assist的条件删除GhdqsOampUserRole
     *
     * @param assist
     * @return
     */
    int deleteGhdqsOampUserRoleByAssist(Assist assist);


    /**
     * 通过辅助工具Assist的条件更新GhdqsOampUserRole中的数据,包括null值
     *
     * @param value
     * @param assist
     * @return
     */
    int updateGhdqsOampUserRole(@Param("enti") GhdqsOampUserRole value, @Param("assist") Assist assist);

    /**
     * 通过辅助工具Assist的条件更新GhdqsOampUserRole中属性不为null的数据
     *
     * @param value
     * @param assist
     * @return
     */
    int updateNotNullGhdqsOampUserRole(@Param("enti") GhdqsOampUserRole value, @Param("assist") Assist assist);
}