package redis.handler;

import redis.service.Jedis;

/**
 * @author raychong
 */
public class RedisHashHandler implements RedisHandler {
    @Override
    public String get(String key) {
        return Jedis.getInstance().hgetAll(key).toString();
    }
}
