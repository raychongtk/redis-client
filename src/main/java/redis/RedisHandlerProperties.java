package redis;

import redis.service.RedisType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author raychong
 */
public class RedisHandlerProperties {
    private static final Map<RedisType, RedisHandlerProperty> PROPERTIES = properties();

    public static RedisHandlerProperty of(RedisType dataType) {
        Objects.requireNonNull(dataType);
        return PROPERTIES.get(dataType);
    }

    private static Map<RedisType, RedisHandlerProperty> properties() {
        Map<RedisType, RedisHandlerProperty> properties = new EnumMap<>(RedisType.class);
        properties.put(RedisType.STRING, new RedisHandlerProperty(true));
        properties.put(RedisType.SET, new RedisHandlerProperty(true));
        properties.put(RedisType.HASH, new RedisHandlerProperty(true));
        properties.put(RedisType.LIST, new RedisHandlerProperty(false));
        properties.put(RedisType.ZSET, new RedisHandlerProperty(false));
        properties.put(RedisType.STREAM, new RedisHandlerProperty(false));
        return properties;
    }
}
