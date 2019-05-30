package Photos28.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;

public class AlbumHomeController {
	
	@FXML Button logoutButton;
	@FXML Button delete;
	@FXML Button editCaption;
	@FXML Button displaySlideshow;
	@FXML Button backButton;
	
	@FXML FlowPane photoPane;
	@FXML Label earliestDate;
	@FXML Label latestDate;
	@FXML Label numPhotos;
	@FXML Label buttonAction;
	@FXML Label albumName;
	
	/**
	 * Boolean field to see if the user is trying to delete
	 */
	boolean del = false; 
	
	/**
	 * Boolean field to see if the user is trying to edit caption
	 */
	boolean editCap = false; 
	
	/**
	 * Boolean field to see if the user is trying to display slideshow
	 */
	boolean dispSlide = false;
	
	/**
	 * name of the album
	 */
	String name;
	
	/**
	 * Initializes the boolean fields and views photos
	 */
	public void initialize() {
		Platform.runLater(() -> { 
			del = false; editCap = false; dispSlide = false;
			photosView(name);
			
		});
	}
	
	/**
	 * Sets name of the album
	 * @param name name of the album
	 */
	public void setName(String name) {
		this.name = name;
		albumName.setText(name);
	}
	
	/**
	 * Checks which button is clicked an takes action accordingly
	 * @param e which button is clicked
	 */
	@FXML void buttonHelper(ActionEvent e) {
		del = false; editCap = false; dispSlide = false;
		Button button = (Button) e.getSource();
		
		if(button == delete) {
			buttonAction.setText("Select the photo you want to delete");
			del = true;
		}
		else if(button == editCaption){
			buttonAction.setText("Select the photo whose caption you want to edit");
			editCap = true;
		}
		else if(button == displaySlideshow) {
			buttonAction.setText("Select the photo you want to display");
			dispSlide = true;
		}
	}
	
	/**
	 * Calls the appropriate album according to the boolean fields
	 * @param photo selected photo
	 * @param label label of the photo
	 */
	public void methodHelper(Photo photo, Label label) {
		if(del == true) {
			deletePhoto(photo, label);
		}else if(editCap == true) {
			editCaption(photo);
		}else if(dispSlide == true) {
			displaySlideshow();
		}else {
			return;
		}
		
		 if(!del && !editCap) {
	            Stage stage = (Stage) delete.getScene().getWindow();
	            stage.close();
	        }
	}
	
