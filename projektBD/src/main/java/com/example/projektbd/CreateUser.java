package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateUser {

    @FXML
    private TextField NewEmail;

    @FXML
    private TextField NewLogin;

    @FXML
    private TextField NewPass;

    @FXML
    private TextField NewRePass;
    private Connection connection;

    public void createNew() throws SQLException {
        connection = ConnectDB.getConnection();

        String newEmail = NewEmail.getText();
        String newLogin = NewLogin.getText();
        String newPass = NewPass.getText();
        String newRePass = NewRePass.getText();

        if (newPass.equals(newRePass)){
            String sql = "INSERT INTO users (user_login, user_email, user_pass, acc_type) VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newLogin);
            statement.setString(2, newEmail);
            statement.setString(3, newPass);
            statement.setString(4, "user");

            statement.execute();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Gratulacje");
            alert.setHeaderText(null);
            alert.setContentText("Udało ci sie stworzyć nowego uzytkownika");
            alert.showAndWait();

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Bład");
            alert.setHeaderText(null);
            alert.setContentText("Hasła sie nie zgadzają");
            alert.showAndWait();
        }

    }
}
