package com.zcode.zjw.schedule.quartz.controller;

import com.zcode.zjw.schedule.quartz.model.BusinessJobConfig;
import com.zcode.zjw.schedule.quartz.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述
 *
 * @author zhangjiwei
 * @date 2023/6/4
 */
@RestController
@RequestMapping("/quartz/")
@Slf4j
public class TestQuartController {

    @Autowired
    private JobService jobService;

    @GetMapping("/task/create/{id}")
    public String createTask(@PathVariable("id") String messageId) {
        String jobName = jobService.getJobName("repeatSendFourJob", "run", messageId);
        if (jobService.isJobExist(jobName)) {
            log.info("{}--任务已存在", jobName);
        } else {
            BusinessJobConfig jobConfig = new BusinessJobConfig();
            jobConfig.setServiceName("repeatSendFourJob");
            jobConfig.setMethodName("run");
            jobConfig.setId(messageId);
            jobConfig.setCronExpression("0/15 * * * * ?");
            jobConfig.setStatus("1");
            jobConfig.setRunAsAdmin(false);

            Map<String, Object> params = new HashMap<>();
            params.put("jobName", jobName);
            params.put("messageId", messageId);
            jobConfig.setParams(params);
            jobService.createJob(jobConfig);
        }
        return "ok";
    }
}
