package com.example.projektbd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersList {

    @FXML
    private TextField searchUser;
    @FXML
    private TableColumn<listOfUsers, Integer> userId;
    @FXML
    private TableColumn<listOfUsers, String> userLogin;
    @FXML
    private TableColumn<listOfUsers, String> userEmail;
    @FXML
    private TableColumn<listOfUsers, String> userPass;
    @FXML
    private TableColumn<listOfUsers, String> userAcc;
    @FXML
    private TableView<listOfUsers> usersTable;
    ObservableList<listOfUsers> dataList;
    ObservableList<listOfUsers> usersList = FXCollections.observableArrayList();

    @FXML
    private void initialize() throws SQLException {
        loadDate();
        search_user();
        enableEditing();
    }

    private void enableEditing() {
        usersTable.setEditable(true);

        userLogin.setCellFactory(TextFieldTableCell.forTableColumn());
        userEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        userPass.setCellFactory(TextFieldTableCell.forTableColumn());
        userAcc.setCellFactory(TextFieldTableCell.forTableColumn());

        userLogin.setOnEditCommit(event -> updateUserField(event, "user_login", event.getNewValue()));
        userEmail.setOnEditCommit(event -> updateUserField(event, "user_email", event.getNewValue()));
        userPass.setOnEditCommit(event -> updateUserField(event, "user_pass", event.getNewValue()));
        userAcc.setOnEditCommit(event -> updateUserField(event, "acc_type", event.getNewValue()));
    }

    private void loadDate() throws SQLException {
        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userLogin.setCellValueFactory(new PropertyValueFactory<>("userLogin"));
        userEmail.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        userPass.setCellValueFactory(new PropertyValueFactory<>("userPass"));
        userAcc.setCellValueFactory(new PropertyValueFactory<>("userAcc"));

        Connection connection = ConnectDB.getConnection();

        String query = "SELECT * FROM users";
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()){
            usersList.add(new listOfUsers(
                    resultSet.getInt("user_id"),
                    resultSet.getString("user_login"),
                    resultSet.getString("user_email"),
                    resultSet.getString("user_pass"),
                    resultSet.getString("acc_type"))
            );
        }
        usersTable.setItems(usersList);
    }

    public static ObservableList<listOfUsers> getUsers() {
        ObservableList<listOfUsers> lista = FXCollections.observableArrayList();

        try (Connection connection = ConnectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                lista.add(new listOfUsers(
                        rs.getInt("user_id"),
                        rs.getString("user_login"),
                        rs.getString("user_email"),
                        rs.getString("user_pass"),
                        rs.getString("acc_type")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @FXML
    void search_user() throws SQLException {
        dataList = getUsers();

        FilteredList<listOfUsers> filteredData = new FilteredList<>(dataList, b -> true);
        searchUser.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((listOfUsers user) -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(user.getUserId()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (user.getUserLogin().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (user.getUserEmail().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (user.getUserPass().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (user.getUserAcc().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false;
            });
        });

        SortedList<listOfUsers> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(usersTable.comparatorProperty());
        usersTable.setItems(sortedData);
    }

    private void updateUserField(TableColumn.CellEditEvent<listOfUsers, ?> event, String fieldName, Object newValue) {
        listOfUsers user = event.getRowValue();
        String query = "UPDATE users SET " + fieldName + " = ? WHERE user_id = ?";

        // Validate the account type if the field being updated is acc_type
        if (fieldName.equals("acc_type")) {
            String newAccType = (String) newValue;
            if (!newAccType.equals("user") && !newAccType.equals("admin")) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Niepoprawny typ konta: " + newAccType + ". Typ konta musi być 'user' albo 'admin'.");
                return;
            }
        }

        try (Connection connection = ConnectDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Check if the new value already exists in the column
            if (isValueExistsInColumn(fieldName, newValue)) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Wartość '" + newValue + "' już istnieje w tabeli.");
                return;
            }

            stmt.setObject(1, newValue);
            stmt.setInt(2, user.getUserId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                // Update the value in the observable list
                switch (fieldName) {
                    case "user_login":
                        user.setUserLogin((String) newValue);
                        break;
                    case "user_email":
                        user.setUserEmail((String) newValue);
                        break;
                    case "user_pass":
                        user.setUserPass((String) newValue);
                        break;
                    case "acc_type":
                        user.setUserAcc((String) newValue);
                        break;
                }
                usersTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Dane użytkownika zostały zaktualizowane.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Aktualizacja danych użytkownika nie powiodła się.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił błąd podczas aktualizacji danych.");
        }
    }

    private boolean isValueExistsInColumn(String columnName, Object value) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE " + columnName + " = ?";
        try (Connection connection = ConnectDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, value);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
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
