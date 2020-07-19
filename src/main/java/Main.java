import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.Property;

import java.io.IOException;

/**
 * @author raychong
 */
public class Main extends Application {
    public static void main(String[] args) {
        Property.load("app.properties");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setScene(new Scene(root, 700 , 700));
        primaryStage.setTitle("Redis Client");
        primaryStage.show();
    }
}
