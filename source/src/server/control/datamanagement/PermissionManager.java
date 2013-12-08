package server.control.datamanagement;

import java.sql.SQLException;

import com.sun.rowset.CachedRowSetImpl;

import server.dbinterface.DBInterface;

/**
 * This is a static class that determines if a User is allowed to perform the given SQL statement on the database using the permissions matrix
 * Also the most embarrassing code of my life...
 * @author Peter Abelseth
 * @version 3
 */
public final class PermissionManager implements UserPermissions {

	public static boolean checkAllowed(String sqlStatement, int userID, int userType){
		boolean isStub = true;
		if(isStub){
			return true;
		}
			
		sqlStatement = sqlStatement.toLowerCase().trim();
		
		if(sqlStatement.startsWith("select"))
			return isAllowedSelect(sqlStatement,userID,userType);
		
		if(sqlStatement.startsWith("insert"))
			return isAllowedInsert(sqlStatement,userID,userType);
		
		if(sqlStatement.startsWith("update"))
			return isAllowedUpdate(sqlStatement,userID,userType);
		
		if(sqlStatement.startsWith(("delete")))
			return isAllowedDelete(sqlStatement,userID,userType);
		
		return false;
	}

	private static boolean isAllowedSelect(String sqlStatement, int userID,	int userType) {
		String tableName = getTableNameSelect(sqlStatement);
		if(tableName == null)
			return false;
		int userTypeToModify = getUserTypeModifyingSelect(sqlStatement,tableName,userID);
		if(userTypeToModify > userType)
			return false;

		int bitNumber = 0;
		if(userTypeToModify >= 0)
			bitNumber += 4;
		
		return checkBinaryTable(tableName,bitNumber,userType);
	}

	private static boolean isAllowedInsert(String sqlStatement, int userID,	int userType) {
		String tableName = getTableNameInsert(sqlStatement);
		if(tableName == null)
			return false;
		int userTypeToModify = getUserTypeModifyingInsert(sqlStatement,tableName,userID);
		if(userTypeToModify > userType)
			return false;

		int bitNumber = 2;
		if(userTypeToModify >= 0)
			bitNumber += 4;
		
		return checkBinaryTable(tableName,bitNumber,userType);
	}

	private static boolean isAllowedUpdate(String sqlStatement, int userID, int userType) {
		String tableName = getTableNameUpdate(sqlStatement);
		if(tableName == null)
			return false;
		int userTypeToModify = getUserTypeModifyingUpdate(sqlStatement,tableName,userID);
		if(userTypeToModify > userType)
			return false;

		int bitNumber = 1;
		if(userTypeToModify >= 0)
			bitNumber += 4;
		
		return checkBinaryTable(tableName,bitNumber,userType);
	}

	private static boolean isAllowedDelete(String sqlStatement, int userID, int userType) {
		String tableName = getTableNameDelete(sqlStatement);
		if(tableName == null)
			return false;
		int userTypeToModify = getUserTypeModifyingDelete(sqlStatement,tableName,userID);
		if(userTypeToModify > userType)
			return false;
		
		int bitNumber = 3;
		if(userTypeToModify >= 0)
			bitNumber += 4;
		
		return checkBinaryTable(tableName,bitNumber,userType);
	}

	
	private static String getTableNameSelect(String sqlStatement) {
		int tableNameStart = 0, tableNameEnd = 0;		//start and end of the where the table name is
		
		int inQuote = -1;		//to check if we're inside a quote
		for(int i=0;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			if(inQuote < 0 && sqlStatement.substring(i).startsWith("from ")){  //get index of character after from clause
				tableNameStart = i + "from ".length();
				break;
			}
		}
		
		if(tableNameStart == 0)	//table name cannot start at begining of a select statement
			return null;
		
		inQuote = -1;
		for(int i=tableNameStart;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			if(inQuote < 0 && sqlStatement.substring(i).startsWith(" where")){	//found end of the table name
				tableNameEnd = i;
				break;
			}
		}
		if(tableNameEnd == 0)
			return null;
		
		String tableName = sqlStatement.substring(tableNameStart,tableNameEnd).trim();
		
		return tableName;
		
	}
	
	
	private static String getTableNameInsert(String sqlStatement) {
		int tableNameStart = 0, tableNameEnd = 0;		//start and end of the where the table name is
		
		int inQuote = -1;		//to check if we're inside a quote
		for(int i=0;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			if(inQuote < 0 && sqlStatement.substring(i).startsWith("insert ")){  //get index of start of table name
				if(sqlStatement.substring(i+"insert ".length()).startsWith("into ")){
					tableNameStart = i + "insert into ".length();
					break;
				}
				tableNameStart = i + "insert ".length();
				break;
			}
		}
		
		if(tableNameStart == 0)	//table name cannot start at beginning of the statement
			return null;
		
		inQuote = -1;
		for(int i=tableNameStart;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			if(inQuote < 0 && sqlStatement.substring(i).startsWith("(")){	//found end of the table name
				tableNameEnd = i;
				break;
			}
		}
		if(tableNameEnd == 0)
			return null;
		
		String tableName = sqlStatement.substring(tableNameStart,tableNameEnd).trim();
		
		return tableName;
	}
	private static String getTableNameUpdate(String sqlStatement) {
		int tableNameStart = 0, tableNameEnd = 0;		//start and end of the where the table name is
		
		int inQuote = -1;		//to check if we're inside a quote
		for(int i=0;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			if(inQuote < 0 && sqlStatement.substring(i).startsWith("update ")){  //get index of start of table name
				tableNameStart = i + "update ".length();
				break;
			}
		}
		
		if(tableNameStart == 0)	//table name cannot start at beginning of the statement
			return null;
		
		inQuote = -1;
		for(int i=tableNameStart;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			if(inQuote < 0 && sqlStatement.substring(i).startsWith(" set")){	//found end of the table name
				tableNameEnd = i;
				break;
			}
		}
		if(tableNameEnd == 0)
			return null;
		
		String tableName = sqlStatement.substring(tableNameStart,tableNameEnd).trim();
		
		return tableName;
	}
	
	
	
