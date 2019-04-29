package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.sun.org.apache.xpath.internal.FoundIndex;

import application.model.Client;
import application.model.ClientViewModel;
import application.model.Database;
import application.model.Main;
import application.model.UsefullFunctions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientsController {
	
	private Database db = Database.getInstance();
	private UsefullFunctions uff = UsefullFunctions.getInstance();
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private ScrollPane clients;
    private static GridPane grid = new GridPane();
    
    @FXML
    private JFXTextField searchClients;
    @FXML
    void showClients(KeyEvent e) {
    	
    	showClientsInGrid(db.getClients(searchClients.getText()));
    	Main.clientSearch = searchClients == null ? "" : searchClients.getText();
    	
    }
  
    @FXML JFXButton addClientBtn;
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	clients.setContent(grid);
    	grid.setHgap(5);
    	grid.setVgap(5);
    	
    	FontAwesomeIconView addUserIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
    	addUserIcon.setStyle("-fx-fill: #efefef");
    	addClientBtn.setGraphic(addUserIcon);
    	addClientBtn.setCursor(Cursor.HAND);
    	addClientBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Main.showClient = "New";
				
				AnchorPane root;
				try {
					root = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/ShowClient.fxml"));
					Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
					Stage showClient = new Stage();
					showClient.setScene(scene);
					showClient.initStyle(StageStyle.UNDECORATED);
					showClient.show();
				} catch (IOException e) {
					e.printStackTrace();
					uff.showAlerts("Could not open the show client window, please re-try again later...", "error");
				}
			}
		});
    	
    	
    	showClientsInGrid(db.getClients(""));
    }
    
    public static void showClientsInGrid(ObservableList<Client> clients) {
    	grid.getChildren().clear();
    	
    	FontAwesomeIconView noUsersFound = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
    	noUsersFound.setGlyphSize(20);
    	noUsersFound.setStyle("-fx-fill: #f39c12");
    	Label noUsers = new Label("No clients found with that particular criteria try searching for something different!", noUsersFound);
    	
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
