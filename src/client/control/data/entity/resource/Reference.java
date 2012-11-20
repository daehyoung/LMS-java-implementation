package client.control.data.entity.resource;
import java.util.Date;

import client.control.data.entity.Entity;

/**
 * Represents a Reference period of a Resource under a User's account
 * 
 * @author Jeremy Lerner
 * @version 2
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
	 * Returns the ID number of the ResourceCopy associated with the Reference
	 * 
	 * @return The ID number of the ResourceCopy associated with the Reference
	 */
	public int getResourceCopy(){
		return resourceCopy;
	}
	/**
	 * Sets the ResourceCopy associated with the Reference
	 * 
	 * @param The new ResourceCopy's ID number
	 */
	public void setResourceCopy(int newResourceCopy){
		resourceCopy = newResourceCopy;
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
	 * Sets the Reference's start date
	 * 
	 * @param The new start date
	 */
	public void setStartDate(Date newStartDate){
		startDate = newStartDate;
	}
	/**
	 * Returns the Reference's end date
	 * 
	 * @return The Reference's end date
	 */
	public Date getEndDate(){
		return endDate;
	}
	/**
	 * Sets the Reference's end date
	 * 
	 * @param The new end date
	 */
	public void setEndDate(Date newEndDate){
		endDate = newEndDate;
	}
}