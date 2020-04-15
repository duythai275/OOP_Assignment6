package sait.mms.managers;

import sait.mms.exceptions.*;
import java.util.*;
import java.sql.*;

/**
 * This class has functions to connect movies database, query data by year or randomly, add and delete a record to movies database
 * 
 * @author Seulgi Kim, Thai Nguyen
 * @version April 13, 2020
 */
public class MovieManagementSystem {
	
	/**
	 * this scanner variable is for entering
	 */
	private Scanner input = new Scanner(System.in);
	
	/**
	 * this variable is for connecting to database
	 */
	private Connection conn;
	
	/**
	 * This constructor will create connection to database once the Object initialized
	 */
	public MovieManagementSystem() {
		try {
			this.conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/cprg251?user=cprg251&password=password");
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
	
	/**
	 * displayMenu methods show options : add/delete movie, get movies
	 */
	public void displayMenu() {
		
		int option = 0;
		
		while ( option != 5 ) {
			
			System.out.println("Movie Management System");
			System.out.println("1. Add New Movie and Save");
			System.out.println("2. Generate List of Movies Released in a Year");
			System.out.println("3. Generate List of Random Movies");
			System.out.println("4. Delete Movie by Id");
			System.out.println("5. Exit");
			System.out.print("Enter an option: ");
			try {
				option = input.nextInt();
				System.out.println();
				
				switch(option) {
					case 1: 
						System.out.println("Adding a new movie");
						addMovie();
						break;
					case 2: 
						System.out.println("List of Movies released in a Year");
						generateMovieInYear();
						break;
					case 3: 
						System.out.println("List of Random movies");
						generateRandomMovie();
						break;
					case 4:
						System.out.println("Deleting a movie");
						deleteMovie();
						break;
					case 5:
						conn.close();
						System.out.println("Program closed.");
						break;
					default: 
						System.out.print("Invalid option! Please select option from 1 to 5");
						break;
				}
			} catch (InputMismatchException e) {
				System.out.println("ERROR: Invalid Option! Please enter an option from 1 to 5.");
				input.nextLine();
				System.out.println();
			} catch (SQLException e) {
				System.out.println("ERROR: " + e.getMessage());
				System.out.println();
			}
		}
		
		input.close();
	}

	/**
	 * addMovie method allows entering a new movie with its attributes
	 */
	public void addMovie() {
		try {
			System.out.print("Enter duration: ");
			int duration = inputPositiveNumber(input.nextInt());
			input.nextLine();
			System.out.print("Enter movie title: ");
			String title = input.nextLine();
			System.out.print("Enter year: ");
			int year = inputPositiveNumber(input.nextInt());
			input.nextLine();
			System.out.println("Adding movies...");
			
			Statement stmt = conn.createStatement();
			int rows = stmt.executeUpdate("INSERT INTO movies (duration, title, year) VALUES ('"+duration+"','"+title+"','"+year+"')");
			System.out.println( rows + " movie(s) added.");
			stmt.close();
			System.out.println();
		}
		catch (NegativeNumberException e) {
			System.out.println("ERROR: " + e.getMessage());
			input.nextLine();
			System.out.println();
		}
		catch (InputMismatchException e) {
			System.out.println("ERROR: Input should be a Positive Integer");
			input.nextLine();
			System.out.println();
		} 
		catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
			System.out.println();
		}
	}
	
	/**
	 * generateMovieInYear method generates and displays all movies by a selected released year
	 */
	public void generateMovieInYear() {
		System.out.print("Enter in year: ");
		try {
			int year = inputPositiveNumber(input.nextInt());
			System.out.println("Movie List");
			System.out.printf("%-10s %-10s %s", "Duration", "Year", "Movie" );
			
			Statement stmt = conn.createStatement();
			
			ResultSet result = stmt.executeQuery("SELECT duration, year, title FROM movies WHERE year = '" + year + "'");
			while(result.next()) {
				System.out.printf("%n%-10d %-10d %s", result.getInt("duration"), result.getInt("year"), result.getString("title") );
			}
			result.close();
			
			System.out.println();
			result = stmt.executeQuery("SELECT SUM(duration) FROM movies WHERE year = '" + year + "'");
			while(result.next()) {
				System.out.println("Total duration: " + result.getInt(1) + " minutes");
			}
			result.close();
			
			stmt.close();
			System.out.println();
		}
		catch (NegativeNumberException e) {
			System.out.println("ERROR: " + e.getMessage());
			input.nextLine();
			System.out.println();
		}
		catch (InputMismatchException e) {
			System.out.println("ERROR: Invalid year! Year should be an Integer.");
			input.nextLine();
			System.out.println();
		} 
		catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
			System.out.println();
		}
	}
	
	/**
	 * generateRandomMovie method creates and displays the random movie list
	 */
	public void generateRandomMovie() {
		System.out.print("Enter number of movies: ");
		try {
			int numberOfMoives = inputPositiveNumber(input.nextInt());
			System.out.println("Movie List");
			System.out.printf("%-10s %-10s %s", "Duration", "Year", "Movie" );
			int durationTotal = 0;
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * FROM movies ORDER BY RAND() LIMIT " + numberOfMoives);
			while(result.next()) {
				System.out.printf("%n%-10d %-10d %s", result.getInt("duration"), result.getInt("year"), result.getString("title") );
				durationTotal += result.getInt("duration");
			}
			System.out.println();
			System.out.println("Total duration: " + durationTotal + " minutes");
			result.close();
			stmt.close();
			System.out.println();
		}
		catch (NegativeNumberException e) {
			System.out.println("ERROR: " + e.getMessage());
			input.nextLine();
			System.out.println();
		}
		catch (InputMismatchException e) {
			System.out.println("ERROR: Invalid year! Number of Movies should be a Positive Integer.");
			input.nextLine();
			System.out.println();
		} 
		catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
			System.out.println();
		}
	}
	
	/**
	 * deleteMovie method is used to delete a record in movies table by movieId
	 */
	public void deleteMovie() {
		System.out.print("Enter movie Id: ");
		try {
			int movieId = inputPositiveNumber(input.nextInt());
			Statement stmt = conn.createStatement();
			int rows = stmt.executeUpdate("DELETE FROM movies WHERE id = '" + movieId + "'");
			System.out.println(rows + " movie(s) deleted.");
			stmt.close();
			System.out.println();
		} 
		catch (NegativeNumberException e) {
			System.out.println("ERROR: Movie ID is a positive integer.");
			input.nextLine();
			System.out.println();
		}
		catch (InputMismatchException e) {
			System.out.println("ERROR: Invalid MovieId! MovieId is a positive integer.");
			input.nextLine();
			System.out.println();
		} 
		catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
			System.out.println();
		}
	}
	
	/**
	 * inputPositiveNumber method is used to validate the inputted number
	 * , to be sure the inputted number is positive number
	 * 
	 * @param inputNumber is value that user enters
	 * @return only a positive number
	 * @throws NegativeNumberException will be thrown when the inputted value is negative
	 */
	public int inputPositiveNumber(int inputNumber) throws NegativeNumberException {
		if ( inputNumber < 0 ) {
			throw new NegativeNumberException();
		}
		return inputNumber;
	}
}
