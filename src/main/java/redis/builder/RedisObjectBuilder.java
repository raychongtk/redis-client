package redis.builder;

import redis.domain.RedisData;
import redis.domain.RedisObject;
import redis.domain.RedisType;

/**
 * @author raychong
 */
public class RedisObjectBuilder {
    private final RedisObject redisObject;

    public RedisObjectBuilder() {
        redisObject = new RedisObject();
    }

    public RedisObjectBuilder key(String key) {
        redisObject.key = key;
        return this;
    }

    public RedisObjectBuilder type(RedisType type) {
        redisObject.type = type;
        return this;
    }

    public RedisObjectBuilder data(RedisData data) {
        redisObject.data = data;
        return this;
    }

    public RedisObject build() {
        return redisObject;
    }
}
