package redis.handler;

/**
 * @author raychong
 */
public interface RedisHandler {
    RedisData get(String key);

    void update(String key, RedisData data);
}
