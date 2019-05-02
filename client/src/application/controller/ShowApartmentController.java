package application.controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import application.model.Apartment;
import application.model.Client;
import application.model.Database;
import application.model.Main;
import application.model.UsefullFunctions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class ShowApartmentController {
	
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
	@FXML JFXComboBox<Client> id_client;
	@FXML JFXButton submitBtn;
	@FXML JFXTextField num_rooms;
	@FXML JFXTextField max_capacity;
	@FXML JFXTextField address;
	@FXML JFXTextField price_night;
	@FXML JFXTextArea description;
	@FXML JFXComboBox<String> state;
	@FXML Label title;
    
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
    
    @FXML void addApartment(ActionEvent event) {
    	
    	if(num_rooms.getText().equalsIgnoreCase("") || num_rooms.getText().matches("^[^0-9]+$") || num_rooms == null) {
    		uff.showAlerts("Number of rooms should be a number...", "error");
    	}else{
    		if(max_capacity.getText().equalsIgnoreCase("") || max_capacity.getText().matches("^[^0-9]+$") || max_capacity == null) {
    			uff.showAlerts("Max capacity should be a number...", "error");
    		}else {
    			if(price_night.getText().equalsIgnoreCase("") || price_night.getText().matches("^[^0-9]+$") || price_night == null) {
        			uff.showAlerts("Price should be a number...", "error");
        		}else {
        			if( description.getText().equalsIgnoreCase("") || description == null ) {
        				uff.showAlerts("Description can't be blank...", "error");
        			}else {
        				if( address.getText().equalsIgnoreCase("") || address == null ) {
            				uff.showAlerts("Address can't be blank...", "error");
            			}else {
            				
            				if(id_client.getSelectionModel().getSelectedItem() == null) {
            					uff.showAlerts("Owner can't be blank...", "error");
            				}else {
            					Apartment apart = new Apartment(5000, Integer.parseInt(num_rooms.getText()), Integer.parseInt(max_capacity.getText()), description.getText(), address.getText(), id_client.getSelectionModel().getSelectedItem().getId(), Double.parseDouble(price_night.getText()), state.getValue(), id_client.getSelectionModel().getSelectedItem().getFullName());
            					if(ApartmentsController.selected == null) {
                					if(db.addApartment(apart)) {
                						((Stage) closeStageBtn.getScene().getWindow()).close();
                						
                						Main.stageTitle.setText("Apartments - Manager");
                						Main.managerMainScene.setCenter(null);
                				        try {
                							AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/Apartments.fxml"));
                							Main.managerMainScene.setCenter(root);
                				        } catch(Exception e) {
                				        	e.printStackTrace();
                				        }
                						
                					}
            					}else {
            						
            						if(db.updateApartment(ApartmentsController.selected, apart)) {
            							((Stage) closeStageBtn.getScene().getWindow()).close();
                						
                						Main.stageTitle.setText("Apartments - Manager");
                						Main.managerMainScene.setCenter(null);
                				        try {
                							AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/Apartments.fxml"));
                							Main.managerMainScene.setCenter(root);
                				        } catch(Exception e) {
                				        	e.printStackTrace();
                				        }
            						}
            						
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
        
        state.getItems().addAll("", "Maintenance");
        state.setValue("");
        
        if(Client.getLoggedInUser().getAccess_type().equalsIgnoreCase("user")) {
        	id_client.getItems().add(Client.getLoggedInUser());
        	id_client.setValue(Client.getLoggedInUser());
        	id_client.setDisable(true);
        }else {
        	id_client.setItems(db.getClients(""));
        }
        
        if(ApartmentsController.selected != null) {
        	Apartment sel = ApartmentsController.selected;
        	num_rooms.setText(String.valueOf(sel.getNum_rooms()));
        	max_capacity.setText(String.valueOf(sel.getMax_capacity()));
        	description.setText(sel.getDescription());
        	address.setText(sel.getAddress());
        	for(int i = 0; i < id_client.getItems().size(); i++) {
        		if( id_client.getItems().get(i).getId() == sel.getId_client() ) {
        			id_client.setValue(id_client.getItems().get(i));
        		}
        	}
        	state.setValue(sel.getState());
        	price_night.setText(String.valueOf(sel.getPrice_night()));
        	submitBtn.setText("Update Apartment");
        	title.setText("UPDATE APARTMENT");
        	
        }
    	
    }

    
}
