package com.wangtao.redisson.basic.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2022/7/25 22:35
 */
@RestController
public class LockController {

    private static final String LOCK_PREFIX = "lock:";

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/lockApi")
    public void lockApi() {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + 1);
        lock.lock(60, TimeUnit.SECONDS);
        try {
            System.out.println("get lock");
            TimeUnit.SECONDS.sleep(10);
            System.out.println("finish");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
