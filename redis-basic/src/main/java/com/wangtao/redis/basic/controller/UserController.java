package com.wangtao.redis.basic.controller;

import com.wangtao.redis.basic.cache.UserCache;
import com.wangtao.redis.basic.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author wangtao
 * Created at 2022/7/30 18:33
 */
@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    private UserCache userCache;

    @GetMapping("/set")
    public void set() {
        User user = new User();
        user.setUserCode(BigDecimal.valueOf(9818537));
        user.setUsername("admin");
        user.setAge(30);
        user.setBirthday(LocalDate.of(1997, 5, 3));
        userCache.set(user);
    }

    @GetMapping("/get/{userCode}")
    public User get(@PathVariable BigDecimal userCode) {
        return userCache.get(userCode);
    }
}
