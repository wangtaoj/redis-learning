package com.wangtao.redis.cluster;

import com.wangtao.redis.cluster.scan.RedisScanCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

/**
 * @author wangtao
 * Created at 2023-09-30
 */
@SpringBootTest
public class RedisScanCommandTest {

    @Autowired
    private RedisScanCommand redisScanCommand;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void set() {
        for (int i = 1; i <= 50; i++) {
            redisTemplate.boundValueOps("user" + i).set("user" + i);
        }
    }

    @Test
    public void testScan() {
        // 使用keys的结果作比较, 验证正确性
        Set<String> keys1 = redisTemplate.keys("user*");
        Set<String> keys2 = redisScanCommand.scan("user*", 10);
        Assertions.assertEquals(keys1, keys2);
    }
}
