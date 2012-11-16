package client.control.data.entity.resource;

/**
 * Represents a resource type with a name and labels for the title, creator, company, and serial number. For example, a Resource Type May have the title "Book", and the title, creator, company, and serial number labeled as "Title", "Author", "Publisher", and "ISBN Number" respectively.
 * 
 * @author Jeremy Lerner
 * @version 0
 *
 */
public class ResourceType{
	String typeName; //I declare these all in different statements to increase readability
	String titleLabel;
	String creatorLabel;
	String companyLabel;
	String serialNumberLabel;
	boolean enabled;
	
	/**
	 * Constructs a new ResourceType with all fields set to null
	 */
	public ResourceType(){
		typeName = null;
		titleLabel = null;
		creatorLabel = null;
		companyLabel = null;
		serialNumberLabel = null;
		enabled = false;
	}

	/**
	 * Returns the name
	 * 
	 * @return The name
	 */
	public String getName(){
		return typeName;
	}
	/**
	 * Sets the name
	 * 
	 * @param The new name
	 */
	public void setName(String newName){
		typeName = newName;
	}

	/**
	 * Returns the title label
	 * 
	 * @return The title label
	 */
	public String getTitleLabel(){
		return titleLabel;
	}
	/**
	 * Sets the title label
	 * 
	 * @param The new title label
	 */
	public void setTitleLabel(String newTitleLabel){
		titleLabel = newTitleLabel;
	}

	/**
	 * Returns the creator label
	 * 
	 * @return The creator label
	 */
	public String getCreatorLabel(){
		return creatorLabel;
	}	
	/**
	 * Sets the creator label
	 * 
	 * @param The new creator label
	 */
	public void setCreatorLabel(String newCreatorLabel){
		creatorLabel = newCreatorLabel;
	}

	/**
	 * Returns the company label
	 * 
	 * @return The creator label
	 */
	public String getCompanyLabel(){
		return companyLabel;
	}
	/**
	 * Sets the company label
	 * 
	 * @param The new company label
	 */
	public void setCompanyLabel(String newCompanyLabel){
		companyLabel = newCompanyLabel;
	}

	/**
	 * Returns the serial number label
	 * 
	 * @return The serial number label
	 */
	public String getSerialNumberLabel(){
		return serialNumberLabel;
	}
	/**
	 * Sets the serial number label
	 * 
	 * @param The new serial number label
	 */
	public void setSerialNumberLabel(String newSerialNumberLabel){
		serialNumberLabel = newSerialNumberLabel;
	}

	
}