package util;

import java.util.Optional;
import java.util.prefs.Preferences;

/**
 * @author raychong
 */
public class Preference {
    private static final Preferences preferences = Preferences.userNodeForPackage(Preference.class);

    public static void put(String key, String value) {
        preferences.put(key, value);
    }

    public static Optional<String> get(String key) {
        return Optional.ofNullable(preferences.get(key, null));
    }

    public static void remove(String key) {
        preferences.remove(key);
    }
}
