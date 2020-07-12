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

    @Override
    public void update(String key, RedisData data) {
        Jedis.getInstance().spop(key, Jedis.getInstance().scard(key));
        Jedis.getInstance().sadd(key, data.set.toArray(String[]::new));
    }
}
