package com.staymaster.controllers;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;

import com.staymaster.config.NavigationManager;
import com.staymaster.config.SessionHandler;
import com.staymaster.config.UserSession;
import com.staymaster.config.UserSessionManager;
import com.staymaster.dao.BookingDao;
import com.staymaster.dao.RoomDao;
import com.staymaster.hibernate.SessionManager;
import com.staymaster.models.Booking;
import com.staymaster.models.Room;
import com.staymaster.models.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class BookRoomController {
	
	boolean ableToBook = true;

	@FXML
	private TableView<Room> roomTbl;

	@FXML
	private ComboBox<String> comboRoomType;

	@FXML
	private Button buttonReset;

	@FXML
	private Button buttonBook;

	@FXML
	private DatePicker checkInDatePicker;

	@FXML
	private DatePicker checkOutDatePicker;

	private ObservableList<Room> originalRooms;

	@FXML
	private void initialize() {
		comboRoomType.getItems().addAll("All", "Deluxe", "Executive");

		comboRoomType.getSelectionModel().selectFirst();

		comboRoomType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				if (newValue.equals("All")) {
					loadRoomData(); // Reload all rooms
				} else {
					filterRoomsByType(newValue);
				}
			}
		});
		
		//checkInDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> loadRoomData());
		checkOutDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> loadRoomData());


		buttonBook.setOnAction(event -> {
			bookRoom();
		});

		setupRoomTable();
		//loadRoomData();
		
		
	}

	@FXML
	private void buttonReset() {
		// Reset combo box selection to "All"
		comboRoomType.getSelectionModel().selectFirst();

		// Reset check-in and check-out dates
		checkInDatePicker.setValue(null);
		checkOutDatePicker.setValue(null);

		// Reload all rooms
		loadRoomData();
	}

	private void filterRoomsByType(String roomType) {
		ObservableList<Room> allRooms = originalRooms != null ? originalRooms : roomTbl.getItems();
		ObservableList<Room> filteredRooms = FXCollections.observableArrayList();

		for (Room room : allRooms) {
			if (roomType.equals("All") || room.getRoomType().equalsIgnoreCase(roomType)) {
				filteredRooms.add(room);
			}
		}
		roomTbl.setItems(filteredRooms);
	}

	private void resetTable() {
		if (originalRooms != null) {
			roomTbl.setItems(originalRooms);
			originalRooms = null;
		}
	}

	@FXML
	private void buttonBack() throws IOException {
		NavigationManager.navigateTo("/com/staymaster/views/UserDashboard.fxml", "User Dashboard");
	}

	@SuppressWarnings("unchecked")
	private void setupRoomTable() {
		TableColumn<Room, Integer> roomIdCol = new TableColumn<>("Room ID");
		TableColumn<Room, String> roomTypeCol = new TableColumn<>("Room Type");
		TableColumn<Room, String> hotelNameCol = new TableColumn<>("Hotel Name");
		TableColumn<Room, Double> priceCol = new TableColumn<>("Price");
		TableColumn<Room, Integer> capacityCol = new TableColumn<>("Capacity");
		TableColumn<Room, String> roomStatusCol = new TableColumn<>("Room Status");

		roomIdCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));
		roomTypeCol.setCellValueFactory(new PropertyValueFactory<>("roomType"));
		hotelNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getHotel() != null ? cellData.getValue().getHotel().getName() : ""));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
		roomStatusCol.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));

		roomTbl.getColumns().addAll(roomIdCol, roomTypeCol, hotelNameCol, priceCol, capacityCol, roomStatusCol);

	}

	private void loadRoomData() {
	    try {
	        SessionFactory sessionFactory = SessionManager.getSessionFactory();
	        RoomDao roomDao = new RoomDao(sessionFactory);

	        LocalDate checkIn = checkInDatePicker.getValue();
	        LocalDate checkOut = checkOutDatePicker.getValue();
	        String selectedRoomType = comboRoomType.getSelectionModel().getSelectedItem();

	        List<Room> rooms;

	        if (checkIn != null && checkOut != null && !checkOut.isBefore(checkIn)) {
	            rooms = roomDao.findAvailableRooms(Date.valueOf(checkIn), Date.valueOf(checkOut), selectedRoomType);
	        } else {
	        	showAlert(AlertType.INFORMATION, "No Rooms", "No available rooms found for the selected criteria. Showing all rooms we have");
	            rooms = roomDao.findAll(); // fallback if no valid date
	            if (!selectedRoomType.equalsIgnoreCase("All")) {
	                rooms.removeIf(room -> !room.getRoomType().equalsIgnoreCase(selectedRoomType));
	            }
	            ableToBook = false;
	        }

	        originalRooms = FXCollections.observableArrayList(rooms);
	        roomTbl.setItems(FXCollections.observableArrayList(rooms));
	    } catch (Exception e) {
	        e.printStackTrace();
	        showAlert(AlertType.ERROR, "Error", "Failed to load room data.");
	    }
	}



	private void showAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void bookRoom() {
		Room selectedRoom = roomTbl.getSelectionModel().getSelectedItem();
		LocalDate checkInDate = checkInDatePicker.getValue();
		LocalDate checkOutDate = checkOutDatePicker.getValue();
		LocalDate currentDate = LocalDate.now();

		if (selectedRoom == null || checkInDate == null || checkOutDate == null) {
			showAlert(AlertType.WARNING, "Warning", "Please select a room and specify check-in and check-out dates.");
			return;
		}

		if (checkInDate.isBefore(currentDate)) {
			showAlert(AlertType.WARNING, "Warning", "Check-in date cannot be before today.");
			return;
		}

		if (checkOutDate.isBefore(checkInDate)) {
			showAlert(AlertType.WARNING, "Warning", "Check-out date cannot be before the check-in date.");
			return;
		}
		
		if (selectedRoom.getRoomStatus().equalsIgnoreCase("Not Available")) {
			showAlert(AlertType.WARNING, "Warning", "This room is currently not available for booking.");
			return;
		}

		if (!ableToBook) {
			showAlert(AlertType.WARNING, "Warning", "Please correct criteria");
			return;
		}

		try {
			// Retrieve the current user
			User currentUser = getCurrentUser();

			if (currentUser == null) {
				showAlert(AlertType.ERROR, "Error", "User not found.");
				return;
			}

			SessionFactory sessionFactory = SessionManager.getSessionFactory();
			BookingDao bookingDao = new BookingDao(sessionFactory);
			RoomDao roomDao = new RoomDao(sessionFactory);

			// Create a new Booking instance
			Booking booking = new Booking();
			booking.setUser(currentUser);
			booking.setRoom(selectedRoom);
			booking.setCheckInDate(Date.valueOf(checkInDate));
			booking.setCheckOutDate(Date.valueOf(checkOutDate));
			//booking.setStatus("Confirmed");
			booking.setPriority(1);
			

			bookingDao.update(booking);

			selectedRoom.setRoomStatus("Booked");
			roomDao.update(selectedRoom);

			showAlert(AlertType.INFORMATION, "Success", "Room booked successfully.");
			
			sendEmail(currentUser.getEmail(), currentUser.getFirstName(), booking.getCheckInDate(), booking.getCheckOutDate(), selectedRoom.getHotel().getName(), selectedRoom.getRoomType());
			// Refresh table data
			loadRoomData();
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error", "Failed to book room.");
		}
	}
	
	//Mail trigger
	private void sendEmail(String email, String user, Date startDate, Date endDate, String hotelName, String roomType) {
		//Email Setup
		final String password = "ibsremjnquvvkapq";
		String fromEmail = "bostonhomies4@gmail.com";
		String toEmail = email;
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		properties.put("mail.smtp.starttls.required", "true");
		properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		});
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(fromEmail));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			msg.setSubject(user + " Your Booking Is Confirmed!");

			String bodyText = "Dear " + user + ",\n\n" + "Your room booking is confirmed!.\n" + 
					"Below are your booking details: \n" + "Hotel:" + hotelName + "\n" + "Room Type:" + roomType + "\n"
					+ "Check-In Date " + startDate + "\n" + "Check-Out Date: " + endDate + "\n\n" + "Best regards,\n"
					+ "StayMaster";

			// Set the body content
			msg.setText(bodyText);

			// Send the email
			Transport.send(msg);
			System.out.println("Sent message successfully to " + toEmail);
		} catch (MessagingException e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}

	private User getCurrentUser() {
		// Get the current session ID
		String sessionId = SessionHandler.getCurrentSessionId();

		if (sessionId != null) {
			// Retrieve the user session using the session ID
			UserSessionManager userSessionManager = new UserSessionManager();
			UserSession userSession = userSessionManager.getSession(sessionId);

			// Get the user object from the user session
			return userSession.getUser();
		} else {
			return null; // Return null if session ID is null
		}
	}

}
