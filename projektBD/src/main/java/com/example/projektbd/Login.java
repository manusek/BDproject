package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    @FXML
    private TextField Login;
    @FXML
    private TextField Pass;
    private Connection connection;


    public void loginUser() {
        connection = ConnectDB.getConnection();

        String username = Login.getText();
        String password = Pass.getText();

        try {
            String sql = "SELECT * FROM users WHERE user_login = ? AND user_pass = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Stage loginStage = (Stage) Login.getScene().getWindow();
                loginStage.close();

                int userID = resultSet.getInt("user_id");
                // TODO to sprawia ze mamy dostepne ID aktualnie zalogowanego uzytkownika wszedzie
                User.setUserID(userID);
                String type = resultSet.getString("acc_type");
                User.setType(type);
                User user = new User(resultSet.getString("user_login"), userID, type);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("main_view.fxml"));
                Parent parent = loader.load();
                MainView controller = loader.getController();
                controller.setCurrentUser(user);

                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd logowania");
                alert.setHeaderText(null);
                alert.setContentText("Niepoprawne dane");
                alert.showAndWait();
            }

            resultSet.close();
            statement.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    public void forgotPass() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("forgot_pass.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    public void createUser() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("create_user.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
}