package client.control.data;
/**
 * User manager control - file UserManager.java 
 * 
 * @author Sardor Isakov
 * @version 1.0
 */

import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import communication.RequestPacket;
import client.control.data.entity.user.User;
import client.serverinterface.ServerInterface;

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
	        String sql = "SELECT * FROM librisDB.user WHERE user_id = "+user.getID();
	        
	        // Constracting some empty objects
			User result = new User();
			RequestPacket request = new RequestPacket();
			
			// Building request object
			request.setCode(RequestPacket.TEST);
			request.setType(RequestPacket.REQUEST_SQL);
			request.setSqlStatment(sql);
			
			// Sending request package to server and receiving Response object 
			request = ServerInterface.getInstance().send(request);

			// Retrieving resultset from Response object which was sent by server
			CachedRowSet RowSet = request.getRowSet();
			
			// Populating empty user object
			while (RowSet.next()) {
				result.setID(RowSet.getInt("user_ID"));
			    result.setFirstName(RowSet.getString("first_name"));
				result.setLastName(RowSet.getString("last_name"));
				result.setEmailAddress(RowSet.getString("email"));
			}
			
			// Returning populated User object
			return result;
	}

}

