package com.zcode.zjw.schedule.quartz.support;

import org.quartz.Job;
import org.quartz.JobDataMap;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhangjiwei
 * @since 2022年04月28日
 */
public abstract class AbstractJob implements Job {

    public static final String JOB_NAME_PREFIX = "component_job_name_";
    public static final String JOB_GROUP_NAME_PREFIX = "component_job_group_name_";

    private JobDataMap jobDataMap;

    public abstract String getName();

    public abstract String getGroupName();

    public abstract String getCronExpression();

    protected String getDefaultName() {
        return JOB_NAME_PREFIX + UUID.randomUUID().toString().replace("-", "");
    }

    protected String getDefaultGroupName() {
        return JOB_GROUP_NAME_PREFIX + UUID.randomUUID().toString().replace("-", "");
    }

    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    public JobDataMap getJobDataMap() {
        return this.jobDataMap != null ? this.jobDataMap : new JobDataMap();
    }

    public Object getAttribute(String name) {
        return isBlank(jobDataMap) ? null : jobDataMap.get(name);
    }

    public void addAttribute(String name, Object value) {
        if (null != jobDataMap) {
            jobDataMap.put(name, value);
        }
    }

    public static boolean isBlank(Object object) {
        if (object instanceof Collection) {
            Collection<Object> objects = (Collection<Object>) object;
            return isBlank(objects);
        } else if (object instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>) object;
            return isBlank(map);
        } else {
            return object == null || "".equals(object);
        }
    }

}
