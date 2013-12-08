package server.serverinterface.clientinterface;
/**
 * Server side application - file Server.java 
 * 
 * The server that can be run both as a console application or a GUI
 * Server runs in infinite loop listening to port 1500 by default 
 * (it can be changed to any port)
 * 
 * @author Sardor Isakov
 * @version 3.0
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import server.control.errormanagement.ErrorManager;
import server.control.errormanagement.LogManager;

/** ClientConnectionManager, waits for clients to connect and assigns them a new Thread
 */
public class ClientConnectionManager extends Thread {
	
	private final static int MAX_CLIENTS = 40;
	private int numClients = 0;
	
	private ArrayList<ClientConnectionThread> clientThreads;
	private int port;
	private boolean keepGoing;
	private ServerSocket serverSocket;
	
	/** 
	 * ClientConnectionManager constructor that receives the port for ServerSocket to listen to
	 * 
	 * @param port	Port for the ServerSocket
	 */
	public ClientConnectionManager(int port) {
		this.port = port;
		//ArrayList for the Client list
		clientThreads = new ArrayList<ClientConnectionThread>();
	}
	
	
	/** 
	 * Starts the ServerSocket and runs an infinite loop listening for clients who connect
	 * 
	 */
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			ErrorManager.getReference().fatalSubsystemError(e, this);
			return;
		}
		
		keepGoing = true;
		LogManager.getReference().logMessage("Server waiting for Clients on port " + port + ".");
		while(keepGoing){
			Socket socket;
			try {
				socket = serverSocket.accept();	//accept a conncection
				if(!keepGoing)
					break;
				
				if(numClients >= MAX_CLIENTS){
					LogManager.getReference().logError(socket.getInetAddress() + " rejected. Max Clients Reached.");
				}
				else{
					numClients ++;
					ClientConnectionThread t = new ClientConnectionThread(socket,this);  	// make a new thread for it
					clientThreads.add(t);		// add it to the ArrayList
					t.start();		//start running the new thread
				}
			} catch (IOException e) {
				//error connecting a client, could possibly log error or check if happening too often
			}
		}
		for(ClientConnectionThread client: clientThreads)	//close each ClientConnectionThread if the closing the server
			client.close();
	}
	
	/** 
	 * Close the ClientConnectionManager
	 */
	public void close() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			//leave burning bridges
		}
		keepGoing = false;
	}
	
	/**
	 * Removes the given ClientConnectionThread from the list of clients
	 * 
	 * @param thread The client to remove
	 */
	protected void removeClient(ClientConnectionThread thread){
		if(clientThreads.remove(thread))
			numClients --;
	}
}
