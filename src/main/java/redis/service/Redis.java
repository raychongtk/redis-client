package redis.service;

import redis.RedisHandlers;
import redis.clients.jedis.Jedis;

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

    public void flushAll() {
        jedis.flushAll();
    }

    public String get(String key) {
        return RedisHandlers.of(RedisType.valueOf(jedis.type(key).toUpperCase())).get(key);
    }
}
