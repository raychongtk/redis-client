package redis.exception;

/**
 * @author raychong
 */
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = -2158108981660747023L;

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
