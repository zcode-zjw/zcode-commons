package com.zcode.zjw.timejob.service;

import com.zcode.zjw.timejob.union.listener.JobTimer;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/7/18
 */
@Service
public class TestTimeJobService extends JobTimer {

    @Override
    public void runTask(Map<String, Object> params) {
        System.out.println("测试job正在运行...");
    }

    @Override
    public Class<? extends JobTimer> register() {
        return TestTimeJobService.class;
    }

    @Override
    public boolean trigger() {
        return true;
    }

}
