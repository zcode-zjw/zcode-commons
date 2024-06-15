package com.zcode.zjw.web.encrypt.config;

import cn.hutool.extra.spring.SpringUtil;
import com.zcode.zjw.web.encrypt.interception.DecryptInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhangjiwei
 * @description
 * @date 2023/2/10 上午9:49
 */
public class EncryptConfig implements WebMvcConfigurer {

    private final DecryptInterceptor decryptInterceptor = SpringUtil.getBean(DecryptInterceptor.class);

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(decryptInterceptor).addPathPatterns("/**");
    }

}
