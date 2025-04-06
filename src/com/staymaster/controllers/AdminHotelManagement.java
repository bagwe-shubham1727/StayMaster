package com.staymaster.controllers;

import java.io.IOException;
import java.util.Map;

import com.staymaster.config.NavigationManager;
import com.staymaster.dao.RoomDao;
import com.staymaster.hibernate.SessionManager;
import com.staymaster.models.Room;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminHotelManagement {

	@FXML
	private TableView<Room> roomStatusTbl;

	public void initialize() {
		setup();
		System.out.println("inside initialize");
		loadRoomData();

	}

	public void setup() {
		TableColumn<Room, Integer> roomIdCol = new TableColumn<>("Room ID");
		roomIdCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));

		TableColumn<Room, String> roomStatusCol = new TableColumn<>("Room Status");
		roomStatusCol.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));

		roomStatusTbl.getColumns().addAll(roomIdCol, roomStatusCol);

	}

	@FXML
	public void handleRoomBtn(ActionEvent event) throws IOException {
		try {
			NavigationManager.navigateTo("/com/staymaster/views/Rooms.fxml", "Add Rooms");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleBookingBtn(ActionEvent event) throws IOException {
		try {
			NavigationManager.navigateTo("/com/staymaster/views/BookingAdmin.fxml", "Admin Booking");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleHotelDetails(ActionEvent event) throws IOException {
		System.out.print("testing");
		try {
			NavigationManager.navigateTo("/com/staymaster/views/AdminAddHotel.fxml", "Hotel Management");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleBackBtn(ActionEvent event) throws IOException {
		NavigationManager.goBack();
	}

	private void loadRoomData() {
		RoomDao roomDao = new RoomDao(SessionManager.getSessionFactory());
		Map<Integer, String> statuses = roomDao.findAllRoomsAndUpdateStatus();

		ObservableList<Room> observableRooms = FXCollections.observableArrayList();
		statuses.forEach((id, status) -> {
			Room room = new Room();
			room.setRoomId(id);
			room.setRoomStatus(status);
			observableRooms.add(room);
		});

		roomStatusTbl.setItems(observableRooms);
	}

}
