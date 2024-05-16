package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Tankinfo implements Initializable {


    @FXML
    private Label tankInfoAmount;

    @FXML
    private Label tankInfoDate;

    @FXML
    private Label tankInfoDesc;

//    @FXML
//    private ImageView tankInfoImg;

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




    public void setTankId(int tankId) {
        this.tankId = tankId;
        loadDataFromDatabase();
    }


    private void loadDataFromDatabase() {
        try {
            Connection connection = ConnectDB.getConnection();

            String query = "SELECT t.name AS tank_name, type, t.description AS tank_desc, prod_place, amount, data, nation_name, a.name AS ammo_name, a.description AS ammo_desc " +
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
                String date = resultSet.getString("data");
                String ammoName = resultSet.getString("ammo_name");
                String ammoDesc = resultSet.getString("ammo_desc");
                String prod = resultSet.getString("prod_place");

                tankInfoName.setText(name);
                tankInfoNation.setText(nation);
                tankInfoType.setText(type);
                tankInfoDesc.setText(description);
                tankInfoAmount.setText(String.valueOf(amount));
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


    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (currentUser != null && currentUser.getUserID() != 2) {
            editButton.setVisible(false);
            deleteButton.setVisible(false);
        }
    }


    @FXML
    private void deleteTank() {
        try {
            Connection connection = ConnectDB.getConnection();

            // Usuń powiązane dane z tabeli tank_ammunition
            String deleteTankAmmunitionQuery = "DELETE FROM tank_ammunition WHERE tank_id = ?";
            PreparedStatement deleteTankAmmunitionStatement = connection.prepareStatement(deleteTankAmmunitionQuery);
            deleteTankAmmunitionStatement.setInt(1, tankId);
            deleteTankAmmunitionStatement.executeUpdate();
            deleteTankAmmunitionStatement.close();

            // Usuń czołg z tabeli tanks
            String deleteTankQuery = "DELETE FROM tanks WHERE tank_id = ?";
            PreparedStatement deleteTankStatement = connection.prepareStatement(deleteTankQuery);
            deleteTankStatement.setInt(1, tankId);
            deleteTankStatement.executeUpdate();
            deleteTankStatement.close();

            connection.close();

            // Zamknij widok
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

        // Pobranie danych dotyczących wybranego czołgu
        controller.setTankDataForEdit(tankId);

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Sprawdź czy użytkownik jest zalogowany i czy jego ID jest różne od 2
        if (currentUser != null && currentUser.getUserID() != 2) {
            // Jeśli tak, ukryj przyciski
            editButton.setVisible(false);
            deleteButton.setVisible(false);
        }
    }
}
