package com.example.projektbd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class MainView {

    // Metoda do wylogowywania użytkownika
    public void logOut(ActionEvent event) {
        // Czyszczenie sesji
        currentUser = null;

        // Zamknięcie aktualnego okna
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        // Otwarcie okna logowania
        openLoginWindow();
    }

    // Metoda do otwierania okna logowania
    private void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Obsługa błędu otwierania okna logowania
        }
    }

    @FXML
    private Label Username;
    @FXML
    private Label UserID;
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (currentUser != null) {
            Username.setText(currentUser.getUsername());
            UserID.setText(String.valueOf(currentUser.getUserID()));
        }
    }

    @FXML
    private GridPane tanksGrid;

    public void initialize() {

        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < 10; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("tank_view.fxml"));

                AnchorPane tankItem = fxmlLoader.load();

                if (column == 2) {
                    column = 0;
                    ++row;
                }

                tanksGrid.add(tankItem, column++, row);
                GridPane.setMargin(tankItem, new Insets(5));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
