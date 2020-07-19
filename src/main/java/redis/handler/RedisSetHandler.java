package redis.handler;

import redis.domain.RedisData;
import redis.domain.RedisObject;
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
    public void update(RedisObject redisObject) {
        Jedis.getInstance().spop(redisObject.key, Jedis.getInstance().scard(redisObject.key));
        Jedis.getInstance().sadd(redisObject.key, redisObject.data.set.toArray(String[]::new));
    }

    @Override
    public void add(RedisObject redisObject) {
        Jedis.getInstance().sadd(redisObject.key, redisObject.data.set.toArray(String[]::new));
    }
}
