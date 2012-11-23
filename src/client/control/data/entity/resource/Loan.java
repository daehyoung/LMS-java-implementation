package client.control.data.entity.resource;
import java.util.Date;

import client.control.data.entity.Entity;

/**
 * Represents a Loan of a Resource to a User
 * 
 * @author Jeremy Lerner
 * @version 4
 */
public class Loan extends Entity{
	int resourceCopy;
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
	 * Copy constructor
	 * @param The Loan to copy
	 */
	public Loan(Loan original){
		super(original);
		resourceCopy = original.getResourceCopy();
		checkOutDate = original.getCheckOutDate();
		dueDate = original.getDueDate();
		checkInDate = original.getCheckInDate();
		fineAmount = original.getFineAmount();
	}
	/**
	 * Constructs a new Loan with all fields specified
	 * 
	 * @param value for ID
	 * @param value for Resource Copy
	 * @param value for Check Out Date
	 * @param value for Due Date
	 * @param value for Check In Date
	 * @param value for Fine Amount
	 */
	public Loan(int newID, int newResourceCopy, Date newCheckOutDate, Date newDueDate, Date newCheckInDate, float newFineAmount){
		super(newID);
		resourceCopy = newResourceCopy;
		checkOutDate = newCheckOutDate;
		dueDate = newDueDate;
		checkInDate = newCheckInDate;
		fineAmount = newFineAmount;
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
	 * Returns the Loan's check-out date
	 * 
	 * @return The Loan's check-out date
	 */
	public Date getCheckOutDate(){
		return checkOutDate;
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
	 * Returns the Loan's due date
	 * 
	 * @return The Loan's due date
	 */
	public Date getDueDate(){
		return dueDate;
	}
	/**
	 * Returns the fine amount on the Loan
	 * 
	 * @return The Fine
	 */
	public float getFineAmount(){
		return fineAmount;
	}
	
	public boolean checkValid(){
		return (dueDate.after(checkOutDate));
	}
}