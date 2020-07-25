package redis.handler;

import redis.clients.jedis.Jedis;
import redis.domain.RedisData;
import redis.domain.RedisObject;
import redis.service.CreateJedis;

/**
 * @author raychong
 */
public class RedisSetHandler implements RedisHandler {
    @Override
    public RedisData get(String key) {
        var data = new RedisData();
        data.set = CreateJedis.getInstance().smembers(key);
        return data;
    }

    @Override
    public void update(RedisObject redisObject) {
        Jedis jedis = CreateJedis.getInstance();
        jedis.spop(redisObject.key, jedis.scard(redisObject.key));
        jedis.sadd(redisObject.key, redisObject.data.set.toArray(String[]::new));
    }

    @Override
    public void add(RedisObject redisObject) {
        CreateJedis.getInstance().sadd(redisObject.key, redisObject.data.set.toArray(String[]::new));
    }
}
