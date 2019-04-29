package application.controller;

import java.util.Date;

import com.jfoenix.controls.JFXDatePicker;

import application.model.Database;
import application.model.Reservations;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;

public class ReservationsController {
	
	private Database db = Database.getInstance();
	
	@FXML TableView<Reservations> reservations;
	@FXML TableColumn<Reservations, String> id_client;
	@FXML TableColumn<Reservations, Integer> id_apartment;
	@FXML TableColumn<Reservations, Integer> id;
	@FXML TableColumn<Reservations, String> card_type;
	@FXML TableColumn<Reservations, String> card_number;
	@FXML TableColumn<Reservations, String> card_expiredate;
	@FXML TableColumn<Reservations, String> status;
	@FXML TableColumn<Reservations, Double> total;
	@FXML TableColumn<Reservations, Date> payment_day;
	@FXML TableColumn<Reservations, Date> res_start;
	@FXML TableColumn<Reservations, Date> res_end;
	
	@FXML JFXDatePicker startDate;
	@FXML JFXDatePicker endDate;
	
	@FXML
	void initialize() {
		
		id_client.setCellValueFactory(new PropertyValueFactory<Reservations, String>("client_name"));
		id_apartment.setCellValueFactory(new PropertyValueFactory<Reservations, Integer>("id_apartment"));
		id.setCellValueFactory(new PropertyValueFactory<Reservations, Integer>("id"));
		card_type.setCellValueFactory(new PropertyValueFactory<Reservations, String>("card_type"));
		card_number.setCellValueFactory(new PropertyValueFactory<Reservations, String>("card_number"));
		card_expiredate.setCellValueFactory(new PropertyValueFactory<Reservations, String>("card_expiredate"));
		status.setCellValueFactory(new PropertyValueFactory<Reservations, String>("status"));
		total.setCellValueFactory(new PropertyValueFactory<Reservations, Double>("total"));
		payment_day.setCellValueFactory(new PropertyValueFactory<Reservations, Date>("payment_day"));
		res_start.setCellValueFactory(new PropertyValueFactory<Reservations, Date>("res_start"));
		res_end.setCellValueFactory(new PropertyValueFactory<Reservations, Date>("res_end"));
		
		reservations.setItems(db.getReservations("", ""));
		
		
		startDate.valueProperty().addListener((o, old, newV) -> {
			
			reservations.setItems(db.getReservations((startDate.getValue() == null) ? "" : startDate.getValue().toString(), (endDate.getValue() == null) ? "" : endDate.getValue().toString()));
			
		});
		
		endDate.valueProperty().addListener((o, old, newV) -> {
			
			reservations.setItems(db.getReservations((startDate.getValue() == null) ? "" : startDate.getValue().toString(), (endDate.getValue() == null) ? "" : endDate.getValue().toString()));
			
		});
		
		
	}
    
}
