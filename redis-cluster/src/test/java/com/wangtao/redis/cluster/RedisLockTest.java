package com.wangtao.redis.cluster;

import com.wangtao.redis.cluster.lock.RedisLock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author wangtao
 * Created at 2023/6/29 21:59
 */
@SpringBootTest
public class RedisLockTest {

    @Autowired
    private RedisLock redisLock;

    @Test
    public void testLockAndUnLock() {
        final String key = "1";
        assertTrue(redisLock.tryLock(key, 10, TimeUnit.MINUTES));
        assertFalse(redisLock.tryLock(key, 10, TimeUnit.MINUTES));
        assertTrue(redisLock.unlock(key));
    }

    @Test
    public void testOnlyUnlock() {
        final String key = "1";
        assertThrows(IllegalMonitorStateException.class, () -> redisLock.unlock(key));
    }
}
