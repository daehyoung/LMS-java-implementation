package client.control.data;

import javax.sql.RowSet;
import client.serverinterface.ServerInterface;
import client.control.data.entity.management.Subscription;

/**
 * Control Object for Subscriptions
 * 
 * @author Jeremy Lerner
 * @version 0
 *
 */
public class SubscriptionManager{
	
	/**
	 * Returns an array of Subscription objects with fields matching the initialized fields of a parameter
	 * 
	 * @param A Subscription object with some fields initialized
	 * @return An array of Subscription objects from the Server with fields matching those initialized in the parameter
	 */
	public static Subscription[] getSubscription(Subscription searchKey){
		//Write query
		//Query Server
		//Construct array of Subscriptions
		System.out.println("SubscriptionManager.getSubscription(Subscription) is a stub");
		return null;
	}
	
	/**
	 * Returns a Subscription object with a given ID number
	 * 
	 * @param A Subscription ID number
	 * @return A Subscription object with the given ID number
	 */
	public static Subscription getSubscription(int idNumber){
		//Write Query
		//Query Server
		//Construct Subscription object
		System.out.println("SubscriptionManager.getSubscription(int) is a stub");
		return null;
	}
	
	/**
	 * Edits a Subscription entry in the database
	 * 
	 * @param A Subscription object with updated fields. The ID number field must be set in the Subscription.
	 * @return True if successful, false otherwise
	 */
	public static boolean setSubscription(Subscription newEntry){
		//Check that the Subscription exists
		//Write Query
		//Query Server
		System.out.println("SubscriptionManager.setSubscription(Subscription) is a stub");
		return false;
	}
	
	/**
	 * Inserts a new Subscription object in the database
	 * 
	 * @param The Subscription object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry's ID number
	 */
	public static int addSubscription(Subscription newEntry){
		//Write query
		//Query Server
		//Return new ID number
		System.out.println("SubscriptionManager.addSubscription(Subscription) is a stub");
		return 0; //Stub
	}
	
	/**
	 * Removes a Subscription object from the database
	 * 
	 * @param The ID number of the Subscription to remove from the database
	 * @return True if successful; false otherwise
	 */
	public static boolean removeSubscription(int idNumber){
		//Write Query
		//Query Server
		System.out.println("SubscriptionManager.removeSubscription(Subscription) is a stub");
		return false;
	}
}