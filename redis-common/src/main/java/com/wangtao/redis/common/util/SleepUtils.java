package com.wangtao.redis.common.util;

import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2023/7/2 17:03
 */
public final class SleepUtils {

    private SleepUtils() {

    }

    public static void sleepQuietly(long timeout, TimeUnit unit) {
        try {
            sleep(timeout, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public static void sleep(long timeout, TimeUnit unit) throws InterruptedException {
        unit.sleep(timeout);
    }
}
