
import java.sql.*;
import java.util.ArrayList;

/**
 * Class for database fetching and insertion.
 * 
 * @methods fetchAds, fetchAgencies, fetchRatings, insert
 * 
 * @author MTs
 * @since 10-26-15
 */

public class DatabaseCommunication {
	
	/*
	 * Just a few test queries for the DB to get you aquainted with how the different methods work. There is no example
	 * for inserting yet!
	 */

	public static void main(String args[]) {
		
		// Use the String sqlStatement to create your query and test it towards the DB if you have any doubts!
		String sqlStatement = "SELECT * FROM Ads;";	
		ArrayList<Ad> result1 = fetchAds(sqlStatement);

		// Prints all results from the fetch.		
		for (Ad s : result1) {
			System.out.println(s);
		}
		
		sqlStatement = "SELECT Name, Email, Phone, Street, Zip, City FROM Agencies, Addresses WHERE Agencies.ID = AgencyID;";		
		ArrayList<Agency> result2 = fetchAgencies(sqlStatement);
	
		// Prints all results from the fetch.		
		for (Agency t : result2) {
			System.out.println(t);
		}
	}
	
	/**
	 * Method for fetching appropriate information about ads for displaying in the application.
	 * 
	 * @param sqlStatement
	 * @return ArrayList<String>
	 */
	
	public static ArrayList<Ad> fetchAds(String sqlStatement) {
		ArrayList<Ad> result = new ArrayList<Ad>();
		Connection c = null;
		Statement stmt = null;
	    
	    try {
	    	// Loading the driver!
	    	System.out.println("Loading JDBC driver...");
	    	Class.forName("org.sqlite.JDBC");
	    	System.out.println("Driver loaded successfully!");
	    	
	    	// Connecting to database!
	    	System.out.println("Attempting to connect to database...");
	    	c = DriverManager.getConnection("jdbc:sqlite:BeeHiveTest");
	    	c.setAutoCommit(false);
	        System.out.println("Opened database successfully!");
	        System.out.println();
	        
	        // Executing incoming query.	        
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery(sqlStatement);

	        // This while-loop adds the results to the arrayList.	        
	        while (rs.next()) {
	           String picture = rs.getString("Picture");
	           String name = rs.getString("Name");
	           String gender = rs.getString("Gender");
	           String species = rs.getString("Species");
	           String type = rs.getString("Type");
	           int age = rs.getInt("Age");
	           String description = rs.getString("Description");
	           String startDate = rs.getString("StartDate");
	           String endDate = rs.getString("EndDate");
	           Ad ad = new Ad(picture, name, gender, species, type, age, description, startDate, endDate);
	           result.add(ad);
	        }
	        
	        // Closing result sets and statements.	        
	        rs.close();
	        stmt.close();
	        c.close();
	        
	        // Catching any and all exceptions, printing an error message.	        
	    } catch (Exception e) { 
	    	System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    	System.exit(0);
	    }
	    return result;
	}
	
	/**
	 * Method for fetching appropriate information about agencies for displaying in the application.
	 * 
	 * @param sqlStatement
	 * @return ArrayList<Agency>
	 */
	
	public static ArrayList<Agency> fetchAgencies(String sqlStatement) {
		ArrayList<Agency> result = new ArrayList<Agency>();
		Connection c = null;
		Statement stmt = null;
	    
	    try {
	    	// Loading the driver!
	    	System.out.println("Loading JDBC driver...");
	    	Class.forName("org.sqlite.JDBC");
	    	System.out.println("Driver loaded successfully!");
	    	
	    	// Connecting to database!
	    	System.out.println("Attempting to connect to database...");
	    	c = DriverManager.getConnection("jdbc:sqlite:BeeHiveTest");
	    	c.setAutoCommit(false);
	        System.out.println("Opened database successfully!");
	        System.out.println();
	        
	        // Executing incoming query.	        
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery(sqlStatement);

	        // This while-loop adds the results to the arrayList.	        
	        while (rs.next()) {
	        	String name = rs.getString("Name");
	        	String logo = rs.getString("Logo");
	        	Agency agency = new Agency(name, logo);
	        	result.add(agency);
	        }
	        
	        // Closing result sets and statements.	        
			rs.close();
			stmt.close();
			c.close();
			
			// Catching any and all exceptions, printing an error message.			
	    } catch (Exception e) { 
	    	System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    	System.exit(0);
	    }
	    return result;
	}
	
	/**
	 * Method for fetching appropriate information about ratings for displaying in the application.
	 * 
	 * @param sqlStatement
	 * @return ArrayList<Rating>
	 */
	
	public static ArrayList<Rating> fetchRatings(String sqlStatement) {
		ArrayList<Rating> result = new ArrayList<Rating>();
		Connection c = null;
		Statement stmt = null;
	    
	    try {
	    	// Loading the driver!
	    	System.out.println("Loading JDBC driver...");
	    	Class.forName("org.sqlite.JDBC");
	    	System.out.println("Driver loaded successfully!");
	    	
	    	// Connecting to database!
	    	System.out.println("Attempting to connect to database...");
	    	c = DriverManager.getConnection("jdbc:sqlite:BeeHiveTest");
	    	c.setAutoCommit(false);
	        System.out.println("Opened database successfully!");
	        System.out.println();
	        
	        // Executing incoming query.	        
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery(sqlStatement);

	        // This while-loop adds the results to the arrayList.	        
	        while (rs.next()) {
	        	int rating = rs.getInt("Rating");
	        	String comment = rs.getString("Comment");
	        	Rating grade = new Rating(rating, comment);
	        	result.add(grade);
	        }
	        
	        // Closing result sets and statements.	        
			rs.close();
			stmt.close();
			c.close();
			
			// Catching any and all exceptions, printing an error message.			
	    } catch (Exception e) { 
	    	System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    	System.exit(0);
	    }
	    return result;
	}
	
	/**
	 * Method for saving input information to the database.
	 * 
	 * @param sqlStatement
	 * @return void
	 */
	
	public static void insert(String sqlStatement) {
		Connection c = null;
		Statement stmt = null;
	    
	    try {
	    	// Loading the driver!
	    	System.out.println("Loading JDBC driver...");
	    	Class.forName("org.sqlite.JDBC");
	    	System.out.println("Driver loaded successfully!");
	    	
	    	// Connecting to database!
	    	System.out.println("Attempting to connect to database...");
	    	c = DriverManager.getConnection("jdbc:sqlite:BeeHiveTest");
	    	c.setAutoCommit(false);
	        System.out.println("Opened database successfully!");
	        System.out.println();
	        
	        // Executing incoming query.	        
	        stmt = c.createStatement();
	        stmt.executeQuery(sqlStatement);
	        
	        // Closing result sets and statements.
	        stmt.close();
	        c.commit();
	        c.close();
	        
	        // Catching any and all exceptions, printing an error message.	        
	    } catch (Exception e) { 
	    	System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    	System.exit(0);
	    }
	}
}