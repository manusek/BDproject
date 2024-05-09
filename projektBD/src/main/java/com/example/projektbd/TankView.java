package com.example.projektbd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class TankView {

    @FXML
    private ImageView tankImg;
    @FXML
    private Label tankName;
    @FXML
    private Label tankNation;

    public void more() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("tank_view.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

}
