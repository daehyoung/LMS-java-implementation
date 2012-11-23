package communication;
/**
 * RequestPacket application - file RequestPacket.java 
 * 
 * This object carries message between server and client
 * 
 * @author Sardor Isakov
 * @version 1.0
 */

import java.io.Serializable;
import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;
/** Representation for RequestPacket object
 */
public class RequestPacket implements Serializable {
	protected static final long serialVersionUID = 200L;

	public static final int REQUEST_SQL = 0, REQUEST_LOGIN = 1, REQUEST_LOGOUT = 2;
	public static final int SELECT = 0, UPDATE = 1, DROP = 2, INSERT = 3, TEST = 99;
	private int Code;
	private int packetType;
	private String sqlStatment;
	private CachedRowSet rowSet;
	private String test;
	private int[] insertResult;
	private boolean updated;
	
	
	//Getter and setter
	public String getTest() {
        return test;
    }
    
    public void setTest(String test) {
        this.test = test;
    }
	
	public int getCode() {
		return Code;
	}
	
	public void setCode(int code) {
		this.Code = code;
	}
	
	public String getSqlStatment() {
		return sqlStatment;
	}
	
	public void setSqlStatment(String sqlStatment) {
		this.sqlStatment = sqlStatment;
	}
	
	public CachedRowSet getRowSet() {
		return rowSet;
	}
	
	public void setRowSet(CachedRowSet rowSet) {
		this.rowSet = rowSet;
	}

	public int getType() {
		return packetType;
	}

	public void setType(int type) {
		this.packetType = type;
	}

	public int[] getInsertResult() {
		return insertResult;
	}

	public void setInsertResult(CachedRowSetImpl cachedRowSetImpl) {
		//this.insertResult = cachedRowSetImpl;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	

}
