package eu.bestbrusselsulb.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}