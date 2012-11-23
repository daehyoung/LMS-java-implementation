package client.control.data.entity.resource;
import client.control.data.entity.Entity;

/**
 * Represents a copy of a resource
 * 
 * @author Jeremy Lerner
 * @version 5
 */
public class ResourceCopy extends Entity{
	int barcode;
	int resource;
	int ownerID;
	boolean enabled;
	
	/**
	 * Constructs a new disabled ResourceCopy with all other fields set to 0
	 */
	public ResourceCopy(){
		super();
		barcode = 0;
		resource = 0;
		ownerID = 0;
		enabled = false;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param The ResourceCopy to copy
	 */
	public ResourceCopy(ResourceCopy original){
		super(original);
		barcode = original.getBarcode();
		resource = original.getResource();
		ownerID = original.getOwnerID();
		enabled = original.getEnabled();
	}
	
	/**
	 * Constructs a new ResourceCopy with all fields specified
	 * 
	 * @param value of ID
	 * @param value of Barcode
	 * @param value of Resource
	 * @param value of Owner
	 * @param value of Enabled
	 */
	public ResourceCopy(int newID, int newBarcode, int newResource, int newOwner, boolean newEnabled){
		super(newID);
		barcode = newBarcode;
		resource = newResource;
		ownerID = newOwner;
		enabled = newEnabled;
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
	 * Returns the ResourceCopy's owner's user ID
	 * 
	 * @return The ResourceCopy's owner's user ID
	 */
	public int getOwnerID(){
		return ownerID;
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
	 * Returns the ResourceCopy's barcode
	 * 
	 * @return The ResourceCopy's barcode
	 */
	public int getBarcode(){
		return barcode;
	}
	
	public boolean checkValid(){
		return true;
	}
}