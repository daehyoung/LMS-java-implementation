package server.control.datamanagement;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import com.sun.rowset.CachedRowSetImpl;

import server.dbinterface.DBInterface;
import server.dbinterface.DBInterfaceXX;

/**
 * This is the Fine Manager that will increment the fines of overdue resources.
 * It will check for fines that should be incremented and set the fine to its appropriate amount 
 * It will calculate the fine amount that the fine should be and if it is currently less than this it will set it to the new calculated amount.
 * If the fine incurred will be greater than the maximum allowed it will set it to the maximum allowed fine.
 * 
 * @author Peter Abelseth
 * @version 1
 */
public class FineManager extends ScheduledTask {
	
	private DBInterfaceXX dbInterface;	//interface to the database
		
	private final static BigDecimal MAX_FINE = new BigDecimal(5.00);	//the maximum fine a single loan can incur
	private final static long INCREMENT_FINE_24HR = 24*60*60*1000L;	//The default time between when a fine is incremented
	private final static long RUN_FINEMGR_24HR = 24*60*60*1000L;	//Run the fine manager once every 24 hours
	
	private long fineIncrementPeriod;		//the time between a fine being incremented
	
	private ArrayList<ResourceType> resourceTypes = new ArrayList<ResourceType>();	//store the list of the resourceType fine amounts
	
