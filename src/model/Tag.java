package model;

import java.io.Serializable;

/**
 * Tag class creates the tag object and includes all the information of a tag
 * @author Ansh Shah and Kandarp Patel
 *
 */
public class Tag implements Serializable{

	/**
	 * Name of the Tag
	 */
	String name;
	
	/**
	 * Value of the tag
	 */
	String value;
	
	/**
	 * Constructor - Initializes name and value of the tag
	 * @param name the name of the tag
	 * @param value the value of the tag
	 */
	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	
	/**
	 * Returns the name of the tag
	 * @return String returns the name of the tag
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the value of the tag
	 * @return String the value of the tag
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the name of the tag
	 * @param name the name of the tag
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the value of the tag
	 * @param value the value of the tag
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Checks if two tags are equal
	 * @param tag the tag to be compared with this tag
	 * @return boolean if two tags are equal
	 */
	public boolean equals(Tag tag) {
		if(tag.name == this.name && tag.value == this.value)
			return true;
		
		return false;
	}
}
