package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author raychong
 */
public class Property {
    private static final Logger logger = LoggerFactory.getLogger(Property.class);
    private static final Properties PROPERTIES = new Properties();

    public static void load(String classPath) {
        try {
            PROPERTIES.load(Property.class.getClassLoader().getResourceAsStream(classPath));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static String property(String key) {
        return PROPERTIES.getProperty(key);
    }
}
