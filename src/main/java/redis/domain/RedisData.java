package redis.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author raychong
 */
public class RedisData {
    public Map<String, String> hash = new HashMap<>();
    public Set<String> set = new HashSet<>();
    public String string;
}
