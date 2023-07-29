package com.wangtao.redis.springcache.config;

import java.util.List;

/**
 * 用于自定义cache的过期时间
 * @author wangtao
 * Created at 2023/7/29 17:54
 */
public interface CacheCustomizer {

    List<CacheConfigInfo> customizeCacheTtl();
}
