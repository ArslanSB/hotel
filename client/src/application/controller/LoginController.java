/**
 * Sample Skeleton for 'Login.fxml' Controller Class
 */

package application.controller;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javax.xml.bind.DatatypeConverter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import application.model.Database;

public class LoginController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

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
    	
    	Database db = Database.getInstance();
    	
    	String pass = "";
    	try {
			MessageDigest md5Password = MessageDigest.getInstance("MD5");
			md5Password.reset();
			md5Password.update(password.getText().getBytes("UTF-8"));
			byte[] result = md5Password.digest();
			pass = DatatypeConverter.printHexBinary(result).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(db.checkLogin(username.getText(), pass)) {
    		errors.setText("User found!!!");
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
