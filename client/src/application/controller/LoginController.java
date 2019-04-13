package application.controller;

import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.model.UsefullFunctions;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
    private JFXButton switchFormBtn;
    
    @FXML
    private VBox signinForm;
    
    @FXML
    private VBox signupForm;
    
    @FXML
    private Label closeWindowBtn;
    
    @FXML
    private Label minimizeWindowBtn;
    
    @FXML
    void closeWindow() {
    	((Stage) signinForm.getScene().getWindow()).close();
    }
    
    @FXML
    void minimizeWindow() {
    	((Stage) signinForm.getScene().getWindow()).setIconified(true);
    }
    
    @FXML
    void loginMethod(ActionEvent event) {
    	
    	UsefullFunctions uff = UsefullFunctions.getInstance();
    	Database db = Database.getInstance();
    	String pass = db.getHashedPassword(password.getText());
    	
    	if(db.checkLogin(username.getText(), pass)) {
    		errors.setText("Logged in successfully");
    		uff.switchScene(((Stage) signinForm.getScene().getWindow()), "../view/Manager.fxml");
    	}else {
    		errors.setText(db.getError());
    	}
    	
    }
    
    @FXML
    void switchForm(ActionEvent event) {
    	
    	String btnValue = switchFormBtn.getText();
    	
    	switch (btnValue) {
		case "Sign in":
			switchFormBtn.setText("Sign up");			
			translateAnimationForm(signinForm, signupForm, Duration.millis(500), Duration.millis(200));
			
			break;
		case "Sign up":
			switchFormBtn.setText("Sign in");			
			translateAnimationForm(signupForm, signinForm, Duration.millis(500), Duration.millis(200));
			
			break;
		default:
			break;
		}
    	
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'Login.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'Login.fxml'.";
        assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'Login.fxml'.";
        
        signinForm.setTranslateY(500);
        signupForm.setTranslateY(500);
        
        translateAnimationForm(signinForm, Duration.millis(500), Duration.millis(500));
        
    }
    
    void translateAnimationForm(VBox in, VBox out, Duration duration, Duration delayIn) {

        TranslateTransition trs = new TranslateTransition(duration, out);
        trs.setByY(500);
        trs.play();
        
        trs = new TranslateTransition(duration, in);
        trs.setDelay(delayIn);
        trs.setByY(-500);
        trs.play();
    	
    }
    
    void translateAnimationForm(VBox in, Duration duration, Duration delayIn) {
        
    	TranslateTransition trs = new TranslateTransition(duration, in);
        trs.setDelay(delayIn);
        trs.setByY(-500);
        trs.play();
    	
    }
    
}
