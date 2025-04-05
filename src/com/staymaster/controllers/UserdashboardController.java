package com.staymaster.controllers;

import java.io.IOException;

import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;

import com.staymaster.config.NavigationManager;
import com.staymaster.config.SessionHandler;
import com.staymaster.config.UserSession;
import com.staymaster.config.UserSessionManager;
import com.staymaster.hibernate.SessionManager;
import com.staymaster.models.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class UserdashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Label username;

    @FXML
    private TextField fieldFirstName;

    @FXML
    private TextField fieldLastName;

    @FXML 
    private Button bookingHistory;

    @FXML
    private TextField fieldPhone;

    @FXML
    private TextField fieldEmail;

    @FXML
    private TextField fieldAddress;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private Button buttonBookRoom;

    @FXML
    private PasswordField fieldConfirmPassword;

    private User user;

    // Method to initialize the user dashboard view
    @FXML
    public void initialize() {
        // Get the current session ID
        String sessionId = SessionHandler.getCurrentSessionId();

        // Check if session ID is not null
        if (sessionId != null) {
            // Retrieve the user session using the session ID
            UserSessionManager userSessionManager = new UserSessionManager();
            UserSession userSession = userSessionManager.getSession(sessionId);

            // Get the user object from the user session
            user = userSession.getUser();

            // Check if username label is properly injected
            if (username != null) {
                // Display a welcome message with the user's name
                welcomeLabel.setText("Welcome, " + " " + user.getFirstName() + "!");
                username.setText(user.getFirstName() + " " + user.getLastName());
            } else {
                System.err.println("Error: 'username' Label is null!");
            }

            // Display user details in text fields
            displayUserDetails();
        } else {
            // If session ID is null, display a generic welcome message
            welcomeLabel.setText("Welcome!");
        }
    }

    private void displayUserDetails() {
        if (user != null) {
            fieldFirstName.setText(user.getFirstName());
            fieldLastName.setText(user.getLastName());
            fieldPhone.setText(user.getPhoneNo());
            fieldEmail.setText(user.getEmail());
            fieldAddress.setText(user.getAddress());
        }
    }

    @FXML
    private void buttonBookRoom() throws IOException {
        NavigationManager.navigateTo("/com/stayease/views/BookRoom.fxml", "Book Room");
    }

    @FXML
    private void buttonSignOut() {
        try {
            SessionHandler.setCurrentSessionId(null);
            SessionHandler.clearSession();
            NavigationManager.navigateTo("/com/mainview/Login.fxml", "Login");

            System.out.println("User logged out");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBookingHistory(ActionEvent event) throws IOException {
        try {
            NavigationManager.navigateTo("/com/stayease/views/BookingHistory.fxml", "Booking History");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buttonUpdate() {
        // Retrieve the current session ID
        String sessionId = SessionHandler.getCurrentSessionId();
        String newPassword = fieldPassword.getText();
        String confirmPassword = fieldConfirmPassword.getText();

        // Validate that all fields are not empty
        if (fieldFirstName.getText().isEmpty() || fieldLastName.getText().isEmpty() || fieldPhone.getText().isEmpty()
                || fieldEmail.getText().isEmpty() || fieldAddress.getText().isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("All fields are required.");
            return;
        }

        // Check if the new password matches the confirm password
        if (!newPassword.equals(confirmPassword)) {
            showAlert("Passwords do not match!");
            return; // Exit the method if passwords do not match
        }

        if (sessionId != null) {
            // Retrieve the user session using the session ID
            UserSession userSession = UserSessionManager.getSession(sessionId);

            // Get the user object from the user session
            User user = userSession.getUser();

            // Update user details with the new values from text fields
            if (user != null) {
                user.setFirstName(fieldFirstName.getText());
                user.setLastName(fieldLastName.getText());
                user.setPhoneNo(fieldPhone.getText());
                user.setEmail(fieldEmail.getText());
                user.setAddress(fieldAddress.getText());

                // Hash the new password before saving
                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                user.setPassword(hashedPassword);

                // Save the updated user details to the database or other data source
                boolean success = updateUserDetails(user);
                if (success) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("User details updated successfully!");
                    alert.showAndWait();
                    System.out.println("User details updated successfully!");
                } else {
                    showAlert("Failed to update user details.");
                }
            }
        } else {
            showAlert("No active session found.");
        }
    }

    @SuppressWarnings("deprecation")
    private boolean updateUserDetails(User user) {
        // Get the current Hibernate session
        Session session = SessionManager.openSession();

        try {
            // Begin a transaction
            session.beginTransaction();

            // Update the user in the database
            session.update(user);

            // Commit the transaction
            session.getTransaction().commit();

            return true; // Update successful
        } catch (Exception e) {
            // Rollback the transaction if an exception occurs
            session.getTransaction().rollback();
            e.printStackTrace();
            return false; // Update failed
        } finally {
            // Close the session
            SessionManager.closeSession();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
