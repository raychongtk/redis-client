package web.payload;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raychong
 */
public class DeleteRedisKeysRequest {
    @NotNull
    public List<String> keys = new ArrayList<>();
}