	private static String getTableNameDelete(String sqlStatement) {
		int tableNameStart = 0, tableNameEnd = 0;		//start and end of the where the table name is
		
		int inQuote = -1;		//to check if we're inside a quote
		for(int i=0;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			if(inQuote < 0 && sqlStatement.substring(i).startsWith("delete from ")){  //get index of start of table name
				tableNameStart = i + "delete from ".length();
				break;
			}
		}
		
		if(tableNameStart == 0)	//table name cannot start at beginning of the statement
			return null;
		
		inQuote = -1;
		for(int i=tableNameStart;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			if(inQuote < 0 && sqlStatement.substring(i).startsWith(" where")){	//found end of the table name
				tableNameEnd = i;
				break;
			}
		}
		if(tableNameEnd == 0)
			return null;
		
		String tableName = sqlStatement.substring(tableNameStart,tableNameEnd).trim();
		
		return tableName;	
	}
	
	
	private static int getUserTypeModifyingSelect(String sqlStatement, String tableName, int userID) {
		sqlStatement = sqlStatement.replace(" ", "");
		String userEquals = "user_user_id=";
		if(tableName.compareTo("user") == 0 || tableName.compareTo("userextended") == 0)
			userEquals = "user_id=";
		
		int inQuote = -1;
		for(int i=0;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			
			if(inQuote < 0 && sqlStatement.substring(i).startsWith(userEquals)){	//if we found start of the user id
				i += userEquals.length();
				if(isIDMatch(sqlStatement.substring(i),userID))	//if it is themself
					return -1;
				int j = i;
				for(;j<sqlStatement.length() && Character.isDigit(sqlStatement.charAt(j));j++);		//loop until we find end of number
				
				return getUserType(sqlStatement.substring(i,j));
			}
		}
		return 0;
	}
	
	private static int getUserTypeModifyingInsert(String sqlStatement, String tableName, int userID) {
		sqlStatement = sqlStatement.replace(" ", "");
		String userEquals = "user_user_id";
		if(tableName.compareTo("user") == 0 || tableName.compareTo("userextended") == 0)
			userEquals = "type";
		
		int commaCount = 0;
		int inQuote = -1;
		boolean declaringNames = false;		//to know if we have started to declare attribute names
		int i=0;
		for(;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			if(inQuote < 0 && declaringNames){
				if(sqlStatement.charAt(i) == ')')
					return 0;
				if(sqlStatement.substring(i).startsWith(userEquals))
					break;
				if(sqlStatement.charAt(i) == ',')
					commaCount ++;
			}
			if(inQuote < 0 && sqlStatement.charAt(i) == '(')
				declaringNames = true;
		}
		inQuote = -1;
		boolean declaringValues = false;
		for(;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			if(inQuote < 0 && declaringValues){
				if(sqlStatement.charAt(i) == ')')
					return 0;
				if(commaCount == 0){	//found where type name is being declared
					int j = i;
					for(;j<sqlStatement.length() && Character.isDigit(sqlStatement.charAt(j));j++);		//loop until we find end of number
					
					if(userEquals.compareTo("type") == 0){
						try{
							int userType = Integer.parseInt(sqlStatement.substring(i,j));
							return userType;
						} catch(NumberFormatException e){}
						return 0;		//return highest user if can't parse to int
					}
					return getUserType(sqlStatement.substring(i,j));
				}
				if(sqlStatement.charAt(i) == ',')
					commaCount --;
			}
			if(inQuote < 0 && sqlStatement.charAt(i) == '(')
				declaringValues = true;
		}
		return 0;
		
		
	}
	
