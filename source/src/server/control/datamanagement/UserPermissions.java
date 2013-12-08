package server.control.datamanagement;

import global.LibrisGlobalInterface;

/**
 * @author Peter Abelseth
 * @version 1
 * 
 * Notes: This is hardcoded, should be coded so that the index of the array matches number of the user type automatically
 */
public interface UserPermissions extends LibrisGlobalInterface {

	public static int USER_BASIC[] = {
		Integer.parseInt("00000000",2),						//guest
		Integer.parseInt("00000011",2),						//student
		Integer.parseInt("00000011",2),						//faculty
		Integer.parseInt("11110011",2),						//staff
		Integer.parseInt("11110011",2),						//librarian
		Integer.parseInt("11111111",2)						//admin
	};
	
	public static final int USER_EXTENDED[] = {
		Integer.parseInt("00000000",2),
		Integer.parseInt("00000001",2),
		Integer.parseInt("00000001",2),
		Integer.parseInt("11010001",2),
		Integer.parseInt("11010001",2),
		Integer.parseInt("11111111",2)
	};
	
	public static final int RESOURCE[] = {
		Integer.parseInt("00010000",2),
		Integer.parseInt("00010000",2),
		Integer.parseInt("00010000",2),
		Integer.parseInt("11110000",2),
		Integer.parseInt("11110000",2),
		Integer.parseInt("11111111",2)
	};

	public static final int RESOURCECOPY[] = {
		Integer.parseInt("00010000",2),
		Integer.parseInt("00010000",2),
		Integer.parseInt("00010000",2),
		Integer.parseInt("11110000",2),
		Integer.parseInt("11110000",2),
		Integer.parseInt("11111111",2)
	};
	
	public static final int SUBSCRIPTION[] = {
		Integer.parseInt("00000000",2),
		Integer.parseInt("00000000",2),
		Integer.parseInt("00000000",2),
		Integer.parseInt("11111111",2),
		Integer.parseInt("11111111",2),
		Integer.parseInt("11111111",2)
	};

	public static final int TODO[] = {
		Integer.parseInt("00000000",2),
		Integer.parseInt("00000000",2),
		Integer.parseInt("00000000",2),
		Integer.parseInt("11111111",2),
		Integer.parseInt("11111111",2),
		Integer.parseInt("11111111",2)
	};

	public static final int LOAN[] = {
		Integer.parseInt("00000000",2),
		Integer.parseInt("00000001",2),
		Integer.parseInt("00000001",2),
		Integer.parseInt("11111111",2),
		Integer.parseInt("11111111",2),
		Integer.parseInt("11111111",2)
	};

	public static final int REFERENCE[] = {
		Integer.parseInt("00000001",2),
		Integer.parseInt("00000001",2),
		Integer.parseInt("00000001",2),
		Integer.parseInt("11111111",2),
		Integer.parseInt("11111111",2),
		Integer.parseInt("11111111",2)
	};

	public static final int RESERVATION[] = {
		Integer.parseInt("00001101",2),
		Integer.parseInt("00001101",2),
		Integer.parseInt("11011101",2),
		Integer.parseInt("11011101",2),
		Integer.parseInt("11011101",2)
	};

/* depreceated
	public static final int CREATOR[] = {
		Integer.parseInt("0b00010000",2),
		Integer.parseInt("0b00010000",2),
		Integer.parseInt("0b00010000",2),
		Integer.parseInt("0b11110000",2),
		Integer.parseInt("0b11110000",2),
		Integer.parseInt("0b11111111",2)
	};
*/
}

/*
ReadSelf (bit 0): Permission to read the specified field in the specified table for an entry that is linked to the user currently logged in.
WriteSelf (bit 1): Permission to change the specified field in the specified table for an entry that is linked to the user currently logged in.
AddSelf (bit 2): Permission to add a new entry to the specified table that links to the user currently logged in.
RemoveSelf (bit 3): Permission to remove the specified entry from the specified table that links to the user currently logged in.
ReadOther (bit 4): Permission to read the specified field in the specified table for an entry that is not linked to the user currently logged in.  This may be linked to another user of equal or lower user class.
WriteOther (bit 5): Permission to change the specified field in the specified table for an entry that is not linked to the user currently logged in.  This may be linked to another user of equal or lower user class.
AddOther (bit 6): Permission to add a new entry to the specified table that links to a user other than the user currently logged in.  This may be linked to another user of equal or lower user class.
RemoveOther (bit 7): Permission to remove the specified entry from the specified table that links to a user other than the user currently logged in.  This may be linked to another user of equal or lower user class.
*/