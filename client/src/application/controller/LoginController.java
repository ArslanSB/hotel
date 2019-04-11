package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.model.UsefullFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import application.model.Database;

public class LoginController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    
    @FXML
    private AnchorPane rootPanel;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="username"
    private TextField username; // Value injected by FXMLLoader

    @FXML // fx:id="password"
    private PasswordField password; // Value injected by FXMLLoader

    @FXML // fx:id="login"
    private Button login; // Value injected by FXMLLoader
    
    @FXML // fx:id="errors"
    private Label errors;

    @FXML
    void loginMethod(ActionEvent event) {
    	
    	UsefullFunctions uff = UsefullFunctions.getInstance();
    	Database db = Database.getInstance();
    	String pass = db.getHashedPassword(password.getText());
    	
    	if(db.checkLogin(username.getText(), pass)) {
    		errors.setText("Logged in successfully");
    		uff.switchScene((Stage) login.getScene().getWindow(), "../view/Manager.fxml");
    	}else {
    		errors.setText(db.getError());
    	}
    	
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'Login.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'Login.fxml'.";
        assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'Login.fxml'.";

    }
}
