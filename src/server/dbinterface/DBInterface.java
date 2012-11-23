package server.dbinterface;

import java.sql.*;
import com.sun.rowset.CachedRowSetImpl;


/**
 * This class is the interface to the database and utilises JDBC
 * 
 * @author Peter Abelseth
 * @version 1
 *
 */
public class DBInterface {
	
	private static DBInterface singleton = null;	//the single object
	
	final private static String driver = "com.mysql.jdbc.Driver";	//the driver of the JDBC connection
	
	private String url;	//the URL of the database
	private String dbName;	//the name of the database to use
	private String userName;	//database username
	private String password;	//database password
	private Connection connection = null;	//connection to the database
	

	/**
	 * Empty constructor of the DBInterface. Using Singleton design Pattern
	 */
	private DBInterface() {
		
	}
	
	/**
	 * Initialises the JDBC connection and 
	 * @param url	The url of the database
	 * @param dbName	The name of the database to use
	 * @param userName	The username for the database to use
	 * @param password	The password of the username for the database
	 * @return singleton	The singleton object of the DBInterface
	 */
	public static synchronized DBInterface createDBInterface(String url, String dbName, String userName, String password){
		if(singleton == null){	//if it already exists, don't create it again
			singleton = new DBInterface();
			singleton.url = url;
			singleton.dbName = dbName;
			singleton.userName = userName;
			singleton.password = password;
		
			singleton.createConnection();
		}
		
		return singleton;	//return the singleton object
	}
	
	/**
	 * Gets the singleton object of the datasbase
	 * @return	singleton The singleton object of the DBInterface, returns null if DBInterface hasn't been created yet
	 */
	public static synchronized DBInterface getInstance(){
		return singleton;
	}
	
	
	/**
	 * Closes the JDBC connection to the database
	 */
	public static synchronized void closeDBConnection(){
		if(singleton != null && singleton.connection != null){
			try {
				singleton.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Executes the given query and returns the result as a CachedRowSet
	 * @param sqlStatement	The query to perform on the database
	 * @return rowSet	The result of the query on the database
	 */
	public CachedRowSetImpl executeQuery(String sqlStatement){
		
		CachedRowSetImpl rowSet = null;
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			
			rowSet = new CachedRowSetImpl();
			rowSet.populate(resultSet);
			
		} catch (SQLException e) {
			return null;
		}
		
		return rowSet;
	}
	
	/**
	 * Executes the given statement as an insert on the database
	 * @param sqlStatement The statement to execute
	 * @return result Should return the full row that was inserted(including ID) but only returns null for now
	 */
	public CachedRowSetImpl executeInsert(String sqlStatement){
		
		
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sqlStatement);
		} catch (SQLException e) {
			return null;
		}
		
		
		return null;
	}
	
	/**
	 * Executes the given sql statement as an insertion/deletion/update on the database
	 * @param sqlStatement	The statement to update the database
	 * @return success Returns if the statement was successful or not.
	 */
	public boolean executeUpdate(String sqlStatement){
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sqlStatement);	
		} catch (SQLException e) {
			return false;	//return false if the statement couldn't be executed, possibly bad syntax or violation of db constraints
		}
		
		return true;	//return true if query executed successfully.
	}
	
	/**
	 * Executes the given command as a delete on the database
	 * @param sqlStatement The delete statement to execute
	 * @return success Returns if the statement was successful or not
	 */
	public boolean executeDelete(String sqlStatement){
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sqlStatement);
		} catch (SQLException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Establishes the connection to the database using the object's attributes
	 */
	private void createConnection(){
		try{
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(url+dbName,userName,password);   //create the connection to the database
		
		} catch(SQLException e){
			//TODO should handle error hear using the global exception handler
			//Most likely error here is bad password/Url/username/databasename
			e.printStackTrace();
			
		} catch(Exception e){
			//catch exceptions from driver declaration, probably fatal error
			e.printStackTrace();
		}
	}

}




