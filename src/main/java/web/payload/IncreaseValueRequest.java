package web.payload;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author raychong
 */
public class IncreaseValueRequest {
    @NotNull
    @Min(1)
    public Long increment;
}
