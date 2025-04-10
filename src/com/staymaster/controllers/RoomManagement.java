package com.staymaster.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;

import com.staymaster.config.HotelContext;
import com.staymaster.config.NavigationManager;
import com.staymaster.dao.HotelDao;
import com.staymaster.dao.RoomDao;
import com.staymaster.hibernate.SessionManager;
import com.staymaster.models.Hotel;
import com.staymaster.models.Room;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;

public class RoomManagement {

    @FXML
    private TableView<Room> roomTbl;

    @FXML
    private ComboBox<String> roomTypeCombo;

    @FXML
    private TextField priceTxt;

    @FXML
    private TextField capacityTxt;

    @FXML
    private ComboBox<String> roomStatusCombo;
    
    @FXML
    private ComboBox<Hotel> hotelComboBox;


    @FXML
    public void initialize() {
        roomStatusCombo.setItems(FXCollections.observableArrayList("Available", "Not Available"));
        roomTypeCombo.setItems(FXCollections.observableArrayList("Deluxe", "Executive"));
        setupRoomTable();
        System.out.println("Inside initialize");
        loadRoomData();
        loadHotelsIntoComboBox(); // Load hotels into ComboBox
    }

    private void setupRoomTable() {
        // Room ID
        TableColumn<Room, Integer> roomIdCol = new TableColumn<>("Room ID");
        roomIdCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));

        // Room Type
        TableColumn<Room, String> roomTypeCol = new TableColumn<>("Room Type");
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<>("roomType"));

        // Hotel Name
        TableColumn<Room, String> hotelNameCol = new TableColumn<>("Hotel");
        hotelNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHotel() != null ? cellData.getValue().getHotel().getName() : ""));

        // Price
        TableColumn<Room, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Capacity
        TableColumn<Room, Integer> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        // Room Status
        TableColumn<Room, String> roomStatusCol = new TableColumn<>("Room Status");
        roomStatusCol.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));

        // Add columns to the table
        roomTbl.getColumns().addAll(roomIdCol, roomTypeCol, hotelNameCol, priceCol, capacityCol, roomStatusCol);
    }

    @FXML
    public void handleAddBtn(ActionEvent event) throws IOException {
    	String roomType = roomTypeCombo.getValue();
        String roomStatus = roomStatusCombo.getValue();
        String capacityStr = capacityTxt.getText();
        String priceStr = priceTxt.getText();

        if (!isValidate(roomType, roomStatus, capacityStr, priceStr)) {
            showErrorMessage("All fields are required and must contain valid values.");
            return;
        }

        try {
            int capacity = Integer.parseInt(capacityStr);
            double price = Double.parseDouble(priceStr);

            SessionFactory sessionFactory = SessionManager.getSessionFactory();
            Room room = new Room();
            RoomDao roomDao = new RoomDao(sessionFactory);
            Hotel hotel = hotelComboBox.getSelectionModel().getSelectedItem();

            room.setRoomStatus(roomStatus);
            room.setCapacity(capacity);
            room.setRoomType(roomType);
            room.setPrice(price);
            room.setHotel(hotel);

            if (hotel == null) {
                showErrorMessage("Please select a hotel from the dropdown.");
                return;
            }

            roomDao.save(room);
            showConfirmationMessage("Room details added to the database.");
            clearFields();
            loadRoomData();
        } catch (NumberFormatException e) {
            showErrorMessage("Capacity and Price must be valid numeric values.");
        }
    }

    @FXML
    public void handleUpdateBtn(ActionEvent event) throws IOException {
        // Implement update logic here
    }

    @FXML
    public void handleBackBtn(ActionEvent event) throws IOException {
        NavigationManager.goBack();
    }

    private void loadRoomData() {
        System.out.println("Loading room data from the database");
        SessionFactory sessionFactory = SessionManager.getSessionFactory();
        RoomDao roomDao = new RoomDao(sessionFactory);

        List<Room> rooms = roomDao.findAll();
        ObservableList<Room> observableRooms = FXCollections.observableArrayList(rooms);
        roomTbl.setItems(observableRooms);
    }
    
    private void loadHotelsIntoComboBox() {
        SessionFactory sessionFactory = SessionManager.getSessionFactory();
        HotelDao hotelDao = new HotelDao(sessionFactory);
        List<Hotel> hotels = hotelDao.findHotel(); // Ensure HotelDao has this method
        ObservableList<Hotel> hotelList = FXCollections.observableArrayList(hotels);
        hotelComboBox.setItems(hotelList);
    }


    private boolean isValidate(String roomType, String roomStatus, String capacity, String price) {
        return !(roomType.isEmpty() || roomStatus.isEmpty() || capacity.isEmpty() || price.isEmpty());
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmationMessage(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        roomStatusCombo.getSelectionModel().clearSelection();
        roomTypeCombo.getSelectionModel().clearSelection();
        priceTxt.clear();
        capacityTxt.clear();
    }
}
