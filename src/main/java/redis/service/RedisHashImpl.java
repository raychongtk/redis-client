package redis.service;

import redis.RedisHash;
import redis.client.RedisClient;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author raychong
 */
public class RedisHashImpl implements RedisHash {
    @Inject
    RedisClient redisClient;

    @Override
    public String get(String key, String field) {
        return redisClient.getInstance().hget(key, field);
    }

    @Override
    public Map<String, String> getAll(String key) {
        return redisClient.getInstance().hgetAll(key);
    }

    @Override
    public long set(String key, String field, String value) {
        return redisClient.getInstance().hset(key, field, value);
    }

    @Override
    public long set(String key, Map<String, String> hash) {
        return redisClient.getInstance().hset(key, hash);
    }

    @Override
    public long increaseBy(String key, String field, long increment) {
        return redisClient.getInstance().hincrBy(key, field, increment);
    }

    @Override
    public long del(String key, String... fields) {
        return redisClient.getInstance().hdel(key, fields);
    }
}
