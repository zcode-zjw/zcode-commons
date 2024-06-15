package com.zcode.zjw.cache.local.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.zcode.zjw.cache.local.common.CacheEnum;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Caffeine配置说明：
     * initialCapacity=[integer]: 初始的缓存空间大小
     * maximumSize=[long]: 缓存的最大条数
     * maximumWeight=[long]: 缓存的最大权重
     * expireAfterAccess=[duration]: 最后一次写入或访问后经过固定时间过期
     * expireAfterWrite=[duration]: 最后一次写入后经过固定时间过期
     * refreshAfterWrite=[duration]: 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存
     * weakKeys: 打开key的弱引用
     * weakValues：打开value的弱引用
     * softValues：打开value的软引用
     * recordStats：开发统计功能
     * 注意：
     * expireAfterWrite和expireAfterAccess同事存在时，以expireAfterWrite为准。
     * maximumSize和maximumWeight不可以同时使用
     * weakValues和softValues不可以同时使用
     */
    @Bean("caffeineCacheManager")
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caffeineCaches = new ArrayList<>();
        for (CacheEnum cacheEnum : CacheEnum.values()) {
            caffeineCaches.add(new CaffeineCache(cacheEnum.getName(),
                    Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(cacheEnum.getSecond()))
                            .initialCapacity(cacheEnum.getInitSize())
                            .maximumSize(cacheEnum.getMaxSize()).build()));
        }
        cacheManager.setCaches(caffeineCaches);
        return cacheManager;
    }
}