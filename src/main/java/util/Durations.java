package util;

import redis.common.TimeUnit;

import java.time.Duration;

/**
 * @author raychong
 */
public class Durations {
    public static Duration parse(TimeUnit timeUnit, int amount) {
        return switch (timeUnit) {
            case DAY -> Duration.ofDays(amount);
            case HOUR -> Duration.ofHours(amount);
            case MINUTE -> Duration.ofMinutes(amount);
            case SECOND -> Duration.ofSeconds(amount);
        };
    }
}
