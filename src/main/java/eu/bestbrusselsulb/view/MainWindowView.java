package eu.bestbrusselsulb.view;

import eu.bestbrusselsulb.controller.MonitorApplication;
import eu.bestbrusselsulb.utils.EventEmitter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MainWindowView extends EventEmitter implements Initializable {

    private static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    private Pane root;

    @FXML
    private Label clock;

    @FXML
    private Region topBar;

    @FXML
    private Label quitButton;

    @FXML
    private VBox messageList;

    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controller called");
        bindToTime();
        setCross();

        /* Add placeholder message */
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MonitorApplication.class.getResource("/MessageFrame.fxml"));
            AnchorPane message = loader.load();
            messageList.getChildren().add(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindToTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
                event-> clock.setText(LocalTime.now().format(TIME_FORMATTER))),
                new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setCross() {
        quitButton.setOnMouseEntered(event -> {
            quitButton.setStyle("-fx-text-fill: #ffffff");
        });
        quitButton.setOnMouseExited(event -> {
            quitButton.setStyle("-fx-text-fill: black");
        });
        quitButton.setOnMouseClicked(event -> {
            emit(EventType.QUIT_COMMAND);
        });
    }

    public Region getTopBar() {
        return topBar;
    }

}