package com.zcode.zjw.timejob.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author : zjw
 * @description : 计划执行定义
 * @since : 2022/8/10 16:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlanExecuteDefBO {

    /**
     * 周期类型 minute:分钟 hour: 小时; day: 天; week: 周; month: 月; quarter: 季; year: 年
     */
    private String cycleType;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 定时任务id
     */
    private String jobId;

    /**
     * 开始时间
     */
    private LocalDate startTime;

    /**
     * 结束时间
     */
    private LocalDate endTime;

    /**
     * 周期内次数
     */
    private Integer numberOfCycles;

    /**
     * 指定一周哪几天
     */
    private List<Integer> weekDays;

    /**
     * 指定一个月哪几天
     */
    private List<Integer> monthDays;

    /**
     * 一周的星期几
     */
    private Integer dayOfWeek;

    /**
     * 第几周
     */
    private Integer week;

    /**
     * 重复规则
     */
    private String repeatRule;

    /**
     * 执行时间
     */
    private LocalTime executionTime;

}