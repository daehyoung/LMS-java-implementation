package client.serverinterface;

import java.io.*;
import java.net.Socket;
import communication.RequestPacket;

/**
 * This is the Network Connection manager, which represents connection between client and server
 * This is protected class and can only be accessed by Server Interface class
 * It will connect to server, and create Input and Output streams for messaging 
 * 
 * @author Sardor Isakov
 * @version 2
 */
class NetworkConnection {
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
	private Socket socket;
	
	// the server, the port and the username
	private String serverIP;
	private int port;
	
	/** 
	 * Constructor of the Network Connection manager class
	 * @param	serverIP	Server's ip address
	 * @param	port		Server's port number
	 */
	protected NetworkConnection(String serverIP, int port) {
		this.serverIP = serverIP;
		this.port = port;
	}
	/**
	 * This method implements client socket. It creates a stream socket 
	 * and connects it to the specified port number at the specified IP address.
	 * @return	boolean		Returns true if connected to server
	 */
	protected boolean start() {
		// try to connect to the server
		try {
				socket = new Socket(serverIP, port);
		} 
		// if it failed not much I can so
		catch(Exception ec) {
			log("Error connecting to server:" + ec);
			return false;
		}
				
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		
		log(msg);
		try {
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			log("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		RequestPacket request = new RequestPacket();
		sendMessage(request);

		return true;
	}
	
	private void log(String msg) {
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
				log("Exception writing to server: " + e);
			}
		}
	
	protected RequestPacket getInputStream() {
		try {
			RequestPacket inputStream = (RequestPacket) sInput.readObject();
			// if console mode print the message and add back the prompt
			return inputStream;		
		}
		catch(IOException e) {
			log("Server has close the connection: " + e);
		}
		// can't happen with a String object but need the catch anyhow
		catch(ClassNotFoundException e2) {
		}
		return null;
	}
}
