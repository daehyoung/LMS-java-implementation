package client.control.data;

import javax.sql.RowSet;
import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.Reserve;

/**
 * Control Object for Reserves
 * 
 * @author Jeremy Lerner
 * @version 0
 *
 */
public class ReserveManager{
	
	/**
	 * Returns an array of Reserve objects with fields matching the initialized fields of a parameter
	 * 
	 * @param A Reserve object with some fields initialized
	 * @return An array of Reserve objects from the Server with fields matching those initialized in the parameter
	 */
	public static Reserve[] getReserve(Reserve searchKey){
		//Write query
		//Query Server
		//Construct array of Reserves
		System.out.println("ReserveManager.getReserve(Reserve) is a stub");
		return null;
	}
	
	/**
	 * Returns a Reserve object with a given ID number
	 * 
	 * @param A Reserve ID number
	 * @return A Reserve object with the given ID number
	 */
	public static Reserve getReserve(int idNumber){
		//Write Query
		//Query Server
		//Construct Reserve object
		System.out.println("ReserveManager.getReserve(int) is a stub");
		return null;
	}
	
	/**
	 * Edits a Reserve entry in the database
	 * 
	 * @param A Reserve object with updated fields. The ID number field must be set in the Reserve.
	 * @return True if successful, false otherwise
	 */
	public static boolean setReserve(Reserve newEntry){
		//Check that the Reserve Exists
		//Write Query
		//Query Server
		System.out.println("ReserveManager.setReserve(Reserve) is a stub");
		return false;
	}
	
	/**
	 * Inserts a new Reserve object in the database
	 * 
	 * @param The Reserve object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry's ID number
	 */
	public static int addReserve(Reserve newEntry){
		//Check that the Resource and User exist
		//Check that the Resource is not on Reference
		//Write query
		//Query Server
		//Return new ID number
		System.out.println("ReserveManager.addReserve(Reserve) is a stub");
		return 0; //Stub
	}
	
	/**
	 * Removes a Reserve object from the database
	 * 
	 * @param The ID number of the Reserve to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeReserve(int idNumber){
		//Write Query
		//Query Server
		System.out.println("ReserveManager.removeReserve(Reserve) is a stub");
		return false;
	}
}