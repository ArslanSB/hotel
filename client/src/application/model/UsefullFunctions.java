package application.model;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class UsefullFunctions {

	private static UsefullFunctions _instance = null;
	private HotelConfigProperties config = HotelConfigProperties.getInstance();
	private ArrayList<Stage> alertBoxes = new ArrayList<Stage>();
	private Rectangle2D screenBounds = Screen.getPrimary().getBounds();
	
	private UsefullFunctions() {
		
	}
	
	public static UsefullFunctions getInstance() {
		UsefullFunctions._instance = (UsefullFunctions._instance == null) ? new UsefullFunctions() : UsefullFunctions._instance;
		return UsefullFunctions._instance;
	}
	
	public void showAlerts(String message, String alertIconColor ) {
    	try {
    		
    		switch (alertIconColor) {
			case "Error":
			case "error":
	    		Main.alertIconColor = "#c0392b";
	    		Main.alertIcon = FontAwesomeIcon.CLOSE;
				break;
			case "Warning":
			case "warning":
				Main.alertIconColor = "#f39c12";
	    		Main.alertIcon = FontAwesomeIcon.EXCLAMATION;
				break;
			case "OK":
			case "ok":
				Main.alertIconColor = "#27ae60";
	    		Main.alertIcon = FontAwesomeIcon.CHECK;
			default:
				break;
			}
			Main.alertMessage = message;
    		
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/AlertWindow.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
			Stage hideStage = new Stage();
			hideStage.initStyle(StageStyle.UTILITY);
			hideStage.setOpacity(0);
			Stage stage = new Stage();
			
			alertBoxes.add(stage);
			
			stage.initOwner(hideStage);
			stage.initStyle(StageStyle.UNDECORATED);
			
			int totalAlerts = alertBoxes.size() - 1;
					
			stage.setY((screenBounds.getHeight() - 150)  - totalAlerts * 110);
			stage.setX(screenBounds.getWidth() - 420);
			stage.setScene(scene);
			stage.setAlwaysOnTop(true);
			
			PauseTransition close = new PauseTransition(Duration.seconds(Integer.parseInt(config.getProperties().getProperty("alert.box.hide.time"))));
			close.setOnFinished(e -> {
				hideStage.close();				
				stackAlerts(stage);
			});
			
			hideStage.show();
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
	
	public void stackAlerts(Stage stage) {
		alertBoxes.remove(stage);
		stage.close();
		for(int i = 0; i < alertBoxes.size(); i++) {
			alertBoxes.get(i).setY((screenBounds.getHeight() - 150)  - i * 110);
		}
	}
	
	public void hoverIconColorChange(FontAwesomeIconView object, String color, Duration duration) {
		Timeline tml = new Timeline();
		KeyValue kv = new KeyValue(object.fillProperty(), Paint.valueOf(color));
		KeyFrame kf = new KeyFrame(duration, kv);
		tml.getKeyFrames().add(kf);
		tml.play();
	}
	
	public void hoverButtonBackgroundColorChange(Label object, String color, Duration duration) {
	
		Timeline tml = new Timeline();
		KeyValue kv = new KeyValue(object.textFillProperty(), Paint.valueOf(color));
		KeyFrame kf = new KeyFrame(duration, kv);
		tml.getKeyFrames().add(kf);
		tml.play();
		
	}
}
