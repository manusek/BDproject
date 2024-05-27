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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;


public class MainView {


    @FXML
    private Button ListNations;
    @FXML
    private Button ListUsers;
    @FXML
    private GridPane tanksGrid;
    @FXML
    private Button AddTank;
    @FXML
    private Button AddNation;
    @FXML
    private Button AddAmmo;
    @FXML
    private Label Username;
    @FXML
    private Label UserID;
    private User currentUser;
    @FXML
    private TextField SearchBar;

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


    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (currentUser != null) {
            Username.setText(currentUser.getUsername());
            UserID.setText(String.valueOf(User.getUserID()));

            if (currentUser != null && !Objects.equals(User.getType(), "admin")) {
                AddTank.setVisible(false);
                AddNation.setVisible(false);
                AddAmmo.setVisible(false);
                ListUsers.setVisible(false);
                ListNations.setVisible(false);
            }
        }
    }

    @FXML
    void refreshContent(MouseEvent event) {
        loadContent();
        System.out.println("odswiezono");
    }

    public void initialize() {
        loadContent();
    }

    private void loadContent() {
        int column = 0;
        int row = 1;
        try {
            tanksGrid.getChildren().clear();

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


    @FXML
    public void addTank() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tank_add.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void addNation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nation_add.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addAmmo(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ammo_add.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    sie moze przydac
//    WHERE nation_id = 2;
//    UPDATE nationality
//    SET prod_place = 'Berlin'
    @FXML
    void showNations(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("list_nations.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showUsers(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("list_users.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
