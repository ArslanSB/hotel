package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.model.Client;
import application.model.Database;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ClientController {

	Database db = Database.getInstance();
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="clientsTable"
    private TableView<Client> clientsTable; // Value injected by FXMLLoader

    @FXML // fx:id="client_id"
    private TextField client_id; // Value injected by FXMLLoader

    @FXML // fx:id="client_username"
    private TextField client_username; // Value injected by FXMLLoader

    @FXML // fx:id="client_password"
    private TextField client_password; // Value injected by FXMLLoader

    @FXML // fx:id="client_email"
    private TextField client_email; // Value injected by FXMLLoader

    @FXML // fx:id="client_accesstype"
    private TextField client_accesstype; // Value injected by FXMLLoader
    
    @FXML // fx:id="updateClientButton"
    private Button updateClientButton; // Value injected by FXMLLoader
    
    @FXML // fx:id="insertClientButton"
    private Button insertClientButton; // Value injected by FXMLLoader
    
    private int client_index;
    
    @FXML
    void showClient(MouseEvent event) {
    	client_index = clientsTable.getSelectionModel().getSelectedIndex();
    	Client client = clientsTable.getSelectionModel().getSelectedItem();
    	
    	client_id.setText(String.valueOf(client.getId()));
    	client_username.setText(client.getUsername());
    	client_password.setText(client.getPassword());
    	client_email.setText(client.getEmail());
    	client_accesstype.setText(client.getAccess_type());
    	
    	client_username.setEditable(true);
    	client_password.setEditable(true);
    	client_email.setEditable(true);
    	
    	
    	updateClientButton.setDisable(false);
    }
    
    @FXML
    void updateClient(ActionEvent event) {
    	
    	db.updateClient(client_id.getText(), client_username.getText(), client_password.getText(), client_email.getText(), client_accesstype.getText());
    	
    }
    
    @FXML
    void insertClient(ActionEvent event) {
    	
    	db.insertClient(client_username.getText(), client_password.getText(), client_email.getText());
    	
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert clientsTable != null : "fx:id=\"clientsTable\" was not injected: check your FXML file 'Client.fxml'.";
        
        TableColumn<Client, Integer> idColumn = new TableColumn<>("_id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Client, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<Client, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        TableColumn<Client, String> emailColumn = new TableColumn<>("E-Mail");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        ObservableList<Client> clients = db.allClients();
        
        clientsTable.setItems(clients);
        clientsTable.getColumns().setAll(idColumn, usernameColumn, passwordColumn, emailColumn);

    }
}