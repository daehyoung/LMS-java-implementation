package client.control.data.entity.resource;
import java.util.Date;

import client.control.data.entity.Entity;

/**
 * Represents a reserve on a Resource
 * 
 * @author Jeremy Lerner
 * @version 3
 *
 */
public class Reserve extends Entity{
	int resource;
	Date reservationDate;
	Date availableDate;
	
	/**
	 * Constructs a new Reserve with a resource ID of 0 and null reservation date
	 */
	public Reserve(){
		super();
		resource = 0;
		reservationDate = null;
		availableDate = null;
	}
	
	/**
	 * Returns the ID number of the resource held on Reserve
	 * 
	 * @return The ID number of the resource held on reserve
	 */
	public int getResource(){
		return resource;
	}
	
	/**
	 * Sets the Reserve's Resource
	 * 
	 * @param The new Resource's ID number
	 */
	public void setResource(int newResource){
		resource = newResource;
	}
	
	/**
	 * Returns the Reserve's reservation date
	 * 
	 * @return The Reserve's reservation date
	 */
	public Date getReservationDate(){
		return reservationDate;
	}
	
	/**
	 * Sets the Reserve's reservation date
	 * 
	 * @param The new reservation date
	 */
	public void setReservationDate(Date newReservationDate){
		reservationDate = newReservationDate;
	}

	/**
	 * Returns the Reserve's available date
	 * 
	 * @return The Reserve's available date
	 */
	public Date getAvailableDate(){
		return availableDate;
	}
	
	/**
	 * Sets the Reserve's available date
	 * 
	 * @param The new available date
	 */
	public void setAvailableDate(Date newAvailableDate){
		reservationDate = newAvailableDate;
	}
}