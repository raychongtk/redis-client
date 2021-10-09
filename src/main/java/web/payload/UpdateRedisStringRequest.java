package web.payload;

import redis.common.TimeUnit;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author raychong
 */
public class UpdateRedisStringRequest {
    @NotNull
    @NotBlank
    public String value;

    @NotNull
    public TimeUnit timeUnit;

    @NotNull
    @Min(1)
    public Integer expiration;
}
