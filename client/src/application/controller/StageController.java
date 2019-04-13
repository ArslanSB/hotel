package application.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class StageController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="closeStageBtn"
    private Label closeStageBtn; // Value injected by FXMLLoader
    
    @FXML
    private Label minimizeStageBtn;

    @FXML
    void closeStage(MouseEvent event) {
    	((Stage) closeStageBtn.getScene().getWindow()).close();
    }
    
    @FXML
    void minimizeStage(MouseEvent event) {
    	Stage window = ((Stage) minimizeStageBtn.getScene().getWindow());
    	window.hide();
    }
    
    @FXML
    void testingDrag(MouseEvent event) {
    	Stage window = ((Stage) minimizeStageBtn.getScene().getWindow());
    	while(event.isPrimaryButtonDown()) {
    		window.setX(event.getSceneX());
        	window.setY(event.getSceneY());
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert closeStageBtn != null : "fx:id=\"closeStageBtn\" was not injected: check your FXML file 'Stage.fxml'.";

    }
}
