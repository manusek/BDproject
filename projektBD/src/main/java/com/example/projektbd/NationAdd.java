package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class NationAdd {

    TankAdd tankAdd = new TankAdd();

    @FXML
    private TextField newNationName;
    @FXML
    private TextField newProdName;
    @FXML
    private Label prodNameError;
    @FXML
    private Label nationNameError;
    private Connection connection;


    public void addNation() throws SQLException {
        String name = newNationName.getText();
        String prodName = newProdName.getText();

        isInputEmpty();

        if (!name.isEmpty() && !prodName.isEmpty() && !tankAdd.containsDigits(name) && !tankAdd.containsDigits(prodName)) {
            if (!isNationExists(name)) {
                saveNation(name, prodName);
            } else {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Nacja o podanej nazwie już istnieje w bazie danych.");
            }
        }
    }

    private boolean isNationExists(String name) throws SQLException {
        connection = ConnectDB.getConnection();

        String query = "SELECT COUNT(*) FROM nationality WHERE nation_name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count > 0;
    }


    private void saveNation(String name, String prodName) throws SQLException {
        connection = ConnectDB.getConnection();

        String query = "CALL add_nation(?, ?)";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, name);
        statement.setString(2, prodName);

        statement.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukces");
        alert.setHeaderText(null);
        alert.setContentText("Nowa nacja została dodana do bazy danych.");

        newNationName.clear();
        newProdName.clear();

        alert.showAndWait();
    }


    @FXML
    private void isInputEmpty() {
        String nationName = newNationName.getText();
        String prodName = newProdName.getText();

        if (nationName.isEmpty() || tankAdd.containsDigits(nationName)) {
            newNationName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newNationName).play();
            nationNameError.setText("Pole nazwa nacji nie może być puste!");
        } else {
            newNationName.setStyle(null);
            nationNameError.setText("");
        }

        if (prodName.isEmpty() || tankAdd.containsDigits(prodName)) {
            newProdName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newProdName).play();
            prodNameError.setText("Pole nazwy produkcji nie może być puste!");
        } else {
            newProdName.setStyle(null);
            prodNameError.setText("");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
