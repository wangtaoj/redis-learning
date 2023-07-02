package com.wangtao.redis.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * @author wangtao
 * Created at 2022/7/30 18:32
 */
@Slf4j
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void contextLoad() {
        log.debug("=============contextLoad==============");
        System.out.println(AutoConfigurationPackages.get(applicationContext));
    }
}
