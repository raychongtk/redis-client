package redis.service;

import redis.RedisHandlers;
import redis.clients.jedis.Jedis;
import redis.domain.RedisData;
import redis.domain.RedisObject;
import redis.domain.RedisType;

import java.util.Set;

/**
 * @author raychong
 */
public class Redis {
    private static Redis instance;

    public static Redis getInstance() {
        if (instance == null) {
            instance = new Redis();
        }
        return instance;
    }

    private final Jedis jedis;

    private Redis() {
        jedis = redis.service.Jedis.getInstance();
    }

    public Set<String> keys(String pattern) {
        return jedis.keys(pattern);
    }

    public Set<String> keys() {
        return keys("*");
    }

    public RedisType type(String key) {
        return RedisType.valueOf(jedis.type(key).toUpperCase());
    }

    public void delete(String key) {
        jedis.del(key);
    }

    public void flushAll() {
        jedis.flushAll();
    }

    public RedisData get(String key) {
        return RedisHandlers.of(type(key)).get(key);
    }

    public void update(RedisObject redisObject) {
        RedisHandlers.of(redisObject.type).update(redisObject);
    }
}
