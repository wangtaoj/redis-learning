package com.wangtao.redis.basic;

import com.wangtao.redis.basic.cache.NumberCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2022/12/6 19:12
 */
@Slf4j
@SpringBootTest
public class PerformanceTest {

    private static final int MAX = 5;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private NumberCache numberCache;

    @Test
    public void perform() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 1; i <= 1000; i++) {
            BoundListOperations<String, String> boundList = redisTemplate.boundListOps("instrSnoSet");
            boundList.leftPush(String.valueOf(i));
        }
        stopWatch.stop();
        log.info("cost {}", stopWatch.getTotalTimeMillis());
    }

    @Test
    public void blockQueue() {
        deleteData();
        new Thread(this::pushData).start();

        CountDownLatch countDownLatch = new CountDownLatch(MAX);
        for (int i = 1; i <= MAX; i++) {
            int finalI = i;
            new Thread(() -> {
                long start = System.currentTimeMillis();
                while (true) {
                    if (System.currentTimeMillis() - start > 60000) {
                        countDownLatch.countDown();
                        break;
                    }
                    Integer value = numberCache.poll(String.valueOf(finalI), 10, TimeUnit.SECONDS);
                    if (value != null) {
                        log.info("{}: {}", Thread.currentThread().getName(), value);
                    }
                }
            }, "thread-" + i).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void deleteData() {
        for (int i = 1; i <= MAX; i++) {
            for (int j = 1; j <= 100; j++) {
                numberCache.del(String.valueOf(i));
            }
        }
    }

    private void pushData() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 1; i <= MAX; i++) {
            for (int j = 1; j <= 100; j++) {
                numberCache.push(String.valueOf(i), j);
            }
        }
        stopWatch.stop();
        log.info("cost {}", stopWatch.getTotalTimeMillis());
    }
}
