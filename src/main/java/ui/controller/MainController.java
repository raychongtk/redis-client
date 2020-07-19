package ui.controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import redis.handler.RedisData;
import redis.service.Redis;
import redis.service.RedisType;

import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import static redis.service.RedisType.HASH;
import static redis.service.RedisType.SET;
import static redis.service.RedisType.STRING;

/**
 * @author raychong
 */
public class MainController implements Initializable {
    private final ListProperty<String> keysListProperty = new SimpleListProperty<>();
    private final ListProperty<String> redisSetResultListProperty = new SimpleListProperty<>();
    private final Redis redis = Redis.getInstance();

    @FXML
    Button refreshKeys;
    @FXML
    Button flushAll;
    @FXML
    Button update;
    @FXML
    TextField searchBox;
    @FXML
    Label resultCount;
    @FXML
    ListView<String> redisKeys;
    @FXML
    TextArea redisResult;
    @FXML
    ListView<String> redisSetResult;
    @FXML
    TableView<Map.Entry<String, String>> redisHashResult;
    @FXML
    TableColumn<Map.Entry<String, String>, String> redisHashField;
    @FXML
    TableColumn<Map.Entry<String, String>, String> redisHashValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Set<String> keys = redis.keys();
        redisKeys.itemsProperty().bind(keysListProperty);
        keysListProperty.set(FXCollections.observableArrayList(keys));

        setResultCount(keys.size());

        redisSetResult.itemsProperty().bind(redisSetResultListProperty);
        redisSetResult.setCellFactory(TextFieldListCell.forListView());

        redisHashField.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        redisHashValue.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        // set editable cell
        redisHashValue.setCellFactory(cell -> new TextFieldTableCell<>(new DefaultStringConverter()));
        redisHashValue.setOnEditCommit(table -> table.getTableView().getItems().get(table.getTablePosition().getRow()).setValue(table.getNewValue()));

        // set column width
        redisHashField.prefWidthProperty().bind(redisHashResult.widthProperty().multiply(0.29));
        redisHashValue.prefWidthProperty().bind(redisHashResult.widthProperty().multiply(0.7));

        // on text changed
        searchBox.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty()) {
                keysListProperty.set(FXCollections.observableArrayList(redis.keys()));
            } else {
                keysListProperty.set(FXCollections.observableArrayList(redis.keys(newText)));
            }
        });
    }

    @FXML
    public void handleListItemEvent() {
        String key = redisKeys.getSelectionModel().getSelectedItem();
        switch (redis.type(key)) {
            case STRING:
                redisResult.setText(redis.get(key).string);
                showResult(true, false, false);
                break;
            case SET:
                redisSetResultListProperty.set(FXCollections.observableArrayList(redis.get(key).set));
                showResult(false, true, false);
                break;
            case HASH:
                redisHashResult.setItems(FXCollections.observableArrayList(redis.get(key).hash.entrySet()));
                showResult(false, false, true);
                break;
            default:
                break;
        }
    }

    @FXML
    public void handleRefreshButtonEvent() {
        keysListProperty.set(FXCollections.observableArrayList(redis.keys()));
    }

    @FXML
    public void handleFlushAllButtonEvent() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to flush all the keys?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            redis.flushAll();
            keysListProperty.set(FXCollections.observableArrayList());
            setResultCount(0);
        }
    }

    @FXML
    public void updateValue() {
        String key = redisKeys.getSelectionModel().getSelectedItem();
        var data = new RedisData();
        RedisType type = redis.type(key);

        if (type == STRING) {
            data.string = redisResult.getText();
        } else if (type == SET) {
            data.set = new HashSet<>(redisSetResultListProperty);
        } else if (type == HASH) {
            data.hash = redisHashResult.getItems().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        redis.update(key, data);
    }

    private void setResultCount(int size) {
        resultCount.setText("Number of Count: " + size);
    }

    private void showResult(boolean string, boolean set, boolean hash) {
        redisResult.setVisible(string);
        redisSetResult.setVisible(set);
        redisHashResult.setVisible(hash);
    }
}
