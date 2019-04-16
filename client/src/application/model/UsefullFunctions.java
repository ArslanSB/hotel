package application.model;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class UsefullFunctions {

	private static UsefullFunctions _instance = null;
	private HotelConfigProperties config = HotelConfigProperties.getInstance();
	
	private UsefullFunctions() {
		
	}
	
	public static UsefullFunctions getInstance() {
		return (UsefullFunctions._instance == null) ? new UsefullFunctions() : UsefullFunctions._instance;
	}
	
	public void showAlerts( FontAwesomeIcon icon, String message, String alertIconColor ) {
    	try {
    		
    		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    		
    		switch (alertIconColor) {
			case "Error":
			case "error":
	    		Main.alertIconColor = "#c0392b";
				break;
			case "Warning":
			case "warning":
				Main.alertIconColor = "#f39c12";
				break;
			case "OK":
			case "ok":
				Main.alertIconColor = "#27ae60";
			default:
				break;
			}
    		
    		Main.alertIcon = icon;
			Main.alertMessage = message;
    		
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/AlertWindow.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setY(screenBounds.getHeight() - 150);
			stage.setX(screenBounds.getWidth() - 420);
			stage.setScene(scene);
			stage.setAlwaysOnTop(true);
			
			PauseTransition close = new PauseTransition(Duration.seconds(Integer.parseInt(config.getProperties().getProperty("alert.box.hide.time"))));
			close.setOnFinished(e -> {
				stage.close();
			});
			
			stage.show();
			close.play();	
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeScene(String FXMLLink, String windowTitle) {
		Main.stageTitle.setText(windowTitle);
		Main.dynamicScene.setCenter(null);
        try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource(FXMLLink));
			Main.dynamicScene.setCenter(root);
        } catch(Exception e) {
        	e.printStackTrace();
        }
	}
}
