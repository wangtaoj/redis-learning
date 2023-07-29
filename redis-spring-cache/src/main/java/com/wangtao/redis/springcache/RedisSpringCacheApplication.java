package com.wangtao.redis.springcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author wangtao
 * Created at 2022/7/17 14:41
 */
@EnableCaching
@SpringBootApplication
public class RedisSpringCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisSpringCacheApplication.class, args);
    }
}
