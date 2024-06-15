package com.zcode.zjw.web.limiter.config;

import cn.hutool.extra.spring.SpringUtil;
import com.zcode.zjw.web.limiter.interception.LimitInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 通用配置
 *
 * @author zhangjiwei
 */
public class LimitConfig implements WebMvcConfigurer {

    private final LimitInterceptor limitInterceptor = SpringUtil.getBean(LimitInterceptor.class);

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /* 添加全局防重复提交拦截器 */
        registry.addInterceptor(limitInterceptor).addPathPatterns("/**");
    }

}
