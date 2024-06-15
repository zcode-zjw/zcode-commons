package com.zcode.zjw.web.repeat.service.impl;

import com.zcode.zjw.cache.redis.utils.RedisCache;
import com.zcode.zjw.common.utils.common.JwtUtil;
import com.zcode.zjw.web.repeat.common.DeduplicateMode;
import com.zcode.zjw.web.repeat.common.RepeatConstant;
import com.zcode.zjw.web.repeat.service.CheckDuplicateService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangjiwei
 * @description 重复检测（通过Redis）
 * @date 2023/2/8 下午5:47
 */
@Service
public class CheckDuplicateCacheImpl implements CheckDuplicateService {

    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean repeatDataValidate(HttpServletRequest request, HttpServletResponse response) {
        String nowUrlParams = getNowUrlParams(request);
        // 获取请求的Token
        String token = response.getHeader(RepeatConstant.tokenFlag);
        // 如果直接没有token，说明没有先获取token，直接放行
        if (token == null) {
            return false;
        }
        try {
            // 解析token
            Claims claims = JwtUtil.parseJWT(token, RepeatConstant.tokenSecretText);
            claims.getSubject();
            // 成功解析后，先尝试去缓存里获取该请求，如果可以获取到，说明是第一次请求，否则是重复请求
            synchronized (this) {
                if (redisCache.getCacheObject(nowUrlParams)) {
                    redisCache.deleteObject(nowUrlParams);
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public boolean supports(DeduplicateMode mode) {
        return mode.equals(DeduplicateMode.CACHE);
    }

}
