package com.zcode.zjw.datasource.multiple.core;

import com.zcode.zjw.datasource.multiple.annotation.DataSource;
import com.zcode.zjw.datasource.multiple.utils.DynamicDataSourceHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 创建多数据源的切面类
 * 切面类主要用于获取被数据与注解指定的方法，拿到其注解中的属性值，再设置到数据源key设置组件中，方便数据源类获取该key
 * 需使用@Order设置切面优先级，否则设置无效
 *
 * @author：zjw
 */
@Aspect
@Order(100)
public class DynamicDataSourceAdviser {

    @Pointcut("@annotation(com.zcode.zjw.datasource.multiple.annotation.DataSource)")
    public void pointcut() {
    }

    ;

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        try {
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            //获取被代理的方法对象
            Method targetMethod = methodSignature.getMethod();
            // 获取数据源注解
            DataSource ds = targetMethod.getAnnotation(DataSource.class);
            if (Objects.nonNull(ds)) {
                DynamicDataSourceHolder.setDataSource(ds.value());
            }
            return point.proceed();
        } finally {
            DynamicDataSourceHolder.remove();
        }


    }

}