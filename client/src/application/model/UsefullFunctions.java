package application.model;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UsefullFunctions {

	private static UsefullFunctions _instance = null;
	private UsefullFunctions() {
		
	}
	
	public static UsefullFunctions getInstance() {
		return (UsefullFunctions._instance == null) ? new UsefullFunctions() : UsefullFunctions._instance;
	}
	
	public void showAlerts( String icon, String errorMsg ) {
    	try {
    		
    		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    		
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/AlertWindow.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setY(screenBounds.getHeight() - 150);
			stage.setX(screenBounds.getWidth() - 420);
			stage.setScene(scene);
			stage.show();
						
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
