package com.staymaster.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;

import com.staymaster.config.NavigationManager;
import com.staymaster.dao.UserDao;
import com.staymaster.hibernate.SessionManager;
import com.staymaster.models.User;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AdminPortalController {

	@FXML
	private TextField firstNameTxt;

	@FXML
	private TextField lastNameTxt;

	@FXML
	private TextField addressTxt;

	@FXML
	private TextField dobTxt;

	@FXML
	private ComboBox<String> genderComboBox;

	@FXML
	private TextField phoneNumberTxt;

	@FXML
	private TextField emailTxt;

	@FXML
	private TextField passwordTxt;

	@FXML
	private TextField confirmPasswordTxt;

	@FXML
	public void handleAddUser(ActionEvent event) throws IOException {
		try {

			NavigationManager.navigateTo("/com/staymaster/views/AdminUserAdd.fxml", "Admin Page");
			
		} catch (Exception e) {
			e.printStackTrace();
			// Show an error message if loading the admin dashboard fails
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to load admin dashboard");
			alert.setContentText("An error occurred while loading the admin dashboard. Please try again.");
			alert.showAndWait();
		}
	}

	@FXML
	public void initialize() {
		System.out.println("initialize() called");
		if (genderComboBox != null) {
			genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female"));
		}
	}

	@FXML
	public void handleAddHotel(ActionEvent event) throws IOException {
		try {
			NavigationManager.navigateTo("/com/staymaster/views/AdminHotelDetails.fxml", "Admin Hotel Dashboard");
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to load admin dashboard");
			alert.setContentText("An error occurred while loading the admin dashboard. Please try again.");
			alert.showAndWait();
		}
	}

	@FXML
	public void handleAddUserToDb(ActionEvent event) throws IOException {

		if (!validateFields()) {
			showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields correctly.");
			return;
		}

		SessionFactory sessionFactory = SessionManager.getSessionFactory();
		String firstName = firstNameTxt.getText();
		String lastName = lastNameTxt.getText();
		String address = addressTxt.getText();
		String phoneNumber = phoneNumberTxt.getText();
		String dateOfBirth = dobTxt.getText();
		String gender = genderComboBox.getValue();
		String email = emailTxt.getText();
		String password = passwordTxt.getText();

		java.sql.Date dob = convertToSqlDate(dateOfBirth);
		// Hash the password using BCrypt
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setAddress(address);
		user.setPhoneNo(phoneNumber);
		user.setGender(gender);
		user.setEmail(email);
		user.setPassword(password);
		user.setPassword(hashedPassword);
		user.setDateOfBirth(dob);

		// Save user to the database

		UserDao userDao = new UserDao(sessionFactory);
		userDao.save(user);
		System.out.println("Successfully added to Database");

		showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully!");
	}

	@FXML
	private void handleBackButton(ActionEvent event) throws IOException {
		// Add your event handling logic here
		System.out.println("Back button clicked!");
		NavigationManager.goBack();

	}

	public static java.sql.Date convertToSqlDate(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		try {
			java.util.Date utilDate = dateFormat.parse(dateString);
			return new java.sql.Date(utilDate.getTime());
		} catch (ParseException e) {
			// Handle the case when the date string is in an invalid format
			e.printStackTrace();
			return null;
		}
	}

	@FXML
	private void handleBackButtonToLogin(ActionEvent event) throws IOException {
		// Add your event handling logic here
		Parent adminPortal = FXMLLoader.load(getClass().getResource("/com/mainview/Login.fxml"));
		Scene adminPortalScene = new Scene(adminPortal);

		// Get the current stage and set the new scene
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(adminPortalScene);
		stage.show();
	}

	// validation for registration fields entered

	private boolean validateFields() {
		return validateFirstName() && validateLastName() && validateAddress() && validatePhoneNumber()
				&& validateDateOfBirth() && validateGender() && validateEmail() && validatePassword()
				&& validateConfirmPassword();
	}

	private boolean validateFirstName() {
		String firstName = firstNameTxt.getText();

		// Check if the first name is empty
		if (firstName.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter your first name.");
			return false;
		}

		// Check if the first name contains only letters
		if (!firstName.matches("[a-zA-Z]+")) {
			showAlert(Alert.AlertType.ERROR, "Validation Error", "First name should only contain letters.");
			return false;
		}

		return true;
	}

	private boolean validateLastName() {
		String lastName = lastNameTxt.getText();
		if (lastName.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter your last name.");
			return false;
		}

		// Check if the first name contains only letters
		if (!lastName.matches("[a-zA-Z]+")) {
			showAlert(Alert.AlertType.ERROR, "Validation Error", "Last name should only contain letters.");
			return false;
		}
		return true;
	}

	private boolean validateAddress() {
		String address = addressTxt.getText();
		if (address.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter the address.");
			return false;
		}
		return true;
	}

	private boolean validatePhoneNumber() {
		String phoneNumber = phoneNumberTxt.getText();
		if (!Pattern.matches("\\d{10}", phoneNumber)) {
			showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid phone number.");
			return false;
		}
		return true;
	}

	private boolean validateDateOfBirth() {
		String dob = dobTxt.getText();

		// Validate date of birth format using regex
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false);
			Date dobDate = sdf.parse(dob);

			// Get today's date and calculate the maximum valid date (100 years ago)
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -100);
			Date maxValidDate = cal.getTime();

			// Check if the entered date is in the future or more than 100 years ago
			if (dobDate.after(new Date())) {
				showAlert(Alert.AlertType.ERROR, "Validation Error", "Date of birth cannot be in the future.");
				return false;
			} else if (dobDate.before(maxValidDate)) {
				showAlert(Alert.AlertType.ERROR, "Validation Error",
						"Date of birth must be within the last 100 years.");
				return false;
			}

			return true;
		} catch (ParseException e) {
			showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid date of birth (YYYY-MM-DD).");
			return false;
		}
	}

	private boolean validateGender() {
		String gender = genderComboBox.getValue();
		return true;
	}

	private boolean validateEmail() {
		String email = emailTxt.getText();
		if (!Pattern.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", email)) {
			showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid email address.");
			return false;
		}
		return true;
	}

	private boolean validatePassword() {
		String password = passwordTxt.getText();
		if (password.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a password.");
			return false;
		}
		return true;
	}

	private boolean validateConfirmPassword() {
		String password = passwordTxt.getText();
		String confirmPassword = confirmPasswordTxt.getText();
		if (!password.equals(confirmPassword)) {
			showAlert(Alert.AlertType.ERROR, "Validation Error", "Passwords do not match.");
			return false;
		}
		return true;
	}

	private void showAlert(Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

}
