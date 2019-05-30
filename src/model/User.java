package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * User class creates a user object and includes all the information of a user
 * @author anshshah
 *
 */
public class User implements Serializable{

	/**
	 * username of the user
	 */
	String username;
	
	/**
	 * list of albums of the user
	 */
	ArrayList<Album> albums;
	
	/**
	 * Constructor - initializes the username and the albums list
	 * @param username the username of the user
	 */
	public User(String username) {
		
		this.username = username;
		this.albums = new ArrayList<Album>();
		
	}
	
	/**
	 * Returns the username of the user
	 * @return String the username of the user
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Returns the list of albums of the user
	 * @return ArrayList<Album> the list of albums
	 */
	public ArrayList<Album> getAlbums(){
		return albums;
	}
	
	/**
	 * Sets the list of albums
	 * @param albums the list of albums
	 */
	public void setAlbums(ArrayList<Album> albums) {
		this.albums = albums;
	}
	
	
	
}
