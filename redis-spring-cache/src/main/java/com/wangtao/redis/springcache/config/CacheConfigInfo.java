package com.wangtao.redis.springcache.config;

import lombok.Data;

/**
 * @author wangtao
 * Created at 2023/7/29 17:55
 */
@Data
public class CacheConfigInfo {

    private String cacheName;

    /**
     * 过期时间, 单位秒
     */
    private int timeoutSecond;

    /**
     * 前缀
     */
    private String keyPrefix;
}
