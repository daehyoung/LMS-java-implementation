package client.control.data;

import javax.sql.RowSet;
import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.ResourceCopy;

/**
 * Control Object for ResourceCopys
 * 
 * @author Jeremy Lerner
 * @version 0
 *
 */
public class ResourceCopyManager{
	
	/**
	 * Returns an array of ResourceCopy objects with fields matching the initialized fields of a parameter
	 * 
	 * @param A ResourceCopy object with some fields initialized
	 * @return An array of ResourceCopy objects from the Server with fields matching those initialized in the parameter
	 */
	public static ResourceCopy[] getResourceCopy(ResourceCopy searchKey){
		//Check that the Resource exists
		//Write query
		//Query Server
		//Construct array of ResourceCopys
		System.out.println("ResourceCopyManager.getResourceCopy(ResourceCopy) is a stub");
		return null;
	}
	
	/**
	 * Returns a ResourceCopy object with a given ID number
	 * 
	 * @param A ResourceCopy ID number
	 * @return A ResourceCopy object with the given ID number
	 */
	public static ResourceCopy getResourceCopy(int idNumber){
		//Write Query
		//Query Server
		//Construct ResourceCopy object
		System.out.println("ResourceCopyManager.getResourceCopy(int) is a stub");
		return null;
	}
	
	/**
	 * Edits a ResourceCopy entry in the database
	 * 
	 * @param A ResourceCopy object with updated fields. The ID number field must be set in the ResourceCopy.
	 * @return True if successful, false otherwise
	 */
	public static boolean setResourceCopy(ResourceCopy newEntry){
		//Check that the ResourceCopy exists
		//Write Query
		//Query Server
		System.out.println("ResourceCopyManager.setResourceCopy(ResourceCopy) is a stub");
		return false;
	}
	
	/**
	 * Inserts a new ResourceCopy object in the database
	 * 
	 * @param The ResourceCopy object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry's ID number
	 */
	public static int addResourceCopy(ResourceCopy newEntry){
		//Check that the Resource exists
		//Write query
		//Query Server
		//Return new ID number
		System.out.println("ResourceCopyManager.addResourceCopy(ResourceCopy) is a stub");
		return 0; //Stub
	}
	
	/**
	 * Removes a ResourceCopy object from the database
	 * 
	 * @param The ID number of the ResourceCopy to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeResourceCopy(int idNumber){
		//Disable any loans, references, or reserves using this ResourceCopy
		//Write Query
		//Query Server
		System.out.println("ResourceCopyManager.removeResourceCopy(ResourceCopy) is a stub");
		return false;
	}
}