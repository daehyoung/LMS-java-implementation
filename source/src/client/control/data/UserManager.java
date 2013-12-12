package client.control.data;

import global.LibrisGlobalInterface;
import java.net.ConnectException;
import java.sql.SQLException;
import util.Utilities;
import com.sun.rowset.CachedRowSetImpl;
import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.Loan;
import client.control.data.entity.resource.Reserve;
import client.control.data.entity.resource.ResourceCopy;
import client.control.data.entity.user.Administrator;
import client.control.data.entity.user.Faculty;
import client.control.data.entity.user.Librarian;
import client.control.data.entity.user.Student;
import client.control.data.entity.user.User;
import client.control.session.ErrorManager;
import client.control.session.RequestException;
import client.control.session.SessionManager;

/**
 * Control Object for Users
 * 
 * @author Jeremy Lerner, Peter Abelseth
 * @version 12
 *
 */
public class UserManager{
	
	/**
	 * Returns an array of User objects with fields matching the initialized fields of a parameter. Returns an empty list should there be no such Users.
	 * 
	 * @param searchKey A User object with some fields initialized
	 * @return An array of User objects from the Server with fields matching those initialized in the parameter
	 */
	public static User[] getUser(User searchKey){
		String sqlSelect = buildSelectStatement(searchKey);
		
		CachedRowSetImpl rowSet = null;
		User[] result = null;
		try{
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		} catch (ConnectException e) {
			ErrorManager.handleTimeOutError(e);
		}
		return result;
	}
	
	/**
	 * Returns a User object with a given ID number
	 * 
	 * @param A User ID number
	 * @return A User object with the given ID number
	 */
	public static User getUser(int idNumber){
		String sqlSelect = "SELECT user_ID, first_name, last_name, email, phone, type, enabled " +
				"FROM user WHERE user_ID = " + idNumber;
		CachedRowSetImpl rowSet = null;
		User result[] = null;
		try{
			rowSet =  ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		} catch (ConnectException e) {
			ErrorManager.handleTimeOutError(e);
		}
		
		if(result != null && result.length != 0)
			return result[0];
		return null;
	}
	
	/**
	 * Edits an existing User entry in the database
	 * 
	 * @param A User object with updated fields. The ID number field must be set in the User.
	 * @return True if successful, false otherwise
	 */
	public static boolean setUser(User newEntry){
		if(!newEntry.getEnabled()){
			if(!checkRemove(newEntry.getID()))
				return false;
		}
		String sqlUpdate = buildUpdateStatement(newEntry);
		boolean success = false;
		try{
			success = ServerInterface.getReference().requestUpdate(sqlUpdate);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		} catch (ConnectException e) {
			ErrorManager.handleTimeOutError(e);
		}
		return success;
	}
	
	/**
	 * Inserts a new User object in the database
	 * 
	 * @param The User object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry
	 */
	public static User addUser(User newEntry){
		if(!checkAdd(newEntry))
			return null;
		String sqlInsert = buildInsertStatement(newEntry);
		int id = 0;
		User newEntity = null;
		try{
			id = ServerInterface.getReference().requestInsert(sqlInsert);
			newEntity = getUser(id);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		} catch (ConnectException e) {
			ErrorManager.handleTimeOutError(e);
		}
		return newEntity;
	}
	
