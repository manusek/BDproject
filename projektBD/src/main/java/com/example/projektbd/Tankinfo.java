package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Tankinfo {

    @FXML
    private Button deleteTank;

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


    private int tankId; // Identyfikator czołgu

    public void setTankId(int tankId) {
        this.tankId = tankId;
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        try {
            // Połączenie z bazą danych
            Connection connection = ConnectDB.getConnection();
            // Zapytanie SQL pobierające wszystkie informacje o czołgu na podstawie jego identyfikatora
            String query = "SELECT * FROM tanks WHERE tank_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, tankId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Jeśli znajdziemy dane o czołgu, ustawiamy je w odpowiednich komponentach
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int nation = resultSet.getInt("nation_id");
                String type = resultSet.getString("type");
                String description = resultSet.getString("description");
                int amount = resultSet.getInt("amount");
                String date = resultSet.getString("data");

                // Ustawienie danych w komponentach
                tankInfoName.setText(name);
                tankInfoNation.setText(String.valueOf(nation));
                tankInfoType.setText(type);
                tankInfoDesc.setText(description);
                tankInfoAmount.setText(String.valueOf(amount));
                tankInfoDate.setText(date);
            }

            // Zamknięcie połączenia z bazą danych
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
