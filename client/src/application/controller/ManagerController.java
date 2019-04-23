package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.model.Client;
import application.model.Database;
import application.model.Main;
import application.model.UsefullFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class ManagerController {
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="clients"
    private Button clients; // Value injected by FXMLLoader

    @FXML // fx:id="rooms"
    private Button rooms; // Value injected by FXMLLoader
    
    @FXML
    private Label username;
    
    UsefullFunctions uff = UsefullFunctions.getInstance();

	private Database db = Database.getInstance();

	@FXML BorderPane mainScene;


    @FXML
    void showClients(ActionEvent event) {
    }

    @FXML
    void showRooms(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert clients != null : "fx:id=\"clients\" was not injected: check your FXML file 'Manager.fxml'.";
        assert rooms != null : "fx:id=\"rooms\" was not injected: check your FXML file 'Manager.fxml'.";

        username.setText(Client.getLoggedInUser().getFullName());
        changeScene("../view/Clients.fxml", "Clients - Manager");
    }

	@FXML public void showSettings(MouseEvent event) {
		
		// for now sign out
		db.signOut();
		
	}
	
	public void changeScene(String FXMLLink, String windowTitle) {
		Main.stageTitle.setText(windowTitle);
		mainScene.setCenter(null);
        try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource(FXMLLink));
			mainScene.setCenter(root);
        } catch(Exception e) {
        	e.printStackTrace();
        }
	}
}