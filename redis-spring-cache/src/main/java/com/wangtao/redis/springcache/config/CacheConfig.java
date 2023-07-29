package com.wangtao.redis.springcache.config;

import com.wangtao.redis.common.util.RedisSerializerUtils;
import com.wangtao.redis.springcache.constant.CacheConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangtao
 * Created at 2023/7/29 16:33
 */
@Configuration
public class CacheConfig {

    /**
     * 创建RedisCacheManager, 覆盖Spring Boot自己默认的配置
     * 主要是为了对于不同的cacheName自定义过期时间以及前缀,
     * 默认的配置所有的cacheName对应同一个过期时间、前缀
     * @see org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
     * org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                     ObjectProvider<CacheCustomizer> cacheTtlCustomizer) {
        RedisCacheManager cacheManager = new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory),
                getDefaultConfiguration(60 * 30, null),
                getRedisCacheConfigurationMap(cacheTtlCustomizer.getIfUnique())
        );
        // 支持事务(通过事务管理器的钩子实现, afterCommit方法)
        cacheManager.setTransactionAware(true);
        return cacheManager;
    }

    /**
     * @param timeout 过期时间, 单位秒
     */
    private RedisCacheConfiguration getDefaultConfiguration(Integer timeout, String keyPrefix) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializerUtils.create()))
                .entryTtl(Duration.ofSeconds(timeout));
        if (StringUtils.isNoneBlank(keyPrefix)) {
            config = config.computePrefixWith((cacheName -> cacheName + CacheConstant.SEPARATOR + keyPrefix));
        } else {
            config = config.computePrefixWith((cacheName -> cacheName + CacheConstant.SEPARATOR));
        }
        return config;
    }

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap(CacheCustomizer cacheCustomizer) {
        if (cacheCustomizer == null) {
            return Collections.emptyMap();
        }
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(16);
        for (CacheConfigInfo cacheConfigInfo : cacheCustomizer.customizeCacheTtl()) {
            redisCacheConfigurationMap.put(cacheConfigInfo.getCacheName(),
                    getDefaultConfiguration(cacheConfigInfo.getTimeoutSecond(), cacheConfigInfo.getKeyPrefix()));
        }
        return redisCacheConfigurationMap;
    }
}
