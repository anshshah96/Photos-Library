package Photos28.view;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

/**
 * Controller class of the admin screen
 * @author Ansh Shah and Kandarp Patel
 *
 */
public class AdminHomeController implements Serializable{

	@FXML Button addUserButton;
	@FXML Button deleteUserButton;
	@FXML Button logoutButton;
	@FXML ScrollPane scrollpane;
	
	/**
	 * the file to store the serializable data in
	 */
	public static final String storeFile = "users.dat";
	
	/**
	 * the list of users
	 */
	public static ArrayList<User> users = new ArrayList<User>();
	
	
	VBox vbox = new VBox();
	
	
	/**
	 * Method to write users' info into the data file 
	 * @param users list of users
	 * @throws IOException
	 */
	public static void write(ArrayList<User> users) throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeFile));
		oos.writeObject(users);
		oos.close();
	}
	
	/**
	 * Method to read from the data file that stores
	 * @return ArrayList<User> the list of users read from the file
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static ArrayList<User> read() throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeFile));
		ArrayList<User> users = (ArrayList<User>) ois.readObject();
		ois.close();
		return users;
	}

	/**
	 * Adds scroll pane
	 * @param user the current user
	 */
	public void addScrollPane(String user){
	    Label label = new Label();
	    label.setId(user);
	    label.setText(user);
	    vbox.getChildren().add(label);
	    scrollpane.setContent(vbox);
	}
	
	/**
	 * Shows users in an information box
	 */
	@FXML
	public void viewUsers() {
		//View users in an pop-up information box
		try {
			users = read();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		String output = "";
		for(int i = 0; i < users.size(); i++) {
			output = output + users.get(i).getUsername() + "\n";
		}
		
		
		Alert userList = new Alert(AlertType.INFORMATION);
		userList.setTitle("List of users");
		userList.setHeaderText("Users:");
		userList.setContentText(output);
		userList.show();
	}

	/**
	 * Method to add user
	 */
	@FXML
	public void addUser() {
		//Add a new user
		String username;
		
		//Create a dialog box to get the name of the new user
		TextInputDialog dialog = new TextInputDialog("Enter user name");
        dialog.setTitle("Creating User");
        dialog.setContentText("Please enter the username:");
       
        Optional<String> input = dialog.showAndWait();
        
        if(input.isPresent())
        	username = input.get();
        else {
        	Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The username field cannot be blank");
            alert.showAndWait();
            return;
        }
        
        File usersFile = new File("users.dat");
        if(usersFile.exists()) {
        	try {
                users = read();
            }catch(Exception e){
                e.printStackTrace();
            }
        
	        
	        //Check to see if the user already exists
	        for(int i = 0; i < users.size(); i++) {
	        	if(username.equals(users.get(i).getUsername())) {
	        		 Alert alert = new Alert(AlertType.ERROR);
	                 alert.setTitle("Error");
	                 alert.setContentText("The user already exists");
	                 alert.showAndWait();
	                 return;
	        	}
	        }
	        
	        //The user doesn't already exist -- Create a new user with the given username
	        User user =  new User(username);
	        users.add(user);
	        
	        try {
	            write(users);
	        }catch (Exception e){
	            e.printStackTrace();
	        }
        }
        else {
        	User user = new User(username);
        	users.add(user);
        	 try {
                 write(users);
             } catch (Exception e){
                 e.printStackTrace();
             }
        }
        
        
	}
	
	/**
	 * Method to delete user 
	 * @throws IOException
	 */
	@FXML
	public void deleteUser() throws IOException {
		//Gets the username and searches to find that user and deletes it or prompts an error if user not found
		String username = null;
		
		//Prompt a dialog box to get the username of the user that is being deleted
		TextInputDialog dialog = new TextInputDialog("Enter User Name");
	    dialog.setTitle("Deleteing User");
	    dialog.setContentText("Please enter username of the user:");
	    Optional<String> answer = dialog.showAndWait();	
	    
	    if(answer.isPresent()) {
	    	username = answer.get();
	    }else {
	    	Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The username field cannot be blank");
            alert.showAndWait();
            return;
	    }
	    
	    try {
	    	users = read();
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	    
	    //Try to find the user with the same username
	    int i;
	    for(i = 0; i < users.size(); i++) {
	    	if(username.equals(users.get(i).getUsername())) {
	    		Alert alert = new Alert(AlertType.CONFIRMATION) ;
	    		alert.setTitle("Deleting User");
	    		alert.setHeaderText("Are you sure you want to delete the user?");
	    		Optional<ButtonType> result = alert.showAndWait();
	    		if(result.get() == ButtonType.OK) {
	    			users.remove(users.get(i));
	    	        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeFile));
	    	        oos.writeObject(users);
	    	        return;
	    		}
	    		
	    	}
	    }
	    
	    //User not found
	    if(i == users.size()) {
	    	 Alert alert = new Alert(AlertType.ERROR);
	         alert.setTitle("User not found");
	         alert.setContentText("No use with the given username exists");
	         alert.show();
	         return;
	    }
	}
	
	/**
	 * Method to log admin out and go back to login screen
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
