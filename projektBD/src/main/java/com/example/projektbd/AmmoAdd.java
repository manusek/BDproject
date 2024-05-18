package com.example.projektbd;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AmmoAdd {

    @FXML
    private Button ammoButton;
    @FXML
    private Label ammoDescError;
    @FXML
    private Label ammoNameError;
    @FXML
    private TextArea newAmmoDesc;
    @FXML
    private TextField newAmmoName;
    private Connection connection;


    public void addAmmo() throws SQLException {
        String name = newAmmoName.getText();
        String description = newAmmoDesc.getText();

        isInputEmpty();

        if (!name.isEmpty() &&  !description.isEmpty()) {
            saveAmmo(name, description);
        }
    }

    private void saveAmmo(String name, String description) throws SQLException {
        connection = ConnectDB.getConnection();

        String query = "INSERT INTO ammunition (name, description) VALUES (?, ?) ";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, name);
        statement.setString(2, description);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Nowa typ amunicji został dodany do bazy danych.");

            newAmmoName.clear();
            newAmmoDesc.clear();

            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Nie udało się dodać nowego typu amunicji do bazy danych.");
            alert.showAndWait();
        }
    }

    @FXML
    private void isInputEmpty() {
        String nationName = newAmmoName.getText();
        String prodName = newAmmoDesc.getText();

        if (nationName.isEmpty()) {
            newAmmoName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newAmmoName).play();
            ammoNameError.setText("Pole nazwa amunicji nie może być puste!");
        } else {
            newAmmoName.setStyle(null);
            ammoNameError.setText("");
        }

        if (prodName.isEmpty()) {
            newAmmoDesc.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newAmmoDesc).play();
            ammoDescError.setText("Pole opisu amunicji nie może być puste!");
        } else {
            newAmmoDesc.setStyle(null);
            ammoDescError.setText("");
        }
    }
}
