package com.wangtao.redis.basic;

import com.wangtao.redis.common.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author wangtao
 * Created at 2022/7/17 14:41
 */
@Import({RedisConfig.class})
@SpringBootApplication
public class RedisBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisBasicApplication.class, args);
    }
}
