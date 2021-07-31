package redis;

import common.RedisDateType;
import redis.client.RedisClient;
import redis.clients.jedis.Jedis;
import redis.service.RedisHashImpl;
import redis.service.RedisListImpl;
import redis.service.RedisSetImpl;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Set;

/**
 * @author raychong
 */
@Singleton
public class Redis {
    private final RedisHash redisHash = new RedisHashImpl();
    private final RedisSet redisSet = new RedisSetImpl();
    private final RedisList redisList = new RedisListImpl();
    @Inject
    RedisClient redisClient;
    private Jedis jedis;

    public boolean connect(String host, int port) {
        jedis = redisClient.create(host, port);
        return isConnected();
    }

    public void disconnect() {
        if (jedis == null) return;
        jedis.disconnect();
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

    public RedisDateType type(String key) {
        return RedisDateType.valueOf(jedis.type(key).toUpperCase());
    }

    public void delete(String... key) {
        jedis.del(key);
    }

    public void expire(String key, Duration expiration) {
        jedis.expire(key, (int) expiration.toSeconds());
    }

    public String get(String key) {
        return jedis.get(key);
    }

    public void set(String key, String value) {
        jedis.set(key, value);
    }

    public void set(String key, String value, Duration expiration) {
        set(key, value);
        expire(key, expiration);
    }

    public long increaseBy(String key, long increment) {
        return jedis.incrBy(key, increment);
    }

    public RedisHash hash() {
        return redisHash;
    }

    public RedisSet set() {
        return redisSet;
    }

    public RedisList list() {
        return redisList;
    }

    public void flushAll() {
        jedis.flushAll();
    }
}
