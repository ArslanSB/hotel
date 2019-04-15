package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.model.UsefullFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ManagerController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="clients"
    private Button clients; // Value injected by FXMLLoader

    @FXML // fx:id="rooms"
    private Button rooms; // Value injected by FXMLLoader
    
    UsefullFunctions uff = UsefullFunctions.getInstance();

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

    }
}