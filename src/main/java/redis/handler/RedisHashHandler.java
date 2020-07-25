package redis.handler;

import redis.domain.RedisData;
import redis.domain.RedisObject;
import redis.service.CreateJedis;

/**
 * @author raychong
 */
public class RedisHashHandler implements RedisHandler {
    @Override
    public RedisData get(String key) {
        var data = new RedisData();
        data.hash = CreateJedis.getInstance().hgetAll(key);
        return data;
    }

    @Override
    public void update(RedisObject redisObject) {
        CreateJedis.getInstance().hset(redisObject.key, redisObject.data.hash);
    }

    @Override
    public void add(RedisObject redisObject) {
        CreateJedis.getInstance().hset(redisObject.key, redisObject.data.hash);
    }
}
