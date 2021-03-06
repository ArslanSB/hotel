package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.model.Main;
import application.model.UsefullFunctions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AlertWindowController {
	
	private UsefullFunctions uff = UsefullFunctions.getInstance();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="closeStageBtn"
    private FontAwesomeIconView closeStageBtn; // Value injected by FXMLLoader
    
    @FXML
    private FontAwesomeIconView alertIcon;
    
    @FXML
    private Label message;
    
    @FXML
    void closeStage(MouseEvent event) {
    	uff.stackAlerts(((Stage) closeStageBtn.getScene().getWindow()));
    }
  
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert closeStageBtn != null : "fx:id=\"closeStageBtn\" was not injected: check your FXML file 'Stage.fxml'.";
        alertIcon.setIcon(Main.alertIcon);
        alertIcon.setGlyphStyle("-fx-fill: " + Main.alertIconColor + ";");
        message.setText(Main.alertMessage);
        
    }

    
}
