package client.control.data;

import javax.sql.RowSet;
import client.serverinterface.ServerInterface;
import client.control.data.entity.management.ToDo;

/**
 * Control Object for ToDos
 * 
 * @author Jeremy Lerner
 * @version 2
 *
 */
public class ToDoManager{
	
	/**
	 * Returns an array of ToDo objects with fields matching the initialized fields of a parameter
	 * 
	 * @param A ToDo object with some fields initialized
	 * @return An array of ToDo objects from the Server with fields matching those initialized in the parameter
	 */
	public static ToDo[] getToDo(ToDo searchKey){
		//Write query
		//Query Server
		//Construct array of ToDos
		System.out.println("ToDoManager.getToDo(ToDo) is a stub");
		return null;
	}
	
	/**
	 * Returns a ToDo object with a given ID number
	 * 
	 * @param A ToDo ID number
	 * @return A ToDo object with the given ID number
	 */
	public static ToDo getToDo(int idNumber){
		//Write Query
		//Query Server
		//Construct ToDo object
		System.out.println("ToDoManager.getToDo(int) is a stub");
		return null;
	}
	
	/**
	 * Edits a ToDo entry in the database
	 * 
	 * @param A ToDo object with updated fields. The ID number field must be set in the ToDo.
	 * @return True if successful, false otherwise
	 */
	public static boolean setToDo(ToDo newEntry){
		//Check that the ToDo Exists
		//Write Query
		//Query Server
		System.out.println("ToDoManager.setToDo(ToDo) is a stub");
		return false;
	}
	
	/**
	 * Inserts a new ToDo object in the database
	 * 
	 * @param The ToDo object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry's ID number
	 */
	public static int addToDo(ToDo newEntry){
		//Write query
		//Query Server
		//Return new ID number
		System.out.println("ToDoManager.addToDo(ToDo) is a stub");
		return 0; //Stub
	}
	
	/**
	 * Removes a ToDo object from the database
	 * 
	 * @param The ID number of the ToDo to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeToDo(int idNumber){
		//Write Query
		//Query Server
		System.out.println("ToDoManager.removeToDo(ToDo) is a stub");
		return false;
	}
}