package ui.controller.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import redis.builder.RedisObjectBuilder;
import redis.domain.RedisData;
import redis.domain.RedisObject;
import redis.domain.RedisType;
import redis.service.Redis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kentsang
 */
public class RedisHashControl extends AnchorPane {
    @FXML
    TableView<Map.Entry<String, String>> redisHashResult;
    @FXML
    TableColumn<Map.Entry<String, String>, String> redisHashField;
    @FXML
    TableColumn<Map.Entry<String, String>, String> redisHashValue;

    private String key;

    public RedisHashControl() {
        super();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tableView.fxml"));
            fxmlLoader.setController(this);
            this.getChildren().add(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //This is how we populate the cell
        redisHashField.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        redisHashValue.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        // set editable cell
        redisHashValue.setCellFactory(TextFieldTableCell.forTableColumn());
        redisHashValue.setOnEditCommit(this::editOnCommitEvent);

        // set column width
        redisHashField.prefWidthProperty().bind(redisHashResult.widthProperty().multiply(0.29));
        redisHashValue.prefWidthProperty().bind(redisHashResult.widthProperty().multiply(0.7));
    }

    @FXML
    public void editOnCommitEvent(TableColumn.CellEditEvent<Map.Entry<String, String>, String> editedEvent) {
        Redis.getInstance().update(key, editedEvent.getRowValue().getKey(), editedEvent.getNewValue());
        redisHashResult.setItems(FXCollections.observableArrayList(Redis.getInstance().get(key).hash.entrySet()));
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TableView<Map.Entry<String, String>> getRedisHashResult() {
        return redisHashResult;
    }
}
