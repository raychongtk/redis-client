package ui.controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.builder.RedisObjectBuilder;
import redis.domain.RedisData;
import redis.domain.RedisObject;
import redis.domain.RedisType;
import redis.service.Redis;
import ui.controller.control.RedisHashControl;
import ui.util.AlertUtil;
import util.Strings;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import static redis.domain.RedisType.HASH;
import static redis.domain.RedisType.SET;
import static redis.domain.RedisType.STRING;

/**
 * @author raychong
 */
public class MainController implements Initializable {
    private final Logger logger = LoggerFactory.getLogger(MainController.class);
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
    Button deleteKey;
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
    RedisHashControl redisHashControl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        redisKeys.itemsProperty().bind(keysListProperty);
        refresh();

        redisSetResult.itemsProperty().bind(redisSetResultListProperty);
        redisSetResult.setCellFactory(TextFieldListCell.forListView());

        // on text changed
        searchBox.textProperty().addListener((obs, oldText, newText) -> {
            Set<String> keySet;
            if (newText.isEmpty()) {
                keySet = redis.keys();
            } else {
                keySet = redis.keys(newText);
            }
            keysListProperty.set(FXCollections.observableArrayList(keySet));
            setResultCount(keySet.size());
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
                redisHashControl.setKey(key);
                redisHashControl.getRedisHashResult().setItems(FXCollections.observableArrayList(redis.get(key).hash.entrySet()));
                showResult(false, false, true);
                break;
            default:
                break;
        }
    }

    @FXML
    public void handleRefreshButtonEvent() {
        refresh();
    }

    @FXML
    public void handleFlushAllButtonEvent() {
        if (AlertUtil.confirm("Do you really want to flush all the keys?", ButtonType.YES)) {
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
            data.hash = redisHashControl.getRedisHashResult().getItems().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        RedisObject redisObject = new RedisObjectBuilder().key(key)
                                                          .type(type)
                                                          .data(data)
                                                          .build();
        redis.update(redisObject);
        AlertUtil.info("update successfully!");
    }

    @FXML
    public void deleteKey() {
        String key = redisKeys.getSelectionModel().getSelectedItem();

        if (AlertUtil.confirm(Strings.format("Do you want to delete {}?", key), ButtonType.YES)) {
            redis.delete(key);
            refresh();
        }
    }

    @FXML
    public void createKey() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../../create-redis-object.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 500, 500));
            stage.setTitle("Create Key");
            stage.show();
        } catch (IOException e) {
            logger.error("cannot open new window", e);
        }
    }

    private void showResult(boolean string, boolean set, boolean hash) {
        redisResult.setVisible(string);
        redisSetResult.setVisible(set);
        redisHashControl.getRedisHashResult().setVisible(hash);
    }

    private void refresh() {
        Set<String> keys = redis.keys();
        keysListProperty.set(FXCollections.observableArrayList(keys));
        setResultCount(keys.size());
    }

    private void setResultCount(int size) {
        resultCount.setText("Number of Count: " + size);
    }

    public ListView<String> getRedisKeys() {
        return redisKeys;
    }
}
