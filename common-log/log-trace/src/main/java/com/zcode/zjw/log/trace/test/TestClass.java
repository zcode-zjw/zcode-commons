package com.zcode.zjw.log.trace.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcode.zjw.log.trace.domain.core.repository.mapper.TraceLogInfoMapper;
import com.zcode.zjw.log.trace.domain.core.repository.po.TraceLogInfo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/8/3
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TestClass {

    @Autowired
    private TraceLogInfoMapper traceLogInfoMapper;

    public String testMethod(int a) {
        StringBuilder res = new StringBuilder();
        int c = a + 1;
        int d = 3;
        User user = new User("zjw", 24);
        Role role = new Role();
        role.setUser(user);
        int age = user.getAge();
        for (int i = 0; i < 3; i++) {
            if (i == 2) {
                continue;
            }
            age += 1;
        }
        System.out.println("testMethod running...");
        try {
            File file = new File("/Users/mac/Desktop/test-dir/myapp/test.txt");
            List<String> strings = FileUtils.readLines(file, "utf-8");
            for (String string : strings) {
                res.append(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        QueryWrapper<TraceLogInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flow_id", "691383a7-93f2-4387-b2f6-6011f6aa0467");
        List<TraceLogInfo> traceLogInfos = traceLogInfoMapper.selectList(queryWrapper);
        System.out.println(traceLogInfos);

        return (d + c + 1 + age) + res.toString();
    }

}
