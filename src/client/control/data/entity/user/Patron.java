package client.control.data.entity.user;

/**
 * This class represents a User of the Patron type, and is intended to be extended by the Faculty and Student classes.
 * 
 * @author Jeremy Lerner
 * @version 1
 */
public class Patron extends User{
	/**
	 * Constructs a new Patron with no reserves, loans, and all other fields set to null
	 */
	public Patron(){
		super();
	}
}