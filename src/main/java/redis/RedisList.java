package redis;

import java.util.List;

public interface RedisList {
    String get(String key, long index);

    long prepend(String key, String... values);

    long append(String key, String... values);

    List<String> range(String key, long start, long end);

    long size(String key);
}