	/**
	 * Default Constructor of the Fine Manager and starts the Fine Manager
	 * This will automatically update the fines of all overdue loan at 12:01am(00:01) every morning.  
	 */
	public FineManager(){
		super(RUN_FINEMGR_24HR);	//set the default interval time
		fineIncrementPeriod = INCREMENT_FINE_24HR;
			
		//get the amount of time to next morning at 12:01 (00:01)
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 1);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) +1 );
		
		start(cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()); //start the FineManager next morning
		
	}
	
	/**
	 * Constructs a Fine Manager and starts the process
	 * @param intervalToRun The interval between the fine manager checking for loans that have fines to be incremented in milliseconds
	 * @param timeToStart The time until the first time the FineManager will be run, in milliseconds
	 * @param fineIncrementPeriod The time between when another fine should be applied to the loan
	 */
	public FineManager(long intervalToRun, long timeToStart, long fineIncrementPeriod) {
		super(intervalToRun);
		this.fineIncrementPeriod = fineIncrementPeriod;
		start(timeToStart);
	}
	
	/**
	 * The scheduled task that will automatically be called by the TaskScheduler
	 */
	protected void performTask(){
		//System.out.println(Calendar.getInstance().getTime());
		try {
			updateFines();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets all overdue loans and increments their fines accordingly
	 * @throws SQLException If couldn't connect to database or invalid query was sent
	 */
	private void updateFines() throws SQLException{
		this.dbInterface = DBInterfaceXX.getReference();
		if(dbInterface == null)
			throw new SQLException("DBInterface not initialized");
		
		ArrayList<Loan> overdueLoans = getOverdueLoans();
		
		for (Loan loan: overdueLoans){	//for each overdue loan
			BigDecimal fineAmount = getFineAmount(loan.typeID);
			long numFinePeriods = (Calendar.getInstance().getTimeInMillis() - loan.dueDate) / fineIncrementPeriod + 1;
			long numFinesApplied = (loan.fine.divide(fineAmount)).longValue();
	
			if(numFinePeriods > numFinesApplied){	//if fine should be added
				BigDecimal numFinesToApply = new BigDecimal(numFinePeriods - numFinesApplied);
				updateFine(loan,fineAmount.multiply(numFinesToApply));
			}
		}
	}
	
	/**
	 * Updates the given loan by adding the fineAmount up to a maximum fine
	 * @param loan The loan to apply the fine to
	 * @param fineAmount The amount of fine to add to the loan
	 * @throws SQLException If the update to the database was unsuccessful
	 */
	private void updateFine(Loan loan, BigDecimal fineAmount) throws SQLException{
		if(loan.fine.compareTo(MAX_FINE) < 0){	//if the loan hasn't reached the maximum fine
			if(loan.fine.compareTo(MAX_FINE.subtract(fineAmount)) > 0)	//if the fine will go past the maximum
				loan.fine = MAX_FINE;	//set the loan's fine to the maximum allowed
			else
				loan.fine = loan.fine.add(fineAmount);  //add the fine to the loan
			dbInterface.executeUpdate("UPDATE loan SET fine = " + loan.fine.doubleValue() + " WHERE loan_ID = " + loan.loanID);	//update the loan in the database with the new fine amount
		}
	}
	
	/**
	 * Gets the fine amount for the type of resource given
	 * @param typeID The resourceType to get fine amount for
	 * @return fineAmount The fine amount applied to that type of resource
	 * @throws SQLException If couldn't get the resource types
	 */
	private BigDecimal getFineAmount(int typeID) throws SQLException{
		for(ResourceType rType: resourceTypes){	//find the corresponding resource type
			if(rType.typeID == typeID)
				return rType.fineAmount;
		}
		resourceTypes = getResourceTypes();	//if the resource type couldn't be found, query all resourceTypes and search again
		for(ResourceType rType: resourceTypes){
			if(rType.typeID == typeID)
				return rType.fineAmount;
		}
		throw new SQLException("Resource Type could not be found");  //if resourceType still couldn't be found, then we're screwed! Database is messed up
	}
	
	
	/**
	 * Queries the database for all overdue loans
	 * @return overdueLoans All loans that are overdue and haven't had their fines paid for
	 * @throws SQLException If couldn't get result from the database
	 */
	private ArrayList<Loan> getOverdueLoans() throws SQLException{
		CachedRowSetImpl result = dbInterface.executeQuery(SQL_GET_OVERDUE_LOANS); //execute query to get overdue loans
		ArrayList<Loan> overdueLoans = new ArrayList<Loan>();
		while(result.next()){	//insert each overdue loan into the array list
				overdueLoans.add(new Loan(
					result.getInt(1),
					result.getLong(2),
					result.getBigDecimal(3),
					result.getInt(4)));
		}
		return overdueLoans;	//return the arraylist
	}
	
	
	/**
	 * Queries the database for the fine amounts for each type of resource
	 * @return resourceTypes All the types of resources and their fine amounts
	 * @throws SQLException If couldn't get result from the database
	 */
	private ArrayList<ResourceType> getResourceTypes() throws SQLException{		
		CachedRowSetImpl result = dbInterface.executeQuery(SQL_GET_RESOURCE_TYPES);	//execute query to get all resourceTypes
		
		ArrayList<ResourceType> resourceTypes = new ArrayList<ResourceType>();
		while(result.next()){	//put each resourceType into an arrayList
			resourceTypes.add(new ResourceType(
					result.getInt(1),
					result.getBigDecimal(2)));
		}
		return resourceTypes; //return the arraylist
	}
	
	
	/**
	 * Nested class to store the overdue loans and all necessary attributes
	 */
	private class Loan{
		public int loanID;
		public long dueDate;
		public BigDecimal fine;
		public int typeID;
		public Loan(int loanID, long dueDate, BigDecimal fine, int typeID){
			this.loanID = loanID;
			this.dueDate = dueDate;
			this.fine = fine;
			this.typeID = typeID;
		}
	};
	
	/**
	 * Nested class to store all resourceTypes and their fine amounts
	 */
	private class ResourceType{
		public int typeID;
		public BigDecimal fineAmount;
		public ResourceType(int typeID, BigDecimal fineAmount){
			this.typeID = typeID;
			this.fineAmount = fineAmount;
		}
	};
	
	
	//SQL Statement to get all the resource types
	private static final String SQL_GET_RESOURCE_TYPES = "SELECT " +
			"type_ID, fine_amount " +
			"FROM resourceType " +
			"WHERE enabled = TRUE " +
			"ORDER BY type_ID";
	
	//SQL Statement to get all overdue loans
	private static final String SQL_GET_OVERDUE_LOANS = "SELECT " +
			"loan.loan_ID, loan.due_date, loan.fine, resource.resourceType_type_ID " +
			"FROM " +
			"loan, user, resourceCopy, resource " +
			"WHERE " +
			"user.enabled = TRUE AND " +
			"user.user_ID = loan.user_user_ID AND " +
			"loan.resourceCopy_barcode = resourceCopy.barcode AND " +
			"resourceCopy.resource_resource_ID = resource.resource_ID AND " +
			"loan.check_in_date IS NULL AND " +
			"loan.fine_paid = FALSE AND " +
			"loan.due_date IS NOT NULL AND " +
			"loan.due_date < (UNIX_TIMESTAMP(NOW())*1000) ";	
	
	
// Used for testing the FineManager

	public static void main(String args[]){
		DBInterface.createDBInterface(
				"jdbc:mysql://192.168.0.65:3306/",
				"librisDB",
				"root",
				"243816");
		FineManager fineMgr = new FineManager(1000,1000,5000);
	}

}
