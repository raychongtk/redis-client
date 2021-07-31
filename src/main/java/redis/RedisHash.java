package redis;

import java.util.Map;

public interface RedisHash {
    String get(String key, String field);

    Map<String, String> getAll(String key);

    long set(String key, String field, String value);

    long set(String key, Map<String, String> hash);

    long increaseBy(String key, String field, long increment);

    long del(String key, String... fields);
}
