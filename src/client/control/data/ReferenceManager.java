package client.control.data;

import javax.sql.RowSet;
import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.Reference;

/**
 * Control Object for References
 * 
 * @author Jeremy Lerner
 * @version 0
 *
 */
public class ReferenceManager{
	
	/**
	 * Returns an array of Reference objects with fields matching the initialized fields of a parameter
	 * 
	 * @param A Reference object with some fields initialized
	 * @return An array of Reference objects from the Server with fields matching those initialized in the parameter
	 */
	public static Reference[] getReference(Reference searchKey){
		//Write query
		//Query Server
		//Construct array of References
		System.out.println("ReferenceManager.getReference(Reference) is a stub");
		return null;
	}
	
	/**
	 * Returns a Reference object with a given ID number
	 * 
	 * @param A Reference ID number
	 * @return A Reference object with the given ID number
	 */
	public static Reference getReference(int idNumber){
		//Write Query
		//Query Server
		//Construct Reference object
		System.out.println("ReferenceManager.getReference(int) is a stub");
		return null;
	}
	
	/**
	 * Edits a Reference entry in the database
	 * 
	 * @param A Reference object with updated fields. The ID number field must be set in the Reference.
	 * @return True if successful, false otherwise
	 */
	public static boolean setReference(Reference newEntry){
		//Check that the Reference Exists
		//Write Query
		//Query Server
		System.out.println("ReferenceManager.setReference(Reference) is a stub");
		return false;
	}
	
	/**
	 * Inserts a new Reference object in the database
	 * 
	 * @param The Reference object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry's ID number
	 */
	public static int addReference(Reference newEntry){
		//Check that the ResourceCopy is not on loan, on reference, or on reserve
		//Write query
		//Query Server
		//Return new ID number
		System.out.println("ReferenceManager.addReference(Reference) is a stub");
		return 0; //Stub
	}
	
	/**
	 * Removes a Reference object from the database
	 * 
	 * @param The ID number of the Reference to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeReference(int idNumber){
		//Write Query
		//Query Server
		System.out.println("ReferenceManager.removeReference(Reference) is a stub");
		return false;
	}
}