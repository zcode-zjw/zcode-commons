package com.zcode.zjw.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zcode.zjw.security.entity.GhdqsOampUser;
import com.zcode.zjw.security.entity.LoginUser;
import com.zcode.zjw.security.mapper.GhdqsOampMenuMapper;
import com.zcode.zjw.security.mapper.GhdqsOampUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * UserDetailsService的服务接口的实现类
 *
 * @author zhangjiwei
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private GhdqsOampUserMapper userMapper;

    @Autowired
    private GhdqsOampMenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<GhdqsOampUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GhdqsOampUser::getUserName, username);
        GhdqsOampUser user = userMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //TODO 根据用户查询权限信息 添加到LoginUser中
        List<String> permissionKeyList = menuMapper.selectPermsByUserId(user.getId());

        //封装成UserDetails对象返回 
        return new LoginUser(user, permissionKeyList);
    }
}