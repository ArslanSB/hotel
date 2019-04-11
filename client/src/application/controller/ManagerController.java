package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ManagerController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> listView;

    @FXML
    public void initialize() {
            	
        ObservableList<String> listItems = FXCollections.observableArrayList();
        listItems.add("Clients");
        listItems.add("Rooms");
        listItems.add("Reservations");
        listItems.add("My Reservations");
        
        listView.setItems(listItems);
        
        assert listView != null : "fx:id=\"listView\" was not injected: check your FXML file 'Manager.fxml'.";
        
    }
}
