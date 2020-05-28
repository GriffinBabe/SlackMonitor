package eu.bestbrusselsulb.controller;

import eu.bestbrusselsulb.utils.EventListener;
import eu.bestbrusselsulb.view.MainWindowView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import eu.bestbrusselsulb.utils.EventEmitter.EventType;

public class MonitorApplication extends Application implements EventListener {

    private Stage primaryStage;

    private Pane primaryStageLayout;

    private MainWindowView applicationView;

    private double xOffset = 0;
    private double yOffset = 0;

    public static void main(String[] args) {
        launch(args); /* Launches the application. */
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Slack Monitor");
        this.primaryStage.initStyle(StageStyle.UNDECORATED);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/MainWindow.fxml"));
        primaryStageLayout = loader.load();

        applicationView = loader.getController();

        applicationView.addListener(this);

        Region bar = applicationView.getTopBar();

        bar.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });

        bar.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });

        Scene scene = new Scene(primaryStageLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void onEvent(Object emitter, EventType type, Object... args) {
        if (type == EventType.QUIT_COMMAND) {
            primaryStage.close();
        }
    }
}
