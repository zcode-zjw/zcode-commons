package com.zcode.zjw.common.web.query.enhancer;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcode.zjw.common.utils.bean.SpringContextUtils;
import com.zcode.zjw.common.utils.common.ObjectUtil;
import com.zcode.zjw.common.web.query.annotation.AggregateQuery;
import com.zcode.zjw.common.web.query.annotation.AggregateQueryField;
import com.zcode.zjw.common.web.query.annotation.AggregateQueryRegexMode;
import com.zcode.zjw.common.web.query.suanfa.AggregateQueries;
import com.zcode.zjw.common.web.query.suanfa.PaginationDTO;
import com.zcode.zjw.common.web.query.util.AggregateQueriesUtil;
import com.zcode.zjw.web.common.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聚合查询切面
 *
 * @author zhangjiwei
 * @since 2023/8/11
 */
@Aspect
@Component
public class AggregateQueryAspect {

    @Pointcut("@annotation(com.zcode.zjw.common.web.query.annotation.AggregateQuery)")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        AggregateQueries<Object, Object, Object> aggregate = null;
        Map<String, Object> commonParams = null;
        Method method = ((MethodSignature) signature).getMethod();
        AggregateQuery aggregateQuery = method.getAnnotation(AggregateQuery.class);
        // 解析请求参数
        Object[] args = joinPoint.getArgs();
        // 遍历参数数组
        for (Object arg : args) {
            if (arg instanceof JSONObject) {
                JSONObject params = ((JSONObject) arg).getJSONObject("aggregateQueryParams");
                commonParams = (Map<String, Object>) ((JSONObject) arg).get("commonParams");
                if (params != null) {
                    aggregate = params.toJavaObject(AggregateQueries.class);
                    mapAggregateProperty(aggregate, aggregateQuery.vo());
                }
            }
        }
        if (aggregate == null) {
            throw new RuntimeException("找不到可解析的聚合参数");
        }

        BaseMapper mapperBean;
        String mapperName = aggregateQuery.mapper();
        Class<?> mapperClass = aggregateQuery.mapperClass();
        if ("".equals(mapperName)) {
            mapperBean = (BaseMapper) SpringContextUtils.getBean(mapperClass);
        } else {
            mapperBean = (BaseMapper) SpringContextUtils.getBean(mapperName);
        }

        if (!aggregate.hasPagination()) {
            return Result.error("查询失败");
        }

