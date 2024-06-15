package com.zcode.zjw.timejob.union.infrastructure.config;

import com.zcode.zjw.timejob.union.listener.JobTimer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author zhangjiwei
 * @description runTask自定义逻辑代理配置
 * @date 2022/11/10 下午9:09
 */
@Configuration
public class JobRunTaskProxyConfig {

    @Resource
    private ApplicationContext applicationContext;

    public void initRunTaskProxy() {
        Map<String, JobTimer> jobTimerChildren = applicationContext.getBeansOfType(JobTimer.class);
        jobTimerChildren.forEach((name, obj) -> {
            InvocationHandler timeJobInvocationHandler = getLogInvocationHandler(obj);
            JobTimer jobRunTaskProxy = (JobTimer) Proxy.newProxyInstance(
                    obj.getClass().getClassLoader(),
                    obj.getClass().getInterfaces(),
                    timeJobInvocationHandler
            );
        });
    }

    private static InvocationHandler getLogInvocationHandler(JobTimer target) {
        return (proxy1, method, args) -> {
            System.out.println(method.getName() + "方法开始执行...");
            Object result = method.invoke(target, args);
            System.out.println(result);
            System.out.println(method.getName() + "方法执行结束...");
            return result;
        };
    }


}
