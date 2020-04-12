package sait.mms.application;

import java.io.IOException;
import sait.mms.managers.*;

/**
 * This program demonstrates the MovieManagementSystem class's displayMenu method
 * @author Seulgi Kim, Thai Nguyen
 * @version  January 17, 2020
 */
public class AppDriver {

	public static void main(String[] args) throws IOException {
		
		// Create a MovieManagementSystem instance
		MovieManagementSystem mms = new MovieManagementSystem();
		
		// call the method to display menu
		mms.displayMenu();
	}

}