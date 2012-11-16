package client.serverinterface;
/**
 * Client.java
 * 
 * Client object will handle connection to server
 * methods are protected and can be accessed by ServerInterface.java
 *
 * @author Sardor Isakov
 * @version 2
 */


import java.io.*;
import java.net.Socket;
import communication.RequestPacket;

/** Representation of connection between server and client
 */
public class NetworkConnection {
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
	private Socket socket;
	
	// the server, the port and the username
	@SuppressWarnings("unused")
	private String server, username, password;
	private int port;
	
	//private Main mn;
	
	/** Constructor
	 * 
	 * @param server ip address
	 * @param port port number
	 */
	protected NetworkConnection(String server, int port) {
		this(server, port,"Guest","guestPwd");
	}
	
	/** Constructor
	 * 
	 * @param server
	 * @param port
	 * @param username
	 * @param password
	 */
	protected NetworkConnection(String server, int port, String username, String password) {
		this.server = server;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	protected boolean start() {
		// try to connect to the server
		try {
				socket = new Socket(server, port);
		} 
		// if it failed not much I can so
		catch(Exception ec) {
			display("Error connecting to server:" + ec);
			return false;
		}
				
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		
		display(msg);
		try {
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			display("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		RequestPacket request = new RequestPacket();
		//request.setUsername(username);
		//request.setPassword(password);
		sendMessage(request);

		return true;
	}
	
	private void display(String msg) {
		//if(mn == null)
			//System.out.println(msg);      // println in console mode
		//else
			//mn.append(msg + "\n");	
	}
	
	/**
	 * 
	 * @param request
	 */
	protected void sendMessage(RequestPacket request) {
			try {
				sOutput.writeObject(request);
			}
			catch(IOException e) {
				display("Exception writing to server: " + e);
			}
		}
	
	protected RequestPacket getInputStream() {
		try {
			RequestPacket inputStream = (RequestPacket) sInput.readObject();
			// if console mode print the message and add back the prompt
			return inputStream;		
		}
		catch(IOException e) {
			display("Server has close the connection: " + e);
		}
		// can't happen with a String object but need the catch anyhow
		catch(ClassNotFoundException e2) {
		}
		return null;
	}
}