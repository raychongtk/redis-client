package redis.handler;

import redis.service.Jedis;

/**
 * @author raychong
 */
public class RedisStringHandler implements RedisHandler {
    @Override
    public String get(String key) {
        return Jedis.getInstance().get(key);
    }
}
