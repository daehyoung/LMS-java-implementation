package server.serverinterface.clientinterface;
/**
 * Server side application - file Server.java 
 * 
 * The server that can be run both as a console application or a GUI
 * Server runs in infinit loop listening to port 1500 by default 
 * (it can be changed to any port)
 * 
 * @author Sardor Isakov
 * @version 1.0
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import server.serverinterface.userinterface.*;
/** Representation for server object
 */
public class ClientConnectionManager {
	private SimpleDateFormat sdf;
	private ArrayList<ClientConnectionThread> al;
	private ServerGUI sg;
	private int port;
	private boolean keepGoing;
	
	/** Constructor
	 * Server constructor that receive the port to listen to for connection as parameter
	 * in console
	 * 
	 * @param	port	specifying port number
	 */
	public ClientConnectionManager(int port) {
		this.port = port;
		// to display hh:mm:ss
		sdf = new SimpleDateFormat("HH:mm:ss");
		//ArrayList for the Client list
		al = new ArrayList<ClientConnectionThread>();
	}
	
	/** Constructor
	 * Server constructor for GUI
	 * @param	port	specifying port number
	 * @param	sg		specifying ServerGUI object
	 */
	public ClientConnectionManager(int port, ServerGUI sg) {
		// GUI or not
		this.sg = sg;
		this.port = port;
		sdf = new SimpleDateFormat("HH:mm:ss");// to display hh:mm:ss
		al = new ArrayList<ClientConnectionThread>();// ArrayList for the Client list
	}
	
	/** Start the server and run in infinit loop
	 * @return void
	 */
	
	public void start() throws Exception {
		keepGoing = true;
		/* create socket server and wait for connection requests */
		try 
		{
			// the socket used by the server
			ServerSocket serverSocket = new ServerSocket(port);

			// infinite loop to wait for connections
			while(keepGoing) 
			{
				// format message saying we are waiting
				display("Server waiting for Clients on port " + port + ".");
				
				Socket socket = serverSocket.accept();  	// accept connection
				// if I was asked to stop
				if(!keepGoing)
					break;
				ClientConnectionThread t = new ClientConnectionThread(socket);  // make a thread of it
				al.add(t);									// save it in the ArrayList
				t.start();
			}
			
			try {
				serverSocket.close();
				for(int i = 0; i < al.size(); ++i) {
					ClientConnectionThread tc = al.get(i);
					try {
					tc.sInput.close();
					tc.sOutput.close();
					tc.socket.close();
					}
					catch(IOException ioE) {
						// not much I can do
					}
				}
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		// something went bad
		catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}
	
	/** Stops the server, it will be called from ServerGUI
	 * @return void
	 */
	
	protected void stop() {
		keepGoing = false;
		// connect to myself as Client to exit statement 
		// Socket socket = serverSocket.accept();
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
			// nothing I can really do
		}
	}
	
	
	/**
	 * Display an event (not a message) to the console or the GUI
	 * @param	ms	Regular string
	 */
	private void display(String msg) {
		String time = sdf.format(new Date()) + " " + msg;
		if(sg == null)
			System.out.println(time);
		else
			sg.appendEvent(time + "\n");
	}
	
	/**
	 * Server's main method
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// start server on port 1500 unless a PortNumber is specified 
		int portNumber = 1600;
		switch(args.length) {
			case 1:
				try {
					portNumber = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Server [portNumber]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Server [portNumber]");
				return;
				
		}
		// create a server object and start it
		ClientConnectionManager server = new ClientConnectionManager(portNumber);
		server.start();
	}

}
