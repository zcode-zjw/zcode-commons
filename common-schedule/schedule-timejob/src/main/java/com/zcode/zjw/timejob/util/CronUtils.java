package com.zcode.zjw.timejob.util;

import com.cronutils.builder.CronBuilder;
import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.expression.FieldExpression;
import com.cronutils.model.field.expression.FieldExpressionFactory;
import com.cronutils.model.field.expression.On;
import com.cronutils.model.field.value.SpecialChar;
import com.cronutils.parser.CronParser;
import com.zcode.zjw.timejob.common.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.cronutils.model.field.expression.FieldExpression.always;
import static com.cronutils.model.field.expression.FieldExpression.questionMark;
import static com.cronutils.model.field.expression.FieldExpressionFactory.and;
import static com.cronutils.model.field.expression.FieldExpressionFactory.on;

/**
 * @author zhangjiwei
 * @description 获取Cron字符串
 * @date 2022/12/11 上午9:34
 */
public class CronUtils {
    /**
     * 星期
     */
    private static final List<Integer> WEEKS =  Arrays.asList(WeekEnum.SUNDAY.getValue(),
            WeekEnum.MONDAY.getValue(),
            WeekEnum.THURSDAY.getValue(),
            WeekEnum.WEDNESDAY.getValue(),
            WeekEnum.THURSDAY.getValue(),
            WeekEnum.FRIDAY.getValue(),
            WeekEnum.SATURDAY.getValue());

    private static CronBuilder cronBuilder;

    static {
        cronBuilder = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
    }

    public static String createCron(PlanExecuteDefBO planExecuteDefBO) {
        LocalTime executionTime = planExecuteDefBO.getExecutionTime();
        LocalDate startTime = planExecuteDefBO.getStartTime();
        String cycleType = planExecuteDefBO.getCycleType();
        int minute = planExecuteDefBO.getExecutionTime().getMinute();
        int hour = executionTime.getHour();
        int day = startTime.getDayOfMonth();
        int month = startTime.getMonth().getValue();
        cronBuilder = cronBuilder.withSecond(on(executionTime.getSecond()));

        // 每分钟一次
        if (Objects.equals(PlanCycleTypeEnum.MINUTE.getCycleType(), cycleType)) {
            return cronBuilder.withDoW(questionMark())
                    .withMonth(always())
                    .withDoM(always())
                    .withHour(always())
                    .withMinute(always()).instance().asString();
        }

        // 每小时一次
        if (Objects.equals(PlanCycleTypeEnum.HOUR.getCycleType(), cycleType)) {
            return cronBuilder.withDoW(questionMark())
                    .withMonth(always())
                    .withDoM(always())
                    .withHour(always())
                    .withMinute(on(executionTime.getMinute())).instance().asString();
        }

        // 每天一次
        if (Objects.equals(PlanCycleTypeEnum.DAY.getCycleType(), cycleType)) {
            return cronBuilder.withDoW(questionMark())
                    .withMonth(always())
                    .withDoM(always())
                    .withHour(on(hour))
                    .withMinute(on(minute))
                    .instance().asString();
        }

        // 每周一次
        if (Objects.equals(PlanCycleTypeEnum.WEEK.getCycleType(), cycleType)) {
            List<FieldExpression> weekDays = new ArrayList<>();
            planExecuteDefBO.getWeekDays().forEach(e -> weekDays.add(FieldExpressionFactory.on(e)));
            return cronBuilder.withDoW(and(weekDays))
                    .withMonth(always())
                    .withDoM(questionMark())
                    .withHour(on(hour))
                    .withMinute(on(minute))
                    .instance().asString();
        }

        // 每月一次
        if (Objects.equals(PlanCycleTypeEnum.MONTH.getCycleType(), cycleType)) {
            List<FieldExpression> monthDays = new ArrayList<>();
            planExecuteDefBO.getMonthDays().forEach(e -> monthDays.add(FieldExpressionFactory.on(e)));
            if (Objects.equals(RepeatRuleEnum.DATE.getType(), planExecuteDefBO.getRepeatRule())) {
                return cronBuilder.withDoW(questionMark())
                        .withMonth(always())
                        .withDoM(and(monthDays))
                        .withHour(on(hour))
                        .withMinute(on(minute))
                        .instance().asString();
            }
            if (Objects.equals(RepeatRuleEnum.WEEK.getType(), planExecuteDefBO.getRepeatRule())) {
                return cronBuilder.withDoW(on(WEEKS.get(planExecuteDefBO.getDayOfWeek()), SpecialChar.HASH, planExecuteDefBO.getWeek()))
                        .withMonth(always())
                        .withDoM(questionMark())
                        .withHour(on(hour))
                        .withMinute(on(minute))
                        .instance().asString();
            }
        }

        // 每季度一次
        if (Objects.equals(PlanCycleTypeEnum.QUARTER.getCycleType(), cycleType)) {
            List<FieldExpression> flist = new ArrayList<>();
            On quarter1 = FieldExpressionFactory.on(1);
            On quarter2 = FieldExpressionFactory.on(4);
            On quarter3 = FieldExpressionFactory.on(7);
            On quarter4 = FieldExpressionFactory.on(10);
            flist.add(quarter1);
            flist.add(quarter2);
            flist.add(quarter3);
            flist.add(quarter4);
            return cronBuilder.withDoW(questionMark())
                    .withMonth(and(flist))
                    .withDoM(on(day))
                    .withHour(on(hour))
                    .withMinute(on(minute))
                    .instance().asString();
        }

        // 每年一次
        if (Objects.equals(PlanCycleTypeEnum.YEAR.getCycleType(), cycleType)) {
            List<FieldExpression> flist = new ArrayList<>();
            On on = FieldExpressionFactory.on(day);
            flist.add(on);
            return cronBuilder.withYear(always())
                    .withDoW(questionMark())
                    .withMonth(on(month))
                    .withDoM(on(day))
                    .withHour(on(hour))
                    .withMinute(on(minute))
                    .instance().asString();
        }

        // 按秒执行
        return cronBuilder.withYear(always())
                .withDoW(questionMark())
                .withMonth(always())
                .withDoM(always())
                .withHour(always())
                .withMinute(always()).instance().asString();
    }

