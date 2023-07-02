package com.wangtao.redis.cluster.lock.annotation;

import java.lang.annotation.*;

/**
 * @author wangtao
 * Created at 2023/7/2 16:36
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {

    String key();

    /**
     * 超时时间, 单位为秒
     */
    long timeout() default 30L;

    /**
     * 获取锁失败时是否抛异常
     */
    boolean throwExpWhenLockFail() default true;
}
