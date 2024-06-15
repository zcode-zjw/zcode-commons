package com.zcode.zjw.common.web.query.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.common.web.query.test.TestUser;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/8/14
 */
public interface TestUserMapper extends BaseMapper<TestUser> {

    @Update("select * from t_user")
    List<TestUser> findAll();
}
