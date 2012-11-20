package client.control.data;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import communication.RequestPacket;

import client.serverinterface.ServerInterface;
import client.control.data.entity.user.Patron;
import client.control.data.entity.user.User;

/**
 * Control Object for Users
 * 
 * @author Jeremy Lerner
 * @version 0
 *
 */
@SuppressWarnings("unused")
public class UserManager{
	
	/**
	 * Returns an array of User objects with fields matching the initialized fields of a parameter
	 * 
	 * @param A User object with some fields initialized
	 * @return An array of User objects from the Server with fields matching those initialized in the parameter
	 */
	public static User[] getUser(User searchKey){
		//Write query
		//Query Server
		//Construct array of Users
		System.out.println("UserManager.getUser(User) is a stub");
		return null;
	}
	
	/**
	 * Returns a User object with a given ID number
	 * 
	 * @param A User ID number
	 * @return A User object with the given ID number
	 * @throws SQLException 
	 */
	public static User getUser(int idNumber) throws SQLException{
		//Write Query
		//Query Server
		//Construct User object
		
		String sql = "SELECT * FROM librisDB.user WHERE user_id = "+idNumber;
	    Patron user = new Patron();
		RequestPacket message = new RequestPacket();
		
		message.setCode(RequestPacket.SELECT);
		message.setType(RequestPacket.REQUEST_SQL);
		message.setSqlStatment(sql);
		
		message = ServerInterface.getInstance().send(message);
		CachedRowSet rowSet = message.getRowSet();//ServerInterface.getInstance().request(request);
			
		// Populating empty user object
		while (rowSet.next()) {
			user.setID(rowSet.getInt("user_ID"));	
			user.setFirstName(rowSet.getString("first_name"));
			user.setLastName(rowSet.getString("last_name"));
			user.setEmailAddress(rowSet.getString("email"));
		}
		rowSet.close();
		
		// Returning populated User object
		System.out.println("UserManager.getUser(int) is a stub");
		return user;
	}
	
	/**
	 * Edits a User entry in the database
	 * 
	 * @param A User object with updated fields. The ID number field must be set in the User.
	 * @return True if successful, false otherwise
	 */
	public static boolean setUser(User newEntry){
		//Write Query
		//Query Server
		System.out.println("UserManager.setUser(User) is a stub");
		return false;
	}
	
	/**
	 * Inserts a new User object in the database
	 * 
	 * @param The User object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @param The type of user to add
	 * @return The new entry's ID number
	 */
	public static int addUser(User newEntry, int type){
		//Check that no other users share the same E-mail
		//Write query
		//Query Server
		//Return new ID number
		System.out.println("UserManager.addUser(User) is a stub");
		return 0; //Stub
	}
	
	/**
	 * Removes a User object from the database
	 * 
	 * @param The ID number of the User to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeUser(int idNumber){
		//Check that the User being removed is not the User performing the removal (Sessionmanager.getInstance().getID())
		//Write Query
		//Query Server
		System.out.println("UserManager.removeUser(User) is a stub");
		return false;
	}
}