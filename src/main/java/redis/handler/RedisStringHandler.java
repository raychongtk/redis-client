package redis.handler;

import redis.domain.RedisData;
import redis.domain.RedisObject;
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

    @Override
    public void update(RedisObject redisObject) {
        Jedis.getInstance().set(redisObject.key, redisObject.data.string);
    }

    @Override
    public void add(RedisObject redisObject) {
        Jedis.getInstance().set(redisObject.key, redisObject.data.string);
    }
}