        return Result.success(getRecords(aggregate, mapperBean, aggregateQuery, commonParams));
    }

    private static Object getRecords(AggregateQueries<Object, Object, Object> aggregate, BaseMapper mapperBean,
                                     AggregateQuery aggregateQuery, Map<String, Object> commonParams) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String mapperMethodName = aggregateQuery.mapperMethod();
        if (!"".equals(mapperMethodName)) {
            String argsKey = aggregateQuery.mapperArgsKey();
            Class<?> argsType = aggregateQuery.mapperArgsType();
            Method mapperMethod;
            if (argsType.equals(Class.class)) {
                mapperMethod = mapperBean.getClass().getMethod(mapperMethodName);
            } else {
                mapperMethod = mapperBean.getClass().getMethod(mapperMethodName, argsType);
            }
            Object invokeRes;
            if (!"".equals(argsKey)) {
                Object argObj = commonParams.get(argsKey);
                if (!argsType.equals(Map.class)) {
                    argObj = JSONObject.parseObject(JSONObject.toJSONString(commonParams.get(argsKey)))
                            .toJavaObject(argsType);
                }
                invokeRes = mapperMethod.invoke(mapperBean, argObj);
            } else {
                invokeRes = mapperMethod.invoke(mapperBean);
            }

            return invokeRes;
        } else {
            PaginationDTO pagination = aggregate.getPagination();
            QueryWrapper<Object> wrapper = AggregateQueriesUtil.splicingAggregateQueries(new QueryWrapper<>(), aggregate);
            Page<Object> page = new Page<>(pagination.getPage(), pagination.getSize());
            Page<Object> addressPage = (Page<Object>) mapperBean.selectPage(page, wrapper);
            return addressPage.getRecords();
        }
    }

    private void mapAggregateProperty(AggregateQueries<Object, Object, Object> aggregate, Class<?> vo) throws Exception {
        // 获取vo的排除字段
        Field[] declaredFieldsForVo = vo.getDeclaredFields();
        List<String> conditionContainsList = new ArrayList<>();
        List<String> queriesContainsList = new ArrayList<>();
        List<String> fuzzyContainsList = new ArrayList<>();
        for (Field field : declaredFieldsForVo) {
            AggregateQueryField annotation = field.getAnnotation(AggregateQueryField.class);
            if (annotation != null) {
                boolean condition = annotation.condition();
                if (condition && !conditionContainsList.contains(field.getName())) {
                    conditionContainsList.add(field.getName());
                }
                AggregateQueryRegexMode regexMode = annotation.regexMode();
                if (regexMode.equals(AggregateQueryRegexMode.EXACT) && !queriesContainsList.contains(field.getName())) {
                    queriesContainsList.add(field.getName());
                } else if (regexMode.equals(AggregateQueryRegexMode.FUZZY) && !fuzzyContainsList.contains(field.getName())) {
                    fuzzyContainsList.add(field.getName());
                }
            }
        }

        Object queries = aggregate.getQueries();
        Object fuzzyQueries = aggregate.getFuzzyQueries();
        Object condition = aggregate.getCondition();

        // 剔除字段（防止前端肆无忌惮的操作）
        if (vo.getDeclaredAnnotation(AggregateQueryField.class) == null) { // 如果po直接标注了该注解，放行
            if (queries != null) {
                Map<String, Object> queriesRes = new HashMap<>();
                Map<String, Object> queriesMap = (Map<String, Object>) queries;
                for (Map.Entry<String, Object> entry : queriesMap.entrySet()) {
                    if (queriesContainsList.contains(entry.getKey())) {
                        queriesRes.put(entry.getKey(), entry.getValue());
                    }
                }
                queries = queriesRes;
            }
            if (fuzzyQueries != null) {
                Map<String, Object> fuzzyRes = new HashMap<>();
                Map<String, Object> fuzzyMap = (Map<String, Object>) fuzzyQueries;
                for (Map.Entry<String, Object> entry : fuzzyMap.entrySet()) {
                    if (fuzzyContainsList.contains(entry.getKey())) {
                        fuzzyRes.put(entry.getKey(), entry.getValue());
                    }
                }
                fuzzyQueries = fuzzyRes;
            }
            if (condition != null) {
                Map<String, Object> conditionRes = new HashMap<>();
                Map<String, Object> conditionMap = (Map<String, Object>) condition;
                for (Map.Entry<String, Object> entry : conditionMap.entrySet()) {
                    if (conditionContainsList.contains(entry.getKey())) {
                        conditionRes.put(entry.getKey(), entry.getValue());
                    }
                }
                condition = conditionRes;
            }
        }

        // 获取前端传来的规则 -> 前端规则pojo
        if (queries != null) {
            // 动态生成pojo对象
            Object queriesPojo = ObjectUtil.createObjectByMap((Map<String, Object>) queries);
            // 设置pojo
            if (queriesPojo.getClass().getDeclaredFields().length == 0) {
                aggregate.setQueries(null);
            } else {
                aggregate.setQueries(queriesPojo);
            }
        }
        if (fuzzyQueries != null) {
            // 动态生成pojo对象
            Object fuzzyQueriesPojo = ObjectUtil.createObjectByMap((Map<String, Object>) fuzzyQueries);
            // 设置pojo
            if (fuzzyQueriesPojo.getClass().getDeclaredFields().length == 0) {
                aggregate.setFuzzyQueries(null);
            } else {
                aggregate.setFuzzyQueries(fuzzyQueriesPojo);
            }
        }
        if (condition != null) {
            // 动态生成pojo对象
            Object conditionPojo = ObjectUtil.createObjectByMap((Map<String, Object>) condition);
            // 设置pojo
            if (conditionPojo.getClass().getDeclaredFields().length == 0) {
                aggregate.setCondition(null);
            } else {
                aggregate.setCondition(conditionPojo);
            }
        }
    }

}
