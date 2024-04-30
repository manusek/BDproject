package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgotPass {
    private Connection connection;
    @FXML
    private TextField ForgotEmail;

    public void remindPass() throws SQLException {
        connection = ConnectDB.getConnection();

        String forgotEmail = ForgotEmail.getText();
        String sql = "SELECT * FROM users WHERE user_email = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, forgotEmail);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()){
            String login = resultSet.getString("user_login");
            String password = resultSet.getString("user_pass");
            String email = resultSet.getString("user_email");

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Info");
            alert.setHeaderText(null);
            alert.setContentText("Twoje dane: " + login + " hasło: " + password + " email: " + email);
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Info");
            alert.setHeaderText(null);
            alert.setContentText("Brak danych z takim emailem!!");
            alert.showAndWait();
        }
    }
}
