package application.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import application.model.Apartment;
import application.model.Client;
import application.model.Database;
import application.model.Main;
import application.model.Reservations;
import application.model.UsefullFunctions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class ShowReservationController {
	
	private UsefullFunctions uff = UsefullFunctions.getInstance();
	private Database db = Database.getInstance();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    private double x = 0, y = 0;

	@FXML HBox topBorder;

	@FXML Label topBorderTitle;
	
	@FXML FontAwesomeIconView closeStageBtn;
	@FXML Label title;
	
	@FXML JFXComboBox<Client> id_client;
	@FXML JFXComboBox<Apartment> id_apartment;
	@FXML JFXComboBox<String> status;
	
	@FXML JFXTextField card_type;
	@FXML JFXTextField card_number;
	@FXML JFXTextField card_exdate;
	@FXML JFXTextField total;
	
	@FXML JFXDatePicker startDate;
	@FXML JFXDatePicker endDate;
	
	@FXML JFXButton addNew;
    
	@FXML
    void closeStage(MouseEvent event) {
    	((Stage) closeStageBtn.getScene().getWindow()).close();
    }
	
    void makeWindowDragable() {
    	topBorder.setOnMousePressed((e) -> {
    		x = e.getSceneX();
    		y = e.getSceneY();
    	});
    	
    	topBorder.setOnMouseDragged((e) -> {
    		((Stage) topBorder.getScene().getWindow()).setX((e.getScreenX() - x));
    		((Stage) topBorder.getScene().getWindow()).setY((e.getScreenY() - y));
    	});
    }
    
    @FXML void addNewReservation(ActionEvent e) {
    	
    	if(id_client.getSelectionModel().getSelectedItem() == null) {
    		uff.showAlerts("Client can not be blank..", "error");
    	}else {
    		if(id_apartment.getSelectionModel().getSelectedItem() == null) {
    			uff.showAlerts("Apartment can not be blank..", "error");
    		}else {
    			if(card_type.getText() == null || card_type.getText().equalsIgnoreCase("")) {
    				uff.showAlerts("Card type can't be blank.. set it VISA or MASTERCARD..", "error");
    			}else {
    				if(card_number.getText() == null || card_number.getText().equalsIgnoreCase("")) {
    					uff.showAlerts("Card number can't be blank...", "error");
    				}else {
    					if(card_exdate.getText() == null || card_exdate.getText().equalsIgnoreCase("")) {
    						uff.showAlerts("Card expire date can't be null..", "error");
    					}else {
    						Reservations r = new Reservations(
    								id_client.getSelectionModel().getSelectedItem().getId(),
    								id_apartment.getSelectionModel().getSelectedItem().getId(),
    								2000,
    								card_type.getText(),
    								card_number.getText(),
    								card_exdate.getText(),
    								status.getValue(),
    								(Double) Double.parseDouble(total.getText()),
    								status.getValue().equalsIgnoreCase("not paid") ? null : java.sql.Date.valueOf(LocalDate.now()),
    								java.sql.Date.valueOf(startDate.getValue()),
    								java.sql.Date.valueOf(endDate.getValue()),
    								id_client.getSelectionModel().getSelectedItem().getFullName()
    								);
    						if(db.addReservation(r)) {
    							((Stage) closeStageBtn.getScene().getWindow()).close();
        						
        						Main.stageTitle.setText("Reservations - Manager");
        						Main.managerMainScene.setCenter(null);
        				        try {
        							AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/view/Reservations.fxml"));
        							Main.managerMainScene.setCenter(root);
        				        } catch(Exception e1) {
        				        	e1.printStackTrace();
        				        }
    						}
    					}
    				}
    			}
    		}
    	}
    	
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
       
    	makeWindowDragable();
        closeStageBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
        	@Override
			public void handle(MouseEvent event) {
        		uff.hoverIconColorChange(closeStageBtn, "#efefef", Duration.millis(300));
        	}
		});
        
        closeStageBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
        	@Override
			public void handle(MouseEvent event) {
        		uff.hoverIconColorChange(closeStageBtn, "#636e72", Duration.millis(300));
        	}
		});
        
        topBorderTitle.setText("New reservation");
        total.setDisable(true);
        
        
        id_client.setConverter(new StringConverter<Client>() {
			
			@Override
			public String toString(Client object) {
				return object.getFullName();
			}
			
			@Override
			public Client fromString(String string) {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		});
        
        if(Client.getLoggedInUser().getAccess_type().equalsIgnoreCase("user")) {
        	id_client.getItems().add(Client.getLoggedInUser());
        	id_client.setValue(Client.getLoggedInUser());
        	id_client.setDisable(true);
        	status.setDisable(true);
        }else {
        	id_client.setItems(db.getClients(""));
        	status.getItems().addAll("paid");
        }
    	status.getItems().add("not paid");
    	status.setValue("not paid");
        
        id_apartment.setConverter(new StringConverter<Apartment>() {

			@Override
			public Apartment fromString(String string) {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public String toString(Apartment object) {
				return object.getAddress() + " - " + object.getNum_rooms() + " - " + object.getPrice_night() + "€/Night";
			}
		});
        
        id_apartment.setDisable(true);
        
        id_apartment.valueProperty().addListener((o, old, newV) -> {
        	
        	Date sdate = Date.from(startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        	Date edate = Date.from(endDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        	long diff = edate.getTime() - sdate.getTime();
        	int days = (int) (diff / (24 * 60 * 60 * 1000));
        	
        	if(id_apartment.getSelectionModel().getSelectedItem() != null) {
        		total.setText(String.valueOf(newV.getPrice_night() * days));
        	}else {
        		total.setText("0");
        	}
        	
        });
        
        startDate.valueProperty().addListener((o, old, newV) -> {
        	if(newV != null && endDate.getValue() != null) {
        		id_apartment.setItems(db.getApartmentsByReservationDate(startDate.getValue(), endDate.getValue()));
        		id_apartment.setDisable(false);
        	}else {
        		id_apartment.setDisable(true);
        	}
		});
		
		endDate.valueProperty().addListener((o, old, newV) -> {
			
			if(newV != null && startDate.getValue() != null) {
        		id_apartment.setItems(db.getApartmentsByReservationDate(startDate.getValue(), endDate.getValue()));
        		id_apartment.setDisable(false);
        	}else {
        		id_apartment.setDisable(true);
        	}
			
		});
        
    }

    
}
