package com.zcode.zjw.timejob.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangjiwei
 * @since 2022-08-01 17:05
 * cron表达式转换工具
 */
public class CronConvertUtil {

    public static ThreadLocal<String> convert(String convertStr) {
        return ThreadLocal.withInitial(() -> {
            int cycleSecondNum = Integer.parseInt(convertStr);
            String[] flagStr = new String[]{"0","0","0","0","0"};
            String cronString = "";
            int minute = 60;
            int hour = minute * 60;
            int day = hour * 24;
            int month = day * 30;
            int year = month * 12;
            List<String> timeList = new ArrayList<>();
            // 获取年份
            if (cycleSecondNum - year >= 0) {
                int yearNum = cycleSecondNum / year;
                timeList.add(yearNum + "");
                cycleSecondNum -= (yearNum * year);
                flagStr[0] = "1";
            }
            // 获取月数
            if ("1".equals(flagStr[0]) || cycleSecondNum - month >= 0) {
                int monthNum = cycleSecondNum / month;
                timeList.add(monthNum + "");
                cycleSecondNum -= (monthNum * month);
                flagStr[1] = "1";
            }
            // 获取天数
            if ("1".equals(flagStr[1]) || cycleSecondNum - day >= 0) {
                int dayNum = cycleSecondNum / day;
                timeList.add(dayNum + "");
                cycleSecondNum -= (dayNum * day);
                flagStr[2] = "1";
            }
            // 获取小时数
            if ("1".equals(flagStr[2]) || cycleSecondNum - hour >= 0) {
                int hourNum = cycleSecondNum / hour;
                timeList.add(hourNum + "");
                cycleSecondNum -= (hourNum * hour);
                flagStr[3] = "1";
            }
            // 获取分钟数
            if ("1".equals(flagStr[3]) || cycleSecondNum - minute >= 0) {
                int minuteNum = cycleSecondNum / minute;
                timeList.add(minuteNum + "");
                cycleSecondNum -= (minuteNum * minute);
            }
            // 获取秒数
            timeList.add(cycleSecondNum + "");
            // 拼接cron字符串
            Collections.reverse(timeList);
            for (String timeString : timeList) {
                if ("0".equals(timeString)) {
                    cronString += " 0";
                    continue;
                }
                cronString += (" 0/" + timeString);
            }
            int length = 5 - timeList.size();
            for (int i = 0; i < length; i++) {
                cronString += " *";
            }
            // 如果指定年，特殊处理
            String result = "";
            if (length < 0) {
                result = cronString;
            } else {
                result = (cronString + " ?").trim();
            }
            String newRes = "";
            String[] resArrays = result.trim().split(" ");
            for (int i = 0; i < resArrays.length; i++) {
                if (resArrays[i].equals("0")) {
                    newRes += getTimeByI(i) + " ";
                } else {
                    newRes += resArrays[i] + " ";
                }
            }
            return result.trim();
        });
    }

    private static String getTimeByI(int i) {
        if (i == 0) {
            return DateUtils.getCurrentSecondByNum().get() + "";
        } else if (i == 1) {
            return DateUtils.getCurrentMinuteByNum().get() + "";
        } else if (i == 2) {
            return DateUtils.getCurrentHourByNum().get() + "";
        } else if (i == 3) {
            return DateUtils.getCurrentDayNumByNum().get() + "";
        } else if (i == 4) {
            return DateUtils.getCurrentMonthNumByNum().get() + "";
        } else if (i == 5) {
            return DateUtils.getCurrentYearByNum().get() + "";
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(convert(String.valueOf(3600 * 24 * 3)).get());
        System.out.println(convert(String.valueOf(3600 * 24 * 30 * 3)).get());
        System.out.println(convert(String.valueOf(3600 * 24 * 30 * 12 * 3)).get());
    }


}
