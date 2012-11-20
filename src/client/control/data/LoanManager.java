package client.control.data;

import javax.sql.RowSet;
import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.Loan;

/**
 * Control Object for Loans
 * 
 * @author Jeremy Lerner
 * @version 0
 *
 */
public class LoanManager{
	
	/**
	 * Returns an array of Loan objects with fields matching the initialized fields of a parameter
	 * 
	 * @param A Loan object with some fields initialized
	 * @return An array of Loan objects from the Server with fields matching those initialized in the parameter
	 */
	public static Loan[] getLoan(Loan searchKey){
		//Write query
		//Query Server
		//Construct array of Loans
		System.out.println("LoanManager.getLoan(Loan) is a stub");
		return null;
	}
	
	/**
	 * Returns a Loan object with a given ID number
	 * 
	 * @param A Loan ID number
	 * @return A Loan object with the given ID number
	 */
	public static Loan getLoan(int idNumber){
		//Write Query
		//Query Server
		//Construct Loan object
		System.out.println("LoanManager.getLoan(int) is a stub");
		return null;
	}
	
	/**
	 * Edits a Loan entry in the database
	 * 
	 * @param A Loan object with updated fields. The ID number field must be set in the Loan.
	 * @return True if successful, false otherwise
	 */
	public static boolean setLoan(Loan newEntry){
		//Check that the Loan Exists
		//Check that the Resource exists
		//Write Query
		//Query Server
		System.out.println("LoanManager.setLoan(Loan) is a stub");
		return false;
	}
	
	/**
	 * Inserts a new Loan object in the database
	 * 
	 * @param The Loan object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry's ID number
	 */
	public static int addLoan(Loan newEntry){
		//Check that the Resource and User exist
		//Check that the resource is not on Loan or on Reference
		//If there are reserves for the Resource:
		    //Check that the User Has a Reserve and that the number of Reserves for that Resource with an earlier StartDate that is within the pickup period, is less than or equal to the number of copies of that resource that are not on reference or on loan
		//Write query
		//Query Server
		//Return new ID number
		System.out.println("LoanManager.addLoan(Loan) is a stub");
		return 0; //Stub
	}
	
	/**
	 * Removes a Loan object from the database
	 * 
	 * @param The ID number of the Loan to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeLoan(int idNumber){
		//Write Query
		//Query Server
		System.out.println("LoanManager.removeLoan(Loan) is a stub");
		return false;
	}
}