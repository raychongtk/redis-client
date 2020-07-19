package redis.handler;

import redis.domain.RedisData;
import redis.domain.RedisObject;

/**
 * @author raychong
 */
public interface RedisHandler {
    RedisData get(String key);

    void update(RedisObject redisObject);
}
