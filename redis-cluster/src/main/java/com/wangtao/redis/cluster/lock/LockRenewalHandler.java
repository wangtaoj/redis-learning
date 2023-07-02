package com.wangtao.redis.cluster.lock;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author wangtao
 * Created at 2023/7/2 18:55
 */
@Slf4j
@Component
public class LockRenewalHandler implements InitializingBean {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private long interval = 5;

    private final CopyOnWriteArrayList<LockRenewalInfo> lockRenewalInfos = new CopyOnWriteArrayList<>();

    private final ScheduledExecutorService renewalExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(@NotNull Runnable r) {
            return new Thread(r, "lock-renewal");
        }
    });

    private final ExecutorService addRenewalInfoExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(@NotNull Runnable r) {
            return new Thread(r, "add-renewal-info");
        }
    });

    /**
     * 异步添加续约信息
     */
    public void addLockRenewalInfoAsync(String key, long timeout, TimeUnit unit, long base, Thread thread) {
        addRenewalInfoExecutor.execute(() -> {
            LockRenewalInfo lockRenewalInfo = new LockRenewalInfo(key, timeout, unit, base);
            lockRenewalInfo.setThread(thread);
            addLockRenewalInfo(lockRenewalInfo);
        });
    }

    private void addLockRenewalInfo(LockRenewalInfo lockRenewalInfo) {
        lockRenewalInfos.add(lockRenewalInfo);
    }

    @Override
    public void afterPropertiesSet() {
        // 下一次执行续约逻辑是在本次执行完之后的5s后
        renewalExecutor.scheduleWithFixedDelay(this::lockRenewal, 0, interval, TimeUnit.SECONDS);
    }

    /**
     * 锁续约
     */
    private void lockRenewal() {
        if (lockRenewalInfos.isEmpty()) {
            return;
        }
        List<LockRenewalInfo> removeList = new ArrayList<>();
        for (LockRenewalInfo lockRenewalInfo : lockRenewalInfos) {
            // 锁是否还存在
            Boolean hasKey = redisTemplate.hasKey(lockRenewalInfo.getKey());
            if (hasKey == null || !hasKey) {
                removeList.add(lockRenewalInfo);
                log.info("===========lock '{}' will be remove!", lockRenewalInfo.getKey());
                continue;
            }
            // 判断是否到达最大续约次数
            if (lockRenewalInfo.isReachMaxRenewalCount()) {
                log.info("lock '{}' is reach max renewal count", lockRenewalInfo.getKey());
                // 中断线程
                lockRenewalInfo.getThread().interrupt();
                removeList.add(lockRenewalInfo);
            }
            // 还没有到达续约的时间
            if (!lockRenewalInfo.isReachRenewalTime()) {
                continue;
            }
            // 开始续约
            log.info("========start renewal lock: {}", lockRenewalInfo.getKey());
            redisTemplate.expire(lockRenewalInfo.getKey(), lockRenewalInfo.getTimeout(), lockRenewalInfo.getUnit());
            // 续约次数加1
            lockRenewalInfo.updateRenewalInfo();
        }
        if (!removeList.isEmpty()) {
            lockRenewalInfos.removeAll(removeList);
        }
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
