package client.control.data.entity.management;

import java.util.Date;

/**
 * Represents a TO-DO Note with a description, start date and end date
 * 
 * @author Jeremy Lerner
 * @version 1
 */
public class ToDo {
	int idNumber;
	Date startDate;
	Date endDate;
	String description;

	/**
	 * Constructs a new ToDo with all fields set to null
	 */
	public ToDo() {
		startDate = null;
		endDate = null;
		description = null;
	}
	
	/**
	 * Returns the ToDo's ID number
	 * 
	 * @return The ID number
	 */
	public int getID(){
		return idNumber;
	}
	
	/**
	 * Sets the ToDo's ID number
	 * 
	 * @param The new ID number
	 */
	public void setID(int newID){
		idNumber = newID;
	}
	
	/**
	 * Returns the ToDo's description
	 * 
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the ToDo's description
	 * 
	 * @param The new description
	 */
	public void setDescription(String newDescription) {
		description = newDescription;
	}

	/**
	 * Returns the ToDo's start date
	 * 
	 * @return The start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the ToDo's start date
	 * 
	 * @param The new start date
	 */
	public void setStartDate(Date newStartDate) {
		startDate = newStartDate;
	}

	/**
	 * Returns the ToDo's end date
	 * 
	 * @return The end date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the ToDo's end date
	 * 
	 * @param The new end date
	 */
	public void setEndDate(Date newEndDate) {
		endDate = newEndDate;
	}
}