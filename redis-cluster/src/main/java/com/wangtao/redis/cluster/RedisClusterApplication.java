package com.wangtao.redis.cluster;

import com.wangtao.redis.common.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author wangtao
 * Created at 2023/6/29 20:39
 */
@Import({RedisConfig.class})
@SpringBootApplication
public class RedisClusterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisClusterApplication.class, args);
    }
}
