package com.example.projektbd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class TankAdd {


    @FXML
    private ChoiceBox<String> newTankNation;

    @FXML
    private ChoiceBox<String> newTankAmmo;
    @FXML
    private ImageView image;
    @FXML
    private TextField newTankName;
    @FXML
    private TextField newTankAmount;
    @FXML
    private DatePicker newTankDate;
    @FXML
    private TextArea newTankDesc;

    @FXML
    private TextField newTankType;
    private Connection connection;


    @FXML
    public void initialize() throws SQLException {
        fetchNations();
    }

    public void fetchNations() throws SQLException {
        connection = ConnectDB.getConnection();

        String sql = "SELECT nation_id, nation_name FROM nationality";

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();

        ObservableList<String> nations = FXCollections.observableArrayList();

        while (resultSet.next()) {
            nations.add(resultSet.getInt("nation_id") + ": " + resultSet.getString("nation_name"));
        }

        newTankNation.setItems(nations);

        // ====================================

        String sql2 = "SELECT ammo_id, name FROM ammunition";

        PreparedStatement statement2 = connection.prepareStatement(sql2);

        ResultSet resultSet2 = statement2.executeQuery();

        ObservableList<String> ammunnition = FXCollections.observableArrayList();

        while (resultSet2.next()) {
            ammunnition.add(resultSet2.getInt("ammo_id") + ": " + resultSet2.getString("name"));
        }

        newTankAmmo.setItems(ammunnition);
    }

//    @FXML
//    public void handleChoiceBoxAction(ActionEvent event) {
//        // Pobierz wybraną nację z ChoiceBox
//        String selectedNation = newTankNation.getSelectionModel().getSelectedItem();
//
//        // Możesz tutaj dalej przetwarzać wybraną nację, np. wyświetlić ją w etykiecie
//        System.out.println("Wybrana nacja: " + selectedNation);
//    }

    @FXML
    public void img(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Wybierz zdjęcie");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Zdjęcia", "*.png", "*.jpg", "*.gif"), new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {

            Image img = new Image(selectedFile.toURI().toString());
            image.setImage(img);

        } else {
            System.out.println("Nie wybrano pliku.");
        }
    }


    @FXML
    public void saveTank(ActionEvent event) throws SQLException {
        String name = newTankName.getText();
        String type = newTankType.getText();
        String desc = newTankDesc.getText();

        String nationSelection = newTankNation.getValue();
        String[] nationParts = nationSelection.split(":");
        int nationId = Integer.parseInt(nationParts[0].trim());

        int amount = Integer.parseInt(newTankAmount.getText());
        String date = newTankDate.getValue().toString();

        String ammoSelection = newTankAmmo.getValue();
        String[] ammoParts = ammoSelection.split(":");
        int ammoId = Integer.parseInt(ammoParts[0].trim());

        // Wykonaj zapis do bazy danych
        saveTankToDatabase(name, type, desc, nationId, amount, date, ammoId);
    }

    private void saveTankToDatabase(String name, String type, String desc, int nationId, int amount, String date, int ammoId) throws SQLException {
        connection = ConnectDB.getConnection();

        String query = "INSERT INTO tanks (nation_id, name, type, description, amount, data) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(query);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        Date sqlDate = Date.valueOf(localDate);

        statement.setInt(1, nationId);
        statement.setString(2, name);
        statement.setString(3, type);
        statement.setString(4, desc);
        statement.setInt(5, amount);
        statement.setDate(6, sqlDate);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Nowy czołg został dodany do bazy danych.");

            // Wyczyszczenie pól
            newTankName.clear();
            newTankType.clear();
            newTankDesc.clear();
            newTankAmount.clear();
            newTankDate.setValue(null);

            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Nie udało się dodać nowego czołgu do bazy danych.");

            alert.showAndWait();
        }

        String lastId = "SELECT tank_id FROM tanks ORDER BY tank_id DESC LIMIT 1";
        PreparedStatement last = connection.prepareStatement(lastId);
        ResultSet resultSet = last.executeQuery();

        int tankID = 0;
        if (resultSet.next()) {
            tankID = resultSet.getInt(1);
        }

        String query2 = "INSERT INTO tank_ammunition (ammo_id, tank_id) VALUES (?, ?)";

        PreparedStatement statement2 = connection.prepareStatement(query2);
        statement2.setInt(1, ammoId);
        statement2.setInt(2, tankID);
        statement2.executeUpdate();


    }
}
