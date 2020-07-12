package redis.service;

import util.Property;

/**
 * @author raychong
 */
public class Jedis {
    private static redis.clients.jedis.Jedis jedis;

    public static redis.clients.jedis.Jedis getInstance() {
        if (jedis == null) {
            jedis = new redis.clients.jedis.Jedis(Property.property("redis.uri"));
        }
        return jedis;
    }

    private Jedis() {
    }
}
