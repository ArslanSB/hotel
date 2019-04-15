package application.controller;

import java.io.File;
import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import application.model.Database;
import application.model.Main;
import application.model.UsefullFunctions;

public class LoginController {

	private Database db = Database.getInstance();
	private UsefullFunctions uff = UsefullFunctions.getInstance();
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    
    @FXML
    private AnchorPane rootPanel;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML
    private MediaView video;

    @FXML
    private JFXButton switchFormBtn;
    
    @FXML
    private VBox signinForm;
    @FXML
    private JFXTextField siusername;
    @FXML
    private JFXPasswordField sipassword;
    @FXML
    private JFXCheckBox siremember;
    @FXML
    private JFXButton signinBtn;
    
    @FXML
    void signmein(ActionEvent event) {
    	login();   	
    }
    
    void login() {
    	String username = siusername.getText();
    	String password = db.getHashedPassword(sipassword.getText());
    	
    	boolean loginTrue = db.checkLogin(username, password);
    	if(loginTrue) {
    		System.out.println("User has been loged in successfully....");
    		uff.changeScene("../view/Manager.fxml", "Manager");
    	}else {
    		System.out.println("Please check your credencials....");
    	}
    }
    
    @FXML
    void loginOnEnter(KeyEvent event) {
    	if(event.getCode().compareTo(KeyCode.ENTER) == 0) {
    		login();
    	}
    }
    
    @FXML
    void signupOnEnter(KeyEvent event) {
    	System.out.println(event.getCharacter());
    }
    
    @FXML
    private VBox signupForm;
    
    @FXML
    private Label closeWindowBtn;
    
    @FXML
    private Label minimizeWindowBtn;
        
    @FXML
    void closeWindow() {
    	Main.mainStage.close();
    }
    
    @FXML
    void minimizeWindow() {
    	Main.mainStage.setIconified(true);
    }
    
    @FXML
    void switchForm(ActionEvent event) {
    	
    	UsefullFunctions uff = UsefullFunctions.getInstance();
    	uff.showAlerts("CLOSE", "FOUND AN ERROR:....");
    	
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
        
//    	Set video as the login background
//    	Look for good short videos
//    	
//    	String path = new File("src/application/resources/video.mp4").getAbsolutePath();
//    	Media me = new Media(new File(path).toURI().toString());
//    	MediaPlayer md = new MediaPlayer(me);
//    	
//    	video.setMediaPlayer(md);
//    	md.setAutoPlay(true);
    	
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
