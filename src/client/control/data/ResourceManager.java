package client.control.data;

import javax.sql.RowSet;
import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.Resource;

/**
 * Control Object for Resources
 * 
 * @author Jeremy Lerner
 * @version 0
 *
 */
public class ResourceManager{
	
	/**
	 * Returns an array of Resource objects with fields matching the initialized fields of a parameter
	 * 
	 * @param A Resource object with some fields initialized
	 * @return An array of Resource objects from the Server with fields matching those initialized in the parameter
	 */
	public static Resource[] getResource(Resource searchKey){
		//Write query
		//Query Server
		//Construct array of Resources
		System.out.println("ResourceManager.getResource(Resource) is a stub");
		return null;
	}
	
	/**
	 * Returns a Resource object with a given ID number
	 * 
	 * @param A Resource ID number
	 * @return A Resource object with the given ID number
	 */
	public static Resource getResource(int idNumber){
		//Write Query
		//Query Server
		//Construct Resource object
		System.out.println("ResourceManager.getResource(int) is a stub");
		return null;
	}
	
	/**
	 * Edits a Resource entry in the database
	 * 
	 * @param A Resource object with updated fields. The ID number field must be set in the Resource.
	 * @return True if successful, false otherwise
	 */
	public static boolean setResource(Resource newEntry){
		//Check that the Resource Exists
		//Write Query
		//Query Server
		System.out.println("ResourceManager.setResource(Resource) is a stub");
		return false;
	}
	
	/**
	 * Inserts a new Resource object in the database
	 * 
	 * @param The Resource object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry's ID number
	 */
	public static int addResource(Resource newEntry){
		//Check that the ResourceType exists
		//Write query
		//Query Server
		//Return new ID number
		System.out.println("ResourceManager.addResource(Resource) is a stub");
		return 0; //Stub
	}
	
	/**
	 * Removes a Resource object from the database
	 * 
	 * @param The ID number of the Resource to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeResource(int idNumber){
		//Remove all copies of the Resource
		//Write Query
		//Query Server
		System.out.println("ResourceManager.removeResource(Resource) is a stub");
		return false;
	}
}