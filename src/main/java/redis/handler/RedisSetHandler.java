package redis.handler;

import redis.service.Jedis;

/**
 * @author raychong
 */
public class RedisSetHandler implements RedisHandler {
    @Override
    public RedisData get(String key) {
        var data = new RedisData();
        data.set = Jedis.getInstance().smembers(key);
        return data;
    }
}
