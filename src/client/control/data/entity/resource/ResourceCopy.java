package client.control.data.entity.resource;
import client.control.data.entity.Entity;

/**
 * Represents a copy of a resource
 * 
 * @author Jeremy Lerner
 * @version 3
 */
public class ResourceCopy extends Entity{
	int resource;
	int ownerID;
	boolean enabled;
	
	/**
	 * Constructs a new disabled ResourceCopy with all other fields set to 0
	 */
	public ResourceCopy(){
		super();
		resource = 0;
		ownerID = 0;
		enabled = false;
	}
	
	/**
	 * Returns the ID of the resource that the calling ResourceCopy is a copy of
	 * 
	 * @return The Resource that the calling ResourceCopy is a copy of
	 */
	public int getResource(){
		return resource;
	}
	/**
	 * Sets the resource that the calling ResourceCopy is a copy of
	 * 
	 * @param The ID of the Resource that the ResourceCopy should be a copy of
	 */
	public void setResource(int newResource){
		resource = newResource;
	}

	/**
	 * Returns the ResourceCopy's owner's user ID
	 * 
	 * @return The ResourceCopy's owner's user ID
	 */
	public int getOwnerID(){
		return ownerID;
	}
	/**
	 * Sets the ResourceCopy's owner
	 * 
	 * @param The new owner's user ID
	 */
	public void setOwnerID(int newOwnerID){
		ownerID = newOwnerID;
	}

	/**
	 * Returns true if the ResourceCopy is enabled, false otherwise
	 * 
	 * @return True if the ResourceCopy is enabled, false otherwise
	 */
	public boolean getEnabled(){
		return enabled;
	}
	/**
	 * Enables or disables the ResourceCopy based on a boolean parameter
	 * 
	 * @param True for enabling, False for disabling
	 */
	public void setEnabled(boolean newEnabled){
		enabled = newEnabled;
	}
}