package web;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import redis.Redis;
import util.Durations;
import web.payload.GetRedisStringResponse;
import web.payload.IncreaseValueRequest;
import web.payload.UpdateRedisStringRequest;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.time.Duration;

/**
 * @author raychong
 */
@Controller("/ajax/redis/string")
public class RedisStringWebService {
    @Inject
    Redis redis;

    @Get(uri = "/{key}")
    public GetRedisStringResponse get(@NotBlank String key) {
        var response = new GetRedisStringResponse();
        response.value = redis.get(key);
        return response;
    }

    @Post(uri = "/{key}")
    public void set(@NotBlank String key, UpdateRedisStringRequest request) {
        if (request.timeUnit == null) {
            redis.set(key, request.value);
        } else {
            Duration duration = Durations.parse(request.timeUnit, request.expiration);
            redis.set(key, request.value, duration);
        }
    }

    @Put(uri = "/{key}/increaseBy")
    public void increaseBy(@NotBlank String key, IncreaseValueRequest request) {
        redis.increaseBy(key, request.increment);
    }
}
