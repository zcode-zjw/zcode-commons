package com.zcode.zjw.timejob.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.zcode.zjw.timejob.util.CronUtil.createCronExpression;
import static com.zcode.zjw.timejob.util.CronUtil.createDescription;

/**
 * 根据周期生成cron表达式
 *
 * @author zhangjiwei
 * @since 2023/7/18
 */
@Getter
@AllArgsConstructor
@ToString
public enum GenerateCronExpressByType {

    DAY("day", "天") {
        @Override
        protected Map<String, String> getCronExpression(TaskScheduleModel taskScheduleModel, String datetimeString) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime time = LocalTime.parse(datetimeString, formatter);
            Map<String, String> res = new HashMap<>();
            taskScheduleModel.setJobType(1);//按每天
            Integer hour = time.getHour(); //时
            Integer minute = time.getMinute(); //分
            Integer second = time.getSecond(); //秒
            taskScheduleModel.setHour(hour);
            taskScheduleModel.setMinute(minute);
            taskScheduleModel.setSecond(second);
            String cropExp = createCronExpression(taskScheduleModel);
            //System.out.println(cropExp + ":" + createDescription(taskScheduleModel));
            res.put("expression", cropExp);
            res.put("desc", createDescription(taskScheduleModel));
            return res;
        }
    },
    WEEK("week", "周") {
        @Override
        protected Map<String, String> getCronExpression(TaskScheduleModel taskScheduleModel, String datetimeString) {
            Map<String, String> res = new HashMap<>();
            taskScheduleModel.setJobType(3);//每周的哪几天执行
            String[] datetimeStrings = datetimeString.split(",");
            Integer[] dayOfWeeks = new Integer[datetimeStrings.length];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < datetimeStrings.length; i++) {
                LocalDateTime dateTime = LocalDateTime.parse(datetimeStrings[i], formatter);
                dayOfWeeks[i] = dateTime.getDayOfWeek().getValue();
                Integer hour = dateTime.getHour(); //时
                Integer minute = dateTime.getMinute(); //分
                Integer second = dateTime.getSecond(); //秒
                taskScheduleModel.setHour(hour);
                taskScheduleModel.setMinute(minute);
                taskScheduleModel.setSecond(second);
            }
            taskScheduleModel.setDayOfWeeks(dayOfWeeks);
            String cropExp = createCronExpression(taskScheduleModel);
            //System.out.println(cropExp + ":" + createDescription(taskScheduleModel));
            res.put("expression", cropExp);
            res.put("desc", createDescription(taskScheduleModel));
            return res;
        }
    },
    MONTH("month", "月") {
        @Override
        protected Map<String, String> getCronExpression(TaskScheduleModel taskScheduleModel, String datetimeString) {
            Map<String, String> res = new HashMap<>();
            taskScheduleModel.setJobType(2);//每月的哪几天执行
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String[] datetimeStrings = datetimeString.split(",");
            Integer[] dayOfMonths = new Integer[datetimeStrings.length];
            for (int i = 0; i < datetimeStrings.length; i++) {
                LocalDateTime dateTime = LocalDateTime.parse(datetimeStrings[i], formatter);
                dayOfMonths[i] = dateTime.getMonthValue();
                Integer hour = dateTime.getHour(); //时
                Integer minute = dateTime.getMinute(); //分
                Integer second = dateTime.getSecond(); //秒
                taskScheduleModel.setHour(hour);
                taskScheduleModel.setMinute(minute);
                taskScheduleModel.setSecond(second);
            }
            taskScheduleModel.setDayOfMonths(dayOfMonths);
            String cropExp = createCronExpression(taskScheduleModel);
            //System.out.println(cropExp + ":" + createDescription(taskScheduleModel));
            res.put("expression", cropExp);
            res.put("desc", createDescription(taskScheduleModel));
            return res;
        }
    };

    private final String code;

    private final String desc;

    public static Map<String, String> getCronExpressionByCycleType(String datetimeString, GenerateCronExpressByType cronCycleUnitType) {
        for (GenerateCronExpressByType type : GenerateCronExpressByType.values()) {
            if (Objects.equals(type.getCode(), cronCycleUnitType.getCode())) {
                TaskScheduleModel taskScheduleModel = new TaskScheduleModel();
                return cronCycleUnitType.getCronExpression(taskScheduleModel, datetimeString);
            }
        }

        throw new NotTransferCronExpressionException("cron表达式匹配失败，请检查日期字符串是否正确 -> " + datetimeString);
    }

    static class NotTransferCronExpressionException extends RuntimeException {
        public NotTransferCronExpressionException() {
        }

        public NotTransferCronExpressionException(String message) {
            super(message);
        }

        public NotTransferCronExpressionException(String message, Throwable cause) {
            super(message, cause);
        }

        public NotTransferCronExpressionException(Throwable cause) {
            super(cause);
        }

        public NotTransferCronExpressionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

    protected abstract Map<String, String> getCronExpression(TaskScheduleModel taskScheduleModel, String datetimeString);

    public static void main(String[] args) {
        System.out.println(GenerateCronExpressByType.getCronExpressionByCycleType("12:12:12", GenerateCronExpressByType.DAY));
        System.out.println(GenerateCronExpressByType.getCronExpressionByCycleType("2023-07-18 12:12:12,2023-07-19 12:12:12", GenerateCronExpressByType.WEEK));
        System.out.println(GenerateCronExpressByType.getCronExpressionByCycleType("2023-07-18 12:12:12", GenerateCronExpressByType.MONTH));
    }

}
