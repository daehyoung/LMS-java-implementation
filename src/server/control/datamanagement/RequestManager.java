package server.control.datamanagement;

import server.dbinterface.*;
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
					request.setRowSet(DBInterface.getInstance().executeQuery(sql));
				} catch (Exception e) {
					e.printStackTrace();
				} break;
			case RequestPacket.INSERT:
				try {
					request.setInsertResult(DBInterface.getInstance().executeInsert(sql));
				} catch (Exception e) {
					e.printStackTrace();
				} break;
			case RequestPacket.UPDATE:
				try {
					request.setUpdated(DBInterface.getInstance().executeUpdate(sql));
				} catch (Exception e) {
					e.printStackTrace();
				} break;
			case RequestPacket.TEST:
				try {
					request.setTest("Hello form RequestManager");
				} catch (Exception e) {
					e.printStackTrace();
				} break;
		}
		
		return request;
	}
}
//END

