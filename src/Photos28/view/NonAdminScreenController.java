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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import model.*;

public class NonAdminScreenController {
	
	@FXML Button logoutButton;
	@FXML Button CreateAlbumButton;
	@FXML Button RenameAlbumButton;
	@FXML Button RemoveAlbumButton;
	@FXML Button selectButton;
	@FXML TextField userHomeField;
	@FXML ScrollPane aList;
	
	/**
	 * Current user
	 */
	User user;
	
	/**
	 * list of albums
	 */
	ArrayList<Album> albums = new ArrayList<Album>();
	
	/**
	 * calls the populate Album method
	 */
	public void initialize(){
        populateAlbum(LoginController.current, "DisplayPhotoScreen.fxml" );
    }
	
	/**
	 * method that handles the date search button
	 */
	@FXML
	public void dateSearch() {
		try {
			Stage c = (Stage) logoutButton.getScene().getWindow();
			c.close();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchScreen.fxml"));
			Parent root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Date Search");
			stage.show();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * method that handles the tag search button
	 */
	@FXML
	public void tagSearch() {
		try {
			Stage c = (Stage) logoutButton.getScene().getWindow();
			c.close();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TagSearchScreen.fxml"));
			Parent root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Tag Search");
			stage.show();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * method the allows the user to create an album
	 */
	@FXML
	public void createAlbum () {
		
		try {
			AdminHomeController.users = AdminHomeController.read();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		TextInputDialog dialog = new TextInputDialog("Enter album name");
		dialog.setTitle("Create Album");
		dialog.setHeaderText("Album");
		dialog.setContentText("Please Enter album name: ");
		
		Optional<String> r = dialog.showAndWait();
		if (!r.isPresent()) {
			return;
		}
		
		String aName = null;
		if (r.isPresent()) {
			aName = r.get();
		}
		
		//album duplicate check
		if (duplicateAlbum(aName, LoginController.current)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Duplicate Album Name");
			alert.setContentText("Album Name Already Exists, Please try again");
			alert.showAndWait();
			return;
		}
		
		Album album = new Album(aName);
		LoginController.current.getAlbums().add(album);
		for (User u : AdminHomeController.users) {
			if (u.getUsername().equals(LoginController.current.getUsername())) {
				u.setAlbums(LoginController.current.getAlbums());
				break;
			}
		}
		
		populateAlbum(LoginController.current, "DisplayPhotoScreen.fxml");
		//Serialize
		try {
			AdminHomeController.write(AdminHomeController.users);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * method that checks if album is duplicate
	 * @param a name of album
	 * @param u current user
	 * @return boolean if album is duplicate
	 */
	public boolean duplicateAlbum (String a, User u) {
		
		for (Album album : u.getAlbums()) {
			if (album.getName().equals(a)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Shows the list of albums of the current user
	 * @param u current user
	 * @param view Display photo screen
	 */
	public void populateAlbum (User u, String view) {
		VBox vBox = new VBox();
		if (u.getAlbums().isEmpty()) {
			vBox.getChildren().removeAll();
			aList.setContent(vBox);
			return;
		}
		for (Album a : u.getAlbums()) {
			
			TitledPane tp = new TitledPane();
			tp.setExpanded(false);
			tp.setId(a.getName());
			String date1 = setDate(a.getEarliestPhotoDate());
			String date2 = setDate(a.getLatestPhotoDate());
			tp.setText(a.getName() + " Photos : " + a.getPhotos().size()+" Date:" +date1+"-"+date2);
			vBox.getChildren().add(tp);
			tp.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("AlbumHomeScreen.fxml"));
						Parent root = (Parent) loader.load();
						AlbumHomeController ahsc = loader.getController();
						TitledPane t = (TitledPane)event.getSource();
						ahsc.setName(a.getName());
						Stage s = new Stage();
						s.setScene(new Scene(root));
						Stage c = (Stage) aList.getScene().getWindow();
						c.close();
						s.setTitle(a.getName());
						s.show();
						return;
					}catch(Exception e)	{
						e.printStackTrace();
					}	
				}
			});
			
		}
		aList.setContent(vBox);
	}
	
	/**
	 * sets the date of the albums
	 * @param date the date of the album
	 * @return String the date of the album
	 */
	public String setDate(Calendar date) {
		
		if (date == null) {
			return "";
		}
		Date d = date.getTime();
		SimpleDateFormat f1 = new SimpleDateFormat("MM-dd-yyyy");
		String inActiveDate = null;
		try {
			inActiveDate = f1.format(d);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return inActiveDate;
	}
	
	/**
	 * Method that handles the deletion of an album
	 */
	@FXML 
	public void deleteAlbum () {
		try {
			AdminHomeController.users = AdminHomeController.read();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		TextInputDialog d = new TextInputDialog("Enter an album that you want to delete: ");
		d.setTitle("Delete Album");
		d.setHeaderText("Album here :");
		d.setContentText("Please enter the album you want to delete: ");
		
		Optional<String> r = d.showAndWait();
		
		ArrayList<Album> a = LoginController.current.getAlbums();
		Album delete = null;
		
		for (Album b : a) {
			if (r.get().equals(b.getName())) {
				delete = b;
			}
		}
		
		if (delete == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText("Album NOT Found.");
			alert.showAndWait();
		}
		a.remove(delete);
		LoginController.current.setAlbums(a);
		
		for (User u: AdminHomeController.users) {
			if (u.getUsername().equals(LoginController.current.getUsername())) {
				u.setAlbums(LoginController.current.getAlbums());
				break;
			}
		}
 		populateAlbum(LoginController.current, "DisplayPhotoScreen.fxml");
		
 		try {
 			AdminHomeController.write(AdminHomeController.users);
 			
 		}catch(Exception e) {
 			e.printStackTrace();
 		}
 		
	}
	
	/**
	 * Method that handles the renaming of an album
	 */
	@FXML 
	public void renameAlbum() {
		try {
			AdminHomeController.users = AdminHomeController.read();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		TextInputDialog d = new TextInputDialog("Enter the album you want to rename");
		d.setTitle("Rename Album");
		d.setHeaderText("Album");
		d.setContentText("Please enter the album you want to rename: ");
		
		Optional <String> r = d.showAndWait();
		
		TextInputDialog d2 = new TextInputDialog("Enter new Name");
		d2.setTitle("Rename Album");
		d2.setHeaderText("Album");
		d2.setContentText("Please enter new name of the Album: ");
		
		Optional<String> rename = d2.showAndWait();
		ArrayList<Album> a = LoginController.current.getAlbums();
		
		if (duplicateAlbum(rename.get(), LoginController.current)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Duplicate Album Name");
			alert.setContentText("Album already exists");
			alert.showAndWait();
			return;
		}
		
		Album rN = null;
		for (Album b: a) {
			if (r.get().equals(b.getName())) {
				rN = b;
				b.setName(rename.get());
			}
		}
		if (rN == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setHeaderText("Album NOT found");
			alert.showAndWait();
			return;
		}
		LoginController.current.setAlbums(a);
		for (User u : AdminHomeController.users) {
			if (u.getUsername().equals(LoginController.current.getUsername())) {
				u.setAlbums(LoginController.current.getAlbums());
				break;
			}
		}
		
	 	populateAlbum(LoginController.current, "DisplayPhotoScreen.fxml");
	 	
	 	try {
	 		AdminHomeController.write(AdminHomeController.users);
	 	}catch (Exception e) {
	 		e.printStackTrace();
	 	}
 	}
	
	/**
	 * Album that allows user to log out and go back to the login screen
	 */
	@FXML
	public void logout() {
		Stage s = (Stage) logoutButton.getScene().getWindow();
		s.close();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
