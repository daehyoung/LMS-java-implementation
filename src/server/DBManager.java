package server;
/**
 * DBManager.java 
 * 
 * DBManager object I'll write more comments later

 *
 * @author Sardor Isakov
 * @version 1.0
 */
import network.Request;
import network.Response;

public class DBManager {
	
	public DBManager() {}

	public Response getReponse(Request request) {
		Response response = new Response();
		switch(request.getCode()) {
			case Request.SELECT:
				try {
					response.setRowSet(DBInterface.INSTANCE.getRecord(request.getSqlStatment()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				response.setMessage("Hello from server/DBManager.java");
				break;
			case Request.TEST:
				response.setMessage("Hello from server/DBManager.java");
				break;
		}
		
		return response;
	}

}
