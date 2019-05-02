package application.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.model.Client;
import application.model.Database;
import application.model.Main;
import application.model.UsefullFunctions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class ManagerController {
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="clients"
    private Button clients; // Value injected by FXMLLoader

    @FXML // fx:id="rooms"
    private Button rooms; // Value injected by FXMLLoader
    
    @FXML
    private Label username;
    
    UsefullFunctions uff = UsefullFunctions.getInstance();

	private Database db = Database.getInstance();

	@FXML BorderPane mainScene;
	
	ArrayList<Label> managerButtons = new ArrayList<Label>();

	@FXML VBox sideBar;

	@FXML FontAwesomeIconView signOutBtn;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert clients != null : "fx:id=\"clients\" was not injected: check your FXML file 'Manager.fxml'.";
        assert rooms != null : "fx:id=\"rooms\" was not injected: check your FXML file 'Manager.fxml'.";

        Main.managerMainScene = mainScene;
        
        username.setText(Client.getLoggedInUser().getFullName());
        
        signOutBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
        	@Override
			public void handle(MouseEvent event) {
        		uff.hoverIconColorChange(signOutBtn, "#efefef", Duration.millis(300));
        	}
		});
        
        signOutBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
        	@Override
			public void handle(MouseEvent event) {
        		uff.hoverIconColorChange(signOutBtn, "#636e72", Duration.millis(300));
        	}
		});
        
        if(!Client.getLoggedInUser().getAccess_type().equalsIgnoreCase("user")) {
            changeScene("../view/Clients.fxml", "Clients");
        	managerButton("clients", FontAwesomeIcon.USER, true);
        }else {
        	changeScene("../view/Apartments.fxml", "Apartments");
        }
        managerButton("apartments", FontAwesomeIcon.HOME, (Client.getLoggedInUser().getAccess_type().equalsIgnoreCase("user") ? true : false));
        managerButton((Client.getLoggedInUser().getAccess_type().equalsIgnoreCase("user") ? "my reservations" : "reservations"), FontAwesomeIcon.CALENDAR_CHECK_ALT, false);
        
        
    }

	@FXML public void signOut(MouseEvent event) {
		
		// for now sign out
		db.signOut();
		
	}
	
	public void changeScene(String FXMLLink, String windowTitle) {
		Main.stageTitle.setText(windowTitle + " - Manager");
		mainScene.setCenter(null);
        try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource(FXMLLink));
			mainScene.setCenter(root);
        } catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	private void managerButton(String buttonText, FontAwesomeIcon buttonIcon, boolean active) {
		
		Label button = new Label("   " + buttonText.toUpperCase());
		FontAwesomeIconView icon = new FontAwesomeIconView(buttonIcon);
		icon.setStyle("-fx-fill: #636e72");
		icon.setGlyphSize(16);
        button.setGraphic(icon);
        button.setPrefWidth(224);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(5, 20, 5, 20));
        button.setTextAlignment(TextAlignment.LEFT);
        button.setFocusTraversable(false);
        button.setStyle("-fx-text-fill: #636e72; -fx-font-size: 16px; -fx-background-color: #2c3e50; -fx-background-radius: 0");
        button.setCursor(Cursor.HAND);
        button.setAccessibleHelp("");
        sideBar.getChildren().add(button);
        
        if(active) {
        	button.setStyle("-fx-text-fill: #efefef; -fx-font-size: 16px;");
        	icon.setGlyphSize(16);
        	icon.setStyle("-fx-fill: #efefef;");
        	button.setAccessibleHelp("Active");
        }
        
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
        	@Override
			public void handle(MouseEvent event) {
    			if(!button.getAccessibleHelp().equalsIgnoreCase("Active")) {
    				uff.hoverButtonBackgroundColorChange(button, "#efefef", Duration.millis(300));
            		uff.hoverIconColorChange(icon, "#efefef", Duration.millis(300));
    			}
        	}
		});
        
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
        	@Override
			public void handle(MouseEvent event) {
        		if(!button.getAccessibleHelp().equalsIgnoreCase("Active")) {
	    			uff.hoverButtonBackgroundColorChange(button, "#636e72", Duration.millis(300));
	        		uff.hoverIconColorChange(icon, "#636e72", Duration.millis(300));
        		}
        	}
		});
        
        button.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				for(Label btn : managerButtons) {
					uff.hoverButtonBackgroundColorChange(btn, "#636e72", Duration.millis(300));
            		uff.hoverIconColorChange(((FontAwesomeIconView) btn.getGraphic()), "#636e72", Duration.millis(300));
            		btn.setAccessibleHelp("");
				}
				
				Label curr = (Label) event.getSource();
				uff.hoverButtonBackgroundColorChange(curr, "#efefef", Duration.millis(300));
        		uff.hoverIconColorChange(((FontAwesomeIconView) curr.getGraphic()), "#efefef", Duration.millis(300));
        		curr.setAccessibleHelp("Active");
        		        		
        		switch (curr.getText().toLowerCase()) {
				case "   clients":
					changeScene("../view/Clients.fxml", "Clients");
					break;
				case "   apartments":
					changeScene("../view/Apartments.fxml", "Clients");
					break;
				case "   reservations":
				case "   my reservations":
					changeScene("../view/Reservations.fxml", "Reservations");
					break;
				default:
					break;
				}
        		
			}
		});
        
        managerButtons.add(button);
	}
}