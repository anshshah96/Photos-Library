package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;


/**
 * Album Class creates the album object with information about an album
 * 
 * @author Ansh Shah and Kandarp Patel
 *
 */
public class Album implements Serializable{
	/**
	 * Name of the album
	 */
	String name;
	
	/**
	 * ArrayList of photos in the album
	 */
	ArrayList<Photo> photos;
	
	/**
	 * Date of the earliest photo
	 */
	Calendar earliestPhotoDate;
	
	/**
	 * Date of the latest photo
	 */
	Calendar latestPhotoDate;
	
	/**
	 * Constructor - initializes photos arraylist, name of the album and the earliest and latest dates
	 * @param name the name of the album
	 */
	public Album(String name) {
		this.photos = new ArrayList<Photo>();
		this.name = name;
		earliestPhotoDate = null;
		latestPhotoDate = null;
	}
	
	/**
	 * Returns the name of the album
	 * @return String name of the album
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the list of photos in this album
	 * @return ArrayList<Photo> return the list of photos in this album
	 */
	public ArrayList<Photo> getPhotos(){
		return photos;
	}
	
	/**
	 * Returns the date of the earliest photo
	 * @return Calendar the date of the earliest photo
	 */
	public Calendar getEarliestPhotoDate() {
		return earliestPhotoDate;
	}
	
	/**
	 * Returns the date of the latest photo
	 * @return Calendar the date of the latest photo
	 */
	public Calendar getLatestPhotoDate() {
		return earliestPhotoDate;
	}
	
	/**
	 * returns the number of photos in this album
	 * @return int the number of photos
	 */
	public int getSize() {
		return photos.size();
	}
	
	/**
	 * Sets the name of the album
	 * @param name the name of the album
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the photos list in this album
	 * @param photos the list of photos
	 */
	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}
	
	/**
	 * Goes through the photos list and determines the earliest photo 
	 */
	public void setEarliestPhotoDate() {
		
		if(photos.isEmpty())
			return;
		
		Calendar photoDate = photos.get(0).getDate();
	
		for(int i = 1; i < photos.size(); i++) {
			if(photos.get(i).getDate().before(photoDate))
				photoDate = photos.get(i).getDate();
		}
		
		earliestPhotoDate = photoDate;
		return;
	}
	
	/**
	 * Goes through the photos list and determines the latest photo
	 */
	public void setLatestPhotoDate() {
		if(photos.isEmpty())
			return;
		
		Calendar photoDate = photos.get(0).getDate();
		
		for(int i = 1; i < photos.size(); i++) {
			if(photos.get(i).getDate().after(photoDate))
				photoDate = photos.get(i).getDate();
		}
		
		latestPhotoDate = photoDate;
		return;
	}
	
	
	
}
