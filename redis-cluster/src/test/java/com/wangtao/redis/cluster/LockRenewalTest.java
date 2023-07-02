package com.wangtao.redis.cluster;

import com.wangtao.redis.cluster.lock.example.LockRenewalExample;
import com.wangtao.redis.common.util.SleepUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2023/7/2 22:34
 */
@SpringBootTest
public class LockRenewalTest {

    @Autowired
    private LockRenewalExample lockRenewalExample;

    /**
     * 测试锁续约逻辑
     */
    @Test
    public void lockRenewal() {
        lockRenewalExample.lockRenewal();
        SleepUtils.sleepQuietly(2, TimeUnit.MINUTES);
    }
}
