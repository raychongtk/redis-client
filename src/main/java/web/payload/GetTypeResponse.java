package web.payload;

import redis.common.RedisDateType;

import javax.validation.constraints.NotNull;

/**
 * @author raychong
 */
public class GetTypeResponse {
    @NotNull
    public RedisDateType type;
}
