package com.wangtao.redis.basic.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2022/12/6 19:36
 */
@Component
public class NumberCache {

    private final static String PREFIX = "number_queue_";

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    public void push(String key, Integer value) {
        String realKey = PREFIX + key;
        BoundListOperations<String, Integer> boundList = redisTemplate.boundListOps(realKey);
        boundList.leftPush(value);
    }

    public Integer poll(String key, long timeout, TimeUnit unit) {
        String realKey = PREFIX + key;
        BoundListOperations<String, Integer> boundList = redisTemplate.boundListOps(realKey);
        return boundList.rightPop(timeout, unit);
    }

    public void del(String key) {
        String realKey = PREFIX + key;
        redisTemplate.delete(realKey);
    }
}
