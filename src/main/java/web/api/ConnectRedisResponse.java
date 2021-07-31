package web.api;

import javax.validation.constraints.NotNull;

/**
 * @author raychong
 */
public class ConnectRedisResponse {
    @NotNull
    public Boolean success;
}
