package Photos28.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import model.Photo;
import model.Album;
import model.Tag;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller of the display individual photo and slideshow screen
 * @author Ansh Shah and Kandarp Patel
 *
 */
public class DisplayPhotoController {

	@FXML ImageView imageView;
	@FXML Label captionLabel;
	@FXML Label dateLabel;
	@FXML Label tagsListLabel;
	
	/**
	 * list of photos
	 */
	ArrayList<Photo> photos;
	
	/**
	 * index of the photo
	 */
	int pIn = 0;
	
	/**
	 * name of the album
	 */
	String aName;
	
	/**
	 * initializes the index and views the photo
	 */
	public void initialize() {
		Platform.runLater(()->{
			pIn = 0;
			showPhoto();
		});
	}
	
	/**
	 * sets name of the album
	 * @param aName the name of the album
	 */
	public void setName(String aName) {
		this.aName = aName;
	}
	
	/**
	 * sets the photos list 
	 * @param photos the photos list
	 */
	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}
	
	/**
	 * Method to show the photo
	 */
	public void showPhoto() {
		//get the photo from the photos list and get that image from machine
		
		Photo photo = photos.get(pIn);
		try {
			FileInputStream f = new FileInputStream(photo.getLocation());
			Image image = new Image(f);
			imageView.setImage(image);
			imageView.setId(photo.getName());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		showCaption(photo);
		showDate(photo);
		showTags(photo);
		
	}
	
	/**
	 * Method to show the caption
	 * @param photo the current photo
	 */
	public void showCaption(Photo photo) {
		captionLabel.setText(photo.getCaption());
	}
	
	/**
	 * Method to show the date of the photo
	 * @param photo the current photo
	 */
	public void showDate(Photo photo) {
		Calendar c = photo.getDate();
		Date d = c.getTime();
		
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		String dispDate = format.format(d);
		dateLabel.setText(dispDate);
	}
	
	/**
	 * Method to show the tags of the photo
	 * @param photo the current photo
	 */
	public void showTags(Photo photo) {
		//get the tags list from photo and print them out in the tags label
		ArrayList<Tag> tags = photo.getTags();
		
		if(tags.isEmpty())
			return;
		
		String dispTag = "";
		for(int i = 0; i < tags.size(); i++) {
			dispTag += tags.get(i).getName() + ": " + tags.get(i).getValue() + "\n";
		}
		
		tagsListLabel.setText(dispTag);
	}
	
	/**
	 * Method to go to next photo
	 */
	@FXML
	public void nextPhoto() {
		//When user wants to go to the next photo check if one exists and if so show next photo
		if(pIn == photos.size() - 1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("End of Album");
			alert.setContentText("No more photos");
			alert.showAndWait();
			return;
		}
		else {
			pIn++;
			showPhoto();
		}
	}
	
	/**
	 * method to go the previous photo
	 */
	@FXML
	public void prevPhoto() {
		//When user wants to go to the previous photo check if one exists and if so show previous photo
		if(pIn == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("No previous photos");
			alert.showAndWait();
			return;
		}
		else {
			pIn--;
			showPhoto();
		}
	}
	
	/**
	 * method to copy current photo into another album
	 */
	@FXML
	public void copyPhoto() {
		//Copy the current photo to the album of the user's choice if one exists
		Photo photo = photos.get(pIn);
		
		//If the current album is the only album
		if(LoginController.current.getAlbums().size() == 1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error Copying Photo");
			alert.setContentText("No other albums exist");
			alert.showAndWait();
			return;
		}
		
		//Prompt a dialog box and let user input the name of the album they wish to copy their photo to
		TextInputDialog copyDialog = new TextInputDialog("Enter Album Name");
		copyDialog.setTitle("Copy Photo");
		copyDialog.setContentText("Enter the name of the album you wish to move this photo to");
		
		Optional<String> tarAlbum = copyDialog.showAndWait();
		
		if(tarAlbum.isPresent()) {
			//Trying to copy the photo into the current album
			if(tarAlbum.get().equals(aName)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error Copying Photo");
				alert.setContentText("Cannot copy photo into the current album");
				alert.showAndWait();
				return;
			}
			//Go through the list of albums of current user
			for(int i = 0; i < LoginController.current.getAlbums().size(); i++) {
				
				//Target album found from the list of albums
				if(LoginController.current.getAlbums().get(i).getName().equals(tarAlbum.get())) {
					
					//Go through the list of photos in that album
					for(int j = 0; j < LoginController.current.getAlbums().get(i).getPhotos().size(); j++) {
						
						//Check if the photo already exists in the target album
						if(LoginController.current.getAlbums().get(i).getPhotos().get(j).getName().equals(photo.getName())) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error");
							alert.setHeaderText("Error Copying Photo");
							alert.setContentText("The same photo already exists in the album");
							alert.showAndWait();
							return;
						}
					}
					
					//Add photo
					LoginController.current.getAlbums().get(i).getPhotos().add(photo);
					LoginController.current.getAlbums().get(i).setEarliestPhotoDate();
					LoginController.current.getAlbums().get(i).setLatestPhotoDate();
					
					for(int k = 0; k < AdminHomeController.users.size(); k++) {
						if(AdminHomeController.users.get(k).getUsername().equals(LoginController.current.getUsername())) {
							AdminHomeController.users.get(k).setAlbums(LoginController.current.getAlbums());
						}
					}
					
					//write into the data file
					try {
						AdminHomeController.write(AdminHomeController.users);
			        }catch (Exception e){
			            e.printStackTrace();
					}
					return;
				}
			}
				
			//Target album not found
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error Copying Photo");
			alert.setContentText("The album does not exist");
			alert.showAndWait();
			return;
			
			
		}else {
			//No input string
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error Copying Photo");
			alert.setContentText("Please enter the target album name");
			alert.showAndWait();
			return;
		}
		
	}
	
	/**
	 * method to move current photo into another album
	 */
	@FXML
	public void movePhoto() {
		//move the current photo to the album of the user's choice if one exists
		Photo photo = photos.get(pIn);
				
		//If the current album is the only album
		if(LoginController.current.getAlbums().size() == 1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");	
			alert.setHeaderText("Error Moving Photo");
			alert.setContentText("No other albums exist");
			alert.showAndWait();
			return;
		}
				
		//Prompt a dialog box and let user input the name of the album they wish to copy their photo to
		TextInputDialog copyDialog = new TextInputDialog("Enter Album Name");
		copyDialog.setTitle("Move Photo");
		copyDialog.setContentText("Enter the name of the album you wish to move this photo to");
		
		Optional<String> tarAlbum = copyDialog.showAndWait();
				
		if(tarAlbum.isPresent()) {
			//Trying to copy the photo into the current album
			if(tarAlbum.get().equals(aName)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error Moving Photo");
				alert.setContentText("Cannot move photo into the current album");
				alert.showAndWait();
				return;
			}
			//Go through the list of albums of current user
			for(int i = 0; i < LoginController.current.getAlbums().size(); i++) {
				
				//Target album found from the list of albums
				if(LoginController.current.getAlbums().get(i).getName().equals(tarAlbum.get())) {
					
					//Go through the list of photos in that album
					for(int j = 0; j < LoginController.current.getAlbums().get(i).getPhotos().size(); j++) {
						
						//Check if the photo already exists in the target album
						if(LoginController.current.getAlbums().get(i).getPhotos().get(j).getName().equals(photo.getName())) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error");
							alert.setHeaderText("Error Moving Photo");
							alert.setContentText("The same photo already exists in the album");
							alert.showAndWait();
							return;
						}
						
						
					}
					
					//Move photo
					LoginController.current.getAlbums().get(i).getPhotos().add(photo);
					LoginController.current.getAlbums().get(i).setEarliestPhotoDate();
					LoginController.current.getAlbums().get(i).setLatestPhotoDate();
					
					//Remove photo from current album
					for(int k = 0; k < LoginController.current.getAlbums().size(); k++) {
						//Current album found
						if(LoginController.current.getAlbums().get(k).getName().equals(aName)) {
							LoginController.current.getAlbums().get(k).getPhotos().remove(photo);
							LoginController.current.getAlbums().get(k).setEarliestPhotoDate();
							LoginController.current.getAlbums().get(k).setLatestPhotoDate();
						}
					}
					
					for(int k = 0; k < AdminHomeController.users.size(); k++) {
						if(AdminHomeController.users.get(k).getUsername().equals(LoginController.current.getUsername())) {
							AdminHomeController.users.get(k).setAlbums(LoginController.current.getAlbums());
						}
					}
					
					//write into the data file
					try {
						AdminHomeController.write(AdminHomeController.users);
			        }catch (Exception e){
			            e.printStackTrace();
					}
					return;
				}
			}
				
			//Target album not found
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error Copying Photo");
			alert.setContentText("The album does not exist");
			alert.showAndWait();
			return;
			
		}
		else {
			//No input string
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error Moving Photo");
			alert.setContentText("Please enter the target album name");
			alert.showAndWait();
			return;
		}
			
			
	}
	
	/**
	 * method to change caption of the current photo
	 */
	@FXML
	public void editCaption() {
		Photo photo = photos.get(pIn);
		
		TextInputDialog newCapD = new TextInputDialog("Enter New Caption");
		newCapD.setTitle("Edit Caption");
		newCapD.setContentText("Please enter the caption");
		
		Optional<String> newCap = newCapD.showAndWait();
		
		if(newCap.isPresent()) {
			photo.setCaption(newCap.get());
		}
		
		try {
			AdminHomeController.write(AdminHomeController.users);
        }catch (Exception e){
            e.printStackTrace();
        }
		showCaption(photo);
		
		return;
		
		
	}
	
	/**
	 * method to change tags of the current photo
	 */
	@FXML
	public void editTags() {
		try {
			Stage stage = (Stage) imageView.getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TagScreen.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            TagScreenController tsc = loader.getController();
            tsc.initialize(photos.get(pIn), aName);
            Stage stages = new Stage();
            stages.setScene(new Scene(root));
            stages.show();
            
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * method to go back to the album home screen
	 */
	@FXML
	public void back() {
		try{
			Stage stage = (Stage) imageView.getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AlbumHomeScreen.fxml"));
            Parent root = (Parent) loader.load();
            AlbumHomeController ahs = loader.getController();
            ahs.setName(aName);
            Stage stages = new Stage();
            stages.setScene(new Scene(root));
            stages.show();
            
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
