package redis.service;

import redis.RedisList;
import redis.client.RedisClient;

import javax.inject.Inject;
import java.util.List;

/**
 * @author raychong
 */
public class RedisListImpl implements RedisList {
    @Inject
    RedisClient redisClient;

    @Override
    public String get(String key, long index) {
        return redisClient.getInstance().lindex(key, index);
    }

    @Override
    public long prepend(String key, String... values) {
        return redisClient.getInstance().lpush(key, values);
    }

    @Override
    public long append(String key, String... values) {
        return redisClient.getInstance().rpush(key, values);
    }

    @Override
    public List<String> range(String key, long start, long end) {
        return redisClient.getInstance().lrange(key, start, end);
    }

    @Override
    public long size(String key) {
        return redisClient.getInstance().llen(key);
    }
}