    public static String generateCronString(int sepTime, JobTimeUnitEnum timeUnit) {
        CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
        CronParser parser = new CronParser(cronDefinition);
        CronDescriptor descriptor = CronDescriptor.instance(Locale.UK);
        int year = DateUtils.getCurrentYearByNum().get();
        int month = DateUtils.getCurrentMonthNumByNum().get();
        int day = DateUtils.getCurrentDayNumByNum().get();
        int minute = DateUtils.getCurrentMinuteByNum().get();
        int hour = DateUtils.getCurrentHourByNum().get();
        int second = DateUtils.getCurrentSecondByNum().get();

        String res = null;
        // 每年执行一次
        if (JobTimeUnitEnum.YEAR.equals(timeUnit)) {
            LocalDate startTime = LocalDate.of(year, month, day);
            LocalTime executeTime = LocalTime.of(hour , minute, second);
            PlanExecuteDefBO planExecuteDefBO = new PlanExecuteDefBO();
            planExecuteDefBO.setStartTime(startTime);
            planExecuteDefBO.setExecutionTime(executeTime);
            planExecuteDefBO.setCycleType("year");
            res = CronUtils.createCron(planExecuteDefBO);
        } else if (JobTimeUnitEnum.MONTH.equals(timeUnit)) {
            LocalDate startTime = LocalDate.of(year, month, day);
            LocalTime executeTime = LocalTime.of(hour, minute, second);
            PlanExecuteDefBO planExecuteDefBO = new PlanExecuteDefBO();
            planExecuteDefBO.setStartTime(startTime);
            planExecuteDefBO.setExecutionTime(executeTime);
            planExecuteDefBO.setCycleType("month");

            planExecuteDefBO.setMonthDays(Arrays.asList(1,11,25));
            //planExecuteDefBO.setRepeatRule("date"); // 指定月内某几天执行
            planExecuteDefBO.setRepeatRule("week"); // 可选每月第几周的星期几执行
            planExecuteDefBO.setDayOfWeek(1);
            planExecuteDefBO.setWeek(1);

            res = CronUtils.createCron(planExecuteDefBO);
        } else if (JobTimeUnitEnum.DAY.equals(timeUnit)) {
            LocalDate startTime = LocalDate.of(year, month, day);
            LocalTime executeTime = LocalTime.of(hour, minute, second);
            PlanExecuteDefBO planExecuteDefBO = new PlanExecuteDefBO();
            planExecuteDefBO.setStartTime(startTime);
            planExecuteDefBO.setExecutionTime(executeTime);
            planExecuteDefBO.setCycleType("day");
            res = CronUtils.createCron(planExecuteDefBO);
        } else if (JobTimeUnitEnum.HOUR.equals(timeUnit)) {
            LocalDate startTime = LocalDate.of(year, month, day);
            LocalTime executeTime = LocalTime.of(hour, minute, second);
            PlanExecuteDefBO planExecuteDefBO = new PlanExecuteDefBO();
            planExecuteDefBO.setStartTime(startTime);
            planExecuteDefBO.setExecutionTime(executeTime);
            planExecuteDefBO.setCycleType("hour");
            res = CronUtils.createCron(planExecuteDefBO);
        } else if (JobTimeUnitEnum.MINUTE.equals(timeUnit)) {
            LocalDate startTime = LocalDate.of(year, month, day);
            LocalTime executeTime = LocalTime.of(hour, minute, second);
            PlanExecuteDefBO planExecuteDefBO = new PlanExecuteDefBO();
            planExecuteDefBO.setStartTime(startTime);
            planExecuteDefBO.setExecutionTime(executeTime);
            planExecuteDefBO.setCycleType("minute");
            res = CronUtils.createCron(planExecuteDefBO);
        } else if (JobTimeUnitEnum.SECOND.equals(timeUnit)) {
            res = CronConvertUtil.convert(String.valueOf(sepTime)).get();
        }
        if (timeUnit.equals(JobTimeUnitEnum.SECOND) || res == null) {
            return res;
        }
        StringBuilder result = new StringBuilder();
        boolean count = true;
        for (String s : res.split(" ")) {
            if (count && s.equals("*")) {
                count = false;
                result.append(" */").append(sepTime);
            } else {
                result.append(" ").append(s);
            }
        }
        return result.toString().trim();
    }

    public static void main(String[] args) {
        System.out.println(generateCronString(3, JobTimeUnitEnum.SECOND));
        System.out.println(generateCronString(3, JobTimeUnitEnum.MINUTE));
        System.out.println(generateCronString(3, JobTimeUnitEnum.HOUR));
        System.out.println(generateCronString(3, JobTimeUnitEnum.DAY));
        System.out.println(generateCronString(3, JobTimeUnitEnum.MONTH));
        System.out.println(generateCronString(3, JobTimeUnitEnum.YEAR));
    }


}