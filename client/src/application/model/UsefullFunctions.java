package application.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UsefullFunctions {

	private static UsefullFunctions _instance = null;
	
	private UsefullFunctions() {
		
	}
	
	public static UsefullFunctions getInstance() {
		return (UsefullFunctions._instance == null) ? new UsefullFunctions() : UsefullFunctions._instance;
	}
	
	public void switchScene( Stage closeThis, String url ) {
		closeThis.close();
    	try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource(url));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
