package com.wangtao.redis.cluster.scan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DecoratedRedisConnection;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangtao
 * Created at 2023-09-30
 */
@Component
public class RedisScanCommand {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取所有满足条件的key
     * 会循环获取, 直到游标返回结果为0, 即结束
     * 集群环境时访问所有的节点
     *
     * @param pattern 模式
     * @param count 一次循环返回的建议数量
     * @return 所有满足条件的key
     */
    public Set<String> scan(String pattern, int count) {
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(pattern).count(count).build();
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keys = new HashSet<>();
            RedisConnection realConn = connection;
            if (connection instanceof DecoratedRedisConnection) {
                DecoratedRedisConnection decoratedRedisConnection = (DecoratedRedisConnection) connection;
                realConn = decoratedRedisConnection.getDelegate();
            }
            if (realConn instanceof RedisClusterConnection) {
                RedisClusterConnection clusterConnection = (RedisClusterConnection) realConn;
                Iterable<RedisClusterNode> redisClusterNodes = clusterConnection.clusterGetNodes();
                for (RedisClusterNode redisClusterNode : redisClusterNodes) {
                    // master节点
                    if (redisClusterNode.isMaster() && redisClusterNode.isConnected() && !redisClusterNode.isMarkedAsFail()) {
                        Cursor<byte[]> cursor = clusterConnection.scan(redisClusterNode, scanOptions);
                        while (cursor.hasNext()) {
                            keys.add(String.valueOf(redisTemplate.getKeySerializer().deserialize(cursor.next())));
                        }
                    }
                }
            } else {
                Cursor<byte[]> cursor = connection.scan(scanOptions);
                while (cursor.hasNext()) {
                    keys.add(String.valueOf(redisTemplate.getKeySerializer().deserialize(cursor.next())));
                }
            }
            return keys;
        });
    }
}
