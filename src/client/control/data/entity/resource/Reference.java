package client.control.data.entity.resource;
import java.util.Date;

import client.control.data.entity.Entity;

/**
 * Represents a Reference period of a Resource under a User's account
 * 
 * @author Jeremy Lerner
 * @version 4
 */
public class Reference extends Entity{
	int resourceCopy;
	Date startDate;
	Date endDate;
	
	/**
	 * Constructs a new Reference with a Reference ID, ResourceCopy ID, and fine amount of 0, and check-in/check-out/due dates set to null
	 */
	public Reference(){
		super();
		resourceCopy = 0;
		startDate = null;
		endDate = null;
	}
	/**
	 * Copy constructor
	 * 
	 * @param Reference to copy
	 */
	public Reference(Reference original){
		super(original);
		resourceCopy = original.getResourceCopy();
		startDate = original.getStartDate();
		endDate = original.getEndDate();
	}
	/**
	 * Constructs a new Reference with all fields specified
	 * 
	 * @param value for ID
	 * @param value for Resource Copy
	 * @param value for Start Date
	 * @param value for End Date
	 */
	public Reference(int newID, int newResourceCopy, Date newStartDate, Date newEndDate){
		super(newID);
		resourceCopy = newResourceCopy;
		startDate = newStartDate;
		endDate = newEndDate;
	}
	
	/**
	 * Returns the ID number of the ResourceCopy associated with the Reference
	 * 
	 * @return The ID number of the ResourceCopy associated with the Reference
	 */
	public int getResourceCopy(){
		return resourceCopy;
	}
	
	/**
	 * Returns the Reference's start date
	 * 
	 * @return The Reference's start date
	 */
	public Date getStartDate(){
		return startDate;
	}
	/**
	 * Returns the Reference's end date
	 * 
	 * @return The Reference's end date
	 */
	public Date getEndDate(){
		return endDate;
	}
	
	public boolean checkValid(){
		return (endDate.after(startDate));
	}
}