package com.zcode.zjw.common.utils.id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * 利用redis生成数据库全局唯一性id
 *
 * @author zhangjiwei
 * @since 2020.10.22 10:42
 */
@Service
public class PrimaryKeyUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取年的后两位加上一年多少天+当前小时数作为前缀
     *
     * @param date
     * @return
     */
    public String getIdPrefix(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //String.format("%1$02d", var)
        // %后的1指第一个参数，当前只有var一个可变参数，所以就是指var。
        // $后的0表示，位数不够用0补齐，如果没有这个0（如%1$nd）就以空格补齐，0后面的n表示总长度，总长度可以可以是大于9例如（%1$010d），d表示将var按十进制转字符串，长度不够的话用0或空格补齐。
        String monthFormat = String.format("%1$02d", month + 1);
        String dayFormat = String.format("%1$02d", day);
        String hourFormat = String.format("%1$02d", hour);

        return year + monthFormat + dayFormat + hourFormat;
    }

    /**
     * 生成业务唯一ID
     *
     * @param prefix key的后缀
     * @param flowCodeName 业务名称（英文）
     * @return
     */
    public Long flowId(String flowCodeName, String prefix) {
        String key = flowCodeName + "_ID_" + prefix;
        String currentFlowId = null;
        try {
            Long increment = redisTemplate.opsForValue().increment(key, 1);
            //往前补6位
            currentFlowId = prefix + String.format("%1$06d", increment);
        } catch (Exception e) {
            System.out.println("redis生成唯一ID（" + flowCodeName + "）失败");
            e.printStackTrace();
        }
        return Long.valueOf(currentFlowId);
    }

}
