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
import client.serverinterface.*;


@SuppressWarnings("unused")
public class Main {

	private static String username;
	private static String password;
	/** Main method
	 * 
	 * @param args
	 * @throws SQLException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws SQLException, InterruptedException {
		//Connecting to server
		ServerInterface.getInstance().openConnection("96.49.112.69", 1500);

		
	
		//If you want to search for user with id 1123
		Patron patronX = new Patron();
		patronX.setID(100000005);
		new UserManager();
		// Asking UserManager control to give you user object with data retrieved from server database
		patronX = (Patron) UserManager.getUser(100000005);
		// Displaying results
		System.out.println("\nThis is resut of query:");
		System.out.println("Name: " + patronX.getFirstName() + " " + patronX.getLastName());
		System.out.println("ID: " + patronX.getID());
		System.out.println("Email: " + patronX.getEmailAddress());

		patronX.setID(100000003);
		new UserManager();
		// Asking UserManager control to give you user object with data retrieved from server database
		patronX = (Patron) UserManager.getUser(100000003);
		// Displaying results
		System.out.println("\nThis is resut of query:");
		System.out.println("Name: " + patronX.getFirstName() + " " + patronX.getLastName());
		System.out.println("ID: " + patronX.getID());
		System.out.println("Email: " + patronX.getEmailAddress());
	

		ServerInterface.getInstance().closeConnection();
	}

}
//END