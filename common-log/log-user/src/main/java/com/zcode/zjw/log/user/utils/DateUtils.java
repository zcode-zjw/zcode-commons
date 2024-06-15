package com.zcode.zjw.log.user.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhangjiwei
 * @since 2022-07-05 15:35
 * 时间工具类
 */
public class DateUtils {

    public static Long dayStamp = 86400000L;

    public static Long monthStamp = 2592000000L;

    public static Long yearStamp = 31104000000L;

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 获取当前时间字符串(年月日)
     * @date 2022/7/19 2:45 下午
     */
    public static ThreadLocal<String> getCurrentDateStringByBigUnits(String formatSepStr) {
        return ThreadLocal.withInitial(() -> {
            Date date = new Date();
            String defaultFormatStr = "yyyy-MM-dd";
            return new SimpleDateFormat(defaultFormatStr.replace("-", formatSepStr)).format(date);
        });
    }

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 获取当前时间(年份)
     * @date 2022/7/19 2:45 下午
     */
    public static ThreadLocal<Integer> getCurrentYearByNum() {
        return ThreadLocal.withInitial(() -> {
            Date date = new Date();
            String defaultFormatStr = "yyyy";
            return Integer.parseInt(new SimpleDateFormat(defaultFormatStr).format(date));
        });
    }

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 获取当前时间(小时数)
     * @date 2022/7/19 2:45 下午
     */
    public static ThreadLocal<Integer> getCurrentHourByNum() {
        return ThreadLocal.withInitial(() -> {
            Date date = new Date();
            String defaultFormatStr = "HH";
            return Integer.parseInt(new SimpleDateFormat(defaultFormatStr).format(date));
        });
    }

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 获取当前时间(分钟数)
     * @date 2022/7/19 2:45 下午
     */
    public static ThreadLocal<Integer> getCurrentMinuteByNum() {
        return ThreadLocal.withInitial(() -> {
            Date date = new Date();
            String defaultFormatStr = "mm";
            return Integer.parseInt(new SimpleDateFormat(defaultFormatStr).format(date));
        });
    }

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 获取当前时间(秒数)
     * @date 2022/7/19 2:45 下午
     */
    public static ThreadLocal<Integer> getCurrentSecondByNum() {
        return ThreadLocal.withInitial(() -> {
            Date date = new Date();
            String defaultFormatStr = "ss";
            return Integer.parseInt(new SimpleDateFormat(defaultFormatStr).format(date));
        });
    }

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 获取当前时间(月份)
     * @date 2022/7/19 2:45 下午
     */
    public static ThreadLocal<Integer> getCurrentMonthNumByNum() {
        return ThreadLocal.withInitial(() -> {
            Date date = new Date();
            String defaultFormatStr = "MM";
            return Integer.parseInt(new SimpleDateFormat(defaultFormatStr).format(date));
        });
    }

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 获取当前时间(日)
     * @date 2022/7/19 2:45 下午
     */
    public static ThreadLocal<Integer> getCurrentDayNumByNum() {
        return ThreadLocal.withInitial(() -> {
            Date date = new Date();
            String defaultFormatStr = "dd";
            return Integer.parseInt(new SimpleDateFormat(defaultFormatStr).format(date));
        });
    }

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 获取当前时间字符串(时分秒)
     * @date 2022/7/19 2:45 下午
     */
    public static ThreadLocal<String> getCurrentDateStringBySmallUnits(String formatSepStr) {
        return ThreadLocal.withInitial(() -> {
            Date date = new Date();
            String defaultFormatStr = "HH:mm:ss";
            return new SimpleDateFormat(defaultFormatStr.replace(":", formatSepStr)).format(date);
        });
    }

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 获取当前时间字符串
     * @date 2022/7/19 2:45 下午
     */
    public static ThreadLocal<String> getCurrentDateStringFormat() {
        return ThreadLocal.withInitial(() -> {
            Date date = new Date();
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        });
    }

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 时间戳转为时间字符串
     * @date 2022/7/19 2:45 下午
     */
    public static ThreadLocal<String> timeStampToDateString(long timestamp) {
        return ThreadLocal.withInitial(() -> {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
        });
    }

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 获取当前时间（数值形）
     * @date 2022/7/19 2:45 下午
     */
    public static ThreadLocal<Integer> getCurrentDateNumber() {
        return ThreadLocal.withInitial(() -> {
            Calendar calendar = Calendar.getInstance();
            String year = calendar.get(Calendar.YEAR) + "";
            String month = (calendar.get(Calendar.MONTH) + 1) + "";
            String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
            if (Integer.parseInt(day) < 10) {
                day = "0" + day;
            }
            if (Integer.parseInt(month) < 10) {
                month = "0" + month;
            }
            return Integer.parseInt(year + month + day);
        });

    }

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 将数值型时间格式化为标准时间字符串
     * @date 2022/7/21 4:43 下午
     */
    public static ThreadLocal<String> timeNumberToDateStr(String timeNumber) {
        return ThreadLocal.withInitial(() -> {
            StringBuffer timeNumberStr = new StringBuffer(timeNumber);
            String day = timeNumberStr.substring(timeNumberStr.length() - 2, timeNumberStr.length());
            String month = timeNumberStr.substring(timeNumberStr.length() - 4, timeNumberStr.length() - 2);
            String year = timeNumberStr.substring(0, timeNumberStr.length() - 4);
            return year + "-" + month + "-" + day;
        });
    }

    /**
     * @return ThreadLocal
     * @author zhangjiwei
     * 功能描述: 获取当前年份数值型时间
     * @date 2022/7/19 2:58 下午
     */
    public static ThreadLocal<Integer> getCurrentYearDateNumber() {
        return ThreadLocal.withInitial(() -> {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.YEAR);
        });
    }

    /**
     * @return
     * @author zhangjiwei
     * 功能描述: 时间字符串转为时间戳
     * @date 2022/7/20 9:37 下午
     */

    public static ThreadLocal<Long> timeStrToTimeStamp(String time, String format) {
        return ThreadLocal.withInitial(() -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (!"".equals(format)) {
                simpleDateFormat = new SimpleDateFormat(format);
            }
            long timeStamp = 0;
            try {
                Date date = simpleDateFormat.parse(time);
                timeStamp = date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return timeStamp;
        });
    }


}
