package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TankView {

    @FXML
    private Label tankName;

    @FXML
    private Label tankNation;
    private int tankId;

    public void setTankId(int tankId) {
        this.tankId = tankId;
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        try {
            Connection connection = ConnectDB.getConnection();

            String query = "SELECT name, nation_name " + "FROM tanks " + "JOIN nationality ON tanks.nation_id = nationality.nation_id " + "WHERE tank_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, tankId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String nationName = resultSet.getString("nation_name");

                tankName.setText(name);
                tankNation.setText(nationName);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void more() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tank_info.fxml"));
            Parent parent = loader.load();
            Tankinfo tankinfoController = loader.getController();
            tankinfoController.setTankId(tankId);
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
