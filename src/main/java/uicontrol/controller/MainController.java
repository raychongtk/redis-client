package uicontrol.controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import redis.service.Redis;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author raychong
 */
public class MainController implements Initializable {
    private final ListProperty<String> listProperty = new SimpleListProperty<>();
    private final Redis redis = Redis.getInstance();
    @FXML
    Button refreshKeys;
    @FXML
    Button flushAll;
    @FXML
    ListView<String> redisKeys;
    @FXML
    TextArea redisResult;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        redisKeys.itemsProperty().bind(listProperty);
        listProperty.set(FXCollections.observableArrayList(redis.keys()));
    }

    @FXML
    public void handleListItemEvent() {
        redisResult.setText(redis.get(redisKeys.getSelectionModel().getSelectedItem()));
    }

    @FXML
    public void handleRefreshButtonEvent() {
        listProperty.set(FXCollections.observableArrayList(redis.keys()));
    }

    @FXML
    public void handleFlushAllButtonEvent() {
        redis.flushAll();
        listProperty.set(FXCollections.observableArrayList(redis.keys()));
    }
}
