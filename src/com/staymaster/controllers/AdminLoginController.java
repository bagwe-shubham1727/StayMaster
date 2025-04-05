package com.staymaster.controllers;

import java.io.IOException;

import com.staymaster.config.NavigationManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController {

	@FXML
	private Label errorID;

	@FXML
	private Label errorPassword;
    @FXML
    private TextField txtAdminname;

    @FXML
    private PasswordField txtAdminPassword;

    @FXML
    void Login(ActionEvent event) {
		String username = txtAdminname.getText();
		String password = txtAdminPassword.getText();
		
		if(username.equals("admin") && password.equals("Admin@1234")) {
			try {

				NavigationManager.navigateTo("/com/stayease/views/AdminPortal.fxml", "Admin Page");
			} catch (Exception e) {
			    e.printStackTrace();
			    Alert alert = new Alert(Alert.AlertType.ERROR);
			    alert.setTitle("Error");
			    alert.setHeaderText("Failed to load admin dashboard");
			    alert.setContentText("An error occurred while loading the admin dashboard. Please try again.");
			    alert.showAndWait();
			}
		} else {
		    // Show an error message for invalid login credentials
		    errorID.setText("Invalid username or password");
		    errorPassword.setText("Invalid username or password");
		}
    }

    @FXML
	private void NavUserLogin(ActionEvent event) {
	    try {
	        // Load the Admin Login FXML file
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mainview/Login.fxml"));
	        Parent adminLoginParent = loader.load();

	        // Get the scene from the current stage
	        Scene adminLoginScene = new Scene(adminLoginParent);
	        
	        // Get the current stage
	        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        
	        // Set the scene on the stage
	        currentStage.setScene(adminLoginScene);
	        currentStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
    
    
}
