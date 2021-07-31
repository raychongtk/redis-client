package web.api;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raychong
 */
public class GetKeysResponse {
    @NotNull
    public List<String> keys = new ArrayList<>();
}
