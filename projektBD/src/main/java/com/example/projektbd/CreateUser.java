package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        isEmailValid();

        if (newPass.equals(newRePass) && !newPass.isEmpty() && !newLogin.isEmpty() && !newEmail.isEmpty() && isEmailValid()){ //
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
    private void isPassEquals(){
        String password = NewPass.getText();
        String rePassword = NewRePass.getText();
        if(!rePassword.equals(password)){
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
        // Prosta walidacja adresu email za pomocą wyrażenia regularnego
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

        if (!email.matches(emailRegex)) {
            NewEmail.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(NewEmail).play();
            emailError.setText("Nieprawidłowy format adresu email!");
            return false; // Zwracamy false, jeśli adres email jest niepoprawny
        } else {
            NewEmail.setStyle(null);
            emailError.setText("");
            return true; // Zwracamy true, jeśli adres email jest poprawny
        }
    }

//    @FXML
//    private boolean isEmailValid() {
//        String email = NewEmail.getText();
//        // Prosta walidacja adresu email za pomocą wyrażenia regularnego
//        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
//
//        if (!email.matches(emailRegex)) {
//            NewEmail.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
//            new animatefx.animation.Shake(NewEmail).play();
//            emailError.setText("Nieprawidłowy format adresu email!");
//        } else {
//            NewEmail.setStyle(null);
//            emailError.setText("");
//        }
//        return false;
//    }


}
