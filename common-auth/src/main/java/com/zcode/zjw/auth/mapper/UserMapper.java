package com.zcode.zjw.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.auth.pojo.User;
import com.zcode.zjw.auth.pojo.UserFullInfoVO;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

    UserFullInfoVO finUserFullInfo(@Param("userId") String userId);

}