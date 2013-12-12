package server.control.backupmanagement;

import java.io.IOException;
import java.sql.SQLException;
import com.sun.rowset.CachedRowSetImpl;

import server.control.configmanagement.Server;
import server.control.errormanagement.ErrorManager;
import server.control.errormanagement.LogManager;
import server.dbinterface.DBInterface;

/**
 * Manages backing up the database
 * 
 * @author Peter Abelseth
 * @version 0
 *
 */
public class BackupManager {
	
	private static BackupManager singleton = null;
	
	private String dbName;
	private String dbUserName;
	private String dbPassword;
	
	
	private BackupManager(){
	}
	
	/**
	 * Gets reference for singleton backup manager
	 * 
	 * @return BakupManager The one and only backupManager
	 * @throws InstantiationException If it hasn't been configured yet
	 */
	public static synchronized BackupManager getReference() throws InstantiationException{
		if(singleton == null)
			throw new InstantiationException("BackupManager not configured!");
		return singleton;
	}
	
	public static synchronized BackupManager configureBackupManager(String dbName, String dbUserName, String dbPassword){
		if(singleton == null){
			singleton = new BackupManager();
			singleton.dbName = dbName;
			singleton.dbUserName = dbUserName;
			singleton.dbPassword = dbPassword;
		}
		return singleton;
	}
	
	/**
	 * Closes the backup manager. Just allows garbage collector to eat it up
	 */
	public static synchronized void close(){
		singleton = null;
	}
	
	public boolean backup(String destination){
		String mysqlDump = getMySQLDumpLocation();
		if(mysqlDump == null){
			LogManager.getReference().logError("Error Finding Dump Utility.");
			return false;
		}
		
		
		mysqlDump +="mysqldump -u " + dbUserName + " -p" + dbPassword + " " + dbName + " --result-file=" + destination;
		boolean success = false;
		try {
			success = executeRuntimeCommand(mysqlDump);
		} catch (IOException e) {
			success = false;
		}
		return success;
	}
	
	
	
	public boolean restore(String location){
		String mysqlRestore = getMySQLDumpLocation();
		if(mysqlRestore == null){
			LogManager.getReference().logError("Error Finding Dump Utitlity.");
			return false;
		}
		String[] cmds = new String[] {mysqlRestore + "mysql",
					"--user=" + dbUserName,
					"--password=" + dbPassword,
					dbName,
					"-e",
					" source " + location};
		
		boolean success = false;
		try {
			success = executeRuntimeCommand(cmds);
		} catch (IOException e) {
			success = false;
		}
		return success;
	}
	
	
	
	private boolean executeRuntimeCommand(String command) throws IOException{
		Process proc = Runtime.getRuntime().exec(command);
		
		try {
			if(proc.waitFor() == 0){
				return true;
			}
		} catch (InterruptedException e) {}
		return false;
	}
	
	
	private boolean executeRuntimeCommand(String[] command) throws IOException{
		Process proc = Runtime.getRuntime().exec(command);
		
		try {
			if(proc.waitFor() == 0){
				return true;
			}
		} catch (InterruptedException e) {
		}
		return false;
	}
	
	
	
	private String getMySQLDumpLocation(){
		String mysqlDir = "SELECT @@basedir";
		
		CachedRowSetImpl rowSet = null;
		try{
			if(DBInterface.getReference() == null)
				throw new IllegalArgumentException("Database currently unavailable. Please try again.");
			rowSet = DBInterface.getReference().executeSelect(mysqlDir);
		} catch(RuntimeException e){
			return null;
		}
		String baseLocation = null;
		try {
			if(rowSet.next()){
				baseLocation = rowSet.getString(1);
			}
		} catch (SQLException e) {
			return null;
		}
		baseLocation += "bin/";
		return baseLocation;
	}
}
