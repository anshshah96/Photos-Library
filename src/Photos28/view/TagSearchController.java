package Photos28.view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import model.*;

/**
 * Controller class of the tag search screen
 * @author anshshah
 *
 */
public class TagSearchController {
	
	@FXML TextField valueField;
	@FXML TextField nameField;
	
	/**
	 * name of the album
	 */
	String aName;
	
	/**
	 * method that handles the search button
	 * @throws Throwable
	 */
	@FXML 
	public void search() throws Throwable {
		boolean empty = true;
		Album result = null;
		if (LoginController.current.getAlbums().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText("No Albums exists.");
			alert.showAndWait();
			return;
		}
		for (Album a : LoginController.current.getAlbums()) {
			if (!a.getPhotos().isEmpty()) {
				empty = false;
				break;
			}
		}
		if (empty == true) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText("Albums empty.");
			alert.showAndWait();
			return;
		}
		
		if (valueField.getText() == null || nameField.getText() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText("None of the tag fields can be empty.");
			alert.showAndWait();
			return;
		}
		
		TextInputDialog aDialog = new TextInputDialog("Enter Album name");
		aDialog.setTitle("New Album");
		aDialog.setHeaderText("Album with search results:");
		aDialog.setContentText("Enter Album Name");
		Optional <String> albumName = aDialog.showAndWait();
		boolean ispresent = false;
		if (albumName.isPresent()) {
			aName = albumName.get();
			for (Album a : LoginController.current.getAlbums()) {
				if (a.getName().equals(albumName.get())) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setContentText("Album Already Exists.");
					alert.showAndWait();
					ispresent = true;
					return;
				}
			}
			if (ispresent == false) {
				result = new Album(albumName.get());
			}
		}else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText("Album name cannot be empty");
			alert.showAndWait();
				
		}
			
		for (Album a : LoginController.current.getAlbums()) {
			for (Photo p : a.getPhotos()) {
				for(Tag t : p.getTags()) {
					if(t.getValue().equals(valueField.getText()) && t.getName().equals(nameField.getText())) {
						result.getPhotos().add(p);
						result.setEarliestPhotoDate();
						result.setLatestPhotoDate();
					}
				}
			}
		}
		
		LoginController.current.getAlbums().add(result);
		try {
			AdminHomeController.write(AdminHomeController.users);
		}catch (Exception e) {
			e.printStackTrace();
		}
			
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("NonAdminHomeScreen.fxml"));
			Stage currstage = (Stage) valueField.getScene().getWindow();
			currstage.close();
			AnchorPane root = (AnchorPane) loader.load();
			Stage stage = new Stage ();
			Scene scene = new Scene (root);
			stage.setScene(scene);
			stage.show();
			}catch (Exception e) {
				e.printStackTrace();
			}
		
		
	}
	
	/**
	 * method that allows user to go back to the non admin home screen
	 */
	@FXML 
	public void back() {
		try {
			Stage currstage = (Stage) valueField.getScene().getWindow();
			currstage.close();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("NonAdminHomeScreen.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			Stage stage = new Stage ();
			Scene scene = new Scene (root);
			stage.setScene(scene);
			stage.show();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//return;
	}

}