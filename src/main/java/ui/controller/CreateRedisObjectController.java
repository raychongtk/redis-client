package ui.controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
import ui.util.AlertUtil;
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
    private final Redis redis = Redis.create();

    @FXML
    TextField redisInputKey;
    @FXML
    TextField redisSetInputValue;
    @FXML
    TextField redisHashInputKey;
    @FXML
    TextField redisHashInputValue;
    @FXML
    ComboBox<String> redisInputType;
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

        redisInputType.getItems().addAll(STRING.name(), SET.name(), HASH.name());
        redisInputType.setValue(STRING.name());

        redisInputType.valueProperty().addListener((obs, oldItem, newItem) -> {
            RedisType redisType = RedisType.valueOf(newItem);
            handleComboBoxItemChanged(redisType);
            showInputArea(redisType);
        });
    }

    @FXML
    public void create() {
        String redisKey = redisInputKey.getText();

        if (Strings.isBlank(redisKey)) {
            AlertUtil.warn("key cannot be empty!");
            return;
        }

        RedisType redisType = RedisType.valueOf(redisInputType.getValue());
        RedisData redisData = redisData(redisType);

        if (emptyData(redisData)) {
            AlertUtil.warn("data cannot be empty!");
            return;
        }

        RedisObject redisObject = new RedisObjectBuilder().key(redisKey)
                                                          .type(redisType)
                                                          .data(redisData)
                                                          .build();
        redis.add(redisObject);
        AlertUtil.info("added to redis successfully!");
    }

    @FXML
    public void handleSetInputValue() {
        String value = redisSetInputValue.getText();
        if (Strings.isBlank(value)) {
            AlertUtil.warn("input value cannot be empty!");
            return;
        }

        if (set.contains(value)) {
            AlertUtil.warn(Strings.format("{} already inserted into list", value));
            return;
        }

        redisSetResultListProperty.add(value);
        set.add(value);
    }

    @FXML
    public void handleHashInputValue() {
        String key = redisHashInputKey.getText();
        String value = redisHashInputValue.getText();
        if (Strings.isBlank(key) || Strings.isBlank(value)) {
            AlertUtil.warn("input key/value cannot be empty!");
            return;
        }

        if (hash.containsKey(key)) {
            AlertUtil.warn(Strings.format("{} already inserted into table", key));
            return;
        }

        Map.Entry<String, String> entry = Map.entry(key, value);
        redisHashResult.getItems().add(entry);
        hash.put(key, value);
    }

    private void handleComboBoxItemChanged(RedisType redisType) {
        switch (redisType) {
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

    private boolean emptyData(RedisData redisData) {
        return Strings.isBlank(redisData.string) && redisData.hash.size() == 0 && redisData.set.size() == 0;
    }

    private void showResult(boolean string, boolean set, boolean hash) {
        redisResult.setVisible(string);
        redisSetResult.setVisible(set);
        redisHashResult.setVisible(hash);
    }

    private void showInputArea(RedisType type) {
        switch (type) {
            case SET:
                showKeyValueInput(true, false);
                break;
            case HASH:
                showKeyValueInput(false, true);
                break;
            case STRING:
            default:
                showKeyValueInput(false, false);
        }
    }

    private void showKeyValueInput(boolean set, boolean hash) {
        setGridPane.setManaged(set);
        setGridPane.setVisible(set);
        hashGridPane.setVisible(hash);
        hashGridPane.setManaged(hash);
    }
}
