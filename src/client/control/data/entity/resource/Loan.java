package client.control.data.entity.resource;
import java.util.Date;

import client.control.data.entity.Entity;

/**
 * Represents a Loan of a Resource to a User
 * 
 * @author Jeremy Lerner
 * @version 3
 */
public class Loan extends Entity{
	int resourceCopy;
	int userID;
	Date checkOutDate;
	Date dueDate;
	Date checkInDate;
	float fineAmount;
	
	/**
	 * Constructs a new Loan with a Loan ID, ResourceCopy ID, and fine amount of 0, and check-in/check-out/due dates set to null
	 */
	public Loan(){
		super();
		resourceCopy = 0;
		checkOutDate = null;
		dueDate = null;
		checkInDate = null;
		fineAmount = 0;
	}
	
	/**
	 * Returns the User ID of the User for whom the Loan is
	 * 
	 * @return The User ID of the User for whom the Loan is
	 */
	public int getUserID(){
		return userID;
	}
	/**
	 * Sets the User for whom the Loan is
	 * 
	 * @param The ID of the User for whom the Loan should be
	 */
	public void setUserID(int newUserID){
		userID = newUserID;
	}
	
	/**
	 * Returns the ID number of the ResourceCopy associated with the Loan
	 * 
	 * @return The ID number of the ResourceCopy associated with the Loan
	 */
	public int getResourceCopy(){
		return resourceCopy;
	}
	/**
	 * Sets the ResourceCopy associated with the Loan
	 * 
	 * @param The new ResourceCopy's ID number
	 */
	public void setResourceCopy(int newResourceCopy){
		resourceCopy = newResourceCopy;
	}
	
	/**
	 * Returns the Loan's check-out date
	 * 
	 * @return The Loan's check-out date
	 */
	public Date getCheckOutDate(){
		return checkOutDate;
	}
	/**
	 * Sets the Loan's check-out date
	 * 
	 * @param The new check-out date
	 */
	public void setCheckOutDate(Date newCheckOutDate){
		checkOutDate = newCheckOutDate;
	}
	/**
	 * Returns the Loan's check-in date
	 * 
	 * @return The Loan's check-in date
	 */
	public Date getCheckInDate(){
		return checkInDate;
	}
	/**
	 * Sets the Loan's check-in date
	 * 
	 * @param The new check-in date
	 */
	public void setCheckInDate(Date newCheckInDate){
		checkInDate = newCheckInDate;
	}
	/**
	 * Returns the Loan's due date
	 * 
	 * @return The Loan's due date
	 */
	public Date getDueDate(){
		return dueDate;
	}
	/**
	 * Sets the Loan's due date
	 * 
	 * @param The new due date
	 */
	public void setDueDate(Date newDueDate){
		dueDate = newDueDate;
	}
	
	/**
	 * Returns the fine amount on the Loan
	 * 
	 * @return The Fine
	 */
	public float getFineAmount(){
		return fineAmount;
	}
	
	/**
	 * Sets the fine amount on the Loan
	 * 
	 * @param The new fine amount
	 */
	public void setFineAmount(float newFineAmount){
		fineAmount = newFineAmount;
	}
}