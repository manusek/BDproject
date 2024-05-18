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

    public TankAdd() {
    }

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

    @FXML
    private Button addBut;
    @FXML
    private Button editBut;

    @FXML
    private Label tankAmmoError;

    @FXML
    private Label tankAmmountError;

    @FXML
    private Label tankDateError;

    @FXML
    private Label tankDescError;

    @FXML
    private Label tankNameError;

    @FXML
    private Label tankNationalityError;

    @FXML
    private Label tankTypeError;

    private Connection connection;

    private int currentTankId;

    // nie widac przycisku
    public Button getAddBut() {
        return addBut;
    }

    @FXML
    public void initialize() throws SQLException {
        editBut.setVisible(false);
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
        String amount = newTankAmount.getText();

        String nationSelection = newTankNation.getValue();
        String ammoSelection = newTankAmmo.getValue();

        isInputEmpty();

        //TODO walidacja dziala oprocz tego ze nie moze byc ujemny amount

        // Walidacja daty
        LocalDate dateValue = newTankDate.getValue();
        if (dateValue == null) {
            return;
        }
        String date = dateValue.toString();

        // Walidacja dla nationSelection i ammoSelection
        if (nationSelection == null || ammoSelection == null) {
            return;
        }

        String[] nationParts = nationSelection.split(":");
        int nationId = Integer.parseInt(nationParts[0].trim());

        String[] ammoParts = ammoSelection.split(":");
        int ammoId = Integer.parseInt(ammoParts[0].trim());

        // Walidacja czy amount jest liczbą
        if (!name.isEmpty() && !type.isEmpty() && !desc.isEmpty()  && !isNumeric(type) && isNumeric(amount)  && !containsDigits(type)) {
            int amountInt = Integer.parseInt(amount);
            System.out.println("All fields are valid. Saving tank to database...");

            if (amountInt < 0) {
                System.out.println("Amount cannot be negative.");
                return;
            }

            // Wykonaj zapis do bazy danych
            saveTankToDatabase(name, type, desc, nationId, amountInt, date, ammoId);
        } else {
            // Wyświetl komunikat błędu lub powiadomienie dla użytkownika
            System.out.println("Please ensure that all fields are filled correctly.");
        }
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

    public void setTankDataForEdit(int tankId) {

        this.currentTankId = tankId;

        addBut.setVisible(false);
        editBut.setVisible(true);
        try {
            Connection connection = ConnectDB.getConnection();

            String query = "SELECT t.name AS tank_name, a.ammo_id as a_id, type, t.description AS tank_desc, amount, data, nation_name, t.nation_id as n_id, a.name AS ammo_name, a.description AS ammo_desc " +
                    "FROM tanks t  " +
                    "JOIN nationality ON t.nation_id = nationality.nation_id " +
                    "JOIN tank_ammunition on t.tank_id = tank_ammunition.tank_id " +
                    "JOIN ammunition a on tank_ammunition.ammo_id = a.ammo_id " +
                    "WHERE t.tank_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, tankId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                newTankName.setText(resultSet.getString("tank_name"));
                newTankType.setText(resultSet.getString("type"));
                newTankDesc.setText(resultSet.getString("tank_desc"));
                newTankAmount.setText(String.valueOf(resultSet.getInt("amount")));
                newTankDate.setValue(resultSet.getDate("data").toLocalDate());
                // Ustawienie wybranej nacji w ChoiceBox
                newTankNation.getSelectionModel().select(resultSet.getInt("n_id") + ": " + resultSet.getString("nation_name"));
                newTankAmmo.getSelectionModel().select(resultSet.getInt("a_id") + ": " + resultSet.getString("ammo_name"));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void editTank(ActionEvent event) {
        try {
            String name = newTankName.getText();
            String type = newTankType.getText();
            String desc = newTankDesc.getText();
            String amount = newTankAmount.getText();

            String nationSelection = newTankNation.getValue();
            String ammoSelection = newTankAmmo.getValue();

            isInputEmpty();

            //TODO walidacja dziala oprocz ujemnych liczb

            // Walidacja daty
            LocalDate dateValue = newTankDate.getValue();
            if (dateValue == null) {
                System.out.println("Date is not selected.");
                return;
            }
            String date = dateValue.toString();

            // Walidacja dla nationSelection i ammoSelection
            if (nationSelection == null || ammoSelection == null) {
                System.out.println("Nation or Ammo selection is null.");
                return;
            }

            String[] nationParts = nationSelection.split(":");
            int nationId = Integer.parseInt(nationParts[0].trim());

            String[] ammoParts = ammoSelection.split(":");
            int ammoId = Integer.parseInt(ammoParts[0].trim());

            // Walidacja czy amount jest liczbą i czy type nie zawiera cyfr
            if (!name.isEmpty() && !type.isEmpty() && !desc.isEmpty() && !containsDigits(type) && isNumeric(amount)) {
                int amountInt = Integer.parseInt(amount);
                System.out.println("All fields are valid. Updating tank in database...");

                if (amountInt < 0) {
                    System.out.println("Amount cannot be negative.");
                    return;
                }

                // Wykonaj aktualizację w bazie danych
                updateTankInDatabase(name, type, desc, nationId, amountInt, date, ammoId, currentTankId);
                editBut.setVisible(true);
            } else {
                // Wyświetl komunikat błędu lub powiadomienie dla użytkownika
                System.out.println("Please ensure that all fields are filled correctly.");
                if (containsDigits(type)) {
                    System.out.println("Type contains digits, which is not allowed.");
                }
                if (!isNumeric(amount)) {
                    System.out.println("Amount is not a numeric value.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }


    private void updateTankInDatabase(String name, String type, String desc, int nationId, int amount, String date, int ammoId, int currentID) throws SQLException {
        connection = ConnectDB.getConnection();

        String query = "UPDATE tanks SET nation_id=?, name=?, type=?, description=?, amount=?, data=? WHERE tank_id=?";

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

        statement.setInt(7, currentID);

        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Dane czołgu zostały zaktualizowane w bazie danych.");

            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Nie udało się zaktualizować danych czołgu w bazie danych.");

            alert.showAndWait();
        }

        String updateAmmoQuery = "UPDATE tank_ammunition SET ammo_id=? WHERE tank_id=?";
        PreparedStatement updateAmmoStatement = connection.prepareStatement(updateAmmoQuery);
        updateAmmoStatement.setInt(1, ammoId);
        updateAmmoStatement.setInt(2, currentID);
        updateAmmoStatement.executeUpdate();
    }

    @FXML
    private void isInputEmpty() {

        String tankName = newTankName.getText();
        String tankType = newTankType.getText();
        String tankDesc = newTankDesc.getText();
        String tankAmount = newTankAmount.getText();
//        int tankAmount2 = Integer.parseInt(newTankAmount.getText());

        String tankNation = newTankNation.getValue();
        String tankAmmo = newTankAmmo.getValue();
        LocalDate tankDate = newTankDate.getValue();

        if (tankName.isEmpty()) {
            newTankName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankName).play();
            // Set an error message for tankName if you have a label for it
            tankNameError.setText("Pole nazwa czołgu nie może być puste!");
        } else {
            newTankName.setStyle(null);
            tankNameError.setText("");
        }

        if (tankType.isEmpty() || !isLetter(tankType) || containsDigits(tankType)) {
            newTankType.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankType).play();
            // Set an error message for tankType if you have a label for it
            tankTypeError.setText("Pole typ czołgu nie może być puste!");
        } else {
            newTankType.setStyle(null);
            tankTypeError.setText("");
        }

        if (tankDesc.isEmpty()) {
            newTankDesc.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankDesc).play();
            // Set an error message for tankDesc if you have a label for it
            tankDescError.setText("Pole opis czołgu nie może być puste!");
        } else {
            newTankDesc.setStyle(null);
            tankDescError.setText("");
        }

        if (tankAmount.isEmpty() || isLetter(tankAmount)) {
            newTankAmount.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankAmount).play();
            // Set an error message for tankAmount if you have a label for it
            tankAmmountError.setText("Pole ilość czołgów nie może być puste!");
        } else {
            newTankAmount.setStyle(null);
            tankAmmountError.setText("");
        }

        if (tankNation == null || tankNation.isEmpty()) {
            newTankNation.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankNation).play();
            // Set an error message for tankNation if you have a label for it
            tankNationalityError.setText("Pole nacja czołgu nie może być puste!");
        } else {
            newTankNation.setStyle(null);
            tankNationalityError.setText("");
        }

        if (tankAmmo == null || tankAmmo.isEmpty()) {
            newTankAmmo.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankAmmo).play();
            // Set an error message for tankAmmo if you have a label for it
            tankAmmoError.setText("Pole amunicja czołgu nie może być puste!");
        } else {
            newTankAmmo.setStyle(null);
            tankAmmoError.setText("");
        }

        if (tankDate == null) {
            newTankDate.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankDate).play();
            // Set an error message for tankDate if you have a label for it
            tankDateError.setText("Pole data czołgu nie może być puste!");
        } else {
            newTankDate.setStyle(null);
            tankDateError.setText("");
        }
    }

    public static boolean isLetter(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (Character.isLetter(input.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
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
