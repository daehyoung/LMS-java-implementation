package client.serverinterface;

import communication.RequestPacket;
import javax.sql.rowset.CachedRowSet;
/**
 * ServerInterface application - file ServerInterface.java 
 * 
 * ServerInterface class
 * 
 * @author Sardor Isakov
 * @version 1.0
 */
public class ServerInterface {
	private static ServerInterface INSTANCE = new ServerInterface();
	private int type;
	private static NetworkConnection client;
	private static RequestPacket request;
	
	public static final int LOGIN = 0, LOGOUT = 2;
	
	public boolean openConnection(String serverIP,int port) {		
		client = new NetworkConnection(serverIP, port);
		if(!client.start()) 
			return false;
		return true;
	}
	
	public void closeConnection() {		
		request.setType(LOGOUT);
		client.sendMessage(request);
	}
	
	public CachedRowSet request(RequestPacket message) {
		client.sendMessage(message);
		request = client.getInputStream();
		return request.getRowSet();
	}
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	private ServerInterface(){ }
	
	// Static 'instance' method 
	public static ServerInterface getInstance( ) {
		if(INSTANCE == null){
	         synchronized(ServerInterface.class){
	         //double checked locking - because second check of Singleton instance with lock
	                if(INSTANCE == null){
	                    INSTANCE = new ServerInterface();
	                    request = new RequestPacket();
	                }
	            }
	         }
	     return INSTANCE;
	}

	private Object readResolve() {
		return INSTANCE;
	}
}