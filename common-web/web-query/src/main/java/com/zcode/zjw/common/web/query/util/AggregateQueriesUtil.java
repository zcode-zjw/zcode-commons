package com.zcode.zjw.common.web.query.util;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcode.zjw.common.web.query.suanfa.AggregateQueries;

import java.lang.reflect.Field;

public class AggregateQueriesUtil {

    /**
     * 聚合查询对象拼接
     *
     * @param queries   查询对象
     * @param aggregate 聚合查询对象
     * @return {@link QueryWrapper}<{@link Q}>
     */
    public static <Q, T, C, R> QueryWrapper<Q> splicingAggregateQueries(QueryWrapper<Q> queries, AggregateQueries<T, C, R> aggregate) {
        if (aggregate.hasQueries()) {
            splicingQueries(queries, aggregate.getQueries());
        }
        if (aggregate.hasCondition()) {
            splicingQueries(queries, aggregate.getCondition());
        }
        if (aggregate.hasFuzzyQueries() && !aggregate.hasQueries()) {
            splicingFuzzyQueries(queries, aggregate.getFuzzyQueries());
        }
        if (aggregate.hasSortField()) {
            aggregate.setSortType(aggregate.hasSortType() ? aggregate.getSortType() : 0);
            applySort(queries, aggregate.getSortField(), aggregate.getSortType());
        }
        return queries;
    }

    /**
     * 聚合查询对象拼接
     *
     * @param queries 查询对象
     * @param obj     聚合查询属性对象
     * @return 查询对象
     */
    public static <Q> QueryWrapper<Q> splicingQueries(QueryWrapper<Q> queries, Object obj) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String underlineCase = StrUtil.toUnderlineCase(field.getName());
            try {
                if (field.get(obj) != null) {
                    queries.eq(underlineCase, field.get(obj));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return queries;
    }

    /**
     * 模糊查询对象拼接
     *
     * @param queries 查询对象
     * @param obj     模糊查询属性对象
     * @return 查询对象
     */
    public static <Q> QueryWrapper<Q> splicingFuzzyQueries(QueryWrapper<Q> queries, Object obj) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String underlineCase = StrUtil.toUnderlineCase(field.getName());
            try {
                if (field.get(obj) != null) {
                    queries.like(underlineCase, field.get(obj));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return queries;
    }

    /**
     * 排序
     *
     * @param wrapper   查询对象
     * @param sortField 排序字段
     * @param sortType  排序类型
     */
    private static <Q> void applySort(QueryWrapper<Q> wrapper, String sortField, int sortType) {
        String field = StrUtil.toUnderlineCase(sortField);
        if (sortType == 1) {
            wrapper.orderByDesc(field);
        } else {
            wrapper.orderByAsc(field);
        }
    }


}
