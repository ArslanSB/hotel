package application.controller;

import application.model.Apartment;
import application.model.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ApartmentsController {
	
	private Database db = Database.getInstance();
	
	@FXML TableView<Apartment> apartments;
	@FXML TableColumn<Apartment, Integer> id;
	@FXML TableColumn<Apartment, Integer> num_rooms;
	@FXML TableColumn<Apartment, Integer> max_capacity;
	@FXML TableColumn<Apartment, String> description;
	@FXML TableColumn<Apartment, String> address;
	@FXML TableColumn<Apartment, String> id_client;
	@FXML TableColumn<Apartment, Double> price_night;
	@FXML TableColumn<Apartment, String> state;

	@FXML
	void initialize() {
		
		id.setCellValueFactory(new PropertyValueFactory<Apartment, Integer>("id"));
		num_rooms.setCellValueFactory(new PropertyValueFactory<Apartment, Integer>("num_rooms"));
		max_capacity.setCellValueFactory(new PropertyValueFactory<Apartment, Integer>("max_capacity"));
		description.setCellValueFactory(new PropertyValueFactory<Apartment, String>("description"));
		address.setCellValueFactory(new PropertyValueFactory<Apartment, String>("address"));
		id_client.setCellValueFactory(new PropertyValueFactory<Apartment, String>("client_name"));
		price_night.setCellValueFactory(new PropertyValueFactory<Apartment, Double>("price_night"));
		state.setCellValueFactory(new PropertyValueFactory<Apartment, String>("state"));
				
		apartments.setItems(db.getApartments());
		
		
		ContextMenu cxm = new ContextMenu();
		MenuItem editItem = new MenuItem("Edit");		
		MenuItem deleteItem = new MenuItem("Delete");
		deleteItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				db.deleteApartment(apartments.getSelectionModel().getSelectedItem());
				apartments.setItems(db.getApartments());
			}
		});
		
		cxm.getItems().addAll(editItem, deleteItem);
		
		apartments.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				if(event.getButton() == MouseButton.SECONDARY) {
					if(apartments.getSelectionModel().getSelectedItem() != null) {
						cxm.show(apartments, event.getScreenX(), event.getScreenY());
					}
				} else {
					cxm.hide();
				}
				
			}
			
		});
		
	}
    
}
