package client.applogic.cotrol;
/**
* @author Sardor Isakov
*/

import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import network.Request;
import network.Response;
import network.ServerInterface;

import client.applogic.user.User;

public class UserManager {
	
	/** Constractor
     * @param void
     */
	public UserManager() {
	}
	
	/** getUser() Method
	 * 
     * @param args the command line arguments
     * @return User object with data retrieved from server database
     */
	public User getUser(User user) throws SQLException {
			// Building sql statement, which will be sent to server
	        String sql = "SELECT * FROM library_db.user WHERE user_id = "+user.getUserID();
	        
	        // Constracting some empty objects
			User result = new User();
			Request request = new Request();
			
			// Building request object
			request.setCode(Request.SELECT);
			request.setType(Request.DATABASEQUERY);
			request.setSqlStatment(sql);
			
			// Sending request package to server and receiving Response object 
			Response response = ServerInterface.getInstance().send(request);

			// Retrieving resultset from Response object which was sent by server
			CachedRowSet RowSet = response.getRowSet();
			
			// Populating empty user object
			while (RowSet.next()) {
				result.setUserID(RowSet.getInt("user_ID"));
			    result.setFirstName(RowSet.getString("first_name"));
				result.setLastName(RowSet.getString("last_name"));
				result.setEmailAddress(RowSet.getString("email"));
				result.setUserType(RowSet.getString("type"));
				result.setPhoneNumber(RowSet.getString("phone"));
			}
			
			// Returning populated User object
			return result;
	}

}
