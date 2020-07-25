package redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import util.Preference;

import java.util.Optional;

/**
 * @author raychong
 */
public class CreateJedis {
    private static final Logger logger = LoggerFactory.getLogger(CreateJedis.class);

    private static Jedis jedis;

    public static Jedis getInstance() {
        Optional<String> host = Preference.get("redis.host");
        Optional<String> port = Preference.get("redis.port");

        if (host.isPresent() && port.isPresent() && jedis == null) {
            create(host.get(), port.get());
        }

        return jedis;
    }

    public static void create(String host, String port) {
        try {
            int redisPort = Integer.parseInt(port);
            jedis = new Jedis(host, redisPort);
            jedis.connect();
            if (!jedis.isConnected()) jedis = null;
        } catch (Exception ex) {
            logger.error("create jedis failed", ex);
        }
    }

    private CreateJedis() {
    }
}
