package web.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author raychong
 */
public class GetRedisStringResponse {
    @NotNull
    @NotBlank
    public String value;
}
