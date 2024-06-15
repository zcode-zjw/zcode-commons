package com.zcode.zjw.datasource.interception;

import com.baomidou.mybatisplus.core.MybatisParameterHandler;
import com.zcode.zjw.datasource.annotation.EncryptTransaction;
import com.zcode.zjw.datasource.annotation.SensitiveData;
import com.zcode.zjw.datasource.utils.DBAESUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.PreparedStatement;
import java.util.*;

/**
 * 参数插件ParameterInterceptor
 * 切入mybatis设置参数时对敏感数据进行加密
 * Mybatis插件的使用就是通过实现Mybatis中的Interceptor接口
 * 再配合@Intercepts注解
 *
 * @author zhangjiwei
 * @date 2023/2/13 上午9:10
 */

@Slf4j
@Component
@Intercepts({@Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class)})
public class ParameterInterceptor implements Interceptor {

    @Autowired
    private com.zcode.zjw.datasource.utils.IEncryptUtil IEncryptUtil;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //@Signature 指定了 type= parameterHandler 后，这里的 invocation.getTarget() 便是parameterHandler
        //若指定ResultSetHandler ，这里则能强转为ResultSetHandler
        MybatisParameterHandler parameterHandler = (MybatisParameterHandler) invocation.getTarget();
        // 获取参数对像，即 mapper 中 paramsType 的实例
        Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
        parameterField.setAccessible(true);
        //取出实例
        Object parameterObject = parameterField.get(parameterHandler);
        // 搜索该方法中是否有需要加密的普通字段
        List<String> paramNames = searchParamAnnotation(parameterHandler);
        if (parameterObject != null) {
            Class<?> parameterObjectClass = parameterObject.getClass();
            //对类字段进行加密
            //校验该实例的类是否被@SensitiveData所注解
            SensitiveData sensitiveData = AnnotationUtils.findAnnotation(parameterObjectClass, SensitiveData.class);
            if (Objects.nonNull(sensitiveData)) {
                //取出当前当前类所有字段，传入加密方法
                Field[] declaredFields = parameterObjectClass.getDeclaredFields();
                IEncryptUtil.encrypt(declaredFields, parameterObject);
            }
            // 对普通字段进行加密
            if (!CollectionUtils.isEmpty(paramNames)) {
                // 反射获取 BoundSql 对象，此对象包含生成的sql和sql的参数map映射
                Field boundSqlField = parameterHandler.getClass().getDeclaredField("boundSql");
                boundSqlField.setAccessible(true);
                BoundSql boundSql = (BoundSql) boundSqlField.get(parameterHandler);
                PreparedStatement ps = (PreparedStatement) invocation.getArgs()[0];
                // 改写参数
                processParam(parameterObject, paramNames);
                // 改写的参数设置到原parameterHandler对象
                parameterField.set(parameterHandler, parameterObject);
                parameterHandler.setParameters(ps);
            }
        }
        return invocation.proceed();
    }


    private void processParam(Object parameterObject, List<String> params) throws Exception {
        // 处理参数对象  如果是 map 且map的key 中没有 tenantId，添加到参数map中
        // 如果参数是bean，反射设置值

        if (parameterObject instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, String> map = ((Map<String, String>) parameterObject);
            for (String param : params) {
                String value = map.get(param);
                map.put(param, value == null ? null : DBAESUtil.encrypt(value));
            }
//            parameterObject = map;
        }
    }

    private List<String> searchParamAnnotation(ParameterHandler parameterHandler) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        Class<MybatisParameterHandler> handlerClass = MybatisParameterHandler.class;
        Field mappedStatementFiled = handlerClass.getDeclaredField("mappedStatement");
        mappedStatementFiled.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) mappedStatementFiled.get(parameterHandler);
        String methodName = mappedStatement.getId();
        Class<?> mapperClass = Class.forName(methodName.substring(0, methodName.lastIndexOf('.')));
        methodName = methodName.substring(methodName.lastIndexOf('.') + 1);
        Method[] methods = mapperClass.getDeclaredMethods();
        Method method = null;
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                method = m;
                break;
            }
        }
        List<String> paramNames = null;
        if (method != null) {

            Annotation[][] pa = method.getParameterAnnotations();
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < pa.length; i++) {
                for (Annotation annotation : pa[i]) {
                    if (annotation instanceof EncryptTransaction) {
                        if (paramNames == null) {
                            paramNames = new ArrayList<>();
                        }
                        paramNames.add(parameters[i].getName());
                    }
                }
            }
        }
        return paramNames;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}