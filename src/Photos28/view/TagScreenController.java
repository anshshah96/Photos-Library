package Photos28.view;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import model.*;

/**
 * Controller class of the Edit tag screen
 * @author Ansh Shah and Kandarp Patel
 *
 */
public class TagScreenController {

	/**
	 * Current photo
	 */
	Photo photo;
	
	/**
	 * name of the album
	 */
	String name;
	
	@FXML TextField valueField;
	@FXML TextField nameField;
	
	/**
	 * initializes the photo and the name of the album 
	 * @param photo current photo
	 * @param name album name
	 */
	public void initialize(Photo photo, String name) {
		this.photo = photo;
		this.name = name;
	}
	
	/**
	 * method that handles the adding of the tag
	 */
	@FXML
	public void addTag() {
		
		//Check if both fields have some value
		if(valueField.getText() == null || nameField.getText() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error adding tag");
			alert.setContentText("Please enter the tag value and the tag name");
			alert.showAndWait();
			return;
		}
		
		//Check if tag already exists
		for(Tag t : photo.getTags()) {
			if(t.getValue().equals(valueField.getText()) && t.getName().equals(nameField.getText())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error adding tag");
				alert.setContentText("Tag already exists for this photo");
				alert.showAndWait();
				return;
			}
		}
		
		//Add tag
		Tag newTag = new Tag(nameField.getText(), valueField.getText());
		photo.getTags().add(newTag);
		
		try {
			AdminHomeController.write(AdminHomeController.users);
        }catch (Exception e){
            e.printStackTrace();
        }
		
		back();
	}
	
	/**
	 * method that handles the deletion of the tag
	 */
	@FXML
	public void deleteTag() {
		//Check if both fields have some value
		if(valueField.getText() == null || nameField.getText() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error adding tag");
			alert.setContentText("Please enter the tag value and the tag name");
			alert.showAndWait();
			return;
		}
		
		boolean exists = false;
		//Check if tag doesn't exists
		for(Tag t : photo.getTags()) {
			if(t.getValue().equals(valueField.getText()) && t.getName().equals(nameField.getText())) {
				exists = true;
				break;
			}
		}
		
		//Tag doesn't exist
		if(exists == false) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error adding tag");
			alert.setContentText("Tag does not exist in the photo");
			alert.showAndWait();
			return;
		}else {
			Tag delTag = new Tag(nameField.getText(), valueField.getText());
			 photo.getTags().remove(delTag);
		}
		
		try {
			AdminHomeController.write(AdminHomeController.users);
        }catch (Exception e){
            e.printStackTrace();
        }
		
		back();
		
		
	}
	
	/**
	 * method that allows the user to go back to display photo screen
	 */
	@FXML
	public void back() {
		try {
			 Stage stage = (Stage) valueField.getScene().getWindow();
	         stage.close();
	         FXMLLoader loader = new FXMLLoader(getClass().getResource("displayPhotoScreen.fxml"));
	         AnchorPane root = (AnchorPane) loader.load();
	         DisplayPhotoController dpc = loader.getController();
	         for(int i = 0; i < LoginController.current.getAlbums().size(); i++) {
	             if(LoginController.current.getAlbums().get(i).getName().equals(name)) {
	                 dpc.setPhotos(LoginController.current.getAlbums().get(i).getPhotos());
	                 break;
	             }

	         }
	         dpc.setName(name);
	         Stage stage2 = new Stage();
	         stage2.setScene(new Scene(root));
	         stage2.setTitle(name + " slideshow");
	         stage2.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
