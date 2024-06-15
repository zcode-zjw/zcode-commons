package com.zcode.zjw.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.security.entity.GhdqsOampRoleMenu;
import com.zcode.zjw.common.utils.db.Assist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * GhdqsOampRoleMenu的Dao接口
 *
 * @author zhangjiwei
 */
public interface GhdqsOampRoleMenuMapper extends BaseMapper<GhdqsOampRoleMenu> {

    /**
     * 获得GhdqsOampRoleMenu数据的总行数,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
     *
     * @param assist
     * @return
     */
    long getGhdqsOampRoleMenuRowCount(Assist assist);

    /**
     * 获得GhdqsOampRoleMenu数据集合,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
     *
     * @param assist
     * @return
     */
    List<GhdqsOampRoleMenu> selectGhdqsOampRoleMenu(Assist assist);

    /**
     * 通过GhdqsOampRoleMenu的id获得GhdqsOampRoleMenu对象
     *
     * @param id
     * @return
     */
    GhdqsOampRoleMenu selectGhdqsOampRoleMenuById(Integer id);

    /**
     * 获得一个GhdqsOampRoleMenu对象,以参数GhdqsOampRoleMenu对象中不为空的属性作为条件进行查询,返回符合条件的第一条
     *
     * @param obj
     * @return
     */
    GhdqsOampRoleMenu selectGhdqsOampRoleMenuObjSingle(GhdqsOampRoleMenu obj);

    /**
     * 获得一个GhdqsOampRoleMenu对象,以参数GhdqsOampRoleMenu对象中不为空的属性作为条件进行查询
     *
     * @param obj
     * @return
     */
    List<GhdqsOampRoleMenu> selectGhdqsOampRoleMenuByObj(GhdqsOampRoleMenu obj);

    /**
     * 插入GhdqsOampRoleMenu到数据库,包括null值
     *
     * @param value
     * @return
     */
    int insertGhdqsOampRoleMenu(GhdqsOampRoleMenu value);

    /**
     * 插入GhdqsOampRoleMenu中属性值不为null的数据到数据库
     *
     * @param value
     * @return
     */
    int insertNotNullGhdqsOampRoleMenu(GhdqsOampRoleMenu value);

    /**
     * 批量插入GhdqsOampRoleMenu到数据库,包括null值
     *
     * @param value
     * @return
     */
    int insertGhdqsOampRoleMenuByBatch(List<GhdqsOampRoleMenu> value);

    /**
     * 通过GhdqsOampRoleMenu的id删除GhdqsOampRoleMenu
     *
     * @param id
     * @return
     */
    int deleteGhdqsOampRoleMenuById(Integer id);

    /**
     * 通过辅助工具Assist的条件删除GhdqsOampRoleMenu
     *
     * @param assist
     * @return
     */
    int deleteGhdqsOampRoleMenuByAssist(Assist assist);

    /**
     * 通过GhdqsOampRoleMenu的id更新GhdqsOampRoleMenu中的数据,包括null值
     *
     * @param enti
     * @return
     */
    int updateGhdqsOampRoleMenuById(GhdqsOampRoleMenu enti);

    /**
     * 通过GhdqsOampRoleMenu的id更新GhdqsOampRoleMenu中属性不为null的数据
     *
     * @param enti
     * @return
     */
    int updateNotNullGhdqsOampRoleMenuById(GhdqsOampRoleMenu enti);

    /**
     * 通过辅助工具Assist的条件更新GhdqsOampRoleMenu中的数据,包括null值
     *
     * @param value
     * @param assist
     * @return
     */
    int updateGhdqsOampRoleMenu(@Param("enti") GhdqsOampRoleMenu value, @Param("assist") Assist assist);

    /**
     * 通过辅助工具Assist的条件更新GhdqsOampRoleMenu中属性不为null的数据
     *
     * @param value
     * @param assist
     * @return
     */
    int updateNotNullGhdqsOampRoleMenu(@Param("enti") GhdqsOampRoleMenu value, @Param("assist") Assist assist);
}