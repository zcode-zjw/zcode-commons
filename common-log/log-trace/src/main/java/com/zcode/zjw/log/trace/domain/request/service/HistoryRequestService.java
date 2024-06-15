package com.zcode.zjw.log.trace.domain.request.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zcode.zjw.common.utils.web.HttpUtil;
import com.zcode.zjw.log.trace.domain.request.repository.mapper.ApiRequestRecordMapper;
import com.zcode.zjw.log.trace.domain.request.repository.po.ApiRequestRecord;
import com.zcode.zjw.web.common.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史请求服务
 *
 * @author zhangjiwei
 * @since 2023/8/9
 */
@Service
@Slf4j
public class HistoryRequestService {

    private final ApiRequestRecordMapper apiRequestRecordMapper;

    private final RestTemplate restTemplate;

    public HistoryRequestService(ApiRequestRecordMapper apiRequestRecordMapper, RestTemplate restTemplate) {
        this.apiRequestRecordMapper = apiRequestRecordMapper;
        this.restTemplate = restTemplate;
    }

    public Result<Object> findHistoryRecord(ApiRequestRecord apiRequestRecord) {
        //apiRequestRecordMapper.selectById()
        return null;
    }

    /**
     * 发送历史请求
     *
     * @param id 历史记录ID
     * @return
     */
    public Result<Object> sendHistoryRequest(String id) {
        ApiRequestRecord apiRequestRecord = apiRequestRecordMapper.selectById(id);
        if (apiRequestRecord == null) {
            return Result.error("该请求记录不存在！");
        }
        // 发送请求
        try {
            return SendRequestStrategy.dealRequest(apiRequestRecord, restTemplate)
                    ? Result.success("发送历史请求成功") : Result.error("发送历史请求失败");
        } catch (Exception e) {
            log.error("发送历史请求错误", e);
            return Result.error("发送历史请求错误");
        }
    }

    @Getter
    @AllArgsConstructor
    enum SendRequestStrategy {
        GET("GET") {
            @Override
            protected boolean sendLogic(ApiRequestRecord apiRequestRecord, RestTemplate restTemplate) {
                ResponseEntity<String> response = HttpUtil.sendRequestByGet(restTemplate, apiRequestRecord.getUrl(),
                        apiRequestRecord.getHeaders(), apiRequestRecord.getParams());

                return response.getStatusCodeValue() == 200;
            }
        },
        POST("POST") {
            @Override
            protected boolean sendLogic(ApiRequestRecord apiRequestRecord, RestTemplate restTemplate) {
                //设置请求体参数
                String params = apiRequestRecord.getParams();
                Map<String, String> data = new HashMap<>();
                if (params != null) {
                    List<Map<String, String>> paramsList = (List<Map<String, String>>) JSONArray.parse(apiRequestRecord.getParams());
                    if (!paramsList.isEmpty()) {
                        for (Map<String, String> map : paramsList) {
                            data.putAll(map);
                        }
                    }
                }
                ResponseEntity<String> response = HttpUtil.sendRequestByPost(restTemplate, apiRequestRecord.getUrl(),
                        apiRequestRecord.getHeaders(), JSONObject.toJSONString(data));

                return response.getStatusCodeValue() == 200;
            }
        },
        PUT("PUT") {
            @Override
            protected boolean sendLogic(ApiRequestRecord apiRequestRecord, RestTemplate restTemplate) {
                return false;
            }
        },
        DELETE("DELETE") {
            @Override
            protected boolean sendLogic(ApiRequestRecord apiRequestRecord, RestTemplate restTemplate) {
                return false;
            }
        },
        ;

        private final String method;

        protected abstract boolean sendLogic(ApiRequestRecord apiRequestRecord, RestTemplate restTemplate);

        public static boolean dealRequest(ApiRequestRecord apiRequestRecord, RestTemplate restTemplate) {
            for (SendRequestStrategy strategy : SendRequestStrategy.values()) {
                if (strategy.getMethod().equals(apiRequestRecord.getMethod())) {
                    return strategy.sendLogic(apiRequestRecord, restTemplate);
                }
            }

            throw new RuntimeException("该请求没有可处理的策略：" + apiRequestRecord.toString());
        }

    }

}
