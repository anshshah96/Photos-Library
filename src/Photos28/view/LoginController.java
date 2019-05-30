package Photos28.view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;

/**
 * Controller class for the login screen
 * @author Ansh Shah and Kandarp Patel
 *
 */
public class LoginController {

	@FXML Button loginButton;
	@FXML TextField usernameField;
	
	/**
	 * Current user
	 */
	public static User current;

	/**
	 * Method to handle login button
	 */
	@FXML
	public void handleLoginButton() {
		
		//Check to see if there is something in the username field
		if(usernameField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Login Error");
			alert.setHeaderText("The Username Field Cannot be Empty");
			alert.showAndWait();
			return;
		}
		
		String username = usernameField.getText();
		
		//Stock User trying to log in
		if(username.equals("stock")) {
			try {
				AdminHomeController.users = AdminHomeController.read();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for(int i = 0; i < AdminHomeController.users.size(); i++) {
				if(AdminHomeController.users.get(i).getUsername().equals("stock")) {
					try{
						Stage currentStage = (Stage) loginButton.getScene().getWindow();
	                    currentStage.close();
						current = AdminHomeController.users.get(i);
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("NonAdminHomeScreen.fxml"));
						AnchorPane root = (AnchorPane)loader.load();
						Stage stage = new Stage();
						Scene scene = new Scene(root);
						stage.setScene(scene);
						stage.show();
						
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
		
		//Admin trying to log in
		else if(username.equals("admin")) {
			try {
				Stage currentStage = (Stage) loginButton.getScene().getWindow();
                currentStage.close();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("AdminHomeScreen.fxml"));
				Parent root = (Parent)loader.load();
				Stage stage = new Stage();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		//Normal User trying to log in
		else {
			
			boolean found = false;
			 try {
				 AdminHomeController.users = AdminHomeController.read();
	         }catch(Exception e){
	        	 e.printStackTrace();
	         }
			 
			 for(int i = 0; i < AdminHomeController.users.size(); i++) {
				 if(username.equals(AdminHomeController.users.get(i).getUsername())) {
					 found = true;
					 try {
						Stage currentStage = (Stage) loginButton.getScene().getWindow();
	                    currentStage.close();
						current = AdminHomeController.users.get(i);
	                    FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("NonAdminHomeScreen.fxml"));
						AnchorPane root = (AnchorPane)loader.load();
						Stage stage = new Stage();
						Scene scene = new Scene(root);
						stage.setScene(scene);
						stage.show();
					
					}catch(Exception e) {
						e.printStackTrace();
					}
					
				 }
			}
			
			 if(found == false) {
				//User with the input username not found - prompt an error
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Login Error");
				alert.setHeaderText("The user does not exist");
				alert.showAndWait();
				return;
			 }
		}
		
		
	}

	
}
