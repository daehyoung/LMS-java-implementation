package server.control.datamanagement;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import server.dbinterface.DBInterfaceXX;
import com.sun.rowset.CachedRowSetImpl;

/**
 * This is the Email Manager that will handle all email being sent to the user automatically.
 * It will send an email to a user when their loan is almost overdue, when their loan is overdue and when a user's reservation is ready to be picked up.
 * It will also update the last_email_date of the loan and reservation when it sends the email.
 * 
 * @author Peter Abelseth
 * @version 1
 */
public class EmailManager extends ScheduledTask {
	
	private DBInterfaceXX dbInterface;	//interface to the database


	private final static long RUN_EMAILMGR_10MIN = 10*60*1000;	//Run the email manager once every 10 minutes. Short time to accommodate sending emails to ready reserves in a timely matter
	
	private final static long OVERDUE_EMAIL_PERIOD = 24*60*60*1000L;		//the time between sending another email to an overdue loan
	private final static long OVERDUE_DAYS_TO_EMAIL = 4*7*24*60*60*1000L;	//the number of days until emailing an overdue fine should be stopped
	
	private final static long DAYS_BEFORE_DUE_TO_EMAIL = 2*24*60*60*1000L;	//the number of days before a resource is due to email the user a reminder
	
	private final static long RESERVATION_READY_EMAIL_PERIOD = 2*24*60*60*1000L;	//the time between sending another email to notify a reservation is ready

	
	//Parameters for email server
	private final static String EMAIL_HOST = "smtp.gmail.com";
	private final static String EMAIL_FROM = "librislms@gmail.com";
	private final static String EMAIL_PASSWORD = "Daniel'sAngels";
	private final static String EMAIL_PORT = "465";
	
	
	/**
	 * Constructs an email manager that will run every 10 minutes starting at the next round minute
	 */
	public EmailManager(){
		super(RUN_EMAILMGR_10MIN);
		
		//Get the time of the next round minute
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+1);
		
