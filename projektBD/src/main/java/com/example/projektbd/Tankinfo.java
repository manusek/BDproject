package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javafx.scene.image.Image;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class Tankinfo implements Initializable {

    @FXML
    private Label tankInfoAmount;
    @FXML
    private Label tankInfoDate;
    @FXML
    private Label tankInfoDesc;
    @FXML
    private ImageView tankInfoImg;
    @FXML
    private Label tankInfoName;
    @FXML
    private Label tankInfoNation;
    @FXML
    private Label tankInfoType;
    @FXML
    private Label tankInfoAmmo;
    @FXML
    private Label tankInfoAmmoDesc;
    private int tankId;
    private User currentUser;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Label tankInfoProd;
    Image defaultImage = new Image("file:/C:/Users/flore/Desktop/BD_sem4/PROJEKT_BAZY/BDproject/projektBD/src/main/resources/com/example/projektbd/img/tank.png");


    public void setTankId(int tankId) {
        this.tankId = tankId;
        loadDataFromDatabase();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCurrentUser();
    }

    public void setCurrentUser() {
        String currentType = User.getType(); // PRZECHOWUJE TYP USERA
        int currentID = User.getUserID(); // JAKBY BYLO POTRZEBNE, PRZECHOWUJE TO ID USERA
        if (Objects.equals(currentType, "admin")) {
            editButton.setVisible(true);
            deleteButton.setVisible(true);
        } else {
            editButton.setVisible(false);
            deleteButton.setVisible(false);
        }
    }


    private void loadDataFromDatabase() {
        try {
            Connection connection = ConnectDB.getConnection();

            String query = "SELECT t.name AS tank_name, type, t.description AS tank_desc, img, prod_place, amount, data, nation_name, a.name AS ammo_name, a.description AS ammo_desc " +
                    "FROM tanks t  " +
                    "JOIN nationality ON t.nation_id = nationality.nation_id " +
                    "JOIN tank_ammunition on t.tank_id = tank_ammunition.tank_id " +
                    "JOIN ammunition a on tank_ammunition.ammo_id = a.ammo_id " +
                    "WHERE t.tank_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, tankId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("tank_name");
                String nation = resultSet.getString("nation_name");
                String type = resultSet.getString("type");
                String description = resultSet.getString("tank_desc");
                int amount = resultSet.getInt("amount");
                byte[] imgBytes = resultSet.getBytes("img");
                String date = resultSet.getString("data");
                String ammoName = resultSet.getString("ammo_name");
                String ammoDesc = resultSet.getString("ammo_desc");
                String prod = resultSet.getString("prod_place");

                tankInfoName.setText(name);
                tankInfoNation.setText(nation);
                tankInfoType.setText(type);
                tankInfoDesc.setText(description);
                tankInfoAmount.setText(String.valueOf(amount));

                if (imgBytes != null) {
                    InputStream inputStream = new ByteArrayInputStream(imgBytes);
                    Image image = new Image(inputStream);
                    tankInfoImg.setImage(image);
                } else {
                    tankInfoImg.setImage(defaultImage);
                }

                tankInfoDate.setText(date);
                tankInfoAmmo.setText(ammoName);
                tankInfoAmmoDesc.setText(ammoDesc);
                tankInfoProd.setText(prod);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void deleteTank() {
        try {
            Connection connection = ConnectDB.getConnection();

            String deleteTankAmmunitionQuery = "DELETE FROM tank_ammunition WHERE tank_id = ?";
            PreparedStatement deleteTankAmmunitionStatement = connection.prepareStatement(deleteTankAmmunitionQuery);
            deleteTankAmmunitionStatement.setInt(1, tankId);
            deleteTankAmmunitionStatement.executeUpdate();
            deleteTankAmmunitionStatement.close();

            String deleteTankInMuseum = "DELETE FROM museum_tanks WHERE tank_id = ?";
            PreparedStatement deleteTankInMuseumStatement = connection.prepareStatement(deleteTankInMuseum);
            deleteTankInMuseumStatement.setInt(1, tankId);
            deleteTankInMuseumStatement.executeUpdate();
            deleteTankInMuseumStatement.close();

            String deleteTankQuery = "DELETE FROM tanks WHERE tank_id = ?";
            PreparedStatement deleteTankStatement = connection.prepareStatement(deleteTankQuery);
            deleteTankStatement.setInt(1, tankId);
            deleteTankStatement.executeUpdate();
            deleteTankStatement.close();

            connection.close();

            Stage stage = (Stage) tankInfoName.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editTank() throws IOException {
        Stage stage = (Stage) tankInfoName.getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("tank_add.fxml"));
        Parent root = loader.load();
        TankAdd controller = loader.getController();

        controller.setTankDataForEdit(tankId);

        stage.setScene(new Scene(root));
        stage.show();
    }

}
