package sait.mms.exceptions;

/**
 * This class is used for informing an exception/error
 * 
 * @author Seulgi Kim, Thai Nguyen
 * @version April 13, 2020
 */
public class NegativeNumberException extends Exception {
	
	/**
	 * This constructor uses a generic error message.
	 */
	public NegativeNumberException() {
		super("Negative number");
	}
}
