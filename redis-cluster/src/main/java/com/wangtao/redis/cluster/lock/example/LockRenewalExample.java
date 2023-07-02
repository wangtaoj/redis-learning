package com.wangtao.redis.cluster.lock.example;

import com.wangtao.redis.cluster.lock.annotation.DistributeLock;
import com.wangtao.redis.common.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2023/7/2 22:35
 */
@Slf4j
@Component
public class LockRenewalExample {

    @DistributeLock(key = "lockRenewal")
    public void lockRenewal() {
        log.info("=========lockRenewal==========");
        SleepUtils.sleepQuietly(60, TimeUnit.SECONDS);
        IOUtils.closeQuietly();
    }
}
