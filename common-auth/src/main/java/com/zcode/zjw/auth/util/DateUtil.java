package com.zcode.zjw.auth.util;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author zhangjiwei
 * @since 2022-03-15 17:50
 */
public class DateUtil {

    public static String getNowTimeStr() {
        return ThreadLocal.withInitial(() -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(new Date());
        }).get();
    }

    public static Date getNowTime() {
        return ThreadLocal.withInitial(() -> {
            // 先把LocalDateTime变为ZonedDateTime，然后调用toInstant()
            LocalDateTime now = LocalDateTime.now();
            ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
            // Instant是代表本初子午线的时间，所以比我们的东八区要晚8小时
            Instant instant = zonedDateTime.toInstant();
            return Date.from(instant);
        }).get();
    }


    public static void main(String[] args) {
        System.out.println(DateUtil.getNowTime());
        System.out.println(DateUtil.getNowTimeStr());
    }

}
