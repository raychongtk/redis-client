package redis.handler;

import redis.service.Jedis;

/**
 * @author raychong
 */
public class RedisStringHandler implements RedisHandler {
    @Override
    public RedisData get(String key) {
        var data = new RedisData();
        data.string = Jedis.getInstance().get(key);
        return data;
    }
}
