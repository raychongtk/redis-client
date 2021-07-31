package redis.client;

import redis.clients.jedis.Jedis;

import javax.inject.Singleton;

/**
 * @author raychong
 */
@Singleton
public class RedisClient {
    private Jedis jedis;

    public Jedis getInstance() {
        if (jedis == null) {
            throw new Error("please connect to redis first");
        }

        return jedis;
    }

    public Jedis create(String host, int port) {
        jedis = new Jedis(host, port);
        jedis.connect();
        if (!jedis.isConnected()) jedis = null;
        return jedis;
    }
}
