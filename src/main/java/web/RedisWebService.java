package web;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import redis.Redis;
import util.Durations;
import util.Strings;
import web.api.ConnectRedisRequest;
import web.api.ConnectRedisResponse;
import web.api.DeleteRedisKeysRequest;
import web.api.ExpireKeyRequest;
import web.api.GetKeysResponse;
import web.api.GetTypeResponse;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author raychong
 */
@Controller("/ajax/redis")
public class RedisWebService {
    @Inject
    Redis redis;

    @Post(uri = "/connect")
    public ConnectRedisResponse connect(ConnectRedisRequest request) {
        var response = new ConnectRedisResponse();
        response.success = redis.connect(request.host, request.port);
        return response;
    }

    @Post(uri = "/disconnect")
    public void disconnect() {
        redis.disconnect();
    }

    @Get()
    public GetKeysResponse keys(@QueryValue String pattern) {
        Set<String> keys;
        if (Strings.isBlank(pattern)) {
            keys = redis.keys();
        } else {
            keys = redis.keys(pattern);
        }

        var response = new GetKeysResponse();
        response.keys = new ArrayList<>(keys);
        return response;
    }

    @Get("/{key}/type")
    public GetTypeResponse type(@NotBlank String key) {
        var response = new GetTypeResponse();
        response.type = redis.type(key);
        return response;
    }

    @Delete()
    public void delete(DeleteRedisKeysRequest request) {
        if (request.keys.isEmpty()) return;

        redis.delete(request.keys.toArray(String[]::new));
    }

    @Put(uri = "/{key}")
    public void expire(@NotBlank String key, ExpireKeyRequest request) {
        Duration duration = Durations.parse(request.timeUnit, request.expiration);
        redis.expire(key, duration);
    }

    @Post(uri = "/flush-all")
    public void flushAll() {
        redis.flushAll();
    }
}
