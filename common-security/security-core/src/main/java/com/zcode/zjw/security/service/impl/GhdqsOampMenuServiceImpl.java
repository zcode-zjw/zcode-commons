package com.zcode.zjw.security.service.impl;

import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.entity.GhdqsOampMenu;
import com.zcode.zjw.security.mapper.GhdqsOampMenuMapper;
import com.zcode.zjw.security.service.GhdqsOampMenuService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GhdqsOampMenu的服务接口的实现类
 *
 * @author zhangjiwei
 */
@Service
public class GhdqsOampMenuServiceImpl implements GhdqsOampMenuService {

    private final Logger LOG = LogManager.getLogger(this.getClass());

    @Autowired
    private GhdqsOampMenuMapper ghdqsOampMenuMapper;

    @Override
    public Result<?> find(GhdqsOampMenu value) {
        //TODO这里可以做通过Assist做添加查询
        List<GhdqsOampMenu> result = ghdqsOampMenuMapper.selectGhdqsOampMenu(null);
        if (LOG.isDebugEnabled()) {
            LOG.debug("执行获取GhdqsOampMenu数据集-->结果:", result);
        }
        return Result.success(result);
    }

    @Override
    public Result<?> findOne(Integer id) {
        if (id == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("执行通过GhdqsOampMenu的id获得GhdqsOampMenu对象-->失败: id不能为空");
            }
            return Result.error("失败: id不能为空");
        }
        GhdqsOampMenu result = ghdqsOampMenuMapper.selectGhdqsOampMenuById(id);
        if (LOG.isDebugEnabled()) {
            LOG.debug("执行通过GhdqsOampMenu的id获得GhdqsOampMenu对象-->结果:", result);
        }
        return Result.success(result);
    }

    @Override
    public Result<?> saveNotNull(GhdqsOampMenu value) {
        if (value == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("执行将GhdqsOampMenu中属性值不为null的数据保存到数据库-->失败: 对象不能为空");
            }
            return Result.error("失败: 对象不能为空");
        }
        int result = ghdqsOampMenuMapper.insertNotNullGhdqsOampMenu(value);
        if (LOG.isDebugEnabled()) {
            LOG.debug("执行将GhdqsOampMenu中属性值不为null的数据保存到数据库-->结果:", result);
        }
        return Result.success(result);
    }

    @Override
    public Result<?> updateNotNullById(GhdqsOampMenu value) {
        if (value == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("执行通过GhdqsOampMenu的id更新GhdqsOampMenu中属性不为null的数据-->失败: 对象为null");
            }
            return Result.error("失败: 对象为null");
        }
        int result = ghdqsOampMenuMapper.updateNotNullGhdqsOampMenuById(value);
        if (LOG.isDebugEnabled()) {
            LOG.debug("执行通过GhdqsOampMenu的id更新GhdqsOampMenu中属性不为null的数据-->结果:", result);
        }
        return Result.success(result);
    }

    @Override
    public Result<?> deleteById(Integer id) {
        if (id == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("执行通过GhdqsOampMenu的id删除GhdqsOampMenu-->失败: id不能为空");
            }
            return Result.error("失败: id不能为空");
        }
        int result = ghdqsOampMenuMapper.deleteGhdqsOampMenuById(id);
        if (LOG.isDebugEnabled()) {
            LOG.debug("执行通过GhdqsOampMenu的id删除GhdqsOampMenu-->结果:", result);
        }
        return Result.success(result);
    }


}