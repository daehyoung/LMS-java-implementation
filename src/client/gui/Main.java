package client.gui;

/**
 * Main.java 
 * 
 * this object is for testing
 *
 * @author Sardor Isakov
 */

import java.sql.SQLException;

import communication.RequestPacket;

import client.control.data.*;
import client.control.data.entity.user.*;
import client.control.data.entity.user.Patron;
import client.control.data.entity.user.User;
import client.serverinterface.*;


@SuppressWarnings("unused")
public class Main {
	private static ServerInterface serverInterface = ServerInterface.getInstance();
	private static String username;
	private static String password;
	/** Main method
	 * 
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		//Connecting to server
		serverInterface.openConnection("localhost", 1500);

		
		
		//serverInterface.openConnection("192.168.0.65", 1500, username,password);
		
		
		//this for test only
		RequestPacket request = new RequestPacket();
		request.setCode(RequestPacket.TEST);
		request.setType(RequestPacket.REQUEST_SQL);
		
		// Sending request package to server and receiving Response object 
		RequestPacket response = ServerInterface.getInstance().send(request);
		//System.out.println(response.getMessage());
		
		//===========================================
		// uncomment this Block comment to test server connection
		
		
		//If you want to search for user with id 1123
		Patron patronX = new Patron();
		patronX.setID(100000004);
		
		// Asking UserManager control to give you user object with data retrieved from server database
		patronX = (Patron) new UserManager().getUser(patronX);
		
		// Displaying results
		System.out.println("This is resut:\n");
		System.out.println("Name: " + patronX.getFirstName() + " " + patronX.getLastName());
		System.out.println("ID: " + patronX.getID());
		System.out.println("Email: " + patronX.getEmailAddress());
		System.out.println("User type: ");
		System.out.println("Phone: ");
		
		
		//================================================

	}

}
