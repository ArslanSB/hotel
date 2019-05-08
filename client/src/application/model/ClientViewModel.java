package application.model;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import application.controller.ClientsController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientViewModel extends VBox {

	private Client client = null;
	private static Client selectedClient = null;
	private UsefullFunctions uff = UsefullFunctions.getInstance();
	private Database db = Database.getInstance();
	private double maxWidth = 240;
	
	public ClientViewModel(Client client) {
		this.client = client;
		this.setData();		
	}
	
	private void setData() {
		this.setStyle("-fx-background-color: #ddd");
		this.setPrefWidth(this.maxWidth);
		
		Label fullName = new Label();
		fullName.setText(this.client.getFullName().toUpperCase());
		fullName.setStyle("-fx-background-color:  #2c3e50; -fx-text-fill: #ddd; -fx-font-weight: bold;");
		fullName.setAlignment(Pos.CENTER);
		fullName.setPrefWidth(this.maxWidth);
		fullName.setPrefHeight(40);
		VBox.setMargin(fullName, new Insets(0, 0, 10, 0));

		this.getChildren().add(fullName);

		HBox hbox = new HBox();
		Label key = new Label("Address: ");
		key.setStyle("-fx-font-weight: bold");
		Label value = new Label(this.client.getAddress());
		hbox.getChildren().addAll(key, value);
		hbox.setPadding(new Insets(5,15,5,15));
		this.getChildren().add(hbox);
		
		hbox = new HBox();
		key = new Label("Zipcode: ");
		key.setStyle("-fx-font-weight: bold");
		value = new Label(this.client.getZipcode());
		hbox.getChildren().addAll(key, value);
		hbox.setPadding(new Insets(5,15,5,15));
		this.getChildren().add(hbox);
		
		hbox = new HBox();
		key = new Label("Telephone: ");
		key.setStyle("-fx-font-weight: bold");
		value = new Label(this.client.getTelephone());
		hbox.getChildren().addAll(key, value);
		hbox.setPadding(new Insets(5,15,5,15));
		this.getChildren().add(hbox);
		
		hbox = new HBox();
		key = new Label("Username: ");
		key.setStyle("-fx-font-weight: bold");
		value = new Label(this.client.getUsername());
		hbox.getChildren().addAll(key, value);
		hbox.setPadding(new Insets(5,15,5,15));
		this.getChildren().add(hbox);

		hbox = new HBox();
		key = new Label("Email: ");
		key.setStyle("-fx-font-weight: bold");
		value = new Label(this.client.getEmail());
		hbox.getChildren().addAll(key, value);
		hbox.setPadding(new Insets(5,15,5,15));
		this.getChildren().add(hbox);
		
		hbox = new HBox(10);
		hbox.setPadding(new Insets(10, 15, 10, 15));
		
		FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
		editIcon.setStyle("-fx-font-size: 16px; -fx-fill: #ddd; -fx-font-family:\"FontAwesome\"");
		JFXButton editBtn = new JFXButton("Edit Client", editIcon);
		editBtn.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 0; -fx-text-fill: #ddd;");
		editBtn.setPrefWidth(200);
		editBtn.setCursor(Cursor.HAND);
		hbox.getChildren().add(editBtn);
		
		editBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				Main.showClient = "Edit";
				
				AnchorPane root;
				ClientViewModel.setSelectedClient(client);
				try {
					root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/view/ShowClient.fxml"));
					Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("/application/view/application.css").toExternalForm());
					Stage showClient = new Stage();
					showClient.setScene(scene);
					showClient.initStyle(StageStyle.UNDECORATED);
					showClient.show();
				} catch (IOException e) {
					e.printStackTrace();
					uff.showAlerts("Could not open the show client window, please re-try again later...", "error");
				}
			}
		});
		
		FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_TIMES);
		deleteIcon.setStyle("-fx-font-size: 16px; -fx-fill: #ddd");
		JFXButton deleteBtn = new JFXButton("Delete Client");
		deleteBtn.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 0; -fx-text-fill: #ddd;");
		deleteBtn.setPrefWidth(200);
		deleteBtn.setCursor(Cursor.HAND);
		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				db.deleteUser(client);
				ClientsController.showClientsInGrid(db.getClients(Main.clientSearch));
			}
		});
		hbox.getChildren().add(deleteBtn);
		
		
		this.getChildren().add(hbox);
		
	}

	public static Client getSelectedClient() {
		return selectedClient;
	}

	public static void setSelectedClient(Client selectedClient) {
		ClientViewModel.selectedClient = selectedClient;
	}
	
}
