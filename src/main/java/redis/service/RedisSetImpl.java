package redis.service;

import redis.RedisSet;
import redis.client.RedisClient;

import javax.inject.Inject;
import java.util.Set;

/**
 * @author raychong
 */
public class RedisSetImpl implements RedisSet {
    @Inject
    RedisClient redisClient;

    @Override
    public Set<String> get(String key) {
        return redisClient.getInstance().smembers(key);
    }

    @Override
    public long add(String key, String... members) {
        return redisClient.getInstance().sadd(key, members);
    }

    @Override
    public long remove(String key, String... members) {
        return redisClient.getInstance().srem(key, members);
    }

    @Override
    public Set<String> pop(String key, long count) {
        return redisClient.getInstance().spop(key, count);
    }

    @Override
    public long size(String key) {
        return redisClient.getInstance().scard(key);
    }
}
