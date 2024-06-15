package com.zcode.zjw.timejob.context.impl;

import com.alibaba.fastjson.JSONObject;
import com.zcode.zjw.timejob.common.JobOperateTypeEnum;
import com.zcode.zjw.timejob.service.TestTimeJobService;
import com.zcode.zjw.timejob.union.handler.impl.StartTaskCronStringHandler;
import com.zcode.zjw.timejob.util.GenerateCronExpressByType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/7/18
 */
@SpringBootTest
public class TimeJobApplicationAdviceTest {

    @Autowired
    private TimeJobApplicationAdvice timeJobApplicationAdvice;

    @Test
    public void should_exec_successfully_time_job() throws InterruptedException {
        timeJobApplicationAdvice.startJob(timeJobApplicationAdvice.buildJobParamsEntity()
                .setTaskOperateType(JobOperateTypeEnum.START)
                .setTaskCycleDatetimeString("21:12:21")
                .setCycleType(GenerateCronExpressByType.DAY)
                .setTaskName("first_job_test")
                .setTotalRunCount(2)
                .setTaskStatus(0)
                .setJobService(TestTimeJobService.class)
                .setFinishCallBack((param) -> System.out.println("job完成 -> " + param)).build());
        Thread.sleep(100000);
    }

    @Test
    public void easy_test() {
        String jsonString = JSONObject.toJSONString(new StartTaskCronStringHandler());
        System.out.println(jsonString);
        System.out.println(JSONObject.parseObject(jsonString, StartTaskCronStringHandler.class));
    }

}