package server.control.errormanagement;

import java.util.Calendar;

import server.control.backupmanagement.BackupManager;
import server.control.configmanagement.Server;
import server.control.datamanagement.FineManager;
import server.control.datamanagement.OverdueManager;
import server.control.datamanagement.RequestManager;
import server.control.datamanagement.ReservationManager;
import server.dbinterface.DBInterface;
import server.serverinterface.clientinterface.ClientConnectionManager;
import server.serverinterface.emailinterface.EmailManager;

/**
 * @author Peter Abelseth
 * @version 2
 */
public class ErrorManager {

	private static ErrorManager singleton = null;
	
	private boolean shuttingDown = false;
	
	private static final int ALLOWED_TIME_FATAL_ERROR = 2*60*1000;	//time in millis between 2 fatal errors
	
	private Calendar lastFatalError_DBInterface = null;
 	private Calendar lastFatalError_RequestManager = null;
 	private Calendar lastFatalError_ClientConnectionManager = null;
	private Calendar lastFatalError_EmailManager = null;
	private Calendar lastFatalError_FineManager = null;
	private Calendar lastFatalError_OverdueManager = null;
	private Calendar lastFatalError_ReservationManager = null;
	private Calendar lastFatalError_BackupManager = null;
	
	/**
	 * Private constructor
	 */
	private ErrorManager() {
	}
	
	/**
	 * Returns the reference to the single ErrorManager object
	 * 
	 * @return The ErrorManager
	 */
	public static synchronized ErrorManager getReference(){
		if(singleton == null){
			singleton = new ErrorManager();
		}
		return singleton;
	}
	
	/**
	 * Closes the ErrorManager, allows it to be picked up by garbage collector
	 */
	public static synchronized void close(){
		singleton = null;
	}
	
	/**
	 * Report a fatal system error. This will log the error and then shut down the server.
	 * @param e The error that caused it
	 * @param subsystem The subsystem the error is coming from
	 */
	public void fatalSystemError(Exception e, Object subsystem){
		if(!shuttingDown){
			String errorMessage = "Fatal System Error. Shutting down Server: " + subsystem.toString();
			if(e != null)
				errorMessage += "\n" + e.getMessage();
			LogManager.getReference().logError(errorMessage);
			Server.getReference().shutDownServer();
			shuttingDown = true;
		}
	}
	
	/**
	 * This will restart all necessary subsystems to get the given subsystem back up and running.
	 * 
	 * @param e The exception that caused the error
	 * @param subsystem The subsystem that the error came from
	 */
	public void fatalSubsystemError(Exception e, Object subsystem){
		
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MILLISECOND, (int) ALLOWED_TIME_FATAL_ERROR);
		if(subsystem instanceof DBInterface){
			if(lastFatalError_DBInterface == null || lastFatalError_DBInterface.compareTo(cal) < 0)
				fatalSystemError(e,subsystem);
			else{
				lastFatalError_DBInterface = Calendar.getInstance();
				Server.getReference().restartDBInterface();
			}
		}
		else if(subsystem instanceof RequestManager){
			if(lastFatalError_RequestManager == null || lastFatalError_RequestManager.compareTo(cal) < 0)
				fatalSystemError(e,subsystem);
			else{
				lastFatalError_RequestManager = Calendar.getInstance();
				Server.getReference().restartRequestManager();
			}
		}
		else if(subsystem instanceof ClientConnectionManager){
			if(lastFatalError_ClientConnectionManager == null || lastFatalError_ClientConnectionManager.compareTo(cal) < 0)
				fatalSystemError(e,subsystem);
			else{
				lastFatalError_ClientConnectionManager = Calendar.getInstance();
				Server.getReference().restartClientConnectionManager();
			}
		}
		else if(subsystem instanceof EmailManager){
			if(lastFatalError_EmailManager == null || lastFatalError_EmailManager.compareTo(cal) < 0)
				fatalSystemError(e,subsystem);
			else{
				lastFatalError_EmailManager = Calendar.getInstance();
				Server.getReference().restartEmailManager();
			}
		}
		else if(subsystem instanceof FineManager){
			if(lastFatalError_FineManager == null || lastFatalError_FineManager.compareTo(cal) < 0)
				fatalSystemError(e,subsystem);
			else{
				lastFatalError_FineManager = Calendar.getInstance();
				Server.getReference().restartFineManager();
			}
		}
		else if(subsystem instanceof OverdueManager){
			if(lastFatalError_OverdueManager == null || lastFatalError_OverdueManager.compareTo(cal) < 0)
				fatalSystemError(e,subsystem);
			else{
				lastFatalError_OverdueManager = Calendar.getInstance();
				Server.getReference().restartOverdueManager();
			}
		}
		else if(subsystem instanceof ReservationManager){
			if(lastFatalError_OverdueManager == null || lastFatalError_ReservationManager.compareTo(cal) < 0)
				fatalSystemError(e,subsystem);
			else{
				lastFatalError_ReservationManager = Calendar.getInstance();
				Server.getReference().restartReservationManager();
			}
		}
		else if(subsystem instanceof BackupManager){
			if(lastFatalError_BackupManager == null || lastFatalError_BackupManager.compareTo(cal) < 0)
				fatalSystemError(e,subsystem);
			else{
				lastFatalError_BackupManager = Calendar.getInstance();
				Server.getReference().restartBackupManager();
			}
		}
		else{	//who is calling me!
			
		}
	}
	
}



