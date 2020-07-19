package ui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * @author raychong
 */
public class AlertUtil {
    public static void warn(String message) {
        new Alert(Alert.AlertType.WARNING, message, ButtonType.OK).showAndWait();
    }

    public static void info(String message) {
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
    }

    public static boolean confirm(String message, ButtonType buttonType) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        return alert.getResult() == buttonType;
    }
}
