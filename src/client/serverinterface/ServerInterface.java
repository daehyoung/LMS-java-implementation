package client.serverinterface;
import javax.sql.rowset.CachedRowSet;

/**
 * ServerInterface application - file ServerInterface.java 
 * 
 * ServerInterface class
 * 
 * @author Sardor Isakov
 * @version 1.0
 */
import communication.RequestPacket;
/** Representations of a singleton ServerInterface class
 * 
 * @author sardor
 */
public class ServerInterface {
	private static ServerInterface INSTANCE = new ServerInterface();
	public static final int LOGIN = 0, DATABASEQUERY = 1, LOGOUT = 2, GUEST = 3;
	private int type;
	NetworkConnection client;

	public RequestPacket send(RequestPacket request) {
		client.sendMessage(request);
		return client.getInputStream();
	}
	
	public boolean openConnection(String server,int port) {		
		return openConnection(server,port, null, null);
	}
	
	public boolean openConnection(String server,int port, String username, String password) {
		client = new NetworkConnection(server, port, username, password);
		if(!client.start()) 
			return false;
		return true;
	}

	public void closeConnection() {		
		RequestPacket request = new RequestPacket();
		request.setType(LOGOUT);
		client.sendMessage(request);
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
	                }
	            }
	         }
	     return INSTANCE;
	}
	//Other methods protected by ServerInterface-ness 
	protected static void demoMethod( ) {
		      System.out.println("demoMethod for ServerInterface"); 
	}
	private Object readResolve() {
		return INSTANCE;
	}

	public CachedRowSet request(RequestPacket request) {
		client.sendMessage(request);
		request = send(request);
		return request.getRowSet();
	}
}
