package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import application.model.Client;
import application.model.ClientViewModel;
import application.model.Database;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

public class ClientsController {
	
	private Database db = Database.getInstance();
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private ScrollPane clients;
    private GridPane grid = new GridPane();
    
    @FXML
    private JFXTextField searchClients;
    @FXML
    void showClients(KeyEvent e) {
    	
    	showClientsInGrid(db.getClients(searchClients.getText()));
    	
    }
  
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	clients.setContent(grid);
    	grid.setHgap(5);
    	grid.setVgap(5);
    	
    	showClientsInGrid(db.getClients(""));
    }
    
    void showClientsInGrid(ObservableList<Client> clients) {
    	grid.getChildren().clear();
    	
    	Label noUsers = new Label();
    	noUsers.setText("No clients found with that criteria...");
    	if(clients.size() == 0) {
    		grid.add(noUsers, 0, 0);
    	}
    	
    	int totalRows = (int)  Math.ceil(clients.size() / 3.0);
    	int clientsAdded = 0;    	
    	for(int i = 0; i < totalRows; i++) {
    		
    		int nextThree = (clients.size() - clientsAdded) > 3 ? 3 : clients.size() - clientsAdded;
    		    		
    		for(int j = 0; j < nextThree; j++) {
    			
    			grid.add(new ClientViewModel(clients.get(clientsAdded)), j, i);
    			clientsAdded++;
    			
    		}
    		
    	}
    }

    
}
