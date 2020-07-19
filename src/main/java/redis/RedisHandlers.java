package redis;

import redis.exception.NotFoundException;
import redis.handler.RedisHandler;
import redis.handler.RedisHashHandler;
import redis.handler.RedisSetHandler;
import redis.handler.RedisStringHandler;
import redis.service.RedisType;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author raychong
 */
public class RedisHandlers {
    private static final Map<RedisType, RedisHandler> PROPERTIES = properties();

    public static RedisHandler of(RedisType dataType) {
        RedisHandler redisHandler = PROPERTIES.get(dataType);
        if (redisHandler == null) throw new NotFoundException("redis handler not found, type=" + dataType);
        return redisHandler;
    }

    private static Map<RedisType, RedisHandler> properties() {
        Map<RedisType, RedisHandler> properties = new EnumMap<>(RedisType.class);
        properties.put(RedisType.STRING, new RedisStringHandler());
        properties.put(RedisType.SET, new RedisSetHandler());
        properties.put(RedisType.HASH, new RedisHashHandler());
        return properties;
    }
}
