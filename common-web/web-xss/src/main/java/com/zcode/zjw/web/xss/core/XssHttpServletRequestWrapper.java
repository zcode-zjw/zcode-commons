package com.zcode.zjw.web.xss.core;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper() {
        super(null);
    }

    public XssHttpServletRequestWrapper(HttpServletRequest httpservletrequest) {
        super(httpservletrequest);
    }

    //过滤springmvc中的 @RequestParam 注解中的参数
    public String[] getParameterValues(String s) {

        String[] str = super.getParameterValues(s);
        if (str == null) {
            return null;
        }
        int i = str.length;
        String[] as1 = new String[i];
        for (int j = 0; j < i; j++) {
            //System.out.println("getParameterValues:"+str[j]);
            as1[j] = cleanXSS(cleanSQLInject(str[j]));
        }
        log.info("XssHttpServletRequestWrapper净化后的请求为：==========" + Arrays.toString(as1));
        return as1;
    }

    //过滤request.getParameter的参数
    public String getParameter(String s) {
        String s1 = super.getParameter(s);
        if (s1 == null) {
            return null;
        } else {
            String s2 = cleanXSS(cleanSQLInject(s1));
            log.info("XssHttpServletRequestWrapper净化后的请求为：==========" + s2);
            return s2;
        }
    }

    //过滤请求体 json 格式的
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(inputHandlers(super.getInputStream()).getBytes());

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }


    public String inputHandlers(ServletInputStream servletInputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(servletInputStream, StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (servletInputStream != null) {
                try {
                    servletInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cleanXSS(sb.toString());
    }

    public String cleanXSS(String src) {
        String temp = src;

        src = src.replaceAll("<", "＜").replaceAll(">", "＞");
        src = src.replaceAll("\\(", "（").replaceAll("\\)", "）");
        src = src.replaceAll("'", "＇");
        src = src.replaceAll(";", "；");
        //bgh 2018/05/30  新增
        /**-----------------------start--------------------------*/
        src = src.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        src = src.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41");
        src = src.replaceAll("eval\\((.*)\\)", "");
        src = src.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        src = src.replaceAll("script", "");
        src = src.replaceAll("link", "");
        src = src.replaceAll("frame", "");
        /**-----------------------end--------------------------*/
        Pattern pattern = Pattern.compile("(eval\\((.*)\\)|script)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(src);
        src = matcher.replaceAll("");

        pattern = Pattern.compile("[\\\"\\'][\\s]*javascript:(.*)[\\\"\\']",
                Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(src);
        src = matcher.replaceAll("\"\"");

        // 增加脚本
        src = src.replaceAll("script", "").replaceAll(";", "")
                /*.replaceAll("\"", "").replaceAll("@", "")*/
                .replaceAll("0x0d", "").replaceAll("0x0a", "");

        if (!temp.equals(src)) {
            // System.out.println("输入信息存在xss攻击！");
            // System.out.println("原始输入信息-->" + temp);
            // System.out.println("处理后信息-->" + src);

            log.error("xss攻击检查：参数含有非法攻击字符，已禁止继续访问！！");
            log.error("原始输入信息-->" + temp);

            throw new RuntimeException("xss攻击检查：参数含有非法攻击字符，已禁止继续访问！！");
        }
        return src;
    }

    //输出
    public void outputMsgByOutputStream(HttpServletResponse response, String msg) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream(); //获取输出流
        response.setHeader("content-type", "text/html;charset=UTF-8"); //通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
        byte[] dataByteArr = msg.getBytes(StandardCharsets.UTF_8);// 将字符转换成字节数组，指定以UTF-8编码进行转换
        outputStream.write(dataByteArr);// 使用OutputStream流向客户端输出字节数组
    }

    // 需要增加通配，过滤大小写组合
    public String cleanSQLInject(String src) {
        String lowSrc = src.toLowerCase();
        String temp = src;
        String lowSrcAfter = lowSrc.replaceAll("insert", "forbidI")
                .replaceAll("select", "forbidS")
                .replaceAll("update", "forbidU")
                .replaceAll("delete", "forbidD").replaceAll("and", "forbidA")
                .replaceAll("or", "forbidO");

        if (!lowSrcAfter.equals(lowSrc)) {
            log.error("sql注入检查：输入信息存在SQL攻击！");
            log.error("原始输入信息-->" + temp);
            log.error("处理后信息-->" + lowSrc);
            throw new RuntimeException("sql注入检查：参数含有非法攻击字符，已禁止继续访问！！");

        }
        return src;
    }

}