package com.zcode.zjw.schedule.quartz.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zhangjiwei
 * @since 2022年05月08日
 */
@Configuration
@ConfigurationProperties(prefix = "component.schedule")
public class JobProperties {

    private List<BusinessJobConfig> jobs;

    public List<BusinessJobConfig> getJobs() {
        return jobs;
    }

    public void setJobs(List<BusinessJobConfig> jobs) {
        this.jobs = jobs;
    }
}