		start(cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());	//start at the next round minute
	}
	
	/**
	 * Constructs an email manager to run starting at the timeToStart and repeating based on the intervalToRun
	 * @param intervalToRun The time between running the email manager again
	 * @param timeToStart The time until the email manager should run for the first time
	 */
	public EmailManager(long intervalToRun, long timeToStart){
		super(intervalToRun);
		start(timeToStart);
	}

	/**
	 * The task that is performed periodically, sends out the emails for overdue resource, almost due resources, and reservations that are ready
	 */
	protected void performTask() {
		//System.out.println(Calendar.getInstance().getTime());
		
		
		try {
			this.dbInterface = DBInterfaceXX.getReference();
			if(dbInterface == null)	//if the dbInterface is null, the database hasn't been initialized
				throw new SQLException("Database hasn't been initialized");
			
			sendOverdueEmail();
			sendNearlyDueEmail();
			sendReadyReservationEmail();
		
		} catch (SQLException e) {	//sql command error or db connection error
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {	//email server error
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Manages sending emails to users with overdue resources	
	 * @throws SQLException If couldn't query the database for overdue resources
	 * @throws MessagingException If problem with email server
	 */
	private void sendOverdueEmail() throws SQLException, MessagingException{
		ArrayList<Loan> overdueLoansToEmail = getOverdueLoansToEmail();	//get the overdue resources that need to have emails sent

		for(Loan loan: overdueLoansToEmail){	//for each loan to send email to
			String emailSubject = "Your Loan of \"" + loan.resourceTitle + "\" is Overdue";
			String emailAddress = loan.email;
			String emailMessage = createOverdueMessage(loan);
			sendEmail(emailAddress, emailSubject, emailMessage);
			dbInterface.executeUpdate("UPDATE loan SET last_email_date = UNIX_TIMESTAMP(NOW())*1000 WHERE loan_ID = " + loan.loanID);	//update the last email date in the database
		}
	}
	
	/**
	 * Manages sending emails to user with resources that are due soon
	 * @throws SQLException If couldn't query the database for resources that are almost due
	 * @throws MessagingException If a problem with email server
	 */
	private void sendNearlyDueEmail() throws SQLException, MessagingException{
		ArrayList<Loan> nearlyDueLoansToEmail = getNearlyDueLoansToEmail();

		for(Loan loan: nearlyDueLoansToEmail){
			String emailSubject = "Your Loan of \"" + loan.resourceTitle + "\" is Due Soon";
			String emailAddress = loan.email;
			String emailMessage = createNearlyDueMessage(loan);
			sendEmail(emailAddress, emailSubject, emailMessage);
			dbInterface.executeUpdate("UPDATE loan SET last_email_date = UNIX_TIMESTAMP(NOW())*1000 WHERE loan_ID = " + loan.loanID);
		}
	}

	/**
	 * Manages sending emails to users who have a reservation that is ready to be picked up
	 * @throws SQLException If couldn't query the database for reservations that are ready
	 * @throws MessagingException If problem with email server
	 */
	private void sendReadyReservationEmail() throws SQLException, MessagingException{
		ArrayList<Reservation> readyReservationsToEmail = getReadyReservationsToEmail();
		
		for(Reservation reservation: readyReservationsToEmail){
			String emailSubject = "Your Reservation of \"" + reservation.resourceTitle + "\" is Now Available";
			String emailAddress = reservation.email;
			String emailMessage = createReservationReadyEmail(reservation);
			sendEmail(emailAddress, emailSubject, emailMessage);
			dbInterface.executeUpdate("UPDATE reservation SET last_email_date = UNIX_TIMESTAMP(NOW())*1000 WHERE reservation_ID = " + reservation.reservationID);
		}	
	}
	
	
	/**
	 * Sends an email to the given user with given subject and body
	 * @param emailAddress The address to send the email to
	 * @param emailSubject The subject of the email to send
	 * @param emailMessage The body of the email to send
	 * @throws MessagingException If there is a problem with the email server
	 */
	private void sendEmail(String emailAddress, String emailSubject, String emailMessage) throws MessagingException{
		Properties props = System.getProperties();
		props.put("mail.smtp.host", EMAIL_HOST);
		props.put("mail.smtp.socketFactory.port", EMAIL_PORT);
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.port", EMAIL_PORT);
		
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator(){
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(EMAIL_FROM,EMAIL_PASSWORD);
					}
				});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(EMAIL_FROM));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailAddress));
			message.setSubject(emailSubject);
			message.setText(emailMessage);
			Transport.send(message);
			
		} catch (AddressException e) {
			//Bad email Address
			//For now do nothing. Ideally, if time, could possibly add a flag to the user to update their email
		}
	}
	
	/**
	 * Creates the body message to send to a user with an overdue resource
	 * @param loan The loan that is overdue
	 * @return message The message to send to a user with an overdue resource
	 */
	private String createOverdueMessage(Loan loan){
		String heading = "Dear, " + loan.firstName + " " + loan.lastName + "\n";
		
		Calendar dueDate = Calendar.getInstance();
		dueDate.setTimeInMillis(loan.dueDate);
		Calendar checkOutDate = Calendar.getInstance();
		checkOutDate.setTimeInMillis(loan.checkOutDate);
		
		String bodyP1 = "Your loan of \"" + loan.resourceTitle +
				"\" that was checked out on " +
				checkOutDate.getDisplayName(Calendar.DAY_OF_WEEK,  Calendar.LONG, Locale.CANADA) + " " +
				checkOutDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " " +
				checkOutDate.get(Calendar.DAY_OF_MONTH) + ", " +
				checkOutDate.get(Calendar.YEAR) + " " +
				" was due on " +
				dueDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CANADA) + " " +
				dueDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " " +
				dueDate.get(Calendar.DAY_OF_MONTH) + ", " +
				dueDate.get(Calendar.YEAR) + ".\n";
		
		String bodyP2 = "So far this loan has incurred $" +
				loan.fine.toString() +
				" in fines.\n";
		
		String bodyP3 = "Please return it as soon as possible.\n";
		
		String footer = "\nThank you,\n" + "The UBC CICSR Library";
				
		return heading + bodyP1 + bodyP2 + bodyP3 + footer;
	}
	
	/**
	 * Creates the body message to send to a user with an almost due resource
	 * @param loan The loan that is almost due
	 * @return message The message to send to a user with an almost due resource
	 */
	private String createNearlyDueMessage(Loan loan){
		String heading = "Dear, " + loan.firstName + " " + loan.lastName + "\n";
		
		Calendar dueDate = Calendar.getInstance();
		dueDate.setTimeInMillis(loan.dueDate);
		Calendar checkOutDate = Calendar.getInstance();
		checkOutDate.setTimeInMillis(loan.checkOutDate);
		
		String bodyP1 = "Your loan of \"" + loan.resourceTitle +
				"\" that was checked out on " +
				checkOutDate.getDisplayName(Calendar.DAY_OF_WEEK,  Calendar.LONG, Locale.CANADA) + " " +
				checkOutDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " " +
				checkOutDate.get(Calendar.DAY_OF_MONTH) + ", " +
				checkOutDate.get(Calendar.YEAR) + " " +
				" is due on " +
				dueDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CANADA) + " " +
				dueDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " " +
				dueDate.get(Calendar.DAY_OF_MONTH) + ", " +
				dueDate.get(Calendar.YEAR) + ".\n";
		
		String bodyP2 = "Please return it before the due date to avoid being fined.";
		
		String footer = "\nThank you,\n" + "The UBC CICSR Library";
		
		return heading + bodyP1 + bodyP2 + footer;
	}
	
	/**
	 * Creates the body message to send to a user with a reservation that is ready
	 * @param reservation The Reservation that is ready to be picked up
	 * @return message The message to send to a user with a ready reservation
	 */
	private String createReservationReadyEmail(Reservation reservation){
		String heading = "Dear, " + reservation.firstName + " " + reservation.lastName + "\n";
		
		String bodyP1 = "Your reservation of \"" + reservation.resourceTitle + "\" is now ready to be picked up.\n";
		
		String bodyP2 = "Please come to pick up your reservation soon.\n";
		
		String footer = "\nThank you,\n" + "The UBC CICSR Library";
		
		return heading + bodyP1 + bodyP2 + footer;
	}
	
	/**
	 * Queries the database for a list of overdue loans that need to be emailed
	 * @return overdueLoansToEmail The list of loans that need to be emailed
	 * @throws SQLException If the database couldn't be queried.
	 */
	private ArrayList<Loan> getOverdueLoansToEmail() throws SQLException{
		CachedRowSetImpl result = dbInterface.executeQuery(SQL_GET_OVERDUE_TO_EMAIL); //execute query to get overdue loans
		
		ArrayList<Loan> overdueLoansToEmail = new ArrayList<Loan>();
		while(result.next()){	//insert each overdue loan into the array list
				overdueLoansToEmail.add(new Loan(
					result.getInt(1),
					result.getLong(2),
					result.getLong(3),
					result.getLong(4),
					result.getBigDecimal(5),
					result.getInt(6),
					result.getString(7),
					result.getString(8),
					result.getString(9),
					result.getString(10)));
		}
		return overdueLoansToEmail;	//return the arraylist
	}
	
	/**
	 * Queries the database for a list of loans that are alsmost due and need to be emailed
	 * @return nearlyDueLoansToEmail The list of loans that need to be emailed
	 * @throws SQLException If the database couldn't be queried.
	 */
	private ArrayList<Loan> getNearlyDueLoansToEmail() throws SQLException{
		CachedRowSetImpl result = dbInterface.executeQuery(SQL_GET_ALMOST_DUE_TO_EMAIL);
		ArrayList<Loan> nearlyDueLoansToEmail = new ArrayList<Loan>();
		while(result.next()){
			nearlyDueLoansToEmail.add(new Loan(
					result.getInt(1),
					result.getLong(2),
					result.getLong(3),
					result.getLong(4),
					result.getBigDecimal(5),
					result.getInt(6),
					result.getString(7),
					result.getString(8),
					result.getString(9),
					result.getString(10)));
		}
		return nearlyDueLoansToEmail;
	}
	
	/**
	 * Queries the database for a list of reservations that are ready to be picked up
	 * @return reservationsReadyToEmail The list of reservations that are ready and need to be emailed
	 * @throws SQLException If the database couldn't be queried.
	 */
	private ArrayList<Reservation> getReadyReservationsToEmail() throws SQLException{
		CachedRowSetImpl result = dbInterface.executeQuery(SQL_GET_RESERVATION_READY_TO_EMAIL);
		ArrayList<Reservation> reservationsReadyToEmail = new ArrayList<Reservation>();
		while(result.next()){
			reservationsReadyToEmail.add(new Reservation(
					result.getInt(1),
					result.getLong(2),
					result.getLong(3),
					result.getString(4),
					result.getString(5),
					result.getString(6),
					result.getString(7)));
		}
		return reservationsReadyToEmail;
	}
	
	/**
	 * Nested class to store necessary information about overdue and almost due loans
	 */
	@SuppressWarnings("unused")
	private class Loan{
		public int loanID;
		public long checkOutDate;
		public long dueDate;
		
		public long lastEmailDate;
		public BigDecimal fine;
		public int userID;
		public String firstName;
		public String lastName;
		public String email;
		public String resourceTitle;
		public Loan(int loanID, long checkOutDate, long dueDate, long lastEmailDate, BigDecimal fine, int userID, String firstName, String lastName, String email, String resourceTitle){
			this.loanID = loanID;
			this.checkOutDate = checkOutDate;
			this.dueDate = dueDate;
			this.lastEmailDate = lastEmailDate;
			this.fine = fine;
			this.userID = userID;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.resourceTitle = resourceTitle;
		}
		
	};
	
	
	/**
	 * Nested class used to store necessary information about Reservations that are ready to be picked up
	 */
	@SuppressWarnings("unused")
	private class Reservation{
		public int reservationID;
		public long availableDate;
		public long lastEmailDate;
		public String firstName;
		public String lastName;
		public String email;
		public String resourceTitle;
		public Reservation(int reservationID, long availableDate, long lastEmailDate, String firstName, String lastName, String email, String resourceTitle){
			this.reservationID = reservationID;
			this.availableDate = availableDate;
			this.lastEmailDate = lastEmailDate;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.resourceTitle = resourceTitle;
		}
	};
	
	
	//SQL query to get a list of all overdue resources that need to be emailed
	private final String SQL_GET_OVERDUE_TO_EMAIL = "SELECT " +
			"loan.loan_ID, loan.check_out_date, loan.due_date, loan.last_email_date, loan.fine, user.user_ID, user.first_name, user.last_name, user.email, resource.title " +
			"FROM " +
			"loan,user,resourceCopy,resource " +
			"WHERE " +
			"loan.user_user_ID = user.user_ID AND " +
			"loan.resourceCopy_barcode = resourceCopy.barcode AND " +
			"resourceCopy.resource_resource_ID = resource.resource_ID AND " +
			"loan.check_in_date IS NULL AND " +															//hasn't been checked in
			"loan.due_date < UNIX_TIMESTAMP(NOW())*1000 AND " +											//was due
			"loan.due_date > (UNIX_TIMESTAMP(NOW())*1000 - " + OVERDUE_DAYS_TO_EMAIL + ") AND " +		//wasn't due too long ago
			"(loan.last_email_date IS NULL OR " +														//was never emailed OR
			"loan.last_email_date < (UNIX_TIMESTAMP(NOW())*1000 - " + OVERDUE_EMAIL_PERIOD + "))";		//last email was long enough ago to send another one
	
	//SQL query to get a list of all resources that are almost due to be emailed
	private final String SQL_GET_ALMOST_DUE_TO_EMAIL = "SELECT " +
			"loan.loan_ID, loan.check_out_date, loan.due_date, loan.last_email_date, loan.fine, user.user_ID, user.first_name, user.last_name, user.email, resource.title " +
			"FROM " +
			"loan, user, resourceCopy, resource " +
			"WHERE " +
			"loan.user_user_ID = user.user_ID AND " +
			"loan.resourceCopy_barcode = resourceCopy.barcode AND " +
			"resourceCopy.resource_resource_ID = resource.resource_ID AND " +
			"loan.check_in_date IS NULL AND " +														//hasn't been checked in
			"loan.last_email_date IS NULL AND " +													//hasn't been emailed
			"loan.due_date > UNIX_TIMESTAMP(NOW())*1000 AND " +										//isn't due yet
			"loan.due_date < (UNIX_TIMESTAMP(NOW())*1000 + " + DAYS_BEFORE_DUE_TO_EMAIL + ")";		//is due in less the # days to be considered due soon
	
	//SQL query to get a list of all reservations that are ready to be picked up and need to be emailed
	private final String SQL_GET_RESERVATION_READY_TO_EMAIL = "SELECT " +
			"reservation.reservation_ID,reservation.available_date, reservation.last_email_date, user.first_name, user.last_name, user.email, resource.title " +
			"FROM " +
			"reservation, user, resource " +
			"WHERE " +
			"reservation.user_user_ID = user.user_ID AND " +
			"reservation.resource_resource_ID = resource.resource_ID AND " +
			"reservation.available_date IS NOT NULL AND " +								//resource is available
			"(reservation.last_email_date IS NULL OR " +							//hasn't been email yet OR
			"reservation.last_email_date < (UNIX_TIMESTAMP(NOW())*1000 - " + RESERVATION_READY_EMAIL_PERIOD + "))";		//email has been long enough ago to send another
	
//Just to test if this class is functioning
/*
	public static void main(String args[]){
		DBInterface.createDBInterface(
				"jdbc:mysql://localhost/",
				"librisDB",
				"root",
				"root");
		EmailManager emailMgr = new EmailManager(10*1000, 1000);
	}
*/
}



