package application.controller;

import java.time.ZoneId;
import java.util.Date;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.model.Client;
import application.model.ClientViewModel;
import application.model.Database;
import application.model.Main;
import application.model.UsefullFunctions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ShowClientController {

	private UsefullFunctions uff = UsefullFunctions.getInstance();
	private Database db = Database.getInstance();
	
    @FXML
    private Label clientName;

    @FXML
    private JFXTextField dniField;

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXTextField surnamesField;

    @FXML
    private JFXTextField addressField;

    @FXML
    private JFXTextField zipcodeField;

    @FXML
    private JFXDatePicker dateField;

    @FXML
    private JFXTextField telephoneField;

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;
    
    @FXML
    private JFXButton submitBtn;
    
    private double x = 0, y = 0;

	@FXML HBox topBorder;

	@FXML Label topBorderTitle;
	
	@FXML FontAwesomeIconView closeStageBtn;
    
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
    
    @FXML
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
        		uff.hoverIconColorChange(closeStageBtn, "#34495e", Duration.millis(300));
        	}
		});
    	
        if(Main.showClient.equalsIgnoreCase("Edit")) {
        	topBorderTitle.setText("Update " + ClientViewModel.getSelectedClient().getFullName());
        	clientName.setText(ClientViewModel.getSelectedClient().getFullName().toUpperCase());
        	nameField.setText(ClientViewModel.getSelectedClient().getName());
        	surnamesField.setText(ClientViewModel.getSelectedClient().getSurnames());
        	addressField.setText(ClientViewModel.getSelectedClient().getAddress());
        	zipcodeField.setText(ClientViewModel.getSelectedClient().getZipcode());
        	dateField.setValue(ClientViewModel.getSelectedClient().getLocalDateofbirth());
        	telephoneField.setText(ClientViewModel.getSelectedClient().getTelephone());
        	emailField.setText(ClientViewModel.getSelectedClient().getEmail());
        	usernameField.setText(ClientViewModel.getSelectedClient().getUsername());
        	submitBtn.setText("Update Client");
        }else if(Main.showClient.equalsIgnoreCase("New")){
        	topBorderTitle.setText("Insert new client");
        	submitBtn.setText("Insert Client");
        }
        
        
        submitBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if(Main.showClient.equalsIgnoreCase("New")) {
					
					Client insertClient = new Client(
							1000,
							clientName.getText(),
							surnamesField.getText(),
							addressField.getText(),
							zipcodeField.getText(),
							java.sql.Date.valueOf(dateField.getValue()),
							telephoneField.getText(),
							usernameField.getText(),
							db.getHashedPassword(passwordField.getText()),
							emailField.getText(),
							"user");
					if(db.addnewUser(insertClient)) {
						((Stage) closeStageBtn.getScene().getWindow()).close();
						ClientsController.showClientsInGrid(db.getClients(Main.clientSearch));
					}
				}
				
			}
		});
        
    	
    }

}