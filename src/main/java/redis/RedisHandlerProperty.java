package redis;

/**
 * @author raychong
 */
public class RedisHandlerProperty {
    private final boolean supported;

    public RedisHandlerProperty(boolean supported) {
        this.supported = supported;
    }

    public boolean supported() {
        return supported;
    }
}
