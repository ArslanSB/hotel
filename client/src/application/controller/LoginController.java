package application.controller;

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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
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
    	
    	boolean loginTrue = db.checkLogin(username, password, siremember.isSelected());
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
    private VBox signupForm;
    @FXML
    private JFXTextField suusername;
    @FXML
    private JFXPasswordField supassword;
    @FXML
    private JFXTextField suemail;
    @FXML
    private JFXButton signupBtn;
    
    void signUp() {
    	
    	String username = suusername.getText();
    	String password = db.getHashedPassword(supassword.getText());
    	String email = suemail.getText();
    	
    	if(db.addnewUser(username, password, email)) {
    		translateAnimationForm(signinForm, signupForm, Duration.millis(500), Duration.millis(500));
    	}
    	
    }
    
    @FXML
    void signupMethod(ActionEvent event) {
    	signUp();
    }
    
    @FXML
    void signupOnEnter(KeyEvent event) {
    	if(event.getCode().compareTo(KeyCode.ENTER) == 0) {
        	signUp();
    	}
    }
    
    
    @FXML
    private VBox recoverForm;
    @FXML
    private JFXTextField recoverUser;
    @FXML
    private JFXTextField recoverCode = new JFXTextField();
    @FXML
    private JFXPasswordField newPassword = new JFXPasswordField();
    
    @FXML
    void recoverMethod(ActionEvent event) {    	
    	// recoverCode textfield
    	recoverCode.setPadding(new Insets(10));
    	recoverCode.prefWidth(250);
    	recoverCode.setFocusColor(Paint.valueOf("#efefef"));
    	recoverCode.setUnFocusColor(Paint.valueOf("#636e72"));
    	recoverCode.setStyle("-fx-text-fill: #efefef");
    	recoverCode.setPromptText("Recover Code");
    	recoverCode.setLabelFloat(true);
    	VBox.setMargin(recoverCode, new Insets(30, 0, 0, 0));
    	
    	// new password field
    	newPassword.setPadding(new Insets(10));
    	newPassword.prefWidth(250);
    	newPassword.setFocusColor(Paint.valueOf("#efefef"));
    	newPassword.setUnFocusColor(Paint.valueOf("#636e72"));
    	newPassword.setStyle("-fx-text-fill: #efefef");
    	newPassword.setPromptText("New Password");
    	newPassword.setLabelFloat(true);
    	VBox.setMargin(newPassword, new Insets(30, 0, 0, 0));
    	
    	
    	if(!recoverUser.isDisable()) {
    		 if(db.sendRecoveryCode(recoverUser.getText())) {
            	recoverUser.setDisable(true);
        		recoverForm.getChildren().add(2, recoverCode);
        		recoverCode.setDisable(false);
        		
        		recoverCode.requestFocus();
        		
        		TranslateTransition trs = new TranslateTransition(Duration.millis(100), recoverForm);
                trs.setByY(-33.5);
                trs.play();
        	 }
    	} else {
    		if(!recoverCode.isDisable()) {
    			if(db.checkRecoveryCode(recoverUser.getText(), recoverCode.getText())) {
        			recoverCode.setDisable(true);
        			recoverForm.getChildren().add(3, newPassword);
        			newPassword.requestFocus();
        			
        			TranslateTransition trs = new TranslateTransition(Duration.millis(100), recoverForm);
                    trs.setByY(-33.5);
                    trs.play();
        		} 
    		} else {
    			if(db.updateUserPassword(recoverUser.getText(), newPassword.getText())) {
    				translateAnimationForm(signinForm, recoverForm, Duration.millis(500), Duration.millis(500));
    			}
    			
    		}
    		   		
    	}
    }
    
    @FXML
    void backToSingInMethod(ActionEvent event) {
    	
    	translateAnimationForm(signinForm, recoverForm, Duration.millis(500), Duration.millis(500));
    	
    }
    
    @FXML
    void showRecoverForm() {
    	translateAnimationForm(recoverForm, signinForm, Duration.millis(500), Duration.millis(500));
    }
    
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
    	signinForm.setTranslateY(500);
        signupForm.setTranslateY(500);
        recoverForm.setTranslateY(500);
                
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
