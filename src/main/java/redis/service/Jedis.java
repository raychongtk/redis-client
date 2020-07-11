package redis.service;

import redis.util.RedisProperty;

/**
 * @author raychong
 */
public class Jedis {
    private static redis.clients.jedis.Jedis jedis;

    public static redis.clients.jedis.Jedis getInstance() {
        if (jedis == null) {
            jedis = new redis.clients.jedis.Jedis(RedisProperty.URI);
        }
        return jedis;
    }

    private Jedis() {
    }
}
