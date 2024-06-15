package com.zcode.zjw.common.web.query.config;

import com.zcode.zjw.common.web.query.enhancer.QueryParameterParseProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * QueryParser注解注册
 *
 * @author zhangjiwei
 * @since 2023/8/11
 */
@Configuration
public class QueryParserWebConfig implements WebMvcConfigurer {

    @Autowired
    private QueryParameterParseProcessor queryParameterParseProcessor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(queryParameterParseProcessor);
    }
}
