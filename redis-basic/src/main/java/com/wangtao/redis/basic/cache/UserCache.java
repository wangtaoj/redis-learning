package com.wangtao.redis.basic.cache;

import com.wangtao.redis.basic.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author wangtao
 * Created at 2022/7/30 18:27
 */
@Component
public class UserCache {

    private static final String KEY_PRIFIX = "user:";

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    public void set(User user) {
        redisTemplate.opsForValue().set(KEY_PRIFIX + user.getUserCode().toPlainString(), user);
    }

    public User get(BigDecimal userCode) {
        return redisTemplate.opsForValue().get(KEY_PRIFIX + userCode.toPlainString());
    }
}
