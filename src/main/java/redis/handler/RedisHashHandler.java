package redis.handler;

import redis.service.Jedis;

/**
 * @author raychong
 */
public class RedisHashHandler implements RedisHandler {
    @Override
    public RedisData get(String key) {
        var data = new RedisData();
        data.hash = Jedis.getInstance().hgetAll(key);
        return data;
    }

    @Override
    public void update(String key, RedisData data) {
        Jedis.getInstance().hset(key, data.hash);
    }
}
