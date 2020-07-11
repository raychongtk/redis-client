package redis;

/**
 * @author raychong
 */
public class RedisHandlerProperty {
    private final boolean hasHandler;

    public RedisHandlerProperty(boolean hasHandler) {
        this.hasHandler = hasHandler;
    }

    public boolean hasHandler() {
        return hasHandler;
    }
}
