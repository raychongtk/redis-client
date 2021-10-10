package web.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raychong
 */
@JsonInclude()
public class GetKeysResponse {
    @NotNull
    public List<String> keys = new ArrayList<>();
}
