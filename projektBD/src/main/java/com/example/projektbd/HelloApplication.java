package com.example.projektbd;

import javafx.application.Application;
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

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Muzeum!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();


    }

    @FXML
    private Button CreateButton;

    @FXML
    private Label ForgotPass;

    @FXML
    private TextField Login;

    @FXML
    private Button LoginButton;

    @FXML
    private TextField Pass;

    public Connection connection;

    @FXML
    public void loginUser() throws SQLException {

        connection = ConnectDB.getConnection();

        String username = Login.getText();
        String password = Pass.getText();

        try {
            // Zapytanie SQL do sprawdzenia danych logowania
            String sql = "SELECT * FROM users WHERE user_login = ? AND user_pass = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Użytkownik znaleziony, poprawne logowanie
                Parent parent = FXMLLoader.load(getClass().getResource("main_view.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                stage.show();
            } else {
                // Brak użytkownika lub niepoprawne dane logowania
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect username or password");
                alert.showAndWait();
            }

            // Zamykanie połączenia z bazą danych
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}