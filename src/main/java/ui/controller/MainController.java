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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.builder.RedisObjectBuilder;
import redis.domain.RedisData;
import redis.domain.RedisObject;
import redis.domain.RedisType;
import redis.service.Redis;
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
    private final Redis redis = Redis.create();

    @FXML
    Button refreshKeys;
    @FXML
    Button flushAll;
    @FXML
    Button update;
    @FXML
    Button deleteKey;
    @FXML
    Button connectRedis;
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
        redisKeys.itemsProperty().bind(keysListProperty);
        refresh();

        redisSetResult.itemsProperty().bind(redisSetResultListProperty);
        redisSetResult.setCellFactory(TextFieldListCell.forListView());

        //This is how we populate the cell
        redisHashField.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        redisHashValue.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        // set editable cell
        redisHashValue.setCellFactory(TextFieldTableCell.forTableColumn());
        redisHashValue.setOnEditCommit(table -> table.getTableView().getItems().get(table.getTablePosition().getRow()).setValue(table.getNewValue()));

        // set column width
        redisHashField.prefWidthProperty().bind(redisHashResult.widthProperty().multiply(0.29));
        redisHashValue.prefWidthProperty().bind(redisHashResult.widthProperty().multiply(0.7));

        // on text changed
        searchBox.textProperty().addListener((obs, oldText, newText) -> {
            if (!redis.isConnected()) return;

            Set<String> keys;
            if (Strings.isBlank(newText)) {
                keys = redis.keys();
            } else {
                keys = redis.keys(newText);
            }
            keysListProperty.set(FXCollections.observableArrayList(keys));
            setResultCount(keys.size());
        });

        showResult(false, false, false);

        if (!redis.isConnected()) connectRedis();
    }

    @FXML
    public void handleListItemEvent() {
        if (!redis.isConnected()) return;

        String redisKey = redisKeys.getSelectionModel().getSelectedItem();
        switch (redis.type(redisKey)) {
            case STRING:
                redisResult.setText(redis.get(redisKey).string);
                showResult(true, false, false);
                break;
            case SET:
                redisSetResultListProperty.set(FXCollections.observableArrayList(redis.get(redisKey).set));
                showResult(false, true, false);
                break;
            case HASH:
                redisHashResult.setItems(FXCollections.observableArrayList(redis.get(redisKey).hash.entrySet()));
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
        if (!redis.isConnected()) return;

        if (AlertUtil.confirm("Do you really want to flush all the keys?", ButtonType.YES)) {
            redis.flushAll();
            keysListProperty.set(FXCollections.observableArrayList());
            setResultCount(0);
        }
    }

    @FXML
    public void updateValue() {
        if (!redis.isConnected()) return;

        String redisKey = redisKeys.getSelectionModel().getSelectedItem();
        if (Strings.isBlank(redisKey)) {
            AlertUtil.warn("key cannot be null");
            return;
        }

        RedisType redisType = redis.type(redisKey);
        RedisData redisData = redisData(redisType);
        RedisObject redisObject = new RedisObjectBuilder().key(redisKey)
                                                          .type(redisType)
                                                          .data(redisData)
                                                          .build();
        redis.update(redisObject);
        AlertUtil.info("update successfully!");
    }

    @FXML
    public void deleteKey() {
        if (!redis.isConnected()) return;

        String key = redisKeys.getSelectionModel().getSelectedItem();
        if (Strings.isBlank(key)) {
            AlertUtil.warn("please select a key first!");
            return;
        }

        if (AlertUtil.confirm(Strings.format("Do you want to delete {}?", key), ButtonType.YES)) {
            redis.delete(key);
            refresh();
            showResult(false, false, false);
        }
    }

    @FXML
    public void createKey() {
        if (!redis.isConnected()) return;

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/create-redis-object.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Block events sending to other application
            stage.setScene(new Scene(root, 500, 500));
            stage.setTitle("Create Key");
            stage.setOnCloseRequest(event -> refresh());
            stage.show();
        } catch (IOException e) {
            logger.error("cannot open new window", e);
        }
    }

    @FXML
    public void connectRedis() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/connect-redis.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.setOnCloseRequest(event -> refresh());
            stage.setScene(new Scene(root, 500, 115));
            stage.setTitle("Connect Redis");
            stage.show();
        } catch (IOException e) {
            logger.error("cannot open new window", e);
        }
    }

    private void refresh() {
        if (!redis.isConnected()) return;

        Set<String> keys = redis.keys();
        keysListProperty.set(FXCollections.observableArrayList(keys));
        setResultCount(keys.size());
    }

    private void showResult(boolean string, boolean set, boolean hash) {
        redisResult.setVisible(string);
        redisSetResult.setVisible(set);
        redisHashResult.setVisible(hash);
        update.setVisible(string || set || hash);
    }

    private RedisData redisData(RedisType redisType) {
        var redisData = new RedisData();

        if (redisType == STRING) {
            redisData.string = redisResult.getText();
        } else if (redisType == SET) {
            redisData.set = new HashSet<>(redisSetResultListProperty);
        } else if (redisType == HASH) {
            redisData.hash = redisHashResult.getItems().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        return redisData;
    }

    private void setResultCount(int size) {
        resultCount.setText("Number of Count: " + size);
    }
}
