package client.control.data.entity.resource;
import java.util.Date;

import client.control.data.entity.Entity;

/**
 * Represents a resource held by the library
 * 
 * @author Jeremy Lerner
 * @version 3
 */
public class Resource extends Entity{
	int type;
	String title;
	String creator;
	String company;
	String serialNumber;
	Date publicationDate;
	boolean enabled;
	
	/**
	 * Constructs a new disabled Resource with a type and resource ID of 0, and all other fields set to null
	 */
	public Resource(){
		super();
		type = 0;
		title = null;
		creator = null;
		company = null;
		serialNumber = null;
		publicationDate = null;
		enabled = false;
	}

	/**
	 * Returns the Resource Type ID
	 * 
	 * @return The Resource Type ID
	 */
	public int getResourceType(){
		return type;
	}
	/**
	 * Sets the Resource Type ID
	 * 
	 * @param The new Resource Type ID
	 */
	public void setResourceType(int newResourceType){
		type = newResourceType;
	}

	/**
	 * Returns the title
	 * 
	 * @return The title
	 */
	public String getTitle(){
		return title;
	}
	/**
	 * Sets the title
	 * 
	 * @param The new title
	 */
	public void setTitle(String newTitle){
		title = newTitle;
	}

	/**
	 * Returns the creator
	 * 
	 * @return The creator
	 */
	public String getCreator(){
		return creator;
	}
	/**
	 * Sets the creator
	 * 
	 * @param The new creator
	 */
	public void setCreator(String newCreator){
		creator = newCreator;
	}

	/**
	 * Returns the company
	 * 
	 * @return The company
	 */
	public String getCompany(){
		return company;
	}
	/**
	 * Sets the company
	 * 
	 * @param The new company
	 */
	public void setCompany(String newCompany){
		company = newCompany;
	}

	/**
	 * Returns the serial number
	 * 
	 * @return The serial number
	 */
	public String getSerialNumber(){
		return serialNumber;
	}
	/**
	 * Sets the serial number
	 * 
	 * @param The new serial number
	 */
	public void setSerialNumber(String newSerialNumber){
		serialNumber = newSerialNumber;
	}

	/**
	 * Returns the publication date
	 * 
	 * @return The publication date
	 */
	public Date getPublicationDate(){
		return publicationDate;
	}
	/**
	 * Sets the publication date
	 * 
	 * @param The new publication date
	 */
	public void setPublicationDate(Date newPublicationDate){
		publicationDate = newPublicationDate;
	}

	/**
	 * Returns true if the Resource is enabled, false otherwise
	 * 
	 * @return True if the resource is enabled, false otherwise
	 */
	public boolean getEnabled(){
		return enabled;
	}
	/**
	 * Enables or disables the Resource based on a boolean parameter
	 * 
	 * @param True for enabling, False for disabling
	 */
	public void setEnabled(boolean newEnabled){
		enabled = newEnabled;
	}
}