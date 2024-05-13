package com.example.projektbd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MainView {

    public void logOut(ActionEvent event) {
        currentUser = null;

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        openLoginWindow();
    }

    private void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Label Username;

    @FXML
    private Label UserID;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (currentUser != null) {
            Username.setText(currentUser.getUsername());
            UserID.setText(String.valueOf(currentUser.getUserID()));
        }
    }

    @FXML
    private GridPane tanksGrid;


    public void refreshTanksGrid() {
        tanksGrid.getChildren().clear(); // Wyczyść istniejące elementy w GridPane
        initialize(); // Ponownie załaduj czołgi do GridPane
    }


    public void initialize() {
        int column = 0;
        int row = 1;
        try {
            Connection connection = ConnectDB.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tanks");

            while (resultSet.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("tank_view.fxml"));
                AnchorPane tankItem = fxmlLoader.load();

                TankView tankViewController = fxmlLoader.getController();
                tankViewController.setTankId(resultSet.getInt("tank_id"));

                if (column == 2) {
                    column = 0;
                    ++row;
                }
                tanksGrid.add(tankItem, column++, row);
                GridPane.setMargin(tankItem, new Insets(5));
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
