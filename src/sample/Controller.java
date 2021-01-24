package sample;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.DepthTest;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class Controller {

    @FXML
    private Label text;


    @FXML
    void initialize() {
        text.setAlignment(Pos.CENTER);
        text.setText(Main.startMessage);
    }

}