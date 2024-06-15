package com.zcode.zjw.schedule.quartz.service;

import com.zcode.zjw.schedule.quartz.model.BusinessJobConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 描述
 *
 * @author zhangjiwei
 * @date 2023/6/5
 */
@SpringBootTest
class JobServiceTest {

    @Autowired
    private JobService jobService;

    @Test
    public void should_create_job() throws Exception {
        BusinessJobConfig jobConfig = new BusinessJobConfig();
        jobConfig.setServiceName("testApp");
        jobConfig.setMethodName("run");
        jobConfig.setId("testApp");
        jobConfig.setCronExpression("0/5 * * * * ?");
        jobConfig.setStatus("1");
        jobConfig.setRunAsAdmin(false);

        Map<String, Object> params = new HashMap<>();
        params.put("jobName", "testApp");
        params.put("messageId", "testApp");
        jobConfig.setParams(params);
        jobService.createJob(jobConfig);
        for (int i = 0; i < 10; i++) {
            Thread.sleep(10000);
        }
    }

}