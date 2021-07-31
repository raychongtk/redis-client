package redis;

import java.util.Set;

/**
 * @author raychong
 */
public interface RedisSet {
    Set<String> get(String key);

    long add(String key, String... members);

    long remove(String key, String... members);

    Set<String> pop(String key, long count);

    long size(String key);
}
