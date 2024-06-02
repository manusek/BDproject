package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MuseumAdd {

    @FXML
    private Label museumNameError;
    @FXML
    private TextField newMuseumName;
    @FXML
    private TextField newMuseumPlace;
    private Connection connection;


    @FXML
    void addMuseum(MouseEvent event) throws SQLException {
        String name = newMuseumName.getText();
        String place = newMuseumPlace.getText();

        isInputEmpty();

        if (!name.isEmpty() && !containsDigits(place)){
            saveMuseum(name, place);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Pole miejscowosc nie może zawierać cyfr.");
            alert.showAndWait();
        }
    }


    private void saveMuseum(String name, String place) throws SQLException {
        connection = ConnectDB.getConnection();

        String query = "INSERT INTO museum (name, localization) VALUES (?, ?)";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, name);
        statement.setString(2, place);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Nowe muzeum zostało dodana do bazy danych.");

            newMuseumName.clear();
            newMuseumPlace.clear();

            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Nie udało się dodać nowego muzeum do bazy danych.");
            alert.showAndWait();
        }
    }

    @FXML
    private void isInputEmpty() {
        String museumName = newMuseumName.getText();

        if (museumName.isEmpty()) {
            newMuseumName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newMuseumName).play();
            museumNameError.setText("Pole nazwa nacji nie może być puste!");
        } else {
            newMuseumName.setStyle(null);
            museumNameError.setText("");
        }
    }

    public boolean containsDigits(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
}