	private static int getUserTypeModifyingUpdate(String sqlStatement, String tableName, int userID) {
		sqlStatement = sqlStatement.replace(" ", "");
		String identifier = "_id=";
		
		int inQuote = -1;
		boolean passedWhere = false;
		for(int i=0;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			if(passedWhere && inQuote < 0 && sqlStatement.substring(i).startsWith(identifier)){	//if we found start of the user id
				i += identifier.length();
				if(tableName.compareTo("user") == 0 && isIDMatch(sqlStatement.substring(i),userID))	//if it is themself
					return -1;
				int j = i;
				for(;j<sqlStatement.length() && Character.isDigit(sqlStatement.charAt(j));j++);		//loop until we find end of number
				
				return getUserType(sqlStatement.substring(i,j));
			}
			if(inQuote < 0 && sqlStatement.substring(i).startsWith("where"))
				passedWhere = true;
		}
		return 0;
	}
	
	private static int getUserTypeModifyingDelete(String sqlStatement, String tableName, int userID) {
		sqlStatement = sqlStatement.replace(" ", "");
		String identifier = "_id=";
		
		int inQuote = -1;
		boolean passedWhere = false;
		for(int i=0;i<sqlStatement.length();i++){
			if(sqlStatement.charAt(i) == '\'' && (i <= 0 || sqlStatement.charAt(i-1) != '\\'))	//check if there is a quote
				inQuote *= -1;		//mark if were inside a quote
			if(passedWhere && inQuote < 0 && sqlStatement.substring(i).startsWith(identifier)){	//if we found start of the user id
				i += identifier.length();
				if(tableName.compareTo("user") == 0 && isIDMatch(sqlStatement.substring(i),userID))	//if it is themself
					return -1;
				int j = i;
				for(;j<sqlStatement.length() && Character.isDigit(sqlStatement.charAt(j));j++);		//loop until we find end of number
				int targetUserID = getUserID(tableName, sqlStatement.substring(i, j));
				return getUserType(sqlStatement.substring(i,j));
			}
			if(inQuote < 0 && sqlStatement.substring(i).startsWith("where"))
				passedWhere = true;
		}
		return 0;
	}
	
	private static int getUserID(String tableName, String id) {
		// TODO TAKE THIS OUT, just for testing
		DBInterface.configureDBInterface("jdbc:mysql://localhost:3306/librisDB", "root", "root");
		
		String sqlQuery = "";
		
		if(tableName.compareTo("loan") == 0)
			sqlQuery = "select user_user_id from loan where loan_id = ";
		else if(tableName.compareTo("reference") == 0)
			sqlQuery = "select user_user_id from loan where loan_id = ";
		else if(tableName.compareTo("reservation") == 0)
			sqlQuery = "select user_user_id from loan where loan_id = ";
		else if(tableName.compareTo("resource") == 0)
			sqlQuery = "select user_user_id from loan where loan_id = ";
		else if(tableName.compareTo("resourcecopy") == 0)
			sqlQuery = "select user_user_id from loan where loan_id = ";
		else if(tableName.compareTo("resourcetype") == 0)
			sqlQuery = "select user_user_id from loan where loan_id = ";
		else if(tableName.compareTo("todo") == 0)
			sqlQuery = "select user_user_id from loan where loan_id = ";
		else if(tableName.compareTo("user") == 0){
			try{
				return Integer.parseInt(id);
			} catch(NumberFormatException e){
				return 0;
			}
		}
		
		
		
		
		
		sqlQuery += id;
		try {
			CachedRowSetImpl rowSet = DBInterface.getReference().executeSelect(sqlQuery);
			if(rowSet.next())
				return rowSet.getInt("type");
		} catch (SQLException e) {}
		return 0;
	}

