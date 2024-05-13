package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Tankinfo {

    @FXML
    private Button editTank;

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


    public void setTankId(int tankId) {
        this.tankId = tankId;
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        try {
            Connection connection = ConnectDB.getConnection();

            String query = "SELECT t.name AS tank_name, type, t.description AS tank_desc, amount, data, nation_name, a.name AS ammo_name, a.description AS ammo_desc " +
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

                tankInfoName.setText(name);
                tankInfoNation.setText(nation);
                tankInfoType.setText(type);
                tankInfoDesc.setText(description);
                tankInfoAmount.setText(String.valueOf(amount));
                tankInfoDate.setText(date);
                tankInfoAmmo.setText(ammoName);
                tankInfoAmmoDesc.setText(ammoDesc);
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

            // Odśwież GridPane
            MainView mainViewController = new MainView();
            mainViewController.refreshTanksGrid();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
