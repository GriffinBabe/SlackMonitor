package eu.bestbrusselsulb.view;

import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.WebPage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;

import java.net.URL;
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
        webView.getEngine().loadContent("<html><body style=\"background-color: transparent;\">Hello world!</body></html>");
        webView.setBlendMode(BlendMode.LIGHTEN);
    }

}
