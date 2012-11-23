package server.serverinterface.clientinterface;
import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.control.LogManager;
import server.control.datamanagement.RequestManager;
import communication.*;
/** Representation for Client Thread
 */
/**
 * Client Thread side application - file ClientConnectionThread.java 
 * 
 * Thread started by Server.java
 * 
 * @author Sardor Isakov
 * @version 1.0
 */
class ClientConnectionThread  extends Thread {
	private static int uniqueId; 	// a unique ID for each connection
	Socket socket;					// the socket where to listen/talk
	ObjectInputStream sInput;
	ObjectOutputStream sOutput;
	int threadID;							// thread's unique id (easier for deconnection)

	int userID;
	String userType;
	boolean userAuthenticated = false;

	RequestPacket request;
	RequestManager requestManager;
	String date;
	
	private static Logger logger;
	
	/** Constructor
	 * 
	 * @param socket socket received from server
	 * @throws Exception
	 */
	protected ClientConnectionThread(Socket socket) throws Exception {
		// a unique id
		threadID = ++uniqueId;
		logger = LogManager.getInstance();
		logger.setLevel(Level.ALL);
		
		
		this.socket = socket;
		/* Creating both Data Stream */
		System.out.println("Thread trying to create Object Input/Output Streams");
		logger.fine("Thread trying to create Object Input/Output Streams");
		try {
			// create output first
			sOutput = new ObjectOutputStream(socket.getOutputStream());
			sInput  = new ObjectInputStream(socket.getInputStream());
			
		    request = (RequestPacket) sInput.readObject();
		}
		catch (IOException e) {
			logger.severe("Exception creating new Input/output Streams: " + e);
			return;
		}
		// have to catch ClassNotFoundException
		// but I read a String, I am sure it will work
		catch (ClassNotFoundException e) {
		}
        date = new Date().toString() + "\n";
	}
	
	/**
	 * This will run forever
	 */
	public void run() {
		// to loop until LOGOUT
		boolean keepGoing = true;
		requestManager = new RequestManager();
		while(keepGoing) {
			try {

				request = (RequestPacket) sInput.readObject();
			}
			catch (IOException e) {
				logger.severe("Exception reading Streams: " + e);
				break;				
			}
			catch(ClassNotFoundException e2) {
				break;
			}
			
			switch(request.getType()) {
				case RequestPacket.REQUEST_SQL:
					try {
						request = requestManager.processRequest(request);
						writeMsg(request);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					} break;
				case RequestPacket.REQUEST_LOGOUT:
					
					//writeMsg(response);
					System.out.println(userType + " disconnected with a LOGOUT message.");
					logger.fine(userType + " disconnected with a LOGOUT message.");
					keepGoing = false;
					break;
				case RequestPacket.REQUEST_LOGIN:
					request = requestManager.processRequest(request);
					writeMsg(request);
					break;
			}
		}
		// remove myself from the arrayList containing the list of the
		// connected Clients
		//remove(threadID);
		close();
	}
	
	/**
	 * Closes thread
	 */
	private void close() {
		// try to close the connection
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {}
		try {
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {};
		try {
			if(socket != null) socket.close();
		}
		catch (Exception e) {}
	}
	
	/** Writes message to output stream
	 * 
	 * @param response
	 * @return boolean
	 */
	private boolean writeMsg(RequestPacket response) {
		// if Client is still connected send the message to it
		if(!socket.isConnected()) {
			close();
			return false;
		}
		// write the message to the stream
		try {
			sOutput.writeObject(response);
		}
		// if an error occurs, do not abort just inform the user
		catch(IOException e) {
			logger.warning("Error sending message " + e.toString());
		}
		return true;
	}
}
