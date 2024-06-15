package com.zcode.zjw.common.utils.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

/**
 * 请求工具类
 *
 * @author zhangjiwei
 * @since 2023/8/9
 */
public class HttpUtil {

    /**
     * 发送Get请求
     *
     * @param restTemplate rest客户端
     * @param url          请求URL
     * @param headers      请求头
     * @param params       请求参数
     * @return 响应实体对象
     */
    public static ResponseEntity<String> sendRequestByGet(RestTemplate restTemplate, String url,
                                                          Map<String, String> headers, Map<String, String> params) {
        //首先创建请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        //根据需要设置请求头参数,可以设置多个
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestHeaders.set(entry.getKey(), entry.getValue());
            }
        }

        //设置请求体参数
        UriComponentsBuilder builder = null;
        if (params != null) {
            builder = UriComponentsBuilder.fromHttpUrl(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }

        HttpEntity<String> entity = new HttpEntity<>(requestHeaders);

        return restTemplate.exchange(
                Optional.ofNullable(builder).map(UriComponentsBuilder::toUriString).orElse(url),
                HttpMethod.GET,
                entity,
                String.class
        );
    }

    /**
     * 发送Get请求
     *
     * @param restTemplate rest客户端
     * @param url          请求URL
     * @param jsonHeaders  Json格式请求头
     * @param jsonParams   Json格式请求参数
     * @return 响应实体对象
     */
    public static ResponseEntity<String> sendRequestByGet(RestTemplate restTemplate, String url,
                                                          String jsonHeaders, String jsonParams) {
        //首先创建请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        Map<String, Object> headers = JSONObject.parseObject(jsonHeaders).toJavaObject(Map.class);
        //根据需要设置请求头参数,可以设置多个
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                requestHeaders.set(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        //设置请求体参数
        UriComponentsBuilder builder = null;
        Map<String, Object> params = JSONObject.parseObject(jsonParams).toJavaObject(Map.class);
        if (params != null) {
            builder = UriComponentsBuilder.fromHttpUrl(url);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.queryParam(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        HttpEntity<String> entity = new HttpEntity<>(requestHeaders);

        return restTemplate.exchange(
                Optional.ofNullable(builder).map(UriComponentsBuilder::toUriString).orElse(url),
                HttpMethod.GET,
                entity,
                String.class
        );
    }

    /**
     * 发送POST请求
     *
     * @param restTemplate rest客户端
     * @param url          请求URL
     * @param headers      请求头
     * @param data         请求参数
     * @return 响应实体对象
     */
    public static ResponseEntity<String> sendRequestByPost(RestTemplate restTemplate, String url,
                                                           Map<String, String> headers, Map<String, String> data) {
        //首先创建请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        //根据需要设置请求头参数,可以设置多个
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestHeaders.set(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(data), requestHeaders);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    /**
     * 发送POST请求
     *
     * @param restTemplate rest客户端
     * @param url          请求URL
     * @param jsonHeaders  Json格式请求头
     * @param jsonData     Json格式请求参数
     * @return 响应实体对象
     */
    public static ResponseEntity<String> sendRequestByPost(RestTemplate restTemplate, String url,
                                                           String jsonHeaders, String jsonData) {
        //首先创建请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        Map<String, Object> headers = JSONObject.parseObject(jsonHeaders).toJavaObject(Map.class);
        //根据需要设置请求头参数,可以设置多个
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                requestHeaders.set(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        HttpEntity<String> entity = new HttpEntity<>(jsonData, requestHeaders);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );
    }

}
