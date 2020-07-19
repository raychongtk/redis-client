package util;

import org.slf4j.helpers.MessageFormatter;

/**
 * @author raychong
 */
public class Strings {
    public static String format(String pattern, Object... params) {
        return MessageFormatter.arrayFormat(pattern, params).getMessage();
    }

    public static boolean isBlank(String text) {
        return text == null || text.isBlank();
    }
}
