package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonType;

/**
 * Photo class creates the photo object and includes all the information of a photo
 * 
 * @author Ansh Shah and Kandarp Patel
 *
 */
public class Photo implements Serializable{

	/**
	 * Name of the photo
	 */
	 String name;
	
	 /**
	  * Location of the photo
	  */
	 String location;
	
	 /**
	  * Caption of the photo
	  */
	 String caption;
	
	/**
	 * Date of the photo
	 */
	 Calendar date;
	
	 /**
	  * Tags list of the phobo
	  */
	 ArrayList<Tag> tags;
	
	/**
	 * Constructor - Initializes the name, date, location and the tags list of the photo
	 * @param name the name of the photo
	 * @param date the date of the photo
	 * @param location the location of the photo
	 */
	public Photo(String name, Calendar date, String location) {
		this.name = name;
		this.date = date;
		this.location = location;
		this.tags = new ArrayList<Tag>();
		
		
	}
	
	/**
	 * Returns the name of the photo
	 * @return String the name of the photo
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the location of the photo
	 * @return String the location of the photo
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Returns the caption of the photo
	 * @return String the caption of the photo
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * Returns the Date of the photo
	 * @return Calendar the date of the photo
	 */
	public Calendar getDate() {
		return date;
	}
	
	/**
	 * Returns the list of tags
	 * @return ArrayList<Tag> the list of tags
	 */
	public ArrayList<Tag> getTags(){
		return tags;
	}
	
	/**
	 * Sets the name of the photo
	 * @param name the name of the photo
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the location of the photo
	 * @param location the location of the photo
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * Sets the caption of the photo
	 * @param caption the caption of the photo
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Sets the date of the photo
	 * @param date the date of the photo
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	/**
	 * Sets the list of tags of the photo
	 * @param tags list of tags
	 */
	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}
	
	/**
	 * Adds a tag to the list if it doesn't already exist
	 * @param name name of the tag
	 * @param value value of the tag
	 */
	public void addTag(String name, String value) {
		if(name == null || value == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error Adding New Tag");
			alert.setContentText("Please provide the name and the value of the tag");
			alert.showAndWait();
			Optional<ButtonType> answer = alert.showAndWait();
			if(answer.get() == ButtonType.OK)
				alert.close();
			return;
		}
			
		
		Tag newTag = new Tag(name, value);
		for(int i = 0; i < tags.size(); i++) {
			
			//Tag with the same name and value already exists
			if(tags.get(i).equals(newTag)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error Adding New Tag");
				alert.setContentText("The tag already exists");
				alert.showAndWait();
				Optional<ButtonType> answer = alert.showAndWait();
				if(answer.get() == ButtonType.OK)
					alert.close();
				return;
			}
				
		}
		tags.add(newTag);
		return;
	}
	
	/**
	 * Deletes a tag from the list if it exists
	 * @param tag the tag to be deleted
	 */
	public void deleteTag(Tag tag) {
	
		if(tag.getName() == null || tag.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error Deleting the Tag");
			alert.setContentText("Please provide the name and the value of the tag");
			alert.showAndWait();
			Optional<ButtonType> answer = alert.showAndWait();
			if(answer.get() == ButtonType.OK)
				alert.close();
			return;
		}
			
		//Look for tag and remove
		for(int i = 0; i < tags.size(); i++) {
			if(tags.get(i).equals(tag)) {
				tags.remove(i);
				return;
			}
		}
		
		//Tag not found
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error Deleting the Tag");
		alert.setContentText("No such tag exists");
		alert.showAndWait();
		Optional<ButtonType> answer = alert.showAndWait();
		if(answer.get() == ButtonType.OK)
			alert.close();
		return;
	}
	
	
	
	
}
