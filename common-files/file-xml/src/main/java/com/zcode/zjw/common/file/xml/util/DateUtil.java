package com.zcode.zjw.common.file.xml.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangjiwei
 * @since 2022年03月20日
 */
public class DateUtil {
    public static final Logger log = LoggerFactory.getLogger(DateUtil.class);

    public static final String DATE_FMT_YMD = "yyyyMMdd";
    public static final String DATE_FMT_Y_M_D = "yyyy-MM-dd";
    public static final String DATE_FMT_YMDHMSSSSS = "yyyyMMddHHmmssSSS";
    public static final String DATE_FMT_Y_M_D_HMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FMT_YMDHMS = "yyyyMMddHHmmss";
    public static final String DATE_FMT_YMDH = "yyyyMMddHH";
    public static final String BATCH_NUMBER_FMT_YMDHM = "yyMMddHHmm";
    public static final String DATE_FMT_HMS = "HHmmss";
    public static final String DATE_FMT_Y_M_D_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SHORT_FMT_YMDHM = "yyMMddHHmm";
    public static final String DATE_SHORT_FMT_YMD = "yyMMdd";
    public static final String DATE_SHORT_FMT_YM = "yyMM";
    public static final String DATE_FMT_YM = "yyyyMM";
    public static final String DATE_FMT_YYYY = "yyyy";
    public static final String DATE_FMT_Y_M_D_HMSSS = "yyyy-MM-dd HH:mm:ss.SSS";
    /* 一天86400000ms */
    private static long MILLIS_PER_DAY = 86400000L;
    /* 一年365.25 天 */
    private static final double DAYS_PER_YEAR = 365.25D;

