package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    @FXML
    private Label emailError;

    @FXML
    private Label loginError;

    @FXML
    private Label passError;

    @FXML
    private Label rePassError;

    private Connection connection;

    public void createNew() throws SQLException {
        connection = ConnectDB.getConnection();

        String newEmail = NewEmail.getText();
        String newLogin = NewLogin.getText();
        String newPass = NewPass.getText();
        String newRePass = NewRePass.getText();

        isInputEmpty();
        isPassEquals();
        boolean emailValid = isEmailValid();

        if (newPass.equals(newRePass) && !newPass.isEmpty() && !newLogin.isEmpty() && !newEmail.isEmpty() && emailValid && !isLoginExists(newLogin) && !isEmailExists(newEmail)) {
            String sql = "INSERT INTO users (user_login, user_email, user_pass) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newLogin);
            statement.setString(2, newEmail);
            statement.setString(3, newPass);

            statement.execute();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Gratulacje");
            alert.setHeaderText(null);
            alert.setContentText("Udało ci się stworzyć nowego użytkownika");
            alert.showAndWait();

            Stage stage = (Stage) NewEmail.getScene().getWindow();
            stage.close();
        }
    }

    private boolean isLoginExists(String login) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE user_login = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, login);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        if (resultSet.getInt(1) > 0) {
            NewLogin.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(NewLogin).play();
            loginError.setText("Login już istnieje!");
            return true;
        }
        return false;
    }

    private boolean isEmailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE user_email = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        if (resultSet.getInt(1) > 0) {
            NewEmail.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(NewEmail).play();
            emailError.setText("Email już istnieje!");
            return true;
        }
        return false;
    }

    @FXML
    private void isInputEmpty() {
        String email = NewEmail.getText();
        String login = NewLogin.getText();
        String password = NewPass.getText();
        String rePassword = NewRePass.getText();

        if (email.isEmpty()) {
            NewEmail.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(NewEmail).play();
            emailError.setText("Pole email nie może być puste!");
        } else {
            NewEmail.setStyle(null);
            emailError.setText("");
        }

        if (login.isEmpty()) {
            NewLogin.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(NewLogin).play();
            loginError.setText("Pole login nie może być puste!");
        } else {
            NewLogin.setStyle(null);
            loginError.setText("");
        }

        if (password.isEmpty()) {
            NewPass.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(NewPass).play();
            passError.setText("Pole hasło nie może być puste!");
        } else {
            NewPass.setStyle(null);
            passError.setText("");
        }

        if (rePassword.isEmpty()) {
            NewRePass.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(NewRePass).play();
            rePassError.setText("Pole powtórzone hasło nie może być puste!");
        } else {
            NewRePass.setStyle(null);
            rePassError.setText("");
        }
    }

    @FXML
    private void isPassEquals() {
        String password = NewPass.getText();
        String rePassword = NewRePass.getText();
        if (!rePassword.equals(password)) {
            NewRePass.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            NewPass.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(NewRePass).play();
            new animatefx.animation.Shake(NewPass).play();
            rePassError.setText("Hasla musza byc takie same!");
            passError.setText("Hasla musza byc takie same!");
        }
    }

    @FXML
    private boolean isEmailValid() {
        String email = NewEmail.getText();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

        if (!email.matches(emailRegex)) {
            NewEmail.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(NewEmail).play();
            emailError.setText("Nieprawidłowy format adresu email!");
            return false;
        } else {
            NewEmail.setStyle(null);
            emailError.setText("");
            return true;
        }
    }
}
