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
 * Controller album of the date search screen
 * @author Ansh Shah and Kandarp Patel
 *
 */
public class SearchScreenController {
	
	@FXML TextField fromDate;
	@FXML TextField toDate;
	
	/**
	 * name of the album
	 */
	String aName;
	
	/**
	 * date format
	 */
	final static String DATE_FORMAT = "MM-dd-yyyy";
	
	/**
	 * Method that handles the search button
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
		
		if (fromDate.getText() == null || toDate.getText() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText("None of the date fields can be empty.");
			alert.showAndWait();
			return;
		}
		Calendar fromdate= Calendar.getInstance(), todate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
		
		
		if (checkFormat(fromDate.getText()) && checkFormat(toDate.getText())) {
			fromdate.setTime(sdf.parse(fromDate.getText()));
			todate.setTime(sdf.parse(toDate.getText()));
			
			
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
					if ((p.getDate().after(fromdate) && p.getDate().before(todate)) || (p.getDate().equals(fromdate) || p.getDate().equals(todate))) {
						result.getPhotos().add(p);
						result.setEarliestPhotoDate();
						result.setLatestPhotoDate();
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
				Stage currstage = (Stage) fromDate.getScene().getWindow();
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
		else {
			return;
		}
	}
	
	/**
	 * method that checks the format and validity of the input
	 * @param input the input from the text fields
	 * @return boolean if format is correct
	 */
	private boolean checkFormat(String input) {
		
		int index = 0;
		if (!(input.length() == 10)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText("Please follow the specified format.");
			alert.showAndWait();
			return false;
		}
		
		while (index < 10) {
			if (index == 2 || index == 5) {
				if (!(input.charAt(index) == '-')) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setContentText("Please follow the specified format.");
					alert.showAndWait();
					return false;
				}
				else {
					index++;
					continue;
				}
			}
			else {
				if (!(Character.isDigit(input.charAt(index)))){
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setContentText("Please follow the specified format.");
					alert.showAndWait();
					return false;
				}
				else { 
					index++;
					continue;
				}
			}
		}
		try {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			df.setLenient(false);
			df.parse(input);
			return true;
		}catch (Exception e) {
			return false;
		}
		
	}

	/**
	 * method that allows user to go back to the non admin home screen
	 */
	@FXML 
	public void back() {
		try {
			Stage currstage = (Stage) fromDate.getScene().getWindow();
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
		
	}

}