package server.control.datamanagement;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import javax.mail.MessagingException;
import com.sun.rowset.CachedRowSetImpl;
import server.control.errormanagement.ErrorManager;
import server.control.errormanagement.LogManager;
import server.dbinterface.DBInterface;
import server.serverinterface.emailinterface.EmailManager;;

/**
 * Manages sending emails to users with overdue resources as well as almsot due resources
 * @author Peter Abelseth
 * @version 3
 */
public class OverdueManager extends ScheduledTask {

	private final static long HR24 = 24*60*60*1000L;	//Run the email manager once every 10 minutes. Short time to accommodate sending emails to ready reserves in a timely matter
		
	private String sql_Select_Almost_Due_To_Email;
	private String sql_Select_Overdue_To_Email;
	
		
	public OverdueManager(long timeOfDayToRun, long periodBetweenEmails, long timeUntilStopEmails, long timeBeforeDueToEmail){
		super(HR24);	//run every 24 hours;
		
		setSQLSelectOverdueToEmail(timeUntilStopEmails,periodBetweenEmails);
		setSQLSelectAlmostDueToEmail(timeBeforeDueToEmail);
		
		if(timeOfDayToRun > HR24)
			timeOfDayToRun = HR24;
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.add(Calendar.MILLISECOND, (int)timeOfDayToRun);
		if(cal.compareTo(Calendar.getInstance()) < 0)
			cal.add(Calendar.DAY_OF_YEAR, 1);
		start(cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
		performTask();	//run right away, one time
	}

	protected void performTask() {
		try {
			sendOverdueEmail();
			sendNearlyDueEmail();
			String logMsg = "OverdueManager Running.";
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
	 * Manages sending emails to users with overdue resources	
	 * @throws SQLException If couldn't query the database for overdue resources
	 */
	private void sendOverdueEmail() throws SQLException{
		ArrayList<Loan> overdueLoansToEmail = getOverdueLoansToEmail();	//get the overdue resources that need to have emails sent

		for(Loan loan: overdueLoansToEmail){	//for each loan to send email to
			String emailSubject = "Your Loan of \"" + loan.resourceTitle + "\" is Overdue";
			String emailAddress = loan.email;
			String emailMessage = createOverdueMessage(loan);
			try{
				EmailManager.getReference().sendEmail(emailAddress, emailSubject, emailMessage);
			}
			catch(MessagingException e){
				//bad email address, possibly add flag to the user
			}

			//update the last email date in the database
			if(DBInterface.getReference() == null)
				throw new SQLException("Database currently unavailable.");
			DBInterface.getReference().executeUpdate("UPDATE loan SET last_email_date = UNIX_TIMESTAMP(NOW())*1000 WHERE loan_ID = " + loan.loanID);
		}
	}
	
	/**
	 * Manages sending emails to user with resources that are due soon
	 * @throws SQLException If couldn't query the database for resources that are almost due
	 */
	private void sendNearlyDueEmail() throws SQLException{
		ArrayList<Loan> nearlyDueLoansToEmail = getNearlyDueLoansToEmail();
		try{
			for(Loan loan: nearlyDueLoansToEmail){
				String emailSubject = "Your Loan of \"" + loan.resourceTitle + "\" is Due Soon";
				String emailAddress = loan.email;
				String emailMessage = createNearlyDueMessage(loan);
				EmailManager.getReference().sendEmail(emailAddress, emailSubject, emailMessage);
				if(DBInterface.getReference() == null)
					throw new SQLException("Database currently unavailable.");
				DBInterface.getReference().executeUpdate("UPDATE loan SET last_email_date = UNIX_TIMESTAMP(NOW())*1000 WHERE loan_ID = " + loan.loanID);
			}
		} catch(MessagingException e){
			
		}
	}

	/**
	 * Creates the body message to send to a user with an overdue resource
	 * @param loan The loan that is overdue
	 * @return message The message to send to a user with an overdue resource
	 */
	private String createOverdueMessage(Loan loan){
		String heading = "Dear, " + loan.firstName + " " + loan.lastName + "\n\n";
		
		Calendar dueDate = Calendar.getInstance();
		dueDate.setTimeInMillis(loan.dueDate);
		Calendar checkOutDate = Calendar.getInstance();
		checkOutDate.setTimeInMillis(loan.checkOutDate);
		
		String bodyP1 = "Your loan of \"" + loan.resourceTitle +
				"\" that was checked out on " +
				checkOutDate.getDisplayName(Calendar.DAY_OF_WEEK,  Calendar.LONG, Locale.CANADA) + " " +
				checkOutDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " " +
				checkOutDate.get(Calendar.DAY_OF_MONTH) + ", " +
				checkOutDate.get(Calendar.YEAR) +
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
		String heading = "Dear, " + loan.firstName + " " + loan.lastName + "\n\n";
		
		Calendar dueDate = Calendar.getInstance();
		dueDate.setTimeInMillis(loan.dueDate);
		Calendar checkOutDate = Calendar.getInstance();
		checkOutDate.setTimeInMillis(loan.checkOutDate);
		
		String bodyP1 = "Your loan of \"" + loan.resourceTitle +
				"\" that was checked out on " +
				checkOutDate.getDisplayName(Calendar.DAY_OF_WEEK,  Calendar.LONG, Locale.CANADA) + " " +
				checkOutDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " " +
				checkOutDate.get(Calendar.DAY_OF_MONTH) + ", " +
				checkOutDate.get(Calendar.YEAR) +
				" is due on " +
				dueDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CANADA) + " " +
				dueDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " " +
				dueDate.get(Calendar.DAY_OF_MONTH) + ", " +
				dueDate.get(Calendar.YEAR) + ".\n";
		
		String bodyP2 = "Please return it by the due date to avoid being fined.\n";
		
		String footer = "\nThank you,\n" + "The UBC CICSR Library";
		
		return heading + bodyP1 + bodyP2 + footer;
	}
	
	
	/**
	 * Queries the database for a list of overdue loans that need to be emailed
	 * @return overdueLoansToEmail The list of loans that need to be emailed
	 * @throws SQLException If the database couldn't be queried.
	 */
	private ArrayList<Loan> getOverdueLoansToEmail() throws SQLException{
		ArrayList<Loan> overdueLoansToEmail = new ArrayList<Loan>();
		if(DBInterface.getReference() == null)
			throw new SQLException("Database currently unavailable.");
		CachedRowSetImpl result = DBInterface.getReference().executeSelect(sql_Select_Overdue_To_Email); //execute query to get overdue loans
		while(result.next()){	//insert each overdue loan into the array list
				overdueLoansToEmail.add(new Loan(
					result.getInt("loan_ID"),
					result.getLong("check_out_date"),
					result.getLong("due_date"),
					result.getLong("last_email_date"),
					result.getBigDecimal("fine"),
					result.getInt("user_ID"),
					result.getString("first_name"),
					result.getString("last_name"),
					result.getString("email"),
					result.getString("title")));
		}

		return overdueLoansToEmail;
	}
	
	/**
	 * Queries the database for a list of loans that are alsmost due and need to be emailed
	 * @return nearlyDueLoansToEmail The list of loans that need to be emailed
	 * @throws SQLException If the database couldn't be queried.
	 */
	private ArrayList<Loan> getNearlyDueLoansToEmail() throws SQLException{
		ArrayList<Loan> nearlyDueLoansToEmail = new ArrayList<Loan>();
		if(DBInterface.getReference() == null)
			throw new SQLException("Database currently unavailable.");
		CachedRowSetImpl result = DBInterface.getReference().executeSelect(sql_Select_Almost_Due_To_Email);
		
		while(result.next()){
			nearlyDueLoansToEmail.add(new Loan(
					result.getInt("loan_ID"),
					result.getLong("check_out_date"),
					result.getLong("due_date"),
					result.getLong("last_email_date"),
					result.getBigDecimal("fine"),
					result.getInt("user_ID"),
					result.getString("first_name"),
					result.getString("last_name"),
					result.getString("email"),
					result.getString("title")));
		}
		return nearlyDueLoansToEmail;
	}
	
	/**
	 * Nested class to store necessary information about overdue and almost due loans
	 */
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
		
	}
	
	//SQL query to get a list of all overdue resources that need to be emailed
	private void setSQLSelectOverdueToEmail(long timeUntilStopEmails, long periodBetweenEmails){
		sql_Select_Overdue_To_Email = "SELECT " +
			"loan.loan_ID, loan.check_out_date, loan.due_date, loan.last_email_date, loan.fine, user.user_ID, user.first_name, user.last_name, user.email, resource.title " +
			"FROM " +
			"loan,user,resourceCopy,resource " +
			"WHERE " +
			"loan.user_user_ID = user.user_ID AND " +
			"loan.resourceCopy_resourceCopy_ID = resourceCopy.resourceCopy_ID AND " +
			"resourceCopy.resource_resource_ID = resource.resource_ID AND " +
			"loan.check_in_date IS NULL AND " +															//hasn't been checked in
			"loan.due_date < UNIX_TIMESTAMP(NOW())*1000 AND " +											//was due
			"loan.due_date > (UNIX_TIMESTAMP(NOW())*1000 - " + timeUntilStopEmails + ") AND " +		//wasn't due too long ago
			"(loan.last_email_date IS NULL OR " +														//was never emailed OR
			"loan.last_email_date < (UNIX_TIMESTAMP(NOW())*1000 - " + periodBetweenEmails + "))";		//last email was long enough ago to send another one
	}
	
	//SQL query to get a list of all resources that are almost due to be emailed
		
	private void setSQLSelectAlmostDueToEmail(long timeBeforeDueToEmail){
		sql_Select_Almost_Due_To_Email = "SELECT " +
			"loan.loan_ID, loan.check_out_date, loan.due_date, loan.last_email_date, loan.fine, user.user_ID, user.first_name, user.last_name, user.email, resource.title " +
			"FROM " +
			"loan, user, resourceCopy, resource " +
			"WHERE " +
			"loan.user_user_ID = user.user_ID AND " +
			"loan.resourceCopy_resourceCopy_ID = resourceCopy.resourceCopy_ID AND " +
			"resourceCopy.resource_resource_ID = resource.resource_ID AND " +
			"loan.check_in_date IS NULL AND " +													//hasn't been checked in
			"loan.last_email_date IS NULL AND " +												//hasn't been emailed
			"loan.due_date > UNIX_TIMESTAMP(NOW())*1000 AND " +									//isn't due yet
			"loan.due_date < (UNIX_TIMESTAMP(NOW())*1000 + " + timeBeforeDueToEmail + ")";		//is due in less the # days to be considered due soon
	}
		
}
