package web.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author raychong
 */
public class ConnectRedisRequest {
    @NotNull
    @NotBlank
    public String host;

    @NotNull
    public Integer port;
}
