package com.zcode.zjw.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.security.entity.GhdqsOampUser;
import com.zcode.zjw.common.utils.db.Assist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * GhdqsOampUser的Dao接口
 *
 * @author zhangjiwei
 */
public interface GhdqsOampUserMapper extends BaseMapper<GhdqsOampUser> {

    /**
     * 获得GhdqsOampUser数据的总行数,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
     *
     * @param assist
     * @return
     */
    long getGhdqsOampUserRowCount(Assist assist);

    /**
     * 获得GhdqsOampUser数据集合,可以通过辅助工具Assist进行条件查询,如果没有条件则传入null
     *
     * @param assist
     * @return
     */
    List<GhdqsOampUser> selectGhdqsOampUser(Assist assist);

    /**
     * 通过GhdqsOampUser的id获得GhdqsOampUser对象
     *
     * @param id
     * @return
     */
    GhdqsOampUser selectGhdqsOampUserById(Integer id);

    /**
     * 通过GhdqsOampUser的用户名和密码获得GhdqsOampUser对象
     *
     * @param userName
     * @param password
     * @return
     */
    GhdqsOampUser selectGhdqsOampUserByNameAndPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * 获得一个GhdqsOampUser对象,以参数GhdqsOampUser对象中不为空的属性作为条件进行查询,返回符合条件的第一条
     *
     * @param obj
     * @return
     */
    GhdqsOampUser selectGhdqsOampUserObjSingle(GhdqsOampUser obj);

    /**
     * 获得一个GhdqsOampUser对象,以参数GhdqsOampUser对象中不为空的属性作为条件进行查询
     *
     * @param obj
     * @return
     */
    List<GhdqsOampUser> selectGhdqsOampUserByObj(GhdqsOampUser obj);

    /**
     * 插入GhdqsOampUser到数据库,包括null值
     *
     * @param value
     * @return
     */
    int insertGhdqsOampUser(GhdqsOampUser value);

    /**
     * 插入GhdqsOampUser中属性值不为null的数据到数据库
     *
     * @param value
     * @return
     */
    int insertNotNullGhdqsOampUser(GhdqsOampUser value);

    /**
     * 批量插入GhdqsOampUser到数据库,包括null值
     *
     * @param value
     * @return
     */
    int insertGhdqsOampUserByBatch(List<GhdqsOampUser> value);

    /**
     * 通过GhdqsOampUser的id删除GhdqsOampUser
     *
     * @param id
     * @return
     */
    int deleteGhdqsOampUserById(Integer id);

    /**
     * 通过辅助工具Assist的条件删除GhdqsOampUser
     *
     * @param assist
     * @return
     */
    int deleteGhdqsOampUserByAssist(Assist assist);

    /**
     * 通过GhdqsOampUser的id更新GhdqsOampUser中的数据,包括null值
     *
     * @param enti
     * @return
     */
    int updateGhdqsOampUserById(GhdqsOampUser enti);

    /**
     * 通过GhdqsOampUser的id更新GhdqsOampUser中属性不为null的数据
     *
     * @param enti
     * @return
     */
    int updateNotNullGhdqsOampUserById(GhdqsOampUser enti);

    /**
     * 通过辅助工具Assist的条件更新GhdqsOampUser中的数据,包括null值
     *
     * @param value
     * @param assist
     * @return
     */
    int updateGhdqsOampUser(@Param("enti") GhdqsOampUser value, @Param("assist") Assist assist);

    /**
     * 通过辅助工具Assist的条件更新GhdqsOampUser中属性不为null的数据
     *
     * @param value
     * @param assist
     * @return
     */
    int updateNotNullGhdqsOampUser(@Param("enti") GhdqsOampUser value, @Param("assist") Assist assist);
}