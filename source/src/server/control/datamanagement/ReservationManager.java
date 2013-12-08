package server.control.datamanagement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import javax.mail.MessagingException;
import server.control.errormanagement.ErrorManager;
import server.control.errormanagement.LogManager;
import server.dbinterface.DBInterface;
import server.serverinterface.emailinterface.EmailManager;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Sends out emails for available reservations and deletes old reservation
 * 
 * @author Peter Abelseth
 * @version 4
 */
public class ReservationManager extends ScheduledTask 	{
	
	private static final long HR24 = 24*60*60*1000L;
	private static final int MAX_RUN_TIMES_PER_DAY = 24;
	
	private String sql_Select_Reservation_Ready_To_Email;
	private String sql_Select_Expired_Reservations;
	
	
	/**
	 * Constructs the Manager with the given parameters
	 * @param timeToRunPerDay The number of times a day the reservation manager will be run. It will always run in intervals based off of 1 am
	 * @param periodBetweenEmails The time between sending another email to a user
	 */
	public ReservationManager(int timesToRunPerDay, long periodBetweenEmails){
		super(1);
		if(timesToRunPerDay <= 0)
			timesToRunPerDay = 1;
		else if(timesToRunPerDay > MAX_RUN_TIMES_PER_DAY)
			timesToRunPerDay = MAX_RUN_TIMES_PER_DAY;
		long interval = (HR24)/(long)timesToRunPerDay;
		setInterval(interval);
		
		setSQLSelectResevationReadyToEmailString(periodBetweenEmails);
		setSQLSelectExpiredReservationsString();
		
		Calendar cal = Calendar.getInstance();	//get time of first time it should run
		cal.set(Calendar.HOUR_OF_DAY, 1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		while(cal.compareTo(Calendar.getInstance()) < 0)
			cal.add(Calendar.MILLISECOND, (int)interval);
		start(cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
		performTask();	//run right away, one time
	}
	
	/**
	 * Sends emails to users who have reservations ready and removes expired reservations
	 */
	protected void performTask(){
		try {
			removeExpiredReservations();
			sendReadyReservationEmail();	
			String logMsg = "ReservationManager Running.";
			if(getLastRan() > 0){
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(getLastRan());
				logMsg += " Last Ran: " + cal.getTime();
			}
			LogManager.getReference().logMessage(logMsg);
		
		} catch (SQLException e) {	//sql command error or db connection error
			ErrorManager.getReference().fatalSubsystemError(e,DBInterface.getReference());
		}
		
	}
	
	
	/**
	 * Manages sending emails to users who have a reservation that is ready to be picked up
	 * @throws SQLException If couldn't query the database for reservations that are ready
	 */
	private void sendReadyReservationEmail() throws SQLException{
		ArrayList<Reservation> readyReservationsToEmail = getReadyReservationsToEmail();
		
		for(Reservation reservation: readyReservationsToEmail){
			String emailSubject = "\"" + reservation.resourceTitle + "\" is Ready to be Picked Up";
			String emailAddress = reservation.email;
			String emailMessage = createReservationReadyEmail(reservation);
			try{
				EmailManager.getReference().sendEmail(emailAddress, emailSubject, emailMessage);
			}
			catch(MessagingException e){
				//bad email address, possibly add flag to the user
			}
			if(DBInterface.getReference() == null)
				throw new SQLException("Database currently unavailable.");
			DBInterface.getReference().executeUpdate("UPDATE reservation SET last_email_date = UNIX_TIMESTAMP(NOW())*1000 WHERE reservation_ID = " + reservation.reservationID);
		}
	}
	
	/**
	 * Removes expired reservations from the database
	 * @throws SQLException If couldn't send the query to the database
	 */
	private void removeExpiredReservations() throws SQLException{
		if(DBInterface.getReference() == null)
			throw new SQLException("Database currently unavailable.");
		ArrayList<Reservation> expiredReservations = getExpiredReservations();
		
		for(Reservation reservation: expiredReservations){
			//possibly add email to let user know their reservation expired
			String deleteStatement = "DELETE FROM reservation " +
					"WHERE reservation_ID = " + reservation.reservationID;
			boolean success = DBInterface.getReference().executeDelete(deleteStatement);
			
			if(success){
				String updateStatement = "UPDATE reservation, " +
					"(SELECT reservation_ID, MIN(reserved_date) " +
						"FROM reservation WHERE " +
						"available_date IS NULL AND " +
						"resource_resource_ID = " + reservation.resourceID + " ) AS subtable " +
					"SET available_date = UNIX_TIMESTAMP(NOW())*1000, " +
					"expire_date = (UNIX_TIMESTAMP(NOW())*1000 + 24*60*60*1000*7) " + 
					"WHERE " +
					"reservation.reservation_ID = subtable.reservation_ID";
				DBInterface.getReference().executeUpdate(updateStatement);
			}
		}
		
		
		
		//DBInterface.getReference().executeDelete(sql_Delete_Expired_Reservations);
	}
	
	/**
	 * Creates the body message to send to a user with a reservation that is ready
	 * @param reservation The Reservation that is ready to be picked up
	 * @return message The message to send to a user with a ready reservation
	 */
	private String createReservationReadyEmail(Reservation reservation){
		String heading = "Dear, " + reservation.firstName + " " + reservation.lastName + "\n\n";
		
		String bodyP1 = "Your reservation of \"" + reservation.resourceTitle + "\" is now available to be picked up.\n";
		
		Calendar expireDate = Calendar.getInstance();
		expireDate.setTimeInMillis(reservation.expireDate);
		
		String bodyP2 = "This reservation will expire on " +
				expireDate.getDisplayName(Calendar.DAY_OF_WEEK,  Calendar.LONG, Locale.CANADA) + " " +
				expireDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " " +
				expireDate.get(Calendar.DAY_OF_MONTH) + ", " +
				expireDate.get(Calendar.YEAR) + ".\n";
		
		String bodyP3 = "Please come as soon as possible to pick up your reservation.\n";
		
		String footer = "\nThank you,\n" + "The UBC CICSR Library";
		
		return heading + bodyP1 + bodyP2 + bodyP3 + footer;
	}
	
	/**
	 * Queries the database for a list of reservations that are ready to be picked up
	 * @return reservationsReadyToEmail The list of reservations that are ready and need to be emailed
	 * @throws SQLException If the database couldn't be queried.
	 */
	private ArrayList<Reservation> getReadyReservationsToEmail() throws SQLException{
		ArrayList<Reservation> reservationsReadyToEmail = new ArrayList<Reservation>();
		if(DBInterface.getReference() == null)
			throw new SQLException("Database currently unavailable.");
		CachedRowSetImpl result = DBInterface.getReference().executeSelect(sql_Select_Reservation_Ready_To_Email);
		while(result.next()){
			reservationsReadyToEmail.add(new Reservation(
					result.getInt("reservation_ID"),
					result.getLong("available_date"),
					result.getLong("expire_date"),
					result.getLong("last_email_date"),
					result.getString("first_name"),
					result.getString("last_name"),
					result.getString("email"),
					result.getString("title"),
					result.getInt("resource_ID")));
		}

		return reservationsReadyToEmail;
	}
	
	private ArrayList<Reservation> getExpiredReservations() throws SQLException{
		ArrayList<Reservation> expiredReservations = new ArrayList<Reservation>();
		if(DBInterface.getReference() == null)
			throw new SQLException("Database currently unavailable.");
		CachedRowSetImpl result = DBInterface.getReference().executeSelect(sql_Select_Expired_Reservations);
		while(result.next()){
			expiredReservations.add(new Reservation(
					result.getInt("reservation_ID"),
					result.getLong("available_date"),
					result.getLong("expire_date"),
					result.getLong("last_email_date"),
					result.getString("first_name"),
					result.getString("last_name"),
					result.getString("email"),
					result.getString("title"),
					result.getInt("resource_ID")));
		}
		return expiredReservations;
	}
	
	/**
	 * Nested class used to store necessary information about Reservations that are ready to be picked up
	 */
	private class Reservation{
		public int reservationID;
		public long availableDate;
		public long expireDate;
		public long lastEmailDate;
		public String firstName;
		public String lastName;
		public String email;
		public String resourceTitle;
		public int resourceID;
		public Reservation(int reservationID, long availableDate, long expireDate, long lastEmailDate, String firstName, String lastName, String email, String resourceTitle, int resourceID){
			this.reservationID = reservationID;
			this.availableDate = availableDate;
			this.expireDate = expireDate;
			this.lastEmailDate = lastEmailDate;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.resourceTitle = resourceTitle;
			this.resourceID = resourceID;
		}
	};
	
	private void setSQLSelectResevationReadyToEmailString(long periodBetweenEmails){
		sql_Select_Reservation_Ready_To_Email = "SELECT " +
			"reservation.reservation_ID,reservation.available_date, reservation.expire_date, reservation.last_email_date, user.first_name, user.last_name, user.email, resource.title " +
			"FROM " +
			"reservation, user, resource " +
			"WHERE " +
			"reservation.user_user_ID = user.user_ID AND " +
			"reservation.resource_resource_ID = resource.resource_ID AND " +
			"reservation.available_date IS NOT NULL AND " +													//resource is available
			"(reservation.last_email_date IS NULL OR " +													//hasn't been email yet OR
			"reservation.last_email_date < (UNIX_TIMESTAMP(NOW())*1000 - " + periodBetweenEmails + "))";	//email has been long enough ago to send another
	}

	private void setSQLSelectExpiredReservationsString(){
		sql_Select_Expired_Reservations = "SELECT " +
			"reservation.reservation_ID,reservation.available_date, reservation.expire_date, reservation.last_email_date, user.first_name, user.last_name, user.email, resource.title, resource.resource_ID " +
			"FROM " +
			"reservation, user, resource " +
			"WHERE " +
			"reservation.user_user_ID = user.user_ID AND " +
			"reservation.resource_resource_ID = resource.resource_ID AND " +
			"reservation.expire_date < UNIX_TIMESTAMP(NOW())*1000";
	}
}
