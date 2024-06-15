package com.zcode.zjw.web.encrypt.filter;

import com.zcode.zjw.web.encrypt.strategy.EncryptAlgorithmStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 接口解密过滤器
 *
 * @author zhangjiwei
 * @since 2021/03/19 10:
 **/
@Order(1)
@WebFilter(filterName = "ParamDecryptFilter", urlPatterns = "/*")
@Component
public class ReqParamDecryptFilter implements Filter {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${zcode.web.api-decrypt.open:false}")
    private String open;

    @Value("${zcode.web.api-encrypt.algorithm:rsa}")
    private String encryptAlgorithm;

    @Value("${zcode.web.api-encrypt.black-list:}")
    private List<String> blackList;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        ServletRequest resRequest = req;
        try {
            HttpServletRequest request = (HttpServletRequest) req;
            /* 如果配置了开启并且不在白名单里面 */
            if ("true".equals(open) && (blackList.size() == 0 || !blackList.contains(request.getRequestURI()))) {
                ReqParamWrapper requestParameterWrapper = new ReqParamWrapper(request);
                // 解密处理
                for (EncryptAlgorithmStrategy obj : applicationContext.getBeansOfType(EncryptAlgorithmStrategy.class).values()) {
                    if (obj.supports(encryptAlgorithm)) {
                        obj.decrypt(request, requestParameterWrapper);
                        // 只需要处理一次，假如意外情况多执行几次，那只会执行第一个
                        break;
                    }
                }
                resRequest = requestParameterWrapper;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            chain.doFilter(resRequest, resp);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
