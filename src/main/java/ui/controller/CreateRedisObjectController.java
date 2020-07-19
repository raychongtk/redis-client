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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.converter.DefaultStringConverter;
import redis.builder.RedisObjectBuilder;
import redis.domain.RedisData;
import redis.domain.RedisObject;
import redis.domain.RedisType;
import redis.service.Redis;
import util.Strings;

import java.net.URL;
import java.util.HashMap;
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
public class CreateRedisObjectController implements Initializable {
    private final ListProperty<String> redisSetResultListProperty = new SimpleListProperty<>();
    private final Map<String, String> hash = new HashMap<>();
    private final Set<String> set = new HashSet<>();
    private final Redis redis = Redis.getInstance();

    @FXML
    TextField redisKey;
    @FXML
    TextField redisSetInputValue;
    @FXML
    TextField redisHashInputKey;
    @FXML
    TextField redisHashInputValue;
    @FXML
    ComboBox<String> redisType;
    @FXML
    Button createKey;
    @FXML
    Button addToSet;
    @FXML
    Button addToHash;
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
    @FXML
    GridPane setGridPane;
    @FXML
    GridPane hashGridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        redisSetResult.itemsProperty().bind(redisSetResultListProperty);
        redisSetResult.setCellFactory(TextFieldListCell.forListView());
        redisSetResultListProperty.set(FXCollections.observableArrayList());

        redisHashField.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        redisHashValue.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        // set editable cell
        redisHashValue.setCellFactory(cell -> new TextFieldTableCell<>(new DefaultStringConverter()));
        redisHashValue.setOnEditCommit(table -> table.getTableView().getItems().get(table.getTablePosition().getRow()).setValue(table.getNewValue()));

        // set column width
        redisHashField.prefWidthProperty().bind(redisHashResult.widthProperty().multiply(0.29));
        redisHashValue.prefWidthProperty().bind(redisHashResult.widthProperty().multiply(0.7));

        redisType.getItems().addAll(STRING.name(), SET.name(), HASH.name());
        redisType.setValue(STRING.name());

        redisType.valueProperty().addListener((obs, oldItem, newItem) -> {
            RedisType redisType = RedisType.valueOf(newItem);
            handleComboBoxItemChanged(redisType);
            showInputArea(redisType);
        });
    }

    @FXML
    public void create() {
        String key = redisKey.getText();
        RedisType type = RedisType.valueOf(redisType.getValue());

        var data = new RedisData();
        if (type == STRING) {
            data.string = redisResult.getText();
        } else if (type == SET) {
            data.set = new HashSet<>(redisSetResultListProperty);
        } else if (type == HASH) {
            data.hash = redisHashResult.getItems().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        RedisObject redisObject = new RedisObjectBuilder().key(key)
                                                          .type(type)
                                                          .data(data)
                                                          .build();
        redis.add(redisObject);
        new Alert(Alert.AlertType.INFORMATION, "added to redis successfully!", ButtonType.OK).showAndWait();
    }

    @FXML
    public void handleSetInputValue() {
        String text = redisSetInputValue.getText();
        if (text.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "input value cannot be empty!", ButtonType.OK).showAndWait();
            return;
        }
        if (set.contains(text)) {
            String alertContent = Strings.format("{} already inserted into list", text);
            new Alert(Alert.AlertType.WARNING, alertContent, ButtonType.OK).showAndWait();
            return;
        }
        redisSetResultListProperty.add(text);
        set.add(text);
    }

    @FXML
    public void handleHashInputValue() {
        String key = redisHashInputKey.getText();
        String value = redisHashInputValue.getText();
        if (key.isEmpty() || value.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "input key/value cannot be empty!", ButtonType.OK).showAndWait();
            return;
        }
        if (hash.containsKey(key)) {
            String alertContent = Strings.format("{} already inserted into table", key);
            new Alert(Alert.AlertType.WARNING, alertContent, ButtonType.OK).showAndWait();
            return;
        }

        Map.Entry<String, String> entry = Map.entry(key, value);
        redisHashResult.getItems().add(entry);
        hash.put(key, value);
    }

    private void handleComboBoxItemChanged(RedisType type) {
        switch (type) {
            case STRING:
                showResult(true, false, false);
                break;
            case SET:
                showResult(false, true, false);
                break;
            case HASH:
                showResult(false, false, true);
                break;
            default:
                break;
        }
    }

    private void showResult(boolean string, boolean set, boolean hash) {
        redisResult.setVisible(string);
        redisSetResult.setVisible(set);
        redisHashResult.setVisible(hash);
    }

    private void showInputArea(RedisType type) {
        switch (type) {
            case SET:
                setGridPane.setVisible(true);
                hashGridPane.setVisible(false);
                break;
            case HASH:
                setGridPane.setVisible(false);
                hashGridPane.setVisible(true);
                break;
            case STRING:
            default:
                setGridPane.setVisible(false);
                hashGridPane.setVisible(false);
        }
    }
}
