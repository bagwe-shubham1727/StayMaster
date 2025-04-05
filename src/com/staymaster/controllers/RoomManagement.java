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
import javafx.scene.control.cell.PropertyValueFactory;

public class RoomManagement {

    @FXML
    private TableView<Room> roomTbl;

    @FXML
    private TextField roomTypeTxt;

    @FXML
    private TextField priceTxt;

    @FXML
    private TextField capacityTxt;

    @FXML
    private TextField roomStatusTxt;

    @FXML
    public void initialize() {
        setupRoomTable();
        System.out.println("Inside initialize");
        loadRoomData();
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
        String roomType = roomTypeTxt.getText();
        String roomStatus = roomStatusTxt.getText();
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
            Hotel hotel = HotelContext.getCurrentHotel();

            room.setRoomStatus(roomStatus);
            room.setCapacity(capacity);
            room.setRoomType(roomType);
            room.setPrice(price);

            if (hotel != null) {
                room.setHotel(hotel);

                if (hotel.getRooms() == null) {
                    hotel.setRooms(new ArrayList<>());
                }

                hotel.getRooms().add(room);
            } else {
                HotelDao hotelDao = new HotelDao(sessionFactory);
                hotel = hotelDao.findByName("sample");
                room.setHotel(hotel);
                if (hotel.getRooms() == null) {
                    hotel.setRooms(new ArrayList<>());
                }

                hotel.getRooms().add(room);
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
        roomStatusTxt.clear();
        roomTypeTxt.clear();
        priceTxt.clear();
        capacityTxt.clear();
    }
}
