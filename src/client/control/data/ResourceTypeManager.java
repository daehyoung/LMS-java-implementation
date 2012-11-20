package client.control.data;

import javax.sql.RowSet;
import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.ResourceType;

/**
 * Control Object for ResourceTypes
 * 
 * @author Jeremy Lerner
 * @version 0
 *
 */
public class ResourceTypeManager{
	
	/**
	 * Returns an array of ResourceType objects with fields matching the initialized fields of a parameter
	 * 
	 * @param A ResourceType object with some fields initialized
	 * @return An array of ResourceType objects from the Server with fields matching those initialized in the parameter
	 */
	public static ResourceType[] getResourceType(ResourceType searchKey){
		//Write query
		//Query Server
		//Construct array of ResourceTypes
		System.out.println("ResourceTypeManager.getResourceType(ResourceType) is a stub");
		return null;
	}
	
	/**
	 * Returns a ResourceType object with a given ID number
	 * 
	 * @param A ResourceType ID number
	 * @return A ResourceType object with the given ID number
	 */
	public static ResourceType getResourceType(int idNumber){
		//Write Query
		//Query Server
		//Construct ResourceType object
		System.out.println("ResourceTypeManager.getResourceType(int) is a stub");
		return null;
	}
	
	/**
	 * Edits a ResourceType entry in the database
	 * 
	 * @param A ResourceType object with updated fields. The ID number field must be set in the ResourceType.
	 * @return True if successful, false otherwise
	 */
	public static boolean setResourceType(ResourceType newEntry){
		//Check that the ResourceType exists
		//Check that no ResourceTypes share the name
		//Write Query
		//Query Server
		System.out.println("ResourceTypeManager.setResourceType(ResourceType) is a stub");
		return false;
	}
	
	/**
	 * Inserts a new ResourceType object in the database
	 * 
	 * @param The ResourceType object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry's ID number
	 */
	public static int addResourceType(ResourceType newEntry){
		//Check that no ResourceTypes share the name
		//Write query
		//Query Server
		//Return new ID number
		System.out.println("ResourceTypeManager.addResourceType(ResourceType) is a stub");
		return 0; //Stub
	}
	
	/**
	 * Removes a ResourceType object from the database
	 * 
	 * @param The ID number of the ResourceType to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeResourceType(int idNumber){
		//Remove all the resources of this type
		//Write Query
		//Query Server
		System.out.println("ResourceTypeManager.removeResourceType(ResourceType) is a stub");
		return false;
	}
}