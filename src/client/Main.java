package client;
/**
 * Main.java 
 * 
 * this object is for testing
 *
 * @author Sardor Isakov
 */

import java.sql.SQLException;
import client.applogic.cotrol.*;
import client.applogic.user.User;
import network.Response;
import network.ServerInterface;
import network.Request;


@SuppressWarnings("unused")
public class Main {
	private static ServerInterface serverInterface = ServerInterface.getInstance();

	/** Main method
	 * 
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		//Connecting to server
		serverInterface.connect("70.79.11.83", 1500); 
		
		
		//this for test only
		Request request = new Request();
		request.setCode(Request.TEST);
		request.setType(Request.DATABASEQUERY);
		
		// Sending request package to server and receiving Response object 
		Response response = ServerInterface.getInstance().send(request);
		System.out.println(response.getMessage());
		
		//===========================================
		// uncomment this Block comment to test server connection
		
		
		//If you want to search for user with id 1123
		User userX = new User();
		userX.setUserID(100000004);
		
		// Asking UserManager control to give you user object with data retrieved from server database
		User user = new UserManager().getUser(userX);
		
		// Displaying results
		System.out.println("This is resut:\n");
		System.out.println("Name: " + user.getFirstName() + " " + user.getLastName());
		System.out.println("Email: " + user.getEmailAddress());
		System.out.println("User type: " + user.getUserType());
		System.out.println("Phone: " + user.getPhoneNumber());
		
		
		//================================================


		

		// loging out from server
		// to do that you send LOGOUT code
		Response response2 = serverInterface.send(new Request(Request.LOGOUT));
		System.out.println();
		System.out.println(response2.getMessage());
	}

}
