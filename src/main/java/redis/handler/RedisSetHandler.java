package redis.handler;

import redis.service.Jedis;

/**
 * @author raychong
 */
public class RedisSetHandler implements RedisHandler {
    @Override
    public String get(String key) {
        return Jedis.getInstance().smembers(key).toString();
    }
}
