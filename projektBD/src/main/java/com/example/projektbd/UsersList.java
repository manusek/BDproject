package com.example.projektbd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
}