	/**
	 * Method to view the photos
	 * @param aName name of the album
	 */
	public void photosView(String aName) {
		//Add photos from an album to the view
		
		for(int i = 0; i < LoginController.current.getAlbums().size(); i++) {
			
			if(LoginController.current.getAlbums().get(i).getName().equals(aName)) {
				earliestDate.setText(LoginController.current.getAlbums().get(i).getEarliestPhotoDate().getTime().toString());
				latestDate.setText(LoginController.current.getAlbums().get(i).getLatestPhotoDate().getTime().toString());
				//Album is empty no photos to add to the view
				if(LoginController.current.getAlbums().get(i).getPhotos().isEmpty())
					return;
				
				for(int j = 0; j < LoginController.current.getAlbums().get(i).getPhotos().size(); j++) {
					try {
                       
                        FileInputStream fis = new FileInputStream(LoginController.current.getAlbums().get(i).getPhotos().get(j).getLocation());
                        String caption = LoginController.current.getAlbums().get(i).getPhotos().get(j).getCaption();
                        Label label = new Label(caption);


                        Image image = new Image(fis);
                        ImageView iView = new ImageView(image);
                        iView.setId(LoginController.current.getAlbums().get(i).getPhotos().get(j).getName());
                        iView.setFitHeight(200);
                        iView.setFitWidth(200);
                        label.setGraphic(iView);

                        Photo photo = LoginController.current.getAlbums().get(i).getPhotos().get(j);
                        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                methodHelper(photo, label);
                            }
                        });
                        photoPane.getChildren().add(label);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
				
			}
		}
	}
	
	/**
	 * Method to add the photo
	 */
	@FXML
	public void addPhoto() {
		//Let the user choose a file from their machine and add if the file is a photo and if that photo is not already in the album
		
		FileChooser fc = new FileChooser();
        File selectFile = fc.showOpenDialog(null);
        
        if(selectFile != null){
            Calendar c = Calendar.getInstance();
            Date d = new Date(selectFile.lastModified());
            c.setTime(d);
            c.set(Calendar.MILLISECOND,0);
            //check if file is an image
            try {
                if (ImageIO.read(selectFile) == null) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("The selected file is not an image");
                    alert.showAndWait();
                    return;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            
            for(int i = 0; i < LoginController.current.getAlbums().size(); i++) {
            	
            	if(LoginController.current.getAlbums().get(i).getName().equals(name)) {
            		//Album found in the user
            		for(int j = 0; j < LoginController.current.getAlbums().get(i).getPhotos().size(); j++) {
            			if(LoginController.current.getAlbums().get(i).getPhotos().get(j).getName().equals(selectFile.getName())) {
            				//Photo already exists in the album
            				Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("The photo already exists in the album");
                            alert.showAndWait();
                            return;
            			}
            		}
            	}
            }
            
            //Add the photo
            try {
            	 Stage stage = (Stage)logoutButton.getScene().getWindow();
                 String albumName = stage.getTitle();
                 Photo photo = new Photo(selectFile.getName(), c, selectFile.getPath());

                 for(int i = 0; i < LoginController.current.getAlbums().size(); i++) {
                     //Add photo to album
                	 if(LoginController.current.getAlbums().get(i).getName().equals(albumName)){
                         LoginController.current.getAlbums().get(i).getPhotos().add(photo);
                         
                         //Update the date fields of the album
                         LoginController.current.getAlbums().get(i).setEarliestPhotoDate();
                         LoginController.current.getAlbums().get(i).setLatestPhotoDate();
                         earliestDate.setText(LoginController.current.getAlbums().get(i).getEarliestPhotoDate().getTime().toString());
                         latestDate.setText(LoginController.current.getAlbums().get(i).getLatestPhotoDate().getTime().toString());
                         
                         //Update the number of photos field
                         numPhotos.setText(new Integer(LoginController.current.getAlbums().get(i).getSize()).toString()); 
                        
                         for(int j = 0; j < AdminHomeController.users.size(); j++){
                             if(AdminHomeController.users.get(j).getUsername().equals(LoginController.current.getUsername())){
                            	 AdminHomeController.users.get(j).setAlbums(LoginController.current.getAlbums());
                                 LoginController.current = AdminHomeController.users.get(j);
                                 break;
                             }
                         }
                         break;
                     }
                 }
                 
                 //Change the view
                 FileInputStream fis = new FileInputStream(selectFile.getPath());
                 Label label = new Label();
                 Image image = new Image(fis);
                 ImageView iView = new ImageView(image);
                 iView.setId(photo.getName());
                 iView.setFitHeight(200);
                 iView.setFitWidth(200);
                 label.setGraphic(iView);

                 label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                     @Override
                     public void handle(MouseEvent event) {
                         methodHelper(photo, label);
                     }
                 });
                 photoPane.getChildren().add(label);
                 
                 try {
                	 AdminHomeController.write(AdminHomeController.users);
                 } catch(Exception e) {
                	 e.printStackTrace();
                 }

                 
            } catch(Exception e) {
            	e.printStackTrace();
            }
        
        }else {
        	return;
        }
        
	}
	
	/**
	 * Method to delete the photo
	 * @param photo Selected photo
	 * @param label label of the photo
	 */
	public void deletePhoto(Photo photo, Label label) {
		//Find and remove photo
		
		for(int i = 0; i < LoginController.current.getAlbums().size(); i++) {
			if(LoginController.current.getAlbums().get(i).getName().equals(name)) {
				for(int j = 0; j < LoginController.current.getAlbums().get(i).getPhotos().size(); j++) {
					if(LoginController.current.getAlbums().get(i).getPhotos().get(j).getName().equals(photo.getName())) {
						LoginController.current.getAlbums().get(i).getPhotos().remove(photo);
						LoginController.current.getAlbums().get(i).setEarliestPhotoDate();
                        LoginController.current.getAlbums().get(i).setLatestPhotoDate();
                        earliestDate.setText(LoginController.current.getAlbums().get(i).getEarliestPhotoDate().getTime().toString());
                        latestDate.setText(LoginController.current.getAlbums().get(i).getLatestPhotoDate().getTime().toString());
                        break;
					}
				}
			}
		}
		
		//Find the user and update its albums
		for(int k = 0; k < AdminHomeController.users.size(); k++) {
			if(AdminHomeController.users.get(k).getUsername().equals(LoginController.current.getUsername())){
				AdminHomeController.users.get(k).setAlbums(LoginController.current.getAlbums());
                break;
            }
		}
		
		photoPane.getChildren().remove(label);
		
		try {
            AdminHomeController.write(AdminHomeController.users);
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	/**
	 * Method to change caption
	 * @param photo selected photo
	 */
	public void editCaption(Photo photo) {
		//Prompts a dialog box that asks for a caption for the selected photo
		
		TextInputDialog capDialog = new TextInputDialog("Enter Caption");
		capDialog.setTitle("Caption/Recaption");
		capDialog.setHeaderText("Caption");
		capDialog.setContentText("Enter caption");


        Optional<String> newCap = capDialog.showAndWait();

        if (newCap.isPresent()){
            photo.setCaption(newCap.get());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Caption cannot be blank");
            alert.show();
            return;
        }
        
        photoPane.getChildren().clear();
        photosView(name);

        for(int i = 0; i < LoginController.current.getAlbums().size(); i++) {
        	if(LoginController.current.getAlbums().get(i).getName().equals(name)) {
        		for(int j = 0; j < LoginController.current.getAlbums().get(i).getPhotos().size(); j++) {
        			if(LoginController.current.getAlbums().get(i).getPhotos().get(j).getName().equals(photo.getName())) {
        				LoginController.current.getAlbums().get(i).getPhotos().set(j, photo);
        			}
        		}
        	}
        }
        
        //write the changes into the data file
        try {
            AdminHomeController.write(AdminHomeController.users);
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	/**
	 * Method to display slideshow 
	 */
	@FXML
	public void displaySlideshow() {
		
		for(int i = 0; i < LoginController.current.getAlbums().size(); i++) {
			if(LoginController.current.getAlbums().get(i).getName().equals(name) && LoginController.current.getAlbums().get(i).getPhotos().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Album is empty");
                alert.showAndWait();
                return;
			}
		}
		try {
            Stage stage = (Stage) displaySlideshow.getScene().getWindow();
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
            
        }catch(Exception e){
            e.printStackTrace();
        }
			
	}
	
	/**
	 * Method to go back to the non admin home screen
	 */
	@FXML
	public void back() {
		try{
			Stage currentStage = (Stage) backButton.getScene().getWindow();
			currentStage.close();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("NonAdminHomeScreen.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method to logout
	 */
	@FXML
	public void logout() {
		//If the user tries to logout go back to the login page
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logging Out");
		alert.setHeaderText("Do you want to log out?");
		
		Optional<ButtonType> answer = alert.showAndWait();
		
		if(answer.get() == ButtonType.OK) {
			try {
				Stage currentStage = (Stage) logoutButton.getScene().getWindow();
			    currentStage.close();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("LoginScreen.fxml"));
				AnchorPane root = (AnchorPane)loader.load();
				Stage stage = new Stage();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
				
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		else {
			alert.close();
		}
	}
}
