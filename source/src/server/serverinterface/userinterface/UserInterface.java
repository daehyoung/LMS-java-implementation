package server.serverinterface.userinterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import server.control.backupmanagement.BackupManager;
import server.control.configmanagement.Server;
import server.control.configmanagement.ServerConfiguration;
import server.control.errormanagement.LogManager;

/**
 * @author Peter Abelseth
 * @version 1
 */
public class UserInterface extends Thread {

	private final static String CONFIG_LOCATION = "./config/server.config";
	private BufferedReader consoleInputReader;
	private boolean keepRunning = false;
	
	/**
	 * Creates a new user interface by getting the input stream from the console
	 */
	public UserInterface(){
		consoleInputReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	/**
	 * Starts running the user interface
	 */
	public void start(){
		
		keepRunning = true;
		try {
			Server.getReference().startServer(loadConfiguration());
			LogManager.getReference().logMessage("Server Started Successfully");
		} catch (IOException e) {
			LogManager.getReference().logMessage("Error: " + e.getMessage());
			keepRunning = false;
		}
		super.start();
	}
	
	/**
	 * Method to run the user interface thread
	 */
	public void run(){
		LogManager.getReference().logMessage("User Interface Active. Type help for a list of commands.");
		while(keepRunning){
			try {
				String command = consoleInputReader.readLine();
				if(!keepRunning)
					break;
				handleRequest(command);
			} catch (IOException e) {

			}

		}

	}
	
	/**
	 * stop the user interface
	 */
	public void close(){
		keepRunning = false;
	}
	
	
	/**
	 * Handles the request from the user input
	 * @param request The command given by the user
	 */
	private void handleRequest(String request){
		if(request == null)
			return;
		if(request.compareToIgnoreCase("help") == 0){
			LogManager.getReference().logMessage("Available Commands:\n" +
					"shutdown			- shutdown the server\n" +
					"reset				- reset the server\n" +
					"backup	[destination file]	- backup the database to the given file\n" +
					"restore [source file]		- restore the given backup file. Note this will overwrite the current database");
		}
		else if(request.compareToIgnoreCase("shutdown") == 0){
			if(getConfirmation(request)){
				try {
					consoleInputReader.close();
				} catch (IOException e) { }
				this.close();
				Server.getReference().shutDownServer();
			}
			else
				LogManager.getReference().logMessage(request + " aborted.");
		}
		
		else if(request.compareToIgnoreCase("reset") == 0){
			if(getConfirmation(request))
				try {
					Server.getReference().resetServer(loadConfiguration());
					LogManager.getReference().logMessage("Server reset successful.");
				} catch (IOException e) {
					LogManager.getReference().logMessage("Error: " + e.getMessage());
				}
			else
				LogManager.getReference().logMessage(request + " aborted.");
		}
		else if(request.toLowerCase().startsWith("backup ")){
			try {
				if(BackupManager.getReference().backup(request.substring("backup ".length())))
					LogManager.getReference().logMessage("Backup Successful");
				else
					LogManager.getReference().logMessage("Error Performing Backup");
			} catch (InstantiationException e) {
				LogManager.getReference().logMessage("Error. The Backup Manager has not be started yet.");
			}
		}
		else if(request.toLowerCase().startsWith("restore ")){
			try {
				if(getConfirmation(request)){
					if(BackupManager.getReference().restore(request.substring("restore ".length())))
						LogManager.getReference().logMessage("Restored Successfuly");
					else
						LogManager.getReference().logMessage("Error restoring the Datasbase");
				}
			} catch (InstantiationException e) {
				LogManager.getReference().logMessage("Error. The Backup Manager has not be started yet.");
			}
		}
		else{
			LogManager.getReference().logMessage("Invalid command.");
		}
		
	}
	
	/**
	 * Gets confirmation from the user to perform the given action
	 * @param request The request from the user
	 * @return True if the user confirmed it, false otherwise
	 */
	private boolean getConfirmation(String request){
		LogManager.getReference().logMessage("Are you sure you want to " + request + " ? (Y/N): ");
		try {
			while(true){
				String answer = consoleInputReader.readLine();
				if(answer == null)
					return false;
				if(answer.compareToIgnoreCase("Y") == 0)
					return true;	
				else if(answer.compareToIgnoreCase("N") == 0)
					return false;
				LogManager.getReference().logMessage("Try again (Y/N): ");
			}
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * Loads the server configuration
	 * @return
	 * @throws IOException
	 */
	private ServerConfiguration loadConfiguration() throws IOException{
		File configFile = new File(CONFIG_LOCATION);
		ServerConfiguration config = null;
		
		if(configFile.exists())
			config = readConfigFile(configFile);
		else
			config = createDefaultConfigFile();
		
		return config;		
	}
	
	/**
	 * Creates the default server configuration
	 * @return
	 * @throws IOException
	 */
	private ServerConfiguration createDefaultConfigFile() throws IOException{
		ServerConfiguration config = new ServerConfiguration();
		config.connectionMgr_port = 1500;
		config.db_password = "root";
		config.db_url = "jdbc:mysql://localhost:3306/";
		config.db_dbName = "librisDB";
		config.db_userName = "root";
		config.emailMgr_account = "librislms@gmail.com";
		config.emailMgr_host = "smtp.gmail.com";
		config.emailMgr_password = "Daniel'sAngels";
		config.emailMgr_port = "465";
		config.fineMgr_timeOfDayToRun = 1*60*60*1000L;
		config.overdueMgr_periodBetweenEmails = 2*24*60*60*1000L;
		config.overdueMgr_timeBeforeDueToEmail = 2*24*60*60*1000L;
		config.overdueMgr_timeUntilStopEmails = 4*7*24*60*60*1000L;
		config.overdueMgr_timeOfDayToRun = 90*60*1000L;
		config.reservationMgr_periodBetweenEmails = 2*24*60*60*1000L;
		config.reservationMgr_timesToRunADay = 4;
		
		writeServerConfigToFile(config);
		
		return config;
	}
	
	
	private ServerConfiguration readConfigFile(File configFile) throws IOException{
		if(configFile == null || !configFile.isFile() || configFile.isDirectory())	//make sure we read in a proper file
			throw new FileNotFoundException("Bad config file");
		
		FileInputStream fInStream = new FileInputStream(configFile);
		DataInputStream dInStream = new DataInputStream(fInStream);
		BufferedReader bReader = new BufferedReader(new InputStreamReader(dInStream));
		
		ServerConfiguration serverConfig = new ServerConfiguration();
		String line = null;
		
		while((line = bReader.readLine()) != null){	//read each line of the file
			try{
				String lowerCase = line.toLowerCase().trim();		//lower case it and trim the edges
				
				if(line.startsWith("//"));	//do nothing (comment line)
				
				else if(lowerCase.startsWith(CLIENT_LISTENERPORT) && serverConfig.connectionMgr_port <= 0){
					serverConfig.connectionMgr_port = Integer.parseInt(line.substring(CLIENT_LISTENERPORT.length()));
				}
				else if(line.toLowerCase().startsWith(DB_URL) && serverConfig.db_url == null){
					serverConfig.db_url = line.substring(DB_URL.length());
				}
				else if(line.toLowerCase().startsWith(DB_DBNAME) && serverConfig.db_dbName == null){
					serverConfig.db_dbName = line.substring(DB_DBNAME.length());
				}
				else if(line.toLowerCase().startsWith(DB_DBUSERNAME) && serverConfig.db_userName == null){
					serverConfig.db_userName = line.substring(DB_DBUSERNAME.length());
				}
				else if(line.toLowerCase().startsWith(DB_PASSWORD) && serverConfig.db_password == null){
					serverConfig.db_password = line.substring(DB_PASSWORD.length());
				}
				else if(line.toLowerCase().startsWith(EMAIL_HOST) && serverConfig.emailMgr_host == null){
					serverConfig.emailMgr_host = line.substring(EMAIL_HOST.length());
				}
				else if(line.toLowerCase().startsWith(EMAIL_PORT) && serverConfig.emailMgr_port == null){
					serverConfig.emailMgr_port = line.substring(EMAIL_PORT.length());
				}
				else if(line.toLowerCase().startsWith(EMAIL_ACCOUNT) && serverConfig.emailMgr_account == null){
					serverConfig.emailMgr_account = line.substring(EMAIL_ACCOUNT.length());
				}
				else if(line.toLowerCase().startsWith(EMAIL_PASSWORD) && serverConfig.emailMgr_password == null){
					serverConfig.emailMgr_password = line.substring(EMAIL_PASSWORD.length());
				}
				else if(line.toLowerCase().startsWith(OVERDUE_TIMEOFDAYTORUN) && serverConfig.overdueMgr_timeOfDayToRun < 0){
					serverConfig.overdueMgr_timeOfDayToRun = Long.parseLong(line.substring(OVERDUE_TIMEOFDAYTORUN.length()));
				}
				else if(line.toLowerCase().startsWith(OVERDUE_PERIODBETWEENEMAILS) && serverConfig.overdueMgr_periodBetweenEmails <= 0){
					serverConfig.overdueMgr_periodBetweenEmails = Long.parseLong(line.substring(OVERDUE_PERIODBETWEENEMAILS.length()));
				}
				else if(line.toLowerCase().startsWith(OVERDUE_TIMEUNTILSTOPEMAILS) && serverConfig.overdueMgr_timeUntilStopEmails <= 0){
					serverConfig.overdueMgr_timeUntilStopEmails = Long.parseLong(line.substring(OVERDUE_TIMEUNTILSTOPEMAILS.length()));
				}
				else if(line.toLowerCase().startsWith(OVERDUE_TIMEBEFOREDUETOEMAIL) && serverConfig.overdueMgr_timeBeforeDueToEmail <= 0){
					serverConfig.overdueMgr_timeBeforeDueToEmail = Long.parseLong(line.substring(OVERDUE_TIMEBEFOREDUETOEMAIL.length()));
				}
				else if(line.toLowerCase().startsWith(RESERVATION_TIMESTORUNADAY) && serverConfig.reservationMgr_timesToRunADay <= 0){
					serverConfig.reservationMgr_timesToRunADay = Integer.parseInt(line.substring(RESERVATION_TIMESTORUNADAY.length()));
				}
				else if(line.toLowerCase().startsWith(RESERVATION_PERIODBETWEENEMAILS) && serverConfig.reservationMgr_periodBetweenEmails <= 0){
					serverConfig.reservationMgr_periodBetweenEmails = Long.parseLong(line.substring(RESERVATION_PERIODBETWEENEMAILS.length()));
				}
				else if(line.toLowerCase().startsWith(FINE_TIMEOFDAYTORUN) && serverConfig.fineMgr_timeOfDayToRun < 0){
					serverConfig.fineMgr_timeOfDayToRun = Long.parseLong(line.substring(FINE_TIMEOFDAYTORUN.length()));
				}
			} catch(NumberFormatException e) { }
		}
		
		//close all the readers
		try{
			bReader.close();
		} catch(IOException e) {}
		try{
			dInStream.close();
		} catch(IOException e) {}
		try{
			fInStream.close();
		} catch(IOException e) {}
		

		//make sure the configuration is complete
		if(!serverConfig.everyFieldHasValue())
			throw new IOException("Invalid configuration file.");

		return serverConfig;
	}
	
	/**
	 * Writes the given ServerConfiguration to a file
	 * 
	 * @param config The ServerConfiguration to write to file
	 * @throws IOException If there is a problem writing the file
	 */
	private void writeServerConfigToFile(ServerConfiguration config) throws IOException{
		File configFile = new File(CONFIG_LOCATION);
		
		if(configFile.exists())
			configFile.delete();
		configFile.getParentFile().mkdirs();
		
		try {
			FileWriter fWriter = new FileWriter(configFile);
			BufferedWriter bWriter = new BufferedWriter(fWriter);

			bWriter.write(CLIENT_LISTENERPORT + config.connectionMgr_port + "\n");
			bWriter.write(DB_URL + config.db_url + "\n");
			bWriter.write(DB_DBNAME + config.db_dbName + "\n");
			bWriter.write(DB_DBUSERNAME + config.db_userName + "\n");
			bWriter.write(DB_PASSWORD + config.db_password + "\n");
			bWriter.write(EMAIL_HOST + config.emailMgr_host + "\n");
			bWriter.write(EMAIL_PORT + config.emailMgr_port + "\n");
			bWriter.write(EMAIL_ACCOUNT + config.emailMgr_account + "\n");
			bWriter.write(EMAIL_PASSWORD + config.emailMgr_password + "\n");
			bWriter.write(OVERDUE_TIMEOFDAYTORUN + config.overdueMgr_timeOfDayToRun + "\n");
			bWriter.write(OVERDUE_PERIODBETWEENEMAILS + config.overdueMgr_periodBetweenEmails + "\n");
			bWriter.write(OVERDUE_TIMEUNTILSTOPEMAILS + config.overdueMgr_timeUntilStopEmails + "\n");
			bWriter.write(OVERDUE_TIMEBEFOREDUETOEMAIL + config.overdueMgr_timeBeforeDueToEmail + "\n");
			bWriter.write(RESERVATION_TIMESTORUNADAY + config.reservationMgr_timesToRunADay + "\n");
			bWriter.write(RESERVATION_PERIODBETWEENEMAILS + config.reservationMgr_periodBetweenEmails + "\n");
			bWriter.write(FINE_TIMEOFDAYTORUN + config.fineMgr_timeOfDayToRun + "\n");
			
			try{
				bWriter.close();
			} catch(IOException e) {}
			try{
				fWriter.close();
			} catch(IOException e) {}
		} catch (IOException e) {
			throw new IOException("Error writing the config file.");
		}

	}
	
	
	//Parameters to be filled out in serverconfiguration
	private static final String CLIENT_LISTENERPORT = "listener_port:";
	private static final String DB_URL = "db_url:";
	private static final String DB_DBNAME = "db_dbname:";
	private static final String DB_DBUSERNAME = "db_dbusername:";
	private static final String DB_PASSWORD = "db_password:";
	private static final String EMAIL_HOST = "email_host:";
	private static final String EMAIL_PORT = "email_port:";
	private static final String EMAIL_ACCOUNT = "email_account:";
	private static final String EMAIL_PASSWORD = "email_password:";
	private static final String OVERDUE_TIMEOFDAYTORUN = "overdue_timeofdaytorun:";
	private static final String OVERDUE_PERIODBETWEENEMAILS = "overdue_periodbetweenemails:";
	private static final String OVERDUE_TIMEUNTILSTOPEMAILS = "overdue_timeuntilstopemails:";
	private static final String OVERDUE_TIMEBEFOREDUETOEMAIL = "overdue_timebeforeduetoemail:";
	private static final String RESERVATION_TIMESTORUNADAY = "reservation_timestorunaday:";
	private static final String RESERVATION_PERIODBETWEENEMAILS = "reservation_periodbetweenemails:";
	private static final String FINE_TIMEOFDAYTORUN = "fine_timeofdaytorun:";
}