	private static int getUserType(String id){
		// TODO remove this! using for testing
		DBInterface.configureDBInterface("jdbc:mysql://localhost:3306/librisDB", "root", "root");
		try {
			String sqlQuery = "select type from user where user_id = " + id;
			CachedRowSetImpl rowSet = DBInterface.getReference().executeSelect(sqlQuery);
			if(rowSet.next())
				return rowSet.getInt("type");
		} catch (SQLException e) {}
		return 0;
	}
	
	
	private static boolean checkBinaryTable(String tableName, int bitNumber, int userType){
		if(userType < 0 || userType > USER_TYPE_ADMIN)
			return false;
		
		if(tableName.compareTo("user") == 0){
			int allowed = USER_BASIC[userType] & ( 1 << bitNumber);
			if(allowed > 0)
				return true;
			return false;
		}
		if(tableName.compareTo("userextended") == 0){
			int allowed = USER_EXTENDED[userType] & (1 << bitNumber);
			if(allowed > 0)
				return true;
			return false;
		}
		if(tableName.compareTo("resource") == 0){
			int allowed = RESOURCE[userType] & (1 << bitNumber);
			if(allowed > 0)
				return true;
			return false;
		}
		if(tableName.compareTo("resourcecopy") == 0){
			int allowed = RESOURCECOPY[userType] & (1 << bitNumber);
			if(allowed > 0)
				return true;
			return false;
		}
		if(tableName.compareTo("subscription") == 0){
			int allowed = SUBSCRIPTION[userType] & (1 << bitNumber);
			if(allowed > 0)
				return true;
			return false;
		}
		if(tableName.compareTo("todo") == 0){
			int allowed = TODO[userType] & (1 << bitNumber);
			if(allowed > 0)
				return true;
			return false;
		}
		if(tableName.compareTo("loan") == 0){
			int allowed = LOAN[userType] & (1 << bitNumber);
			if(allowed > 0 )
				return true;
			return false;
		}
		if(tableName.compareTo("reference") == 0){
			int allowed = REFERENCE[userType] & (1 << bitNumber);
			if(allowed > 0 )
				return true;
			return false;
		}
		if(tableName.compareTo("reservation") == 0){
			int allowed = RESERVATION[userType] & (1 << bitNumber);
			if(allowed > 0 )
				return true;
			return false;
		}
		if(tableName.compareTo("resourceSearch") == 0){
			return true;
		}
		return false;
	}
	
	private static boolean isIDMatch(String statement, int id){
		String sID = Integer.toString(id);
		if(statement.startsWith(sID)){
			if(sID.length() < statement.length()){
				if(Character.isDigit(statement.charAt(sID.length())))
					return false;
			}
			return true;
		}
		return false;
	}
	
/*
	public static void main(String args[]){
		//String stmt = "SELECT todo_ID, name, description, due_date, start_date, completed FROM toDo WHERE name LIKE '%Finish LMS%' AND description LIKE '%Finish implementing the Libris LMS%' AND due_date = 0 AND start_date = 0 AND completed = false ";
		//String stmt = "select * from todo where user_user_ID = 100000000";
		//String stmt = "UPDATE toDo SET name = 'Finish LMS', description = 'Finish implementing the Libris LMS', due_date = 0, start_date = 0 WHERE todo_ID = 0";
		//String stmt = "DELETE FROM toDo WHERE todo_ID = 100";
		//String stmt = "INSERT loan (loan_ID, check_out_date, due_date, fine, user_user_ID, resourceCopy_resourceCopy_ID) VALUES (103, '1354239235361', '1354239235361', 0.0, 111, 100000005)";
		//String stmt = "UPDATE loan SET check_out_date = '1354239560099', due_date = '1354239560099', fine = 0.0, fine_paid = false, user_user_ID = 111, resourceCopy_resourceCopy_ID = 100000005 WHERE loan_ID = 103";
		//String stmt = "SELECT loan_id, check_out_date, check_in_date, due_date, last_email_date, fine, fine_paid, user_user_id, resourceCopy_resourceCopy_ID FROM loan WHERE loan_id = 103 AND check_out_date = 1354239617170 AND due_date = 1354239617170 AND fine = 0.0 AND user_user_ID = 100000000  AND resourceCopy_resourceCopy_ID = 100000005  AND fine_paid = false ";
		//String stmt = "INSERT resource (resource_ID, serial, title, creator, company, publication_date, resourceType_type_ID, enabled) VALUES (1, '1337', 'Jeremy\'s control class adventure', 'Francis Ford Coppola', 'Langara Pictures', 0, 2, true)";
		//String stmt =	"SELECT resource_ID, serial, title, creator, publication_date, company, resourceType_type_ID, enabled FROM resource WHERE resource_ID = 1 AND serial LIKE '%1337%' AND title LIKE '%Jeremy\'s control class adventure%' AND creator LIKE '%Francis Ford Coppola%' AND publication_date = 0 AND company LIKE '%Langara Pictures%' AND resourceType_type_ID = 2 AND enabled = true";	
		//System.out.println(checkAllowed(stmt,100000010,0));
		
		System.out.println(getUserTypeModifyingDelete("delete user where user_id=10000000", "todo", 100000000));
	}*/
}
