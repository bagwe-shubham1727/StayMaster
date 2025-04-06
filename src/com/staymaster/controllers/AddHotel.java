package com.staymaster.controllers;

import java.io.IOException;

import org.hibernate.SessionFactory;

import com.staymaster.config.HotelContext;
import com.staymaster.config.NavigationManager;
import com.staymaster.dao.HotelDao;
import com.staymaster.hibernate.SessionManager;
import com.staymaster.models.Hotel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class AddHotel {

    @FXML
    private TextField txtHotelName;
    @FXML
    private TextField streetTxt;
    @FXML
    private TextField cityTxt;
    @FXML
    private TextField stateTxt;
    @FXML
    private TextField countryTxt;
    @FXML
    private TextField zipCodeTxt;
    @FXML
    private TextField totalRoomsTxt;
    @FXML
    private TextField availableRoomTxt;
    @FXML
    private TextField descriptionTxt;
    @FXML
    private TextField amenityTxt;

    @FXML
    public void handleAddHotelBtn(ActionEvent event) throws IOException {
        try {
            // Fetch input data
            String hotelName = txtHotelName.getText();
            String street = streetTxt.getText();
            String city = cityTxt.getText();
            String state = stateTxt.getText();
            String country = countryTxt.getText();
            String zipCodeInput = zipCodeTxt.getText();
            String totalRoomsInput = totalRoomsTxt.getText();
            String availableRoomsInput = availableRoomTxt.getText();
            String description = descriptionTxt.getText();
            String amenities = amenityTxt.getText();

            // Validation
            if (hotelName.isEmpty() || street.isEmpty() || city.isEmpty() || state.isEmpty()
                    || country.isEmpty() || zipCodeInput.isEmpty() || totalRoomsInput.isEmpty()
                    || availableRoomsInput.isEmpty() || description.isEmpty() || amenities.isEmpty()) {
                showErrorAlert("Validation Error", "All fields must be filled out.");
                return;
            }

            // Parse numeric values
            Long zipcode = Long.parseLong(zipCodeInput);
            int totalRooms = Integer.parseInt(totalRoomsInput);
            int availableRooms = Integer.parseInt(availableRoomsInput);

            // Validate numeric fields
            if (zipcode <= 0) {
                showErrorAlert("Validation Error", "Zip Code must be a positive number.");
                return;
            }
            if (totalRooms <= 0) {
                showErrorAlert("Validation Error", "Total Rooms must be greater than 0.");
                return;
            }
            if (availableRooms < 0 || availableRooms > totalRooms) {
                showErrorAlert("Validation Error", "Available Rooms must be between 0 and Total Rooms.");
                return;
            }

            // Proceed with hotel creation
            Hotel hotel = new Hotel();
            hotel.setName(hotelName);
            hotel.setStreet(street);
            hotel.setCity(city);
            hotel.setState(state);
            hotel.setCountry(country);
            hotel.setZipCode(zipcode);
            hotel.setTotalRooms(totalRooms);
            hotel.setAvailableRooms(availableRooms);
            hotel.setDescription(description);

            // Save hotel to database
            SessionFactory sessionFactory = SessionManager.getSessionFactory();
            HotelDao hotelDao = new HotelDao(sessionFactory);
            hotelDao.save(hotel);
            HotelContext.setCurrentHotel(hotel);

            // Show success message
            showConfirmationAlert("Success", "Hotel details have been added successfully.");

            // Clear the fields
            clearFields();

        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Input", "Please ensure numeric fields (Zip Code, Total Rooms, Available Rooms) contain valid numbers.");
        } catch (Exception e) {
            showErrorAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateBtn(ActionEvent event) throws IOException {
        // Similar logic to handleAddHotelBtn but updates an existing hotel
    }

    @FXML
    public void handleBackBtn(ActionEvent event) throws IOException {
        NavigationManager.goBack();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtHotelName.clear();
        streetTxt.clear();
        cityTxt.clear();
        stateTxt.clear();
        countryTxt.clear();
        zipCodeTxt.clear();
        totalRoomsTxt.clear();
        availableRoomTxt.clear();
        descriptionTxt.clear();
        amenityTxt.clear();
    }
}
