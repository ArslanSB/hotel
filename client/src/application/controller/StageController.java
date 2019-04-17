package application.controller;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import application.model.Database;
import application.model.Email;
import application.model.HotelConfigProperties;
import application.model.Main;
import application.model.UsefullFunctions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class StageController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    private UsefullFunctions uff = UsefullFunctions.getInstance();
    private Database db = Database.getInstance();
    private HotelConfigProperties config = HotelConfigProperties.getInstance();
    
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="closeStageBtn"
    private FontAwesomeIconView closeStageBtn; // Value injected by FXMLLoader
    
    @FXML
    private FontAwesomeIconView minimizeStageBtn;

    @FXML
    private HBox topBorder;
    
    @FXML
    private BorderPane dynamicScene;
    
    @FXML
    private Label topBorderTitle;
    
    @FXML
    void closeStage(MouseEvent event) {
    	Main.mainStage.close();
    }
    
    @FXML
    void minimizeStage(MouseEvent event) {
    	Main.mainStage.setIconified(true);
    }
    
    private double x = 0, y = 0;
    
    void makeWindowDragable() {
    	topBorder.setOnMousePressed((e) -> {
    		x = e.getSceneX();
    		y = e.getSceneY();
    	});
    	
    	topBorder.setOnMouseDragged((e) -> {
    		Main.mainStage.setX((e.getScreenX() - x));
    		Main.mainStage.setY((e.getScreenY() - y));
    	});
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert closeStageBtn != null : "fx:id=\"closeStageBtn\" was not injected: check your FXML file 'Stage.fxml'.";

        Main.dynamicScene = dynamicScene;
        Main.stageTitle = topBorderTitle;
        if(false /*checkIfUserShouldBeLoggedIn()*/) {
        	uff.changeScene("../view/Manager.fxml", "Manager");
        }else{
        	uff.changeScene("../view/Login.fxml", "Login");
        }        
        makeWindowDragable();

    }

    boolean checkIfUserShouldBeLoggedIn() {
    	String configUser = config.getProperties().getProperty("user");
    	String configId = config.getProperties().getProperty("id");
    	String configExpire = config.getProperties().getProperty("expire");
    	
    	boolean loggedIn = false;
    	
    	if(!configUser.equalsIgnoreCase("") && configUser != null) {
    		if(!configId.equalsIgnoreCase("") && configId != null) {
    			if(!configExpire.equalsIgnoreCase("") && configExpire != null) {
        			
    				Date now = new Date();
    				Calendar c = Calendar.getInstance();
    				c.setTime(now);
    				if(c.getTimeInMillis() < Long.parseLong(configExpire)) {
    					if(db.checkUserByExpireDate(configUser, configId, configExpire)) {
        					loggedIn = true;
        				}
    				}
    				
        		}
    		}
    	}
    	
    	return loggedIn;
    }
    
}
