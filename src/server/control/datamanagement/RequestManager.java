package server.control.datamanagement;

import server.databaseinterface.DBInterface;
import communication.RequestPacket;

public class RequestManager {

	public RequestPacket getReponse(RequestPacket request) {
		return null;
	}

	public RequestPacket processRequest(RequestPacket request) {
		String sql = request.getSqlStatment();
		
		switch(request.getCode()) {
			case RequestPacket.SELECT:
				try {
					request.setRowSet(DBInterface.INSTANCE.getRecord(sql));
				} catch (Exception e) {
					e.printStackTrace();
				} break;
			case RequestPacket.INSERT:
				try {
					request.setInsertResult(DBInterface.INSTANCE.insertRecord(sql));
				} catch (Exception e) {
					e.printStackTrace();
				} break;
			case RequestPacket.TEST:
				try {
					request.setMsg("Hello form RequestManager");
				} catch (Exception e) {
					e.printStackTrace();
				} break;
		}
		
		return request;
	}
}
//END