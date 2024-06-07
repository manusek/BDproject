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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class TankAdd {

    public TankAdd() {}

    @FXML
    private ChoiceBox<String> newTankNation;
    @FXML
    private ChoiceBox<String> newTankAmmo;
    @FXML
    private ChoiceBox<String> newTankMuseum;
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
    @FXML
    private Label tankMuseumError;
    private Connection connection;
    private int currentTankId;
    private File selectedFile;

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

        String sql = "SELECT * FROM nationality order by nation_id ASC";

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();

        ObservableList<String> nations = FXCollections.observableArrayList();

        while (resultSet.next()) {
            nations.add(resultSet.getInt("nation_id") + ": " + resultSet.getString("nation_name") + ", " + resultSet.getString("prod_place"));
        }

        newTankNation.setItems(nations);

        // ====================================

        String sql2 = "SELECT ammo_id, name FROM ammunition ORDER BY ammo_id ASC";

        PreparedStatement statement2 = connection.prepareStatement(sql2);

        ResultSet resultSet2 = statement2.executeQuery();

        ObservableList<String> ammunnition = FXCollections.observableArrayList();

        while (resultSet2.next()) {
            ammunnition.add(resultSet2.getInt("ammo_id") + ": " + resultSet2.getString("name"));
        }

        newTankAmmo.setItems(ammunnition);

        String sql3 = "SELECT museum_id, name FROM museum ORDER BY museum_id ASC";

        PreparedStatement statement3 = connection.prepareStatement(sql3);

        ResultSet resultSet3 = statement3.executeQuery();

        ObservableList<String> museum = FXCollections.observableArrayList();

        while (resultSet3.next()) {
            museum.add(resultSet3.getInt("museum_id") + ": " + resultSet3.getString("name"));
        }

        newTankMuseum.setItems(museum);
    }

    @FXML
    public void img(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Wybierz zdjęcie");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Zdjęcia", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*"));

        selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            Image img = new Image(selectedFile.toURI().toString());
            image.setImage(img);

        } else {
            System.out.println("Nie wybrano pliku.");
        }
    }


    @FXML
    public void saveTank(ActionEvent event) throws SQLException, IOException {
        String name = newTankName.getText();
        String type = newTankType.getText();
        String desc = newTankDesc.getText();
        String amount = newTankAmount.getText();

        String nationSelection = newTankNation.getValue();
        String ammoSelection = newTankAmmo.getValue();
        String museumSelection = newTankMuseum.getValue();

        isInputEmpty();

        LocalDate dateValue = newTankDate.getValue();
        if (dateValue == null) {
            return;
        }
        String date = dateValue.toString();

        if (nationSelection == null || ammoSelection == null || museumSelection == null) {
            return;
        }

        String[] nationParts = nationSelection.split(":");
        int nationId = Integer.parseInt(nationParts[0].trim());

        String[] ammoParts = ammoSelection.split(":");
        int ammoId = Integer.parseInt(ammoParts[0].trim());

        String[] museumName = museumSelection.split(":");
        int museumId = Integer.parseInt(museumName[0].trim());

        if (!name.isEmpty() && !type.isEmpty() && !desc.isEmpty()  && !isNumeric(type)  && !containsDigits(type)) {
            int amountInt = Integer.parseInt(amount);
            System.out.println("All fields are valid. Saving tank to database...");

            saveTankToDatabase(name, type, desc, nationId, amountInt, date, ammoId, museumId, selectedFile);
        } else {
            System.out.println("Please ensure that all fields are filled correctly.");
        }
    }

    private void saveTankToDatabase(String name, String type, String desc, int nationId, int amount, String date, int ammoId, int museumId, File imageFile) throws SQLException, IOException {
        connection = ConnectDB.getConnection();

        String query = "INSERT INTO tanks (nation_id, name, type, img, description, amount, data) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(query);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        Date sqlDate = Date.valueOf(localDate);

        statement.setInt(1, nationId);
        statement.setString(2, name);
        statement.setString(3, type);

        if (imageFile != null) {
            try (FileInputStream fis = new FileInputStream(imageFile)) {
                byte[] imageData = fis.readAllBytes();
                statement.setBytes(4, imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File defaultImg = new File("C:\\Users\\flore\\Desktop\\BD_sem4\\PROJEKT_BAZY\\BDproject\\projektBD\\src\\main\\resources\\com\\example\\projektbd\\img\\tank.png");
            FileInputStream def = new FileInputStream(defaultImg);
            byte[] defData = def.readAllBytes();
            statement.setBytes(4, defData);
        }

        statement.setString(5, desc);
        statement.setInt(6, amount);
        statement.setDate(7, sqlDate);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Nowy czołg został dodany do bazy danych.");

            newTankName.clear();
            newTankType.clear();
            newTankDesc.clear();
            newTankAmount.clear();
            newTankDate.setValue(null);
            image.setImage(null);
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

        String query3 = "INSERT INTO museum_tanks (museum_id, tank_id) VALUES (?, ?)";

        PreparedStatement statement3 = connection.prepareStatement(query3);
        statement3.setInt(1, museumId);
        statement3.setInt(2, tankID);
        statement3.executeUpdate();
    }

    public void setTankDataForEdit(int tankId) {
        this.currentTankId = tankId;

        addBut.setVisible(false);
        editBut.setVisible(true);

        try {
            Connection connection = ConnectDB.getConnection();

            String query = "SELECT t.name AS tank_name, a.ammo_id as a_id, type, m.museum_id AS m_id, m.name as m_name, t.description AS tank_desc, amount, data, nation_name, t.nation_id as n_id, a.name AS ammo_name, a.description AS ammo_desc " +
                    "FROM tanks t  " +
                    "JOIN nationality ON t.nation_id = nationality.nation_id " +
                    "JOIN tank_ammunition on t.tank_id = tank_ammunition.tank_id " +
                    "JOIN ammunition a on tank_ammunition.ammo_id = a.ammo_id " +
                    "JOIN museum_tanks on t.tank_id = museum_tanks.tank_id " +
                    "JOIN museum m on museum_tanks.museum_id = m.museum_id " +
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


                newTankNation.getSelectionModel().select(resultSet.getInt("n_id") + ": " + resultSet.getString("nation_name"));
                newTankAmmo.getSelectionModel().select(resultSet.getInt("a_id") + ": " + resultSet.getString("ammo_name"));
                newTankMuseum.getSelectionModel().select(resultSet.getInt("m_id") + ": " + resultSet.getString("m_name"));
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
            String museumSelection = newTankMuseum.getValue();

            isInputEmpty();

            LocalDate dateValue = newTankDate.getValue();
            if (dateValue == null) {
                System.out.println("Date is not selected.");
                return;
            }
            String date = dateValue.toString();

            if (nationSelection == null || ammoSelection == null || museumSelection == null) {
                System.out.println("Nation, Ammo or Museum selection is null.");
                return;
            }

            String[] nationParts = nationSelection.split(":");
            int nationId = Integer.parseInt(nationParts[0].trim());

            String[] ammoParts = ammoSelection.split(":");
            int ammoId = Integer.parseInt(ammoParts[0].trim());

            String[] museumName = museumSelection.split(":");
            int museumId = Integer.parseInt(museumName[0].trim());

            if (!name.isEmpty() && !type.isEmpty() && !desc.isEmpty() && !containsDigits(type)) {
                int amountInt = Integer.parseInt(amount);
                System.out.println("All fields are valid. Updating tank in database...");

                updateTankInDatabase(name, type, desc, nationId, amountInt, date, ammoId, museumId, currentTankId, selectedFile);
            } else {
                System.out.println("Please ensure that all fields are filled correctly.");
                if (containsDigits(type)) {
                    System.out.println("Type contains digits, which is not allowed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }


    private void updateTankInDatabase(String name, String type, String desc, int nationId, int amount, String date, int ammoId, int museumId, int currentID, File imageFile) throws SQLException {
        connection = ConnectDB.getConnection();

        byte[] existingImageData = null;

        // pobieramy obecne zdjecie jesli nie wybralismy zadnego do edycji
        if (imageFile == null) {
            String fetchImageQuery = "SELECT img FROM tanks WHERE tank_id = ?";
            PreparedStatement fetchImageStatement = connection.prepareStatement(fetchImageQuery);
            fetchImageStatement.setInt(1, currentID);
            ResultSet resultSet = fetchImageStatement.executeQuery();

            if (resultSet.next()) {
                existingImageData = resultSet.getBytes("img");
            }
            resultSet.close();
            fetchImageStatement.close();
        }

        String query = "UPDATE tanks SET nation_id=?, name=?, type=?, img=?, description=?, amount=?, data=? WHERE tank_id=?";

        PreparedStatement statement = connection.prepareStatement(query);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        Date sqlDate = Date.valueOf(localDate);

        statement.setInt(1, nationId);
        statement.setString(2, name);
        statement.setString(3, type);

        if (imageFile != null) { // jesli wybralismy nowe zdjecie do edycji
            try (FileInputStream fis = new FileInputStream(imageFile)) {
                byte[] imageData = fis.readAllBytes();
                statement.setBytes(4, imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (existingImageData != null) { // jesli nie to zostawiamy to co było
            statement.setBytes(4, existingImageData);
        } else {
            statement.setNull(4, Types.BLOB);
        }

        statement.setString(5, desc);
        statement.setInt(6, amount);
        statement.setDate(7, sqlDate);
        statement.setInt(8, currentID);

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

        String updateMuseum = "UPDATE museum_tanks SET museum_id=? WHERE tank_id=?";
        PreparedStatement updateMuseumStatement = connection.prepareStatement(updateMuseum);
        updateMuseumStatement.setInt(1, museumId);
        updateMuseumStatement.setInt(2, currentID);
        updateMuseumStatement.executeUpdate();
    }

    @FXML
    private void isInputEmpty() {
        String tankName = newTankName.getText();
        String tankType = newTankType.getText();
        String tankDesc = newTankDesc.getText();
        String tankAmount = newTankAmount.getText();

        String tankNation = newTankNation.getValue();
        String tankAmmo = newTankAmmo.getValue();
        String tankMuseum = newTankMuseum.getValue();
        LocalDate tankDate = newTankDate.getValue();


        if (tankMuseum == null || tankMuseum.isEmpty()) {
            newTankMuseum.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankMuseum).play();
            tankMuseumError.setText("Pole muzuem czołgu nie może być puste!");
        } else {
            newTankMuseum.setStyle(null);
            tankMuseumError.setText("");
        }

        if (tankName.isEmpty()) {
            newTankName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankName).play();
            tankNameError.setText("Pole nazwa czołgu nie może być puste!");
        } else {
            newTankName.setStyle(null);
            tankNameError.setText("");
        }

        if (tankType.isEmpty() || !isLetter(tankType) || containsDigits(tankType)) {
            newTankType.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankType).play();
            tankTypeError.setText("Pole typ czołgu nie może być puste!");
        } else {
            newTankType.setStyle(null);
            tankTypeError.setText("");
        }

        if (tankDesc.isEmpty()) {
            newTankDesc.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankDesc).play();
            tankDescError.setText("Pole opis czołgu nie może być puste!");
        } else {
            newTankDesc.setStyle(null);
            tankDescError.setText("");
        }

        if (tankAmount.isEmpty() || isLetter(tankAmount)) {
            newTankAmount.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankAmount).play();
            tankAmmountError.setText("Pole ilość czołgów nie może być puste!");
        } else {
            newTankAmount.setStyle(null);
            tankAmmountError.setText("");
        }

        if (tankNation == null || tankNation.isEmpty()) {
            newTankNation.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankNation).play();
            tankNationalityError.setText("Pole nacja czołgu nie może być puste!");
        } else {
            newTankNation.setStyle(null);
            tankNationalityError.setText("");
        }

        if (tankAmmo == null || tankAmmo.isEmpty()) {
            newTankAmmo.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankAmmo).play();
            tankAmmoError.setText("Pole amunicja czołgu nie może być puste!");
        } else {
            newTankAmmo.setStyle(null);
            tankAmmoError.setText("");
        }

        if (tankDate == null) {
            newTankDate.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 3 ;");
            new animatefx.animation.Shake(newTankDate).play();
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
