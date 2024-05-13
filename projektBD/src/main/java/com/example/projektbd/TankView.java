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
    private int tankId; // Identyfikator czołgu

    public void setTankId(int tankId) {
        this.tankId = tankId;
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        try {
            // Połączenie z bazą danych
            Connection connection = ConnectDB.getConnection();
            // Zapytanie SQL pobierające dane o czołgu na podstawie jego identyfikatora
            String query = "SELECT name, nation_name " +
                    "FROM tanks " +
                    "JOIN nationality ON tanks.nation_id = nationality.nation_id " +
                    "WHERE tank_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, tankId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Jeśli znajdziemy dane o czołgu, ustawiamy je w odpowiednich komponentach
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String nationName = resultSet.getString("nation_name");

                // Ustawienie danych w komponentach
                tankName.setText(name);
                tankNation.setText(nationName);
            }

            // Zamknięcie połączenia z bazą danych
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void more() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("tank_info.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
}
