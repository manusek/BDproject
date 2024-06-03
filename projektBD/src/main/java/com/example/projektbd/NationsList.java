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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NationsList {

    @FXML
    private TextField searchNation;
    @FXML
    private TableColumn<listOfNations, Integer> nationId;
    @FXML
    private TableColumn<listOfNations, String> nationName;
    @FXML
    private TableColumn<listOfNations, String> nationProd;
    @FXML
    private TableView<listOfNations> nationsTable;
    ObservableList<listOfNations> dataList;
    ObservableList<listOfNations> nationsList = FXCollections.observableArrayList();


    @FXML
    private void initialize() throws SQLException {
        loadDate();
        search_nation();
        enableEditing();
    }

    private void enableEditing() {
        nationsTable.setEditable(true);

        nationName.setCellFactory(TextFieldTableCell.forTableColumn());
        nationProd.setCellFactory(TextFieldTableCell.forTableColumn());

        nationName.setOnEditCommit(event -> updateNationField(event, "nation_name", event.getNewValue()));
        nationProd.setOnEditCommit(event -> updateNationField(event, "prod_place", event.getNewValue()));
}

    private void loadDate() throws SQLException {
        nationId.setCellValueFactory(new PropertyValueFactory<>("nationId"));
        nationName.setCellValueFactory(new PropertyValueFactory<>("nationName"));
        nationProd.setCellValueFactory(new PropertyValueFactory<>("nationProd"));

        Connection connection = ConnectDB.getConnection();

        String query = "SELECT * FROM nationality";
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()){
            nationsList.add(new listOfNations(
                    resultSet.getInt("nation_id"),
                    resultSet.getString("nation_name"),
                    resultSet.getString("prod_place"))
            );
        }
        nationsTable.setItems(nationsList);
    }

    public static ObservableList<listOfNations> getNations() {
        ObservableList<listOfNations> lista = FXCollections.observableArrayList();

        try (Connection connection = ConnectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM nationality");
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                lista.add(new listOfNations(
                        rs.getInt("nation_id"),
                        rs.getString("nation_name"),
                        rs.getString("prod_place")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @FXML
    void search_nation() throws SQLException {
        dataList = getNations();

        FilteredList<listOfNations> filteredData = new FilteredList<>(dataList, b -> true);
        searchNation.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((listOfNations nation) -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(nation.getNationId()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (nation.getNationName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (nation.getNationProd().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false;
            });
        });

        SortedList<listOfNations> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(nationsTable.comparatorProperty());
        nationsTable.setItems(sortedData);
    }

    private void updateNationField(TableColumn.CellEditEvent<listOfNations, ?> event, String fieldName, Object newValue) {
        listOfNations nation = event.getRowValue();
        String query = "UPDATE nationality SET " + fieldName + " = ? WHERE nation_id = ?";

        try (Connection connection = ConnectDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setObject(1, newValue);
            stmt.setInt(2, nation.getNationId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                switch (fieldName) {
                    case "nation_name":
                        nation.setNationName((String) newValue);
                        break;
                    case "prod_place":
                        nation.setNationProd((String) newValue);
                        break;
                }
                nationsTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Dane nacji zostały zaktualizowane.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Aktualizacja danych nacji nie powiodła się.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił błąd podczas aktualizacji danych.");
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
