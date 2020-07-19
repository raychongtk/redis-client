package ui.controller.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kentsang
 */
public class RedisHashView extends AnchorPane {
    private final Logger logger = LoggerFactory.getLogger(RedisHashView.class);

    @FXML
    TableView<Map.Entry<String, String>> redisHashResult;
    @FXML
    TableColumn<Map.Entry<String, String>, String> redisHashField;
    @FXML
    TableColumn<Map.Entry<String, String>, String> redisHashValue;

    public RedisHashView() {
        super();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/redis-hash-view.fxml"));
            fxmlLoader.setController(this);
            this.getChildren().add(fxmlLoader.load());
        } catch (IOException e) {
            logger.error("cannot load view file", e);
        }

        //This is how we populate the cell
        redisHashField.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        redisHashValue.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        // set editable cell
        redisHashValue.setCellFactory(TextFieldTableCell.forTableColumn());
        redisHashValue.setOnEditCommit(table -> table.getTableView().getItems().get(table.getTablePosition().getRow()).setValue(table.getNewValue()));

        // set column width
        redisHashField.prefWidthProperty().bind(redisHashResult.widthProperty().multiply(0.29));
        redisHashValue.prefWidthProperty().bind(redisHashResult.widthProperty().multiply(0.7));
    }

    public void visible(boolean value) {
        redisHashResult.setVisible(value);
    }

    public void setItems(ObservableList<Map.Entry<String, String>> items) {
        redisHashResult.setItems(items);
    }

    public Map<String, String> redisHash() {
        return redisHashResult.getItems().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