	/**
	 * Removes a User object from the database
	 * 
	 * @param The ID number of the User to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeUser(int idNumber){
		if(!checkRemove(idNumber))
			return false;
		
		String sqlDisable = "UPDATE user SET enabled = FALSE WHERE user_ID = " + idNumber;
		boolean success = false;
		try{
			success = ServerInterface.getReference().requestUpdate(sqlDisable);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		} catch (ConnectException e) {
			ErrorManager.handleTimeOutError(e);
		}
		return success;
	}
	
	/**
	 * Checks the parameters of a new user to ensure they should be added to the system
	 * 
	 * @param entity The user to check
	 * @return True if ok to add, false otherwise
	 */
	private static boolean checkAdd(User entity){
		if(entity.getID() > 0){
			User sameID = UserManager.getUser(entity.getID());
			if(sameID != null){
				ErrorManager.handleError(new IllegalArgumentException("A user with that ID already exists."));
				return false;
			}
		}		
		if(entity.getFirstName() == null || entity.getFirstName().length() == 0){
			ErrorManager.handleError(new IllegalArgumentException("The user must have a first name"));
			return false;
		}
		if(entity.getLastName() == null || entity.getLastName().length() == 0){
			ErrorManager.handleError(new IllegalArgumentException("The user must have a last name"));
			return false;
		}
		if(entity.getEmailAddress() == null || !Utilities.isValidEmail(entity.getEmailAddress())){
			ErrorManager.handleError(new IllegalArgumentException("Invalid email address."));
			return false;
		}
		
		User[] sameEmailUser = getUser(new User(-1,null,null,null,entity.getEmailAddress(),true));
		if(sameEmailUser != null && sameEmailUser.length > 0){
			if(!ErrorManager.getConfirmation("A user with that email already exists. Add anyways?"))
				return false;
		}
		
		User[] sameNameUser = getUser(new User(-1,entity.getFirstName(),entity.getLastName(),null,null,true));
		if(sameNameUser != null && sameNameUser.length > 0){
			if(!ErrorManager.getConfirmation("A user with that name already exists. Add anyways?"))
				return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if a user to ok to disable
	 * 
	 * @param idNumber The id of the user to check
	 * @return True if ok to disable, false otherwise
	 */
	private static boolean checkRemove(int idNumber){
		User currentUser = SessionManager.getReference().getUser();
		if(currentUser == null){
			ErrorManager.handleError(new ControlException("Must be logged in to disable other users."));
			return false;
		}
		if(currentUser.getID() == idNumber){
			ErrorManager.handleError(new ControlException("Cannot disable you own account"));
			return false;
		}
		if(currentUser instanceof Administrator){
			User[] admins = getUser(new Administrator(-1,null,null,null,null,true));
			if(admins == null || admins.length <= 1){
				ErrorManager.handleError(new ControlException("There must be at least one active adminstrator in the system"));
				return false;
			}
		}
			
		User entity = getUser(idNumber);
		if(entity == null){
			ErrorManager.handleError(new IllegalArgumentException("That user does not exist."));
			return false;
		}

		Loan[] loans = LoanManager.getLoan(new Loan(-1,-1,entity.getID(),null,null,null,-1,false));
		if(loans != null){
			for(Loan loan: loans){
				if(loan.getFineAmount() > 0){
					ErrorManager.handleError(new IllegalArgumentException("Cannot disable at this time. The user has outstanding fines."));
					return false;
				}
				if(loan.getCheckInDate() == null){
					ErrorManager.handleError(new IllegalArgumentException("Cannot disable at this time. The user has active loans."));
					return false;
				}
			}
		}
		
		Reserve[] reserves = ReserveManager.getReserve(new Reserve(-1,entity.getID(),-1,null,null,null));
		if(reserves != null && reserves.length > 0){
			ErrorManager.handleError(new IllegalArgumentException("Cannot disable at this time. The user has active reserves."));
			return false;
		}	
		ResourceCopy[] resourceCopies = ResourceCopyManager.getResourceCopy(new ResourceCopy(-1,-1,-1,entity.getID(),true));
		if(resourceCopies != null && resourceCopies.length > 0 ){
			ErrorManager.handleError(new IllegalArgumentException("Cannot disable at this time. The user owns an active resource copy."));
			return false;
		}
		
		return true;
	}
	
	/**
	 * Builds the select statement to search for a User with similar attributes to the given entity
	 * 
	 * @param entity The entity to base the select statement off of
	 * @return sqlSelect The statement that will return all similar entities in the database to the given entity
	 */
	private static String buildSelectStatement(User entity){
		String sqlSelect = "SELECT " +
				"user_ID, first_name, last_name, email, phone, type, enabled " +
				"FROM user WHERE ";
		
		if(entity.getID() > 0)
			sqlSelect += "user_ID = " + entity.getID() + " AND ";
		if(entity.getFirstName() != null && !entity.getFirstName().isEmpty())
			sqlSelect += "first_name LIKE '%" +entity.getFirstName().replace("'", "\\'") + "%' AND ";
		if(entity.getLastName() != null && !entity.getLastName().isEmpty())
			sqlSelect += "last_name LIKE '%" + entity.getLastName().replace("'",  "\\'") + "%' AND ";
		if(entity.getEmailAddress() != null && !entity.getEmailAddress().isEmpty())
			sqlSelect += "email LIKE '%" + entity.getEmailAddress().replace("'","\\'") + "%' AND ";
		sqlSelect += "enabled = " + entity.getEnabled() + " AND ";
		
		if(entity instanceof Student)
			sqlSelect += "type = " + LibrisGlobalInterface.USER_TYPE_STUDENT + " AND ";
		else if(entity instanceof Faculty)
			sqlSelect += "type = " + LibrisGlobalInterface.USER_TYPE_FACULTY + " AND ";
		else if(entity instanceof Librarian)
			sqlSelect += "type = " + LibrisGlobalInterface.USER_TYPE_LIBRARIAN + " AND ";
		else if(entity instanceof Administrator)
			sqlSelect += "type = " + LibrisGlobalInterface.USER_TYPE_ADMIN + " AND ";
		
		if (sqlSelect.endsWith(" AND "))//May not end with AND
			sqlSelect = sqlSelect.substring(0, sqlSelect.length()-5) + " ";	//get rid of extra AND statement
		if(sqlSelect.endsWith(" WHERE "))
			sqlSelect = sqlSelect.substring(0,sqlSelect.length()-" WHERE ".length());
		
		return sqlSelect;
	}
	
	/**
	 * Builds an update statement based on the values of entity that aren't null or 0
	 * @param entity The entity to update with the desired values
	 * @return sqlUpdate The statement that will update the given entity in the database
	 */
	private static String buildUpdateStatement(User entity){
		String sqlUpdate = "UPDATE user SET ";
		
		if(entity.getFirstName() != null && !entity.getFirstName().isEmpty())
			sqlUpdate += "first_name = '" +entity.getFirstName().replace("'", "\\'") + "', ";
		if(entity.getLastName() != null && !entity.getLastName().isEmpty())
			sqlUpdate += "last_name = '" + entity.getLastName().replace("'","\\'") + "', ";
		if(entity.getEmailAddress() != null && !entity.getEmailAddress().isEmpty())
			sqlUpdate += "email = '" + entity.getEmailAddress().replace("'", "\\'") + "', ";
		
		if(entity instanceof Student)
			sqlUpdate += "type = " +  LibrisGlobalInterface.USER_TYPE_STUDENT + ", ";
		else if(entity instanceof Faculty)
			sqlUpdate += "type = " +  LibrisGlobalInterface.USER_TYPE_FACULTY + ", ";
		else if(entity instanceof Librarian)
			sqlUpdate += "type = " +  LibrisGlobalInterface.USER_TYPE_LIBRARIAN + ", ";
		else if(entity instanceof Administrator)
			sqlUpdate += "type = " +  LibrisGlobalInterface.USER_TYPE_ADMIN + ", ";
		
		
		sqlUpdate += "enabled = " + entity.getEnabled() + ", ";
		
		if(sqlUpdate.endsWith(", "))
			sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length()-2) + " ";	//get rid of extra comma
		
		return sqlUpdate + "WHERE user_ID = " + entity.getID();
	}
	
	/**
	 * Builds an insert statement of the given User
	 * 
	 * @param entity The entity to build the insert statement based off of
	 * @return sqlInsert The insert statement that can insert the given entity into the database
	 */
	private static String buildInsertStatement(User entity){
		String attributes = "(", values = "(";
		
		if(entity.getID() > 0 ){
			attributes += "user_ID, ";
			values += entity.getID() + ", ";
		}
		if( entity.getFirstName() != null && !entity.getFirstName().isEmpty()){
			attributes += "first_name, ";
			values += "'" + entity.getFirstName().replace("'", "\\'") + "', ";
		}
		if(entity.getLastName() != null && !entity.getLastName().isEmpty()){
			attributes += "last_name, ";
			values += "'" + entity.getLastName().replace("'",  "\\'") + "', ";
		}
		if(entity.getEmailAddress() != null && !entity.getEmailAddress().isEmpty()){
			attributes += "email, ";
			values += "'" + entity.getEmailAddress().replace("'","\\'") + "', ";
		}
		attributes += "password, ";
		if(entity.getPassword() != null && !entity.getPassword().isEmpty()){
			values += "MD5('" + entity.getPassword().replace("'", "\\'") + "'), ";		//I really don't like putting the has function here :S
		}
		else{
			values += "MD5('" + createPassword(entity) + "'), ";
		}
		
		
		if(entity instanceof Student){
			values += LibrisGlobalInterface.USER_TYPE_STUDENT + ", ";
			attributes += "type, ";
		}
		else if(entity instanceof Faculty){
			values += LibrisGlobalInterface.USER_TYPE_FACULTY + ", ";
			attributes += "type, ";
		}
		else if(entity instanceof Librarian){
			values += LibrisGlobalInterface.USER_TYPE_LIBRARIAN + ", ";
			attributes += "type, ";
		}
		else if(entity instanceof Administrator){
			values += LibrisGlobalInterface.USER_TYPE_ADMIN + ", ";
			attributes += "type, ";
		}
		//possibly throw exception for invalid user type
		
		attributes += "enabled, ";
		values += entity.getEnabled() + ", ";
		
		if (attributes.endsWith(", ")){ //May not end with a comma
			attributes = attributes.substring(0, attributes.length()-2);	//get rid of extra comma and space
			values = values.substring(0, values.length()-2);
		}
		
		return "INSERT user " + attributes + ") VALUES " + values + ")";
	}
	


	/**
	 * This builds the User(s) from the rowSet given by the database
	 * 
	 * @param rowSet The result from the database (must include all columns of a resouceType)
	 * @return result An array of the Users acquired from the database. If only expecting one, check length first and then take first element of array.
	 * @throws SQLException If couldn't get correct results from rowSet
	 */
	private static User[] buildEntityFromRowSet(CachedRowSetImpl rowSet){
		if(rowSet == null)
			return null;
		int nRows = 0;
		try{
			while(rowSet.next())
				nRows++;
			
			User[] result = new User[nRows];
			rowSet.beforeFirst();
			for(int i=0; rowSet.next() && i < nRows ;i++){
				int userType = rowSet.getInt("type");
				if(userType == LibrisGlobalInterface.USER_TYPE_STUDENT)
					result[i] = new Student(
							rowSet.getInt("user_ID"),
							rowSet.getString("first_name"),
							rowSet.getString("last_name"),
							null,
							rowSet.getString("email"),
							rowSet.getBoolean("enabled")
							);
				else if(userType == LibrisGlobalInterface.USER_TYPE_FACULTY)
					result[i] = new Faculty(
							rowSet.getInt("user_ID"),
							rowSet.getString("first_name"),
							rowSet.getString("last_name"),
							null,
							rowSet.getString("email"),
							rowSet.getBoolean("enabled")
							);
				else if(userType == LibrisGlobalInterface.USER_TYPE_LIBRARIAN)
					result[i] = new Librarian(
							rowSet.getInt("user_ID"),
							rowSet.getString("first_name"),
							rowSet.getString("last_name"),
							null,
							rowSet.getString("email"),
							rowSet.getBoolean("enabled")
							);
				else if(userType == LibrisGlobalInterface.USER_TYPE_ADMIN)
					result[i] = new Administrator(
							rowSet.getInt("user_ID"),
							rowSet.getString("first_name"),
							rowSet.getString("last_name"),
							null,
							rowSet.getString("email"),
							rowSet.getBoolean("enabled")
							);
				else
					result[i] = null;
			}
			return result;
		}
		catch(SQLException e){
			ErrorManager.handleError(e);
			return null;
		}
	}
	
	private static String createPassword(User entity) {
		if(entity.getFirstName() != null && entity.getLastName() != null)
			return entity.getFirstName().replace("'","").charAt(0) + entity.getLastName().replace("'", "\'");
		if(entity.getID() > 0)
			return Integer.toString(entity.getID());
		return "libris";
	}
	
/*
	public static void main(String args[]){
		Student r = new Student(100000000,"Jeremy","Lerner","test","librislms@gmail.com",true);
				
		//ServerInterface.configureServerInterface("154.20.115.46",1500);
		
		//User result = addUser(r);
		System.out.println(buildUpdateStatement(r));
		//User result[] = getUser(r);
		//System.out.println(result[0].getFirstName());
		//boolean success = removeUser(100000000);
		//System.out.println(success);
		
		//ServerInterface.getReference().closeConnection();
	}
*/
}