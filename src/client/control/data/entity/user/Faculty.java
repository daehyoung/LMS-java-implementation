package client.control.data.entity.user;
import java.util.ArrayList;

/**
 * This class represents a User of the Faculty type.
 * 
 * @author Jeremy Lerner
 * @version 1
 */
public class Faculty extends Patron{
	ArrayList<Integer> references;
	
	/**
	 * Constructs a new Faculty with no references, loans, reserves, and all other fields set to null
	 */
	public Faculty(){
		super();
		references = new ArrayList<Integer>(); //It didn't work with the int type
	}
	
	/**
	 * Returns the reference ID of one of the Faculty's references. References are indexed by the Faculty object as an array of reference ID numbers. This function returns an ID number, when given an array index.
	 * 
	 * @param The array index of the reference to get
	 * @return The reference ID of the reference with the given index
	 * @throws Thrown when the parameter is out-of-bounds (negative or greater than or equal to the number of references held by the Faculty)
	 */
	public int getReference(int referenceIndex)throws IndexOutOfBoundsException{
		return references.get(referenceIndex).intValue();
	}
	/**
	 * Adds a new reference to the Faculty
	 * 
	 * @param The reference ID number of the reference to add
	 * @return The Faculty's array index of the newly added reference
	 */
	public int addReference(int newReferenceID){
		Integer newReferenceWrapper = new Integer(newReferenceID);
		references.add(newReferenceWrapper);
		return references.indexOf(newReferenceWrapper);
	}
	/**
	 * Removes a reference from the Faculty
	 * 
	 * @param The Reference ID of the reference to remove
	 * @return True if the Reference ID was present, false otherwise
	 */
	public boolean removeReference(int reserveID){
		return references.remove(new Integer(reserveID)); //This does work because ArrayList.remove(Object) invokes the Object.equals() method, which Integer implements by comparing the values, not addresses.
	}
}