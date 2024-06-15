package com.zcode.zjw.common.utils.cron;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhangjiwei
 * @description Cron表达式工具类
 * 目前支持三种常用的cron表达式
 * 1.每天的某个时间点执行 例:12 12 12 * * ?表示每天12时12分12秒执行
 * 2.每周的哪几天执行         例:12 12 12 ? * 1,2,3表示每周的周1周2周3 ,12时12分12秒执行
 * 3.每月的哪几天执行         例:12 12 12 1,21,13 * ?表示每月的1号21号13号 12时12分12秒执行
 * @since 2021/07/15
 */
@Slf4j
public class CronUtil {


    /**
     * 方法摘要：构建Cron表达式
     *
     * @param taskScheduleModel
     * @return String
     */
    public static String createCronExpression(TaskScheduleModel taskScheduleModel) {
        StringBuffer cronExp = new StringBuffer();

        if (null == taskScheduleModel.getJobType()) {
            log.info("执行周期未配置");//执行周期未配置
        }

        if (null != taskScheduleModel.getSecond()
                && null != taskScheduleModel.getMinute()
                && null != taskScheduleModel.getHour()) {
            //秒   
            cronExp.append(taskScheduleModel.getSecond()).append(" ");
            //分   
            cronExp.append(taskScheduleModel.getMinute()).append(" ");
            //小时   
            cronExp.append(taskScheduleModel.getHour()).append(" ");

            //每天   
            if (taskScheduleModel.getJobType() == 1) {
                cronExp.append("* ");//日 
                cronExp.append("* ");//月 
                cronExp.append("?");//周 
            }

            //按每周   
            else if (taskScheduleModel.getJobType() == 3) {
                //一个月中第几天   
                cronExp.append("? ");
                //月份   
                cronExp.append("* ");
                //周   
                Integer[] weeks = taskScheduleModel.getDayOfWeeks();
                for (int i = 0; i < weeks.length; i++) {
                    if (i == 0) {
                        cronExp.append(weeks[i]);
                    } else {
                        cronExp.append(",").append(weeks[i]);
                    }
                }

            }

            //按每月   
            else if (taskScheduleModel.getJobType() == 2) {
                //一个月中的哪几天   
                Integer[] days = taskScheduleModel.getDayOfMonths();
                for (int i = 0; i < days.length; i++) {
                    if (i == 0) {
                        cronExp.append(days[i]);
                    } else {
                        cronExp.append(",").append(days[i]);
                    }
                }
                //月份   
                cronExp.append(" * ");
                //周   
                cronExp.append("?");
            }

        } else {
            log.info("时或分或秒参数未配置");//时或分或秒参数未配置
        }
        return cronExp.toString();
    }

    /**
     * 方法摘要：生成计划的详细描述
     *
     * @param taskScheduleModel
     * @return String
     */
    public static String createDescription(TaskScheduleModel taskScheduleModel) {
        StringBuffer description = new StringBuffer();
        //计划执行开始时间   
//      Date startTime = taskScheduleModel.getScheduleStartTime();   

        if (null != taskScheduleModel.getSecond()
                && null != taskScheduleModel.getMinute()
                && null != taskScheduleModel.getHour()) {
            //按每天   
            if (taskScheduleModel.getJobType() == 1) {
                description.append("每天");
                description.append(taskScheduleModel.getHour()).append("时");
                description.append(taskScheduleModel.getMinute()).append("分");
                description.append(taskScheduleModel.getSecond()).append("秒");
                description.append("执行");
            }

            //按每周   
            else if (taskScheduleModel.getJobType() == 3) {
                if (taskScheduleModel.getDayOfWeeks() != null && taskScheduleModel.getDayOfWeeks().length > 0) {
                    StringBuilder days = new StringBuilder();
                    for (int i : taskScheduleModel.getDayOfWeeks()) {
                        days.append("周").append(i);
                    }
                    description.append("每周的").append(days).append(" ");
                }
                if (null != taskScheduleModel.getSecond()
                        && null != taskScheduleModel.getMinute()
                        && null != taskScheduleModel.getHour()) {
                    description.append(",");
                    description.append(taskScheduleModel.getHour()).append("时");
                    description.append(taskScheduleModel.getMinute()).append("分");
                    description.append(taskScheduleModel.getSecond()).append("秒");
                }
                description.append("执行");
            }

            //按每月   
            else if (taskScheduleModel.getJobType() == 2) {
                //选择月份   
                if (taskScheduleModel.getDayOfMonths() != null && taskScheduleModel.getDayOfMonths().length > 0) {
                    StringBuilder days = new StringBuilder();
                    for (int i : taskScheduleModel.getDayOfMonths()) {
                        days.append(i).append("号");
                    }
                    description.append("每月的").append(days).append(" ");
                }
                description.append(taskScheduleModel.getHour()).append("时");
                description.append(taskScheduleModel.getMinute()).append("分");
                description.append(taskScheduleModel.getSecond()).append("秒");
                description.append("执行");
            }
        }
        return description.toString();
    }

    public static Date nextScheduledDatetime(String cronExpression) throws ParseException {
        CronExpression cron = new CronExpression(cronExpression);
        return cron.getNextValidTimeAfter(new Date());
    }

    public static String nextScheduledDatetimeStr(String cronExpression) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CronExpression cron = new CronExpression(cronExpression);
        Date nextValidTimeAfterDate = cron.getNextValidTimeAfter(new Date());
        return dateFormat.format(nextValidTimeAfterDate);
    }

    //参考例子 
    public static void main(String[] args) throws ParseException {
        log.info(nextScheduledDatetimeStr("10 * * * * ?"));
        ////执行时间：每天的12时12分12秒 start
        //TaskScheduleModel taskScheduleModel = new TaskScheduleModel();
        //taskScheduleModel.setJobType(1);//按每天
        //Integer hour = 12; //时
        //Integer minute = 12; //分
        //Integer second = 12; //秒
        //taskScheduleModel.setHour(hour);
        //taskScheduleModel.setMinute(minute);
        //taskScheduleModel.setSecond(second);
        //String cropExp = createCronExpression(taskScheduleModel);
        //log.info(cropExp + ":" + createDescription(taskScheduleModel));
        ////执行时间：每天的12时12分12秒 end
        //
        //taskScheduleModel.setJobType(3);//每周的哪几天执行
        //Integer[] dayOfWeeks = new Integer[3];
        //dayOfWeeks[0] = 1;
        //dayOfWeeks[1] = 2;
        //dayOfWeeks[2] = 3;
        //taskScheduleModel.setDayOfWeeks(dayOfWeeks);
        //cropExp = createCronExpression(taskScheduleModel);
        //log.info(cropExp + ":" + createDescription(taskScheduleModel));
        //
        //taskScheduleModel.setJobType(2);//每月的哪几天执行
        //Integer[] dayOfMonths = new Integer[3];
        //dayOfMonths[0] = 1;
        //dayOfMonths[1] = 21;
        //dayOfMonths[2] = 13;
        //taskScheduleModel.setDayOfMonths(dayOfMonths);
        //cropExp = createCronExpression(taskScheduleModel);
        //log.info(cropExp + ":" + createDescription(taskScheduleModel));
    }
} 