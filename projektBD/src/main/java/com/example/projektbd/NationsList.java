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

}
