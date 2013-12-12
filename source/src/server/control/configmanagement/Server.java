package server.control.configmanagement;

import server.control.backupmanagement.BackupManager;
import server.control.datamanagement.FineManager;
import server.control.datamanagement.OverdueManager;
import server.control.datamanagement.RequestManager;
import server.control.datamanagement.ReservationManager;
import server.control.errormanagement.ErrorManager;
import server.dbinterface.DBInterface;
import server.serverinterface.clientinterface.ClientConnectionManager;
import server.serverinterface.emailinterface.EmailManager;
import server.serverinterface.userinterface.UserInterface;

/**
 * This is the main server class. It will handle all subsystems of the server system.
 * @author Peter Abelseth
 * @version 4
 */
public class Server {
	
	private static Server singleton = null;
	
	private ServerConfiguration config;	//the configuration of the server

 	private ClientConnectionManager clientConnectionMgr;
	private FineManager fineMgr;
	private OverdueManager overdueMgr;
	private ReservationManager reservationMgr;
	
	
	private Server(){
	}
	
	/**
	 * Returns the singleton reference to the server
	 * 
	 * @return Server The server
	 */
	public static synchronized Server getReference(){
		if(singleton == null)
			singleton = new Server();
		return singleton;
	}
	
	/**
	 * Starts the server with the given configuration, will do nothing if the server has already been started
	 * 
	 * @param config The configuration of the server
	 */
	public synchronized void startServer(ServerConfiguration config){

		if(singleton == null)
			Server.getReference();
		this.config = config;
		startErrorManager();
		startDBInterface();
		startRequestManager();
		startClientConnectionManager();
		startEmailManager();
		startFineManager();
		startOverdueManager();
		startReservationManager();
		startBackupManager();
		
	}
	
	/**
	 * Resets the server with the new ServerConfiguration
	 * 
	 * @param config The new ServerConfiguration
	 */
	public synchronized void resetServer(ServerConfiguration config){
		this.config = config;
		//please note: order is important!
		startErrorManager();
		restartDBInterface();
		restartRequestManager();
		restartClientConnectionManager();
		restartEmailManager();
		restartFineManager();
		restartOverdueManager();
		restartReservationManager();
		restartBackupManager();
	}
	
	/**
	 * Shuts down all components of the server, and lets garbage collector eat the server
	 * 
	 */
	public synchronized void shutDownServer(){
		config = null;
		
		BackupManager.close();
		if (fineMgr != null){
			fineMgr.stop();
			fineMgr = null;
		}
		if (overdueMgr != null){
			overdueMgr.stop();
			overdueMgr = null;
		}
		if (reservationMgr != null){
			reservationMgr.stop();
			reservationMgr = null;
		}
		EmailManager.close();
		if(clientConnectionMgr != null){
			clientConnectionMgr.close();
			clientConnectionMgr = null;
		}
		RequestManager.close();
		DBInterface.close();
		ErrorManager.close();
		
		System.exit(0);
	}
	
	/**
	 * Restart the DBInterface using the existing configuration
	 */
	public void restartDBInterface(){
		DBInterface.close();
		startDBInterface();
	}
	
	/**
	 * Restart the Request using the existing configuration
	 */
	public void restartRequestManager(){
		RequestManager.close();
		startRequestManager();
	}
		
	/**
	 * Restart the ClientConnectionManager using the existing configuration
	 */
	public void restartClientConnectionManager(){
		if(clientConnectionMgr != null)
			clientConnectionMgr.close();
		clientConnectionMgr = null;
		startClientConnectionManager();
	}
	
	/**
	 * Restart the EmailManager using the existing configuration
	 */
	public void restartEmailManager(){
		EmailManager.close();
		startEmailManager();
	}
	
	/**
	 * Restart the FineManager using the existing configuration
	 */
	public void restartFineManager(){
		if(fineMgr != null)
			fineMgr.stop();
		fineMgr = null;
		startFineManager();
	}
	
	/**
	 * Restart the OverdueManger using the existing configuration
	 */
	public void restartOverdueManager(){
		if(overdueMgr != null)
			overdueMgr.stop();
		overdueMgr = null;
		startOverdueManager();
	}
	
	/**
	 * Restart the ReservationManager using the existing configuration
	 */
	public void restartReservationManager(){
		if(reservationMgr != null)
			reservationMgr.stop();
		reservationMgr = null;
		startReservationManager();
	}
	
	/**
	 * Restart the BackupManager using the existing configuration
	 */
	public void restartBackupManager(){
		BackupManager.close();
		startBackupManager();
	}
	
	
	
	
//Private Methods
	private void startErrorManager(){
		ErrorManager.getReference();
	}
	
	
	private void startDBInterface(){
		if(config != null){
			DBInterface.configureDBInterface(
					config.db_url + config.db_dbName,
					config.db_userName,
					config.db_password);
		}
	}
	

	private void startRequestManager(){
		RequestManager.getReference();
	}
	
	
	private void startClientConnectionManager(){
		if(config != null){
			this.clientConnectionMgr = new ClientConnectionManager(config.connectionMgr_port);
			this.clientConnectionMgr.start();
		}
	}
	
	
	private void startEmailManager(){
		if(config != null){
			EmailManager.configureEmailManager(
				config.emailMgr_host,
				config.emailMgr_account,
				config.emailMgr_password, 
				config.emailMgr_port);
		}
	}
	
	private void startFineManager(){
		if(config != null){
			this.fineMgr = new FineManager(
					config.fineMgr_timeOfDayToRun);
		}
	}
		
	private void startOverdueManager(){
		if(config != null){
			this.overdueMgr = new OverdueManager(
					config.overdueMgr_timeOfDayToRun,
					config.overdueMgr_periodBetweenEmails,
					config.overdueMgr_timeUntilStopEmails,
					config.overdueMgr_timeBeforeDueToEmail);
		}
	}
	
	private void startReservationManager(){
		if(config != null){
			this.reservationMgr = new ReservationManager(
					config.reservationMgr_timesToRunADay,
					config.reservationMgr_periodBetweenEmails);
		}
	}
	
	private void startBackupManager(){
		if(config != null){
			BackupManager.configureBackupManager(
					config.db_dbName,
					config.db_userName,
					config.db_password);
		}
	}


	/**
	 * Main method that launches the user Interface which starts the server and all that good stuff
	 * @param args Not used.
	 */
	public static void main(String args[]){
		new UserInterface().start();
	}
}



