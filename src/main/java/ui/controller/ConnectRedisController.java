package ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import redis.service.CreateJedis;
import redis.service.Redis;
import ui.util.AlertUtil;
import util.Preference;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author raychong
 */
public class ConnectRedisController implements Initializable {
    @FXML
    TextField redisHost;
    @FXML
    TextField redisPort;
    @FXML
    Button connectRedis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Optional<String> host = Preference.get("redis.host");
        Optional<String> port = Preference.get("redis.port");

        redisHost.setText(host.orElse(""));
        redisPort.setText(port.orElse(""));
    }

    @FXML
    public void handleConnectRedis() {
        String host = redisHost.getText();
        String port = redisPort.getText();

        CreateJedis.create(host, port);

        Redis redis = Redis.create();
        Window window = connectRedis.getScene().getWindow();
        if (redis.isConnected()) {
            AlertUtil.info("connect redis successfully", window);
            Preference.put("redis.host", host);
            Preference.put("redis.port", port);
        } else {
            AlertUtil.warn("connect redis failed", window);
        }
    }
}
