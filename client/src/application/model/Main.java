package application.model;
	
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	public static Stage mainStage = null;
	public static BorderPane dynamicScene = null;
	public static Label stageTitle = null;
	public static String alertMessage;
	public static FontAwesomeIcon alertIcon;
	public static String alertIconColor;
	public static String clientSearch = "";
	public static String showClient;
	public static BorderPane managerMainScene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/Stage.fxml"));
			Scene scene = new Scene(root);			
			scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
			mainStage = primaryStage;
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
