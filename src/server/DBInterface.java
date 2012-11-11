package server;
/**
 * DBInterface.java 
 * 
 * DBInterface singleton object 
 * I'll write more comments later
 *
 * @author Sardor Isakov
 * @version 1.0
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import com.sun.rowset.CachedRowSetImpl;

public enum DBInterface {
	INSTANCE;
	
	private static Connection connect = null;
	private static String mysql_username = "root";
	private static String mysql_password = "243816";
	private static String server_ip = "localhost";
	
	/**
	 *It will connect to database and get resultset
	 *@param String sql statement
	 *@return CachedRowSet data object
	 */
	public CachedRowSet getRecord(String sql) throws Exception {
		
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://"+ server_ip +"/librisDB?user="+ mysql_username +"&password="+ mysql_password);
		
		try {

			PreparedStatement prest = connect.prepareStatement(sql);
			prest.setMaxRows(10);
			ResultSet resultSet = prest.executeQuery();
		
			CachedRowSet cachedRowSet=new CachedRowSetImpl();
	        cachedRowSet.populate(resultSet);
	        resultSet.close();
	        prest.close();
	        connect.close();
	        return cachedRowSet;
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}
}
//END