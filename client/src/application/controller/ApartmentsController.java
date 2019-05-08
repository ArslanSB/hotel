package application.controller;

import java.io.IOException;

import application.model.Apartment;
import application.model.Client;
import application.model.Database;
import application.model.UsefullFunctions;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

public class ApartmentsController {
	
	private Database db = Database.getInstance();
	private UsefullFunctions uff = UsefullFunctions.getInstance();
	
	public static Apartment selected = null;
	
	@FXML TableView<Apartment> apartments;
	@FXML TableColumn<Apartment, Integer> id;
	@FXML TableColumn<Apartment, Integer> num_rooms;
	@FXML TableColumn<Apartment, Integer> max_capacity;
	@FXML TableColumn<Apartment, String> description;
	@FXML TableColumn<Apartment, String> address;
	@FXML TableColumn<Apartment, String> id_client;
	@FXML TableColumn<Apartment, Double> price_night;
	@FXML TableColumn<Apartment, String> state;

	@FXML FontAwesomeIconView plusApartment;

	private void editApartment(Apartment selectedItem) {
		selected = selectedItem;
		AnchorPane root;
		try {
			root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/view/ShowApartment.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/view/application.css").toExternalForm());
			Stage showClient = new Stage();
			showClient.setScene(scene);
			showClient.initStyle(StageStyle.UNDECORATED);
			showClient.show();
		} catch (IOException e) {
			e.printStackTrace();
			uff.showAlerts("Could not open the show client window, please re-try again later...", "error");
		}
	}
	
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
		editItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				editApartment(apartments.getSelectionModel().getSelectedItem());
				
			}
		});
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
						if(Client.getLoggedInUser().getAccess_type().equalsIgnoreCase("user")) {
							if(db.findUserById(apartments.getSelectionModel().getSelectedItem().getId_client()).getId() == Client.getLoggedInUser().getId()) {
								cxm.show(apartments, event.getScreenX(), event.getScreenY());
							}
						}else {
							cxm.show(apartments, event.getScreenX(), event.getScreenY());
						}
					}
				} else {
					cxm.hide();
				}
				
			}
			
		});
		
		plusApartment.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				uff.hoverIconColorChange(plusApartment, "#efefef", Duration.millis(300));
				
			}
		});
		
		plusApartment.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				uff.hoverIconColorChange(plusApartment, "#636e72", Duration.millis(300));
				
			}
		});
	}

	@FXML public void addApartment(MouseEvent event) {
		
		ApartmentsController.selected = null;
		AnchorPane root;
		try {
			root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/view/ShowApartment.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/view/application.css").toExternalForm());
			Stage showClient = new Stage();
			showClient.setScene(scene);
			showClient.initStyle(StageStyle.UNDECORATED);
			showClient.show();
		} catch (IOException e) {
			e.printStackTrace();
			uff.showAlerts("Could not open the show client window, please re-try again later...", "error");
		}
		
	}
    
}
