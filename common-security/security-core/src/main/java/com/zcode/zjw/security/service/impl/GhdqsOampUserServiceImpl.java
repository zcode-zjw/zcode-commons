package com.zcode.zjw.security.service.impl;

import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampUser;
import com.zcode.zjw.security.mapper.GhdqsOampUserMapper;
import com.zcode.zjw.security.pojo.GhdqsOampUserDTO;
import com.zcode.zjw.security.service.GhdqsOampUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * GhdqsOampUser的服务接口的实现类
 *
 * @author zhangjiwei
 */
@Service
public class GhdqsOampUserServiceImpl implements GhdqsOampUserService {
    private final Logger LOG = LogManager.getLogger(this.getClass());

    @Autowired
    private GhdqsOampUserMapper ghdqsOampUserMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Result<?> find(GhdqsOampUser value) {
        //TODO这里可以做通过Assist做添加查询
        List<GhdqsOampUser> result = ghdqsOampUserMapper.selectGhdqsOampUser(null);
        if (LOG.isDebugEnabled()) {
            LOG.debug("执行获取GhdqsOampUser数据集-->结果:", result);
        }
        return Result.success(result);
    }

    @Override
    public Result<?> findOne(Integer id) {
        if (id == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("执行通过GhdqsOampUser的id获得GhdqsOampUser对象-->失败:id不能为空");
            }
            return Result.success(null);
        }
        GhdqsOampUser result = ghdqsOampUserMapper.selectGhdqsOampUserById(id);
        if (LOG.isDebugEnabled()) {
            LOG.debug("执行通过GhdqsOampUser的id获得GhdqsOampUser对象-->结果:", result);
        }
        return Result.success(result);
    }

    @Override
    public Result<?> saveNotNull(GhdqsOampUser value) {
        if (value == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("执行将GhdqsOampUser中属性值不为null的数据保存到数据库-->失败:对象不能为空");
            }
            return Result.success(null);
        }
        int result = ghdqsOampUserMapper.insertNotNullGhdqsOampUser(value);
        if (LOG.isDebugEnabled()) {
            LOG.debug("执行将GhdqsOampUser中属性值不为null的数据保存到数据库-->结果:", result);
        }
        return Result.success(result);
    }

    @Override
    public Result<?> updateNotNullById(GhdqsOampUserDTO value) {
        if (value == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("执行通过GhdqsOampUser的id更新GhdqsOampUser中属性不为null的数据-->失败:对象为null");
            }
            return Result.success(null);
        }
        // 查询当前用户的旧密码是否正确
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(value.getUserName(), value.getOldPwd());
        try {
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            if (Objects.isNull(authenticate)) {
                return Result.error("旧密码错误！");
            }
        } catch (InternalAuthenticationServiceException | BadCredentialsException e) {
            return Result.error("旧密码错误！");
        }
        // 加密密码
        value.setPassword(bCryptPasswordEncoder.encode(value.getPassword()));
        // 构造实体对象
        GhdqsOampUser ghdqsOampUser = new GhdqsOampUser();
        BeanUtils.copyProperties(value, ghdqsOampUser);
        int result = ghdqsOampUserMapper.updateNotNullGhdqsOampUserById(ghdqsOampUser);
        if (LOG.isDebugEnabled()) {
            LOG.debug("执行通过GhdqsOampUser的id更新GhdqsOampUser中属性不为null的数据-->结果:", result);
        }
        return Result.success(result);
    }

    @Override
    public Result<?> deleteById(Integer id) {
        if (id == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("执行通过GhdqsOampUser的id删除GhdqsOampUser-->失败:id不能为空");
            }
            return Result.success(null);
        }
        int result = ghdqsOampUserMapper.deleteGhdqsOampUserById(id);
        if (LOG.isDebugEnabled()) {
            LOG.debug("执行通过GhdqsOampUser的id删除GhdqsOampUser-->结果:", result);
        }
        return Result.success(result);
    }


}