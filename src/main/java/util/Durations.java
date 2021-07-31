package util;

import common.TimeUnit;

import java.time.Duration;

/**
 * @author raychong
 */
public class Durations {
    public static Duration parse(TimeUnit timeUnit, int amount) {
        switch (timeUnit) {
            case DAY:
                return Duration.ofDays(amount);
            case HOUR:
                return Duration.ofHours(amount);
            case MINUTE:
                return Duration.ofMinutes(amount);
            case SECOND:
                return Duration.ofSeconds(amount);
            default:
                throw new Error("unknown time unit");
        }
    }
}
