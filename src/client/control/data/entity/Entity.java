package client.control.data.entity;

/**
 * Represents an entity object. This class contains one field, "readOnly", and a getter/setter. This attribute is used to ensure mutual exclusion in record editing.
 * 
 * @author Jeremy
 * @version 2
 */
public abstract class Entity{
	boolean readOnly;
	int idNumber;
	
	/**
	 * Constructs a new read-only Entity with an ID number of 0
	 */
	public Entity(){
		readOnly = true;
		idNumber = 0;
	}
	
	/**
	 * Returns true if the entity is read-only, false otherwise
	 * 
	 * @return true of the entity is read-only, false otherwise
	 */
	public boolean getReadOnly(){
		return readOnly;
	}
	/**
	 * Sets the entity to read-only or readable/writable based on a boolean parameter
	 * 
	 * @param True for read-only, false for readable/writable
	 */
	public void setReadOnly(boolean newReadOnly){
		readOnly = newReadOnly;
	}
	
	/**
	 * Returns the Entity's ID number
	 * 
	 * @return The Entity's ID number
	 */
	public int getID(){
		return idNumber;
	}
	
	/**
	 * Sets the Entity's ID number
	 * 
	 * @param The new ID number
	 */
	public void setID(int newID){
		idNumber = newID;
	}
}