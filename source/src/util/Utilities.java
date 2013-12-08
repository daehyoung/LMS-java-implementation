package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities use throughout the client
 * 
 * @author Daniel
 * @version 2
 */

public class Utilities {

	/**
	 * Formats the given date as a String.
	 * @return the formatted date string
	 */
	public static String formatDate(Date date, String format, String timeZone) {
		if(date == null)
			return new String();
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern(format);
		formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
		return formatter.format(date);
	}

	/**
	 * Parses the date String.
	 * @throws ParseException if the date string is invalid
	 * @return the parsed date
	 */
	public static Date parseDate(String date, String format, String timeZone)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern(format);
		formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
		return formatter.parse(date);
	}
	
	/**
	 * Converts the given date into a date at time 00:00.
	 * For example, it will convert "Septemper 1 at 13:24" to
	 * "September 1 at 00:00".
	 * @return the new date, or null if an error occurs
	 */
	public static Date removeTime(Date date) {
		
		// sanity check
		if (date == null)
			return null;
		
		// create new date
		Date newDate = null;
		try {
		newDate = Utilities.parseDate(Utilities.formatDate(new Date(),
				Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE),
				Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
		} catch (Exception e) {
			// won't happen (famous last words)
			e.printStackTrace();
		}
		
		// return new date
		return newDate;
	}
	
	/**
	 * Returns the given date + the specified number of days.
	 * Note: also removes time (see removeTime method).
	 * @return the new date.
	 */
	public static Date addDate(Date date, int days) {
		String dateStr = formatDate(date, Constants.DEFAULT_DATE_FORMAT,
				Constants.DEFAULT_TIME_ZONE);
		dateStr = String.valueOf(Integer.parseInt(dateStr.substring(0, 1))
				+ days) + dateStr.substring(2);
		try {
			return parseDate(dateStr, Constants.DEFAULT_DATE_FORMAT,
					Constants.DEFAULT_TIME_ZONE);
		} catch (ParseException e) {
			// won't happen (famous last words)
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * Checks to see if the given email address is valid
	 * Source: Rob Bamforth: http://robbamforth.wordpress.com/2009/02/12/java-check-for-a-valid-email-address-string/
	 * 
	 * @param emailAddress The address to check
	 * @return True if valid, false otherwise
	 */
	public static boolean isValidEmail(String emailAddress){
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(emailAddress);
		boolean matchFound = m.matches();
		if(matchFound)
			return true;
		
		return false;
	}
	
}
