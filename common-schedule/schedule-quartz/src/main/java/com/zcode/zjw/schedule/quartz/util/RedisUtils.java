package com.zcode.zjw.schedule.quartz.util;

import com.zcode.zjw.common.utils.common.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhangjiwei
 * @since 2022年02月26日
 */
@Component("redisUtils")
@Slf4j
public class RedisUtils {

    private static final long DEFAULT_EXPIRE = 30L;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void setOrigin(String key, Object value) {
        if (key != null) {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 插入键值对,永久有效
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        setOrigin(getKey(key), value);
    }

    /**
     * 插入数据，添加有效时间，单位秒
     *
     * @param key
     * @param value
     * @param time
     */
    public void set(String key, Object value, long time) {
        if (key != null) {
            set(key, value);
            expire(key, time);
        }
    }

    /**
     * 为记录添加有效时间
     *
     * @param key
     * @param time 单位秒
     */
    public void expire(String key, long time) {
        if (key != null && time > 0L) {
            redisTemplate.expire(getKey(key), time, TimeUnit.SECONDS);
        }
    }

    /**
     * 查询key的值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(getKey(key));
    }

    /**
     * 根据key查询序列化的对象
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getObject(String key, Class clazz) {
        if (null != key) {
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<T>(clazz));
            return (T) redisTemplate.opsForValue().get(getKey(key));
        }
        return null;
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void del(String key) {
        if (StringUtil.isNotBlank(key)) {
            redisTemplate.delete(getKey(key));
        }
    }

    /**
     * 批量删除
     *
     * @param keys
     */
    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            List<String> delKeys = Stream.of(keys).distinct().filter(key -> CommonUtils.isNotEmpty(key))
                    .map(key -> getKey(key)).collect(Collectors.toList());
            redisTemplate.delete(delKeys);
        }
    }

    /**
     * 获取原来的值，并设置新的值，原子操作
     *
     * @param key
     * @param value
     * @return
     */
    public Object getAndSet(String key, Object value) {
        if (null != key) {
            return redisTemplate.opsForValue().getAndSet(getKey(key), value);
        }
        return null;
    }

    /**
     * key存在不设置value，返回false；key不存在设置value，返回true;有setNX操作实现
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setIfAbsent(String key, Object value) {
        if (null != key) {
            return redisTemplate.opsForValue().setIfAbsent(getKey(key), value);
        }
        return false;
    }

    /**
     * 增量和获取，key对应的value加一，并查询出来，RedisAtomicLong保证操作的原子性 若，key不存在，添加key，value为1
     *
     * @param key
     * @return
     */
    public long generate(String key) {
        RedisAtomicLong counter =
                new RedisAtomicLong(getKey(key), redisTemplate.getConnectionFactory());
        return counter.incrementAndGet();
    }

    /**
     * 增量获取，并指定key到期时间
     *
     * @param key
     * @param expireDate 到期时间
     * @return
     */
    public long generate(String key, Date expireDate) {
        RedisAtomicLong counter =
                new RedisAtomicLong(getKey(key), redisTemplate.getConnectionFactory());
        counter.expireAt(expireDate);
        return counter.incrementAndGet();
    }

    /**
     * 增量获取，并指定增量大小，默认为1
     *
     * @param key
     * @param increment 增量大小
     * @return
     */
    public long generate(String key, int increment) {
        RedisAtomicLong counter =
                new RedisAtomicLong(getKey(key), redisTemplate.getConnectionFactory());
        return counter.addAndGet(increment);
    }

    public long generate(String key, int increment, Date expireDate) {
        RedisAtomicLong counter =
                new RedisAtomicLong(getKey(key), redisTemplate.getConnectionFactory());
        counter.expireAt(expireDate);
        return counter.addAndGet(increment);
    }

    // 封装key
    private String getKey(String key) {
        return key;
    }

    public boolean lock(String key) {
        return lock(key, DEFAULT_EXPIRE);
    }

    private String generateLockKey(String key) {
        return String.format("LOCK:%s", key);
    }

    /**
     * 加锁,并且key加前缀
     *
     * @param key
     * @param time 锁的有效时间
     * @return
     */
    public boolean lock(String key, long time) {
        return lockOrigin(getKey(generateLockKey(key)), time);
    }

    /**
     * 加锁 setNx：当key不存在时，才会插入值，保证了锁的互斥性， expire：设置key的有效期，保证锁最终能够得到释放，不会发生死锁。
     *
     * @param key
     * @param time
     * @return
     */
    public boolean lockOrigin(String key, long time) {
        if (CommonUtils.isNotEmpty(key)) {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            // setNX SET if Not eXists的简写。当key不存在时，进行set操作，返回1，否则不操作，返回0
            if (redisTemplate.getConnectionFactory().getConnection().setNX(serializer.serialize(key),
                    new byte[0])) {
                // 因为这里setNx和expire分开来执行，当因为某些原因setNx执行了而expire没有执行，则可能会发生死锁。
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                log.info("add RedisLock[{}].", key);
                return true;
            }
        }
        return false;
    }

    /**
     * 释放锁,key加前缀
     *
     * @param key
     */
    public void unlock(String key) {
        unlockOrigin(getKey(generateLockKey(key)));
    }

    /**
     * 释放锁
     *
     * @param key
     */
    public void unlockOrigin(String key) {
        if (CommonUtils.isNotEmpty(key)) {
            log.info("release lock[{}]", key);
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            redisTemplate.getConnectionFactory().getConnection()
                    .del(new byte[][]{serializer.serialize(key)});
        }
    }

    /**
     * redis锁一般设置key的有效时间来保证不会发生死锁,但是因为setNX和expire操作是分开执行，所以这之间 也有小概率故障发生死锁
     * 在这基础上设置value为锁的到期时间来更好地保证不会发生死锁
     *
     * @param key  锁的key
     * @param time 锁的有效时间，单位
     * @return
     */
    public boolean lockAtomic(String key, long time) {
        String lockKey = generateLockKey(key);
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        String value = String.valueOf(System.currentTimeMillis() + time * 1000);
        // 判断锁是否被占用
        if (redisTemplate.getConnectionFactory().getConnection()
                .setNX(serializer.serialize(getKey(lockKey)), value.getBytes())) {
            // 设置锁的value为到期时间,有可能不被执行
            redisTemplate.expire(getKey(lockKey), time, TimeUnit.SECONDS);
            log.info("add RedisLock[{}]", getKey(lockKey));
            return true;
        } else {
            // key存在
            long lockExpireTime = Long.parseLong(this.get(lockKey).toString());
            // 锁已超时
            if (lockExpireTime < System.currentTimeMillis()) {
                this.set(lockKey, value, time);
                log.info("update RedisLock[{}]", getKey(lockKey));
                return true;
            }
            return false;
        }
    }

    /**
     * 判断锁是否正被占用
     *
     * @param key
     * @return
     */
    public boolean isValidLocked(String key) {
        String lockKey = this.generateLockKey(key);
        Object object = this.get(lockKey);
        return null != object && Long.parseLong(object.toString()) >= System.currentTimeMillis();
    }

    public static void main(String[] args) {
        Object s = "1132131321";
        System.out.println(Long.parseLong(s.toString()));
    }
}
