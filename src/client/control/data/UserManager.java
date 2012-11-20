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
	        
		RequestPacket request = new RequestPacket();
		
		request.setCode(RequestPacket.SELECT);
		request.setType(RequestPacket.REQUEST_SQL);
		request.setSqlStatment(sql);

		CachedRowSet rowSet = ServerInterface.getInstance().request(request);
			
		// Populating empty user object
		while (rowSet.next()) {
			user.setID(rowSet.getInt("user_ID"));	
			user.setFirstName(rowSet.getString("first_name"));
			user.setLastName(rowSet.getString("last_name"));
			user.setEmailAddress(rowSet.getString("email"));
			user.setUserType(rowSet.getString("type"));
		}
		rowSet.close();
		
		// Returning populated User object
		return user;
	}
	
	public User[] getUser(User[] user) {
		
		return null;
	}
	
	public void setUser(User user) {
		//TO-DO: implement
		
	}
	
	public int[] addUser(User user) {
		String sql = "INSERT INTO librisDB.`user` (user_ID,first_name, last_name, password, email, phone, `type`, enabled)" +
				"VALUES ("+user.getID()+",'" + user.getFirstName() + "', '"+ user.getLastName()+"', '"+user.getPassword() +"', '" + user.getEmailAddress()+ "', '6047253344', '"+user.getUserType()+"', DEFAULT)";
		RequestPacket request = new RequestPacket();
		
		// Building request object
		request.setCode(RequestPacket.INSERT);
		request.setType(RequestPacket.REQUEST_SQL);
		request.setSqlStatment(sql);
			
		// Sending request package to server and receiving Response object 
		request = ServerInterface.getInstance().send(request);
		
		return request.getInsertResult();
	}
	
	public void removeUser(User user) {
		
	}

	public User authenticateUser(User user) throws SQLException {
		// Building sql statement, which will be sent to server
        String sql = "SELECT * FROM librisDB.user WHERE last_name = '" + user.getLastName() + "' AND password = " + user.getPassword();
        
		RequestPacket request = new RequestPacket();
		
		// Building request object
		request.setCode(RequestPacket.SELECT);
		request.setType(RequestPacket.REQUEST_SQL);
		request.setSqlStatment(sql);
		
		// Sending request package to server and receiving Response object 
		request = ServerInterface.getInstance().send(request);

		// Retrieving resultset from Response object which was sent by server
		CachedRowSet RowSet = request.getRowSet();
		
		// Populating empty user object
		while (RowSet.next()) {
			user.setID(RowSet.getInt("user_ID"));
			user.setFirstName(RowSet.getString("first_name"));
			user.setLastName(RowSet.getString("last_name"));
			user.setEmailAddress(RowSet.getString("email"));
			user.setUserType(RowSet.getString("type"));
		}
		
		// Returning populated User object
		return user;
	}
}
//END