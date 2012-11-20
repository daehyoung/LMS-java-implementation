package client.control.data.entity.user;
import java.util.ArrayList;

import client.control.data.entity.Entity;

/**
 * This class represents a user, and is intended to be extended by the StaffMember and Patron classes.
 * 
 * @author Jeremy Lerner
 * @version 5
 *
 */
public abstract class User extends Entity{
	
	String firstName;
	String lastName;
	String password;
	String emailAddress;
	ArrayList<Integer> reserves; //It wouldn't work with the int type
	ArrayList<Integer> loans;
	boolean enabled;
	
	/**
	 * Constructs a new User with no loans, reserves, and all other fields set to null
	 */
	public User(){
		super();
		firstName = null;
		lastName = null;
		password = null;
		emailAddress = null;
		reserves = new ArrayList<Integer>();
		loans = new ArrayList<Integer>();
		enabled = false;
	}
	
	/**
	 * Returns the user's first name
	 * 
	 * @return The first name
	 */
	public String getFirstName(){
		return firstName;
	}
	/**
	 * Sets the user's first name
	 * 
	 * @param The new first name
	 */
	public void setFirstName(String newFirstName){
		firstName = newFirstName;
	}

	/**
	 * Returns the user's last name
	 * 
	 * @return The last name
	 */
	public String getLastName(){
		return lastName;
	}
	/**
	 * Sets the user's last name
	 * 
	 * @param The new last name
	 */
	public void setLastName(String newLastName){
		lastName = newLastName;
	}

	/**
	 * Returns the user's E-mail address
	 * 
	 * @return The E-mail address
	 */
	public String getEmailAddress(){
		return emailAddress;
	}
	/**
	 * Sets the user's E-mail address
	 * 
	 * @param The new E-mail address
	 */
	public void setEmailAddress(String newEmailAddress){
		emailAddress = newEmailAddress;
	}

	/**
	 * Returns the user's password
	 * 
	 * @return The password
	 */
	public String getPassword(){
		return password;
	}
	/**
	 * Sets the user's password
	 * 
	 * @param The new password
	 */
	public void setPassword(String newPassword){
		password = newPassword;
	}

	/**
	 * Returns the reserve ID of one of the user's reserves. Reserves are indexed by the User object as an array of reserve ID numbers. This function returns an ID number, when given an array index.
	 * 
	 * @param The array index of the reserve to get
	 * @return The reserve ID of the reserve with the given index
	 * @throws Thrown when the parameter is out-of-bounds (negative or greater than or equal to the number of reserves held by the user)
	 */
	public int getReserve(int reserveIndex)throws IndexOutOfBoundsException{
		return reserves.get(reserveIndex).intValue();
	}
	/**
	 * Adds a new reserve to the user
	 * 
	 * @param The reserve ID number of the reserve to add
	 * @return The user's array index of the newly added reserve
	 */
	public int addReserve(int newReserveID){
		Integer newReserveWrapper = new Integer(newReserveID);
		reserves.add(newReserveWrapper);
		return reserves.indexOf(newReserveWrapper);
	}
	/**
	 * Removes a reserve from the user
	 * 
	 * @param The Reserve ID of the reserve to remove
	 * @return True if the Reserve ID was present, false otherwise
	 */
	public boolean removeReserve(int reserveID){
		return reserves.remove(new Integer(reserveID)); //This does work because ArrayList.remove(Object) invokes the Object.equals() method, which Integer implements by comparing the values, not addresses.
	}
	
	/**
	 * Returns the loan ID of one of the user's loans. Loans are indexed by the User object as an array of loan ID numbers. This function returns an ID number, when given an array index.
	 * 
	 * @param The array index of the loan to get
	 * @return The loan ID of the loan with the given index
	 * @throws Thrown when the parameter is out-of-bounds (negative or greater than or equal to the number of loans held by the user)
	 */
	public int getLoan(int loanIndex) throws IndexOutOfBoundsException{
		return reserves.get(loanIndex).intValue();
	}
	/**
	 * Adds a new loan to the user
	 * 
	 * @param The loan ID number of the loan to add
	 * @return The user's array index of the newly added loan
	 */
	public int addLoan(int newLoanID){
		Integer newLoanWrapper = new Integer(newLoanID);
		loans.add(newLoanWrapper);
		return reserves.indexOf(newLoanWrapper);
	}
	/**
	 * Removes a loan from the user
	 * 
	 * @param The Loan ID of the loan to remove
	 * @return True if the Loan ID was present, false otherwise
	 */
	public boolean removeLoan(int loanID){
		return loans.remove(new Integer(loanID)); //This does work because ArrayList.remove(Object) invokes the Object.equals() method, which Integer implements by comparing the values, not addresses.
	}
	
	/**
	 * Returns true if the User is enabled, false otherwise
	 * 
	 * @return True of the user is enabled, false otherwise
	 */
	public boolean isEnabled(){
		return enabled;
	}

	/**
	 * Enables or disables the User based on the value of the parameter
	 * @param True to enable the User, False to disable it
	 */
	public void setEnabled(boolean newEnabled){
		enabled = newEnabled;
	}
}