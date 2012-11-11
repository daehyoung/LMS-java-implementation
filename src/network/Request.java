package network;
/**
 * Request.java 
 * 
 * this object is for testing
 *
 * @author Sardor Isakov
 * @version 1.0
 */
import java.io.Serializable;
import javax.sql.rowset.CachedRowSet;

public class Request implements Serializable {
	protected static final long serialVersionUID = 1112122200L;
	public static final int LOGIN = 0, DATABASEQUERY = 1, LOGOUT = 2, GUEST = 3;
	public static final int SELECT = 0, UPDATE = 1, DROP = 2, INSERT = 3, TEST = 99;
	private int Code;
	private int type;
	private String sqlStatment;
	private CachedRowSet RowSet;
	private String message;
	private String username;
	private String password;
	
	public String getMsg() {
        return message;
    }
    
    public void setMsg(String message) {
        this.message = message;
    }
	
	public Request() {}
	public Request(int type) {
		this.type = type;
	}
	
	public int getCode() {
		return Code;
	}
	
	public void setCode(int code) {
		Code = code;
	}
	
	public String getSqlStatment() {
		return sqlStatment;
	}
	
	public void setSqlStatment(String sqlStatment) {
		this.sqlStatment = sqlStatment;
	}
	
	public CachedRowSet getRowSet() {
		return RowSet;
	}
	
	public void setRowSet(CachedRowSet rowSet) {
		RowSet = rowSet;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}