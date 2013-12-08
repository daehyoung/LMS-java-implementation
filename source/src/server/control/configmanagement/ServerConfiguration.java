package server.control.configmanagement;


/**
 * This is an entity that just contains the parameters for the server
 * @author Peter Abelseth
 * @version 2
 */
public class ServerConfiguration {
	
	public int connectionMgr_port;
	
	public String db_url;
	public String db_dbName;
	public String db_userName;
	public String db_password;
	
	public String emailMgr_host;
	public String emailMgr_port;
	public String emailMgr_account;
	public String emailMgr_password;
	
	public long overdueMgr_timeOfDayToRun;
	public long overdueMgr_periodBetweenEmails;
	public long overdueMgr_timeUntilStopEmails;
	public long overdueMgr_timeBeforeDueToEmail;
	
	public int reservationMgr_timesToRunADay;
	public long reservationMgr_periodBetweenEmails;

	public long fineMgr_timeOfDayToRun;
	
	/**
	 * Initialises all to a null value, or invalid equivalent for int/long
	 */
	public ServerConfiguration(){
		connectionMgr_port = 0;
		
		db_url = null;
		db_dbName = null;
		db_userName = null;
		db_password = null;

		emailMgr_host = null;
		emailMgr_port = null;
		emailMgr_account = null;
		emailMgr_password = null;

		overdueMgr_timeOfDayToRun = -1;
		overdueMgr_periodBetweenEmails = 0;
		overdueMgr_timeUntilStopEmails = 0;
		overdueMgr_timeBeforeDueToEmail = 0;

		reservationMgr_timesToRunADay = 0;
		reservationMgr_periodBetweenEmails = 0;

		fineMgr_timeOfDayToRun = -1;
		
	}
	
	public boolean everyFieldHasValue(){

		if(connectionMgr_port <= 0)
			return false;
		if(db_url == null)
			return false;
		if(db_dbName == null)
			return false;
		if(db_userName == null)
			return false;
		if(db_password == null)
			return false;
		if(emailMgr_host == null)
			return false;
		if(emailMgr_port == null)
			return false;
		if(emailMgr_account == null)
			return false;
		if(emailMgr_password == null)
			return false;
		if(overdueMgr_timeOfDayToRun < 0 || overdueMgr_timeOfDayToRun >= 24*60*60*1000L)
			return false;
		if(overdueMgr_periodBetweenEmails <= 0)
			return false;
		if(overdueMgr_timeUntilStopEmails <= 0)
			return false;
		if(overdueMgr_timeBeforeDueToEmail <= 0)
			return false;
		if(reservationMgr_timesToRunADay <= 0 || reservationMgr_timesToRunADay >= 24*60 )
			return false;
		if(reservationMgr_periodBetweenEmails <= 0)
			return false;
		if(fineMgr_timeOfDayToRun < 0 || fineMgr_timeOfDayToRun >= 24*60*60*1000L)
			return false;
		return true;
	}
}


