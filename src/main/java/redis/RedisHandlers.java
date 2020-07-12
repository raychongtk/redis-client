package redis;

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
        if (RedisHandlerProperties.of(dataType).supported()) return PROPERTIES.get(dataType);
        throw new UnsupportedOperationException();
    }

    private static Map<RedisType, RedisHandler> properties() {
        Map<RedisType, RedisHandler> properties = new EnumMap<>(RedisType.class);
        properties.put(RedisType.STRING, new RedisStringHandler());
        properties.put(RedisType.SET, new RedisSetHandler());
        properties.put(RedisType.HASH, new RedisHashHandler());
        return properties;
    }
}
