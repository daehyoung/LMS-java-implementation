package network;
/**
 * ServerInterface.java 
 * 
 * Serializale Singleton object
 * 
 *
 * @author Sardor Isakov
 * @version 1.0
 */
import java.io.Serializable;

public class ServerInterface implements Serializable {
	protected static final long serialVersionUID = 200L;

	//Singleton constructor
	private static ServerInterface INSTANCE = new ServerInterface();
	
	public static final int LOGIN = 0, DATABASEQUERY = 1, LOGOUT = 2, GUEST = 3;
	private int type;
	Client client;
	

	public Response send(Request request) {
		client.sendMessage(request);
		Response response = client.getInputStream();
		return response;
	}
	
	public boolean connect(String server,int port) {
		client = new Client(server, port);
		if(!client.start()) 
			return false;
		return true;
	}
	public Response connect2(String server,int port, String username, String password) {
		client = new Client(server, port, username, password);
		if(!client.start()) 
			return client.getInputStream();
		return client.getInputStream();
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void send2(Client client, Request request2) {
		client.sendMessage(request2);
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
}
