package com.wangtao.redis.cluster.lock;

/**
 * @author wangtao
 * Created at 2023/7/2 16:49
 */
public class LockFailException extends RuntimeException {

    public LockFailException() {
    }

    public LockFailException(String message) {
        super(message);
    }

    public LockFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
