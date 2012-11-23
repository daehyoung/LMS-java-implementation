package client.control.data.entity.resource;
import java.util.Date;

import client.control.data.entity.Entity;

/**
 * Represents a reserve on a Resource
 * 
 * @author Jeremy Lerner
 * @version 4
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
	 * Copy constructor
	 * 
	 * @param Reserve object to copy
	 */
	public Reserve(Reserve original){
		super(original);
		resource = original.getResource();
		reservationDate = original.getReservationDate();
		availableDate = original.getAvailableDate();
	}
	/**
	 * Constructs a new Reserve with specified fields
	 * 
	 * @param value for ID
	 * @param value for Resource
	 * @param value for Reservation Date
	 * @param value for Available Date
	 */
	public Reserve(int newID, int newResource, Date newReservationDate, Date newAvailableDate){
		super(newID);
		resource = newResource;
		reservationDate = newReservationDate;
		availableDate = newAvailableDate;
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
	 * Returns the Reserve's reservation date
	 * 
	 * @return The Reserve's reservation date
	 */
	public Date getReservationDate(){
		return reservationDate;
	}
	/**
	 * Returns the Reserve's available date
	 * 
	 * @return The Reserve's available date
	 */
	public Date getAvailableDate(){
		return availableDate;
	}
	
	public boolean checkValid(){
		return (availableDate.after(reservationDate));
	}
}