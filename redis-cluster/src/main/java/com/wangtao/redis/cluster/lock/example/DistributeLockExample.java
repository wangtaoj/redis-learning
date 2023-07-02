package com.wangtao.redis.cluster.lock.example;

import com.wangtao.redis.cluster.lock.annotation.DistributeLock;
import com.wangtao.redis.common.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2023/7/2 16:55
 */
@Slf4j
@Component
public class DistributeLockExample {

    @DistributeLock(key = "executeBizLogic")
    public void executeBizLogic() {
        log.info("=========executeBizLogic==========");
        SleepUtils.sleepQuietly(2, TimeUnit.SECONDS);
        IOUtils.closeQuietly();
    }
}
