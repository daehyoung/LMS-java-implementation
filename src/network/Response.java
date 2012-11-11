package network;
/**
 * Response.java 
 * 
 * this object is for testing
 *
 * @author Sardor Isakov
 * @version 1.0
 */
import java.io.Serializable;

import javax.sql.rowset.CachedRowSet;

public class Response  implements Serializable {
	protected static final long serialVersionUID = 113122200L;
	
	private CachedRowSet RowSet;
	private String message;

	public CachedRowSet getRowSet() {
		return RowSet;
	}
	public void setRowSet(CachedRowSet rowSet) {
		RowSet = rowSet;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
