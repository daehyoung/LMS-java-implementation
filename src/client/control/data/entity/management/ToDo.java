package client.control.data.entity.management;
import java.util.Date;

import client.control.data.entity.Entity;

/**
 * Represents a TO-DO Note with a description, start date and end date
 * 
 * @author Jeremy Lerner
 * @version 3
 */
public class ToDo extends Entity{
	Date startDate;
	Date endDate;
	String title;
	String description;
	boolean completed;

	/**
	 * Constructs a new non-completed ToDo with all other fields set to null
	 */
	public ToDo() {
		super();
		startDate = null;
		endDate = null;
		title = null;
		description = null;
		completed = false;
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
	 * Returns the ToDo's title
	 * 
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the ToDo's title
	 * 
	 * @param The new title
	 */
	public void setTitle(String newTitle) {
		title = newTitle;
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
	
	/**
	 * Returns true of the ToDo is completed, false otherwise
	 * 
	 * @return True if the ToDo is completed, false otherwise
	 */
	public boolean getCompleted(){
		return completed;
	}
	
	/**
	 * Changes the completion value of the ToDo based on a boolean parameter
	 * 
	 * @param True for complete, False for incomplete
	 */
	public void setCompleted(boolean newCompleted){
		completed = newCompleted;
	}
}