package com.zcode.zjw.web.xss.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class XssFilter implements Filter {


    // 忽略权限检查的url地址
    @Value("${security.xss.exclude:null}")
    private List<String> excludeUrls;

    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;

        String pathInfo = req.getPathInfo() == null ? "" : req.getPathInfo();
        //获取请求url的后两层
        String url = req.getServletPath() + pathInfo;
        //获取请求你ip后的全部路径
        String uri = req.getRequestURI();
        //注入xss过滤器实例
        XssHttpServletRequestWrapper reqW = new XssHttpServletRequestWrapper(req);

        //过滤掉不需要的Xss校验的地址
        for (String str : excludeUrls) {
            if (uri.contains(str)) {
                arg2.doFilter(arg0, response);
                return;
            }
        }
        //过滤
        arg2.doFilter(reqW, response);
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }
}