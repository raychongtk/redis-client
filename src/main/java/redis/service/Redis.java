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
    private static Jedis jedis;

    public static Redis create() {
        return new Redis();
    }

    private Redis() {
        jedis = CreateJedis.getInstance();
    }

    public boolean isConnected() {
        return jedis != null && jedis.isConnected();
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

    public void add(RedisObject redisObject) {
        RedisHandlers.of(redisObject.type).add(redisObject);
    }
}
