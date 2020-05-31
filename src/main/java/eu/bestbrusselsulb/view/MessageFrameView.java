package eu.bestbrusselsulb.view;

import eu.bestbrusselsulb.controller.MonitorApplication;
import eu.bestbrusselsulb.model.html.EmojiDatabase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class MessageFrameView implements Initializable {

    @FXML
    private ImageView avatar;

    @FXML
    private Label username;

    @FXML
    private Label channel;

    @FXML
    private WebView webView;

    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane header;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAvatar();
        setWebView();
    }

    private void setAvatar() {
        // Clips the image in a circle
        Circle circle = new Circle();
        circle.setCenterX(avatar.getX() + avatar.getFitWidth() / 2);
        circle.setCenterY(avatar.getY() + avatar.getFitHeight() / 2);
        circle.setRadius(avatar.getFitWidth()/2);
        avatar.setClip(circle);
    }

    private void setWebView() {
        EmojiDatabase database = EmojiDatabase.getInstance();

        String content = "<html>Error html file not found</html>";
        try {
            content = new String(
                    Files.readAllBytes(Paths.get(MonitorApplication.class.getResource("/TestMessage.html").toURI())), StandardCharsets.UTF_8
            );
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        webView.getEngine().loadContent(content);
    }

}
