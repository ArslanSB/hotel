package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;

public class ClientsController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private MaterialDesignIconView userEdit;
  
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        
    }

    
}
