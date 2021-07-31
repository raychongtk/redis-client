package web.api;

import common.TimeUnit;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author raychong
 */
public class ExpireKeyRequest {
    @NotNull
    public TimeUnit timeUnit;

    @NotNull
    @Min(1)
    public Integer expiration;
}
