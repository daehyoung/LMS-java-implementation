package client.serverinterface;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import communication.RequestPacket;

/**
 * This is the Network Connection manager, which represents connection between client and server
 * This is protected class and can only be accessed by Server Interface class
 * It will connect to server, and create Input and Output streams for messaging 
 * 
 * @author Sardor Isakov
 * @version 7
 */
class NetworkConnection {
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
	private Socket socket;
	
	// the server, the port and the address
	private String serverIP;
	private int port;
		
	/** 
	 * Constructor of the Network Connection manager class
	 * 
	 * @param serverIP Server's IP address
	 * @param port Server's port number
	 */
	protected NetworkConnection(String serverIP, int port) {
		this.serverIP = serverIP;
		this.port = port;
	}
	/**
	 * This method creates the client socket. It will initialize the socket, ObjectInputStream, and ObjectOutputStream
	 * 
	 * @return boolean Returns true if connected to server
	 * @throws ConnectException IF could not connect to the server
	 */
	protected boolean configure() throws ConnectException {
		try {
			socket = new Socket(serverIP, port);
			socket.setKeepAlive(true);
		} 
		catch(UnknownHostException e) {
			throw new ConnectException("Could not connect to the server. Please check that the server is running and the connection is configured properly.");
		} catch (IOException e) {
			throw new ConnectException("Could not connect to the server. Please check that the server is running and the connection is configured properly.");
		}
		
		//String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		
		//log(msg);
		try {
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException e) {
			throw new ConnectException("Could not connect to the server. Please check that the server is running and the connection is configured properly.");
		}
		return true;
	}
	
	protected void close(){
		try {
		if(sInput != null)
			sInput.close();
		} catch (IOException e) {}
		try {
		if(sOutput != null)
			sOutput.close();
		} catch (IOException e) {}
		try{
		if(socket != null)
			socket.close();
		} catch (IOException e) {}
	}
	
	
	protected RequestPacket sendRequest(RequestPacket request) throws ConnectException{
		try {
			sOutput.writeObject(request);
			sOutput.flush();
		} catch (SocketException e){
			throw new ConnectException("Connection to the server timed out.");
		} catch (IOException e) {
			throw new ConnectException("Could not connect to the server. Please check that the server is running and the connection is configured properly.");
		}
		Object obj = null;
		try {
			obj = sInput.readObject();
		} catch(EOFException e){
			//do nothing
		}catch (SocketException e){
			throw new ConnectException("Connection to the server timed out.");
		}catch (IOException e) {
			throw new ConnectException("Could not connect to the server. Please check that the server is running and the connection is configured properly.");
		} catch (ClassNotFoundException e) {
			throw new ConnectException("System Error: ClassNotFoundException in NetworkConnection");
		}
		
		if(obj instanceof RequestPacket)
			return (RequestPacket)obj;
		return null;
	}
}
