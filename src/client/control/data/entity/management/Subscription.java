package client.control.data.entity.management;

import java.util.Date;

/**
 * Represents a subscription to a magazine
 * @author Jeremy Lerner
 * @version 0
 *
 */
public class Subscription{
	
	String title;
	Date expirationDate;
	
	/**
	 * Constructs a new Subscription object will all fields set to null
	 */
	public Subscription(){
		title = null;
		expirationDate = null;
	}
	
	/**
	 * Returns the magazine's title
	 * @return The title
	 */
	public String getTitle(){
		return title;
	}
	
	/**
	 * Sets the magazine's title
	 * @param The new title
	 */
	public void setTitle(String newTitle){
		title = newTitle;
	}
	
	/**
	 * Returns the Subscription's expiration date
	 * @return The expiration date
	 */
	public Date getExpirationDate(){
		return expirationDate;
	}
	
	/**
	 * Sets the subscription's expiration date
	 * @param The new expiration date
	 */
	public void setExpirationDate(Date newExpirationDate){
		expirationDate = newExpirationDate;
	}
}