    public static String formatDate(Date date) {
        return new SimpleDateFormat(DATE_FMT_Y_M_D_HMSS).format(date);
    }

    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat(DATE_FMT_Y_M_D_HMSS).parse(dateStr);
        } catch (ParseException e) {
            log.error("解析时间字符串出错---{}", dateStr);
            return null;
        }
    }

    public static Date parseDate(String dateStr, String format) {
        try {
            return new SimpleDateFormat(format).parse(dateStr);
        } catch (ParseException e) {
            log.error("解析时间字符串出错---{}", dateStr);
            return null;
        }
    }

    /**
     * 表示两个时间相差的天数
     *
     * @param startDate 小的时间
     * @param endDate   大的时间
     * @return 0：表示startDate比endDate大,不足一天算一天
     */
    public static long daysBetween(Date startDate, Date endDate) {
        Calendar start = new GregorianCalendar();
        start.setTime(startDate);
        Calendar end = new GregorianCalendar();
        end.setTime(endDate);
        return dysBetween(start, end);
    }

    public static long dysBetween(Calendar startDate, Calendar endDate) {
        Calendar nowDate = (Calendar) startDate.clone();
        long day;
        for (day = 0; nowDate.before(endDate); ++day) {

            nowDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        return day;
    }

    /**
     * 表示两个时间相差的月数
     *
     * @param startDate 小的时间
     * @param endDate   大的时间
     * @return 0：表示startDate比endDate大,不足一月算一月
     */
    public static long monthsBetween(Date startDate, Date endDate) {
        Calendar start = new GregorianCalendar();
        start.setTime(startDate);
        Calendar end = new GregorianCalendar();
        end.setTime(endDate);
        return monthsBetween(start, end);
    }

    public static long monthsBetween(Calendar start, Calendar end) {
        return between(start, end, Calendar.MONTH);
    }

    /**
     * 表示两个时间相差的年数
     *
     * @param startDate 小的时间
     * @param endDate   大的时间
     * @return 0：表示startDate比endDate大,不足一年算一年
     */
    public static long yearsBetween(Date startDate, Date endDate) {
        Calendar start = new GregorianCalendar();
        start.setTime(startDate);
        Calendar end = new GregorianCalendar();
        end.setTime(endDate);
        return yearsBetween(start, end);
    }

    public static long yearsBetween(Calendar start, Calendar end) {
        return between(start, end, Calendar.YEAR);
    }

    public static long weeksBetween(Calendar start, Calendar end) {
        return between(start, end, Calendar.WEEK_OF_MONTH);
    }

    /**
     * 表示两个时间相差的周数
     *
     * @param startDate 小的时间
     * @param endDate   大的时间
     * @return 0：表示startDate比endDate大,不足一周算一周
     */
    public static long weeksBetween(Date startDate, Date endDate) {
        Calendar start = new GregorianCalendar();
        start.setTime(startDate);
        Calendar end = new GregorianCalendar();
        end.setTime(endDate);
        return weeksBetween(start, end);
    }

    /**
     * 重新设置date的时、分、秒, 毫秒默认为0,分或秒超过60会向前一位进一，时超过24为0
     *
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date setTime(Date date, int hour, int minute, int second) {
        if (date == null) {
            return null;
        } else {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }
    }

    /**
     * 重新设置date的年、月、日，月和日超出大小的化会向前进一
     *
     * @param date
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date setDate(Date date, int year, int month, int day) {
        if (date == null) {
            return null;
        } else {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, --month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            return calendar.getTime();
        }
    }

    /**
     * 判断target时间是否在start和end之间
     *
     * @param start
     * @param end
     * @param target
     * @return
     */
    public static boolean isBetween(Date start, Date end, Date target) {
        if (start != null && end != null && target != null) {
            return start.getTime() <= target.getTime() && target.getTime() <= end.getTime();
        } else {
            throw new NullPointerException("Parameter date shouldn't be null");
        }
    }

    public static Calendar getCalendar(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    public static int getYear(Date date) {
        return getCalendar(date).get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        return getCalendar(date).get(Calendar.MONTH);
    }

    /**
     * 查询第几周
     *
     * @param date
     * @return 0-6表示周日至周六
     */
    public static int getWeek(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getDay(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour(Date date) {
        return getCalendar(date).get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        return getCalendar(date).get(Calendar.MINUTE);
    }

    public static int getSecond(Date date) {
        return getCalendar(date).get(Calendar.SECOND);
    }

    public static int getMillsecond(Date date) {
        return getCalendar(date).get(Calendar.MILLISECOND);
    }

    /**
     * 同年同月同日
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSamDay(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return isSamMonth(date1, date2) && getDay(date1) == getDay(date2);
        } else {
            throw new NullPointerException("Parameter date shouldn't be null");
        }
    }

    /**
     * 同年同月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSamMonth(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return isSamYear(date1, date2) && getMonth(date1) == getMonth(date2);
        } else {
            throw new NullPointerException("Parameter date shouldn't be null");
        }
    }

    /**
     * 同年
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSamYear(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return getYear(date1) == getYear(date2);
        } else {
            throw new NullPointerException("Parameter date shouldn't be null");
        }
    }

    /**
     * 判断日期是否是周末
     *
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date) {
        if (date != null) {
            return getWeek(date) == 0 || getWeek(date) == 6;
        } else {
            throw new NullPointerException("Parameter date shouldn't be null");
        }
    }

    /**
     * 根据生日计算年龄
     *
     * @param birthDate
     * @return
     */
    public static int getAgeByBirthDate(Date birthDate) {
        if (birthDate == null) {
            return 0;
        } else {
            Date now = new Date();
            int age = getYear(now) - getYear(birthDate);
            int currentMonth = getMonth(now);
            int birthMonth = getMonth(birthDate);
            int currentDay = getDay(now);
            int birthDay = getDay(birthDate);
            // 后面俩个判断在一起
            if (currentMonth < birthMonth || currentMonth == birthMonth && currentDay < birthDay) {
                --age;
            }
            return age;
        }
    }

    /**
     * 表示两个时间相差的单位（年、月、日或周）数
     *
     * @param start 小的时间
     * @param end   大的时间
     * @param field 表示年、月、日或周
     * @return 0：表示startDate比endDate大,不足一天算一天
     */
    private static long between(Calendar start, Calendar end, int field) {
        Calendar now = (Calendar) start.clone();
        long num;
        for (num = 0; now.before(end); ++num) {
            // 时间添加一天，amount可以为负数，表示减去相应的天数, field表示年、月或日
            now.add(field, 1);
        }
        return num;
    }

}
