package com.wangtao.redis.cluster;

import com.wangtao.redis.cluster.lock.example.DistributeLockExample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

/**
 * @author wangtao
 * Created at 2023/7/2 16:57
 */
@SpringBootTest
public class DistributeLockAnnotationTest {

    @Autowired
    private DistributeLockExample distributeLockExample;

    /**
     * 一个会抛异常, 一个会执行业务逻辑
     */
    @Test
    public void lockAndUnlock() {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            try {
                distributeLockExample.executeBizLogic();
            } finally {
                countDownLatch.countDown();
            }
        }).start();

        new Thread(() -> {
            try {
                distributeLockExample.executeBizLogic();
            } finally {
                countDownLatch.countDown();
            }
        }).start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
