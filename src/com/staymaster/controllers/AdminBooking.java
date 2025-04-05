package com.staymaster.controllers;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import com.staymaster.config.NavigationManager;
import com.staymaster.config.SortUtil;
import com.staymaster.dao.BookingDao;
import com.staymaster.hibernate.SessionManager;
import com.staymaster.models.Booking;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminBooking {
	
	
	@FXML
	private TableView<Booking> bookingTbl;
	
	
	SessionFactory sessionFactory;
	BookingDao bookingDao;
	
	public void initialize() {
		sessionFactory = SessionManager.getSessionFactory();
		bookingDao = new BookingDao(sessionFactory);
		setup();
		loadBookingData();
	}
	
	public void setup() {
	    // Booking ID
	    TableColumn<Booking, Long> bookingIdCol = new TableColumn<>("Booking ID");
	    bookingIdCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));

	    // User
	    TableColumn<Booking, String> userCol = new TableColumn<>("User");
	    userCol.setCellValueFactory(cellData ->
	            new SimpleStringProperty(cellData.getValue().getUser() != null ? cellData.getValue().getUser().getEmail() : ""));
	    
	 // User
	    TableColumn<Booking, String> userFnameCol = new TableColumn<>("FirstName");
	    userFnameCol.setCellValueFactory(cellData ->
	            new SimpleStringProperty(cellData.getValue().getUser() != null ? cellData.getValue().getUser().getFirstName() : ""));

	 // User
	    TableColumn<Booking, String> userLnameCol = new TableColumn<>("LastName");
	    userLnameCol.setCellValueFactory(cellData ->
	            new SimpleStringProperty(cellData.getValue().getUser() != null ? cellData.getValue().getUser().getLastName() : ""));
	    
	    // Room
	    TableColumn<Booking, String> roomCol = new TableColumn<>("Room");
	    roomCol.setCellValueFactory(cellData ->
	            new SimpleStringProperty(cellData.getValue().getRoom() != null ? cellData.getValue().getRoom().getRoomType() : ""));

	    // Check-in Date
	    TableColumn<Booking, Date> checkInDateCol = new TableColumn<>("Check-in Date");
	    checkInDateCol.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));

	    // Check-out Date
	    TableColumn<Booking, Date> checkOutDateCol = new TableColumn<>("Check-out Date");
	    checkOutDateCol.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));

	    // Status
	    TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
	    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

	    // Priority
	    TableColumn<Booking, Integer> priorityCol = new TableColumn<>("Priority");
	    priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));

	    // Adding the columns to the table
	    bookingTbl.getColumns().addAll(bookingIdCol, userCol, userFnameCol,userLnameCol,checkInDateCol, checkOutDateCol, statusCol, priorityCol);
	}

	@FXML
	public void handleSortBtn(ActionEvent event) throws IOException {
		List<Booking> bookings = bookingDao.findAll();
		SortUtil.mergeSort(bookings);
        ObservableList<Booking> observableBookings = FXCollections.observableArrayList(bookings);
        bookingTbl.setItems(observableBookings);
		
	}
	
	@FXML
	public void handleBackBtn(ActionEvent event) throws IOException {
		NavigationManager.goBack();
	}
	
	
	public void loadBookingData() {

		try {
			
            
            List<Booking> bookings = bookingDao.findAll();
            ObservableList<Booking> observableBookings = FXCollections.observableArrayList(bookings);
            bookingTbl.setItems(observableBookings);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
}

	
	
}
