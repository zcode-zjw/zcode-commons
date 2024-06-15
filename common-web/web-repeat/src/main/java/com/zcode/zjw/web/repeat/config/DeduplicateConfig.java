package com.zcode.zjw.web.repeat.config;

import cn.hutool.extra.spring.SpringUtil;
import com.zcode.zjw.web.repeat.intercept.DeduplicateInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 通用配置
 *
 * @author zhangjiwei
 */
public class DeduplicateConfig implements WebMvcConfigurer {

    private final DeduplicateInterceptor deduplicateInterceptor = SpringUtil.getBean(DeduplicateInterceptor.class);

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(deduplicateInterceptor).addPathPatterns("/**");
    }

}
