
import java.sql.*;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Separator;

/**
 * Class for database fetching/insertion/deletion and so on.
 * 
 * @methods fetchAd, fetchAgency, fetchAgencyExtended, fetchRating, insertUpdateDelete
 * 
 * @author MTs
 * @since 10-26-15
 */

public class DatabaseCommunication {
	
	/*
	 * Just a few test queries for the DB to get you aquainted with how the different methods work. There is no example
	 * for inserting yet! For good reason... Don't flood the DB with crap pls.
	 */

	public static void main(String args[]) {
		
		String sqlStatement = "SELECT Agencies.ID,Name,Logo,AVG(Rating) FROM Agencies, Addresses, Ratings WHERE "
				+ "Agencies.ID = Addresses.AgencyID and Agencies.ID = Ratings.AgencyID and City = 'Götlaborg';";

		ArrayList<Agency> result = fetchAgency(sqlStatement);
		
		for (Agency s : result) {
			System.out.println(s); 
		}
		
//		ArrayList<Ad> result1 = fetchAd(sqlStatement);
//		
//		for (Ad s : result1) {
//			System.out.println(s);
//		}
//		
//		sqlStatement = "SELECT Name, Logo, AVG(Rating) FROM Agencies, Ratings WHERE "
//				+ "Agencies.ID = Ratings.AgencyID GROUP BY Agencies.ID;";		
//		ArrayList<Agency> result2 = fetchAgency(sqlStatement);
//	
//		// Prints all results from the fetch.		
//		for (Agency t : result2) {
//			System.out.println(t);
//		}
//
//		
//		sqlStatement = "SELECT Name,Logo,AVG(Rating),Email,Phone,Street,ZIP,City FROM Agencies, Ratings, Addresses WHERE "
//				+ "Agencies.ID = Ratings.AgencyID and Agencies.ID = Addresses.AgencyID GROUP BY Name;";
//		ArrayList<AgencyExtended> result3 = fetchAgencyExtended(sqlStatement);
//		
//		// Prints all results from the fetch.
//		for (AgencyExtended u : result3) {
//			System.out.println(u);
//		}
//		
//		
//		sqlStatement = "SELECT Name,Rating,Comment FROM Ratings, Agencies WHERE Agencies.ID = Ratings.AgencyID;";
//				
//		ArrayList<Rating> result4 = fetchRating(sqlStatement);
//		
//		// well waddya know, it prints the results.
//		for (Rating v : result4) {
//			System.out.println(v);
//		}
//		
//		sqlStatement = "INSERT INTO Ads (AgencyID,Name,Gender,Species,Type,Age,Description) VALUES "
//				+ "(6,'Lucifer','Male','Devil','Red',666,'Omg so evil');";
//		
//		insertUpdateDelete(sqlStatement);
	}
			
	/**
	 * Method for fetching appropriate information about ads for displaying in the application.
	 * 
	 * @param sqlStatement
	 * @return ArrayList<String>
	 */
	
	public static ArrayList<Ad> fetchAd(String sqlStatement) {
		ArrayList<Ad> result = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
	    
		System.out.println("Attempting to retrieve ads...");
		
		try {
	    	Class.forName("org.sqlite.JDBC");	    					// Loading the driver!

	    	conn = DriverManager.getConnection("jdbc:sqlite:BeeHive");	// Connecting to database!
	    	conn.setAutoCommit(false);									// AutoCommit should never be used.	        

	    	stmt = conn.createStatement();								// Executing incoming query.
	    	ResultSet rs = stmt.executeQuery(sqlStatement);				// Adding results of the query to the ResultSet object.

	        while (rs.next()) {											// This while-loop adds the results to the arrayList. All column names must be matched.
	        	int id = rs.getInt("ID");
	        	String picture = rs.getString("Picture");
	        	String name = rs.getString("Name");
	        	String gender = rs.getString("Gender");
	        	String species = rs.getString("Species");
	        	String type = rs.getString("Type");
	        	int age = rs.getInt("Age");
	        	String description = rs.getString("Description");
	        	String startDate = rs.getString("StartDate");
	        	String endDate = rs.getString("EndDate");
	        	Ad ad = new Ad(id, picture, name, gender, species, type, age, description, startDate, endDate);
	        	result.add(ad);	// Each iteration of the loop an object is added to the ArrayList.
	        }
	        rs.close();			// It is good practice to always close all connections when the information has been retrieved.
	        stmt.close();
	        conn.close();
	        
	    } catch (Exception e) { 
	    	System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    	System.exit(0);
	    }
		
		System.out.println("Retrieved ads!");
		
	    return result;
	}
	
	/**
	 * Method for fetching appropriate information about agencies for displaying in the application.
	 * 
	 * @param sqlStatement
	 * @return ArrayList<Agency>
	 */
	
	public static ArrayList<Agency> fetchAgency(String sqlStatement) {
		ArrayList<Agency> result = new ArrayList<>();
		Connection c = null;
		Statement stmt = null;
	    
	    try {
	    	// Loading the driver!
	    	System.out.println("Loading JDBC driver...");
	    	Class.forName("org.sqlite.JDBC");
	    	System.out.println("Driver loaded successfully!");
	    	
	    	// Connecting to database!
	    	System.out.println("Attempting to connect to database...");
	    	c = DriverManager.getConnection("jdbc:sqlite:BeeHive");
	    	c.setAutoCommit(false);
	        System.out.println("Opened database successfully!");
	        System.out.println();
	        
	        // Executing incoming query.	        
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery(sqlStatement);

	        // This while-loop adds the results to the arrayList.	        
	        while (rs.next()) {
	        	int id = rs.getInt("ID");
	        	String name = rs.getString("Name");
	        	String rating = rs.getString("AVG(Rating)");
	        	String logo = rs.getString("Logo");
	        	Agency agency = new Agency(id, name, rating, logo);
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
	 * Method for fetching extended information about agencies for displaying in the application.
	 * 
	 * @param sqlStatement
	 * @return ArrayLisy<AgencyExtended>
	 */
	
	public static ArrayList<AgencyExtended> fetchAgencyExtended(String sqlStatement) {
		ArrayList<AgencyExtended> result = new ArrayList<>();
		Connection c = null;
		Statement stmt = null;
	    
	    try {
	    	// Loading the driver!
	    	System.out.println("Loading JDBC driver...");
	    	Class.forName("org.sqlite.JDBC");
	    	System.out.println("Driver loaded successfully!");
	    	
	    	// Connecting to database!
	    	System.out.println("Attempting to connect to database...");
	    	c = DriverManager.getConnection("jdbc:sqlite:BeeHive");
	    	c.setAutoCommit(false);
	        System.out.println("Opened database successfully!");
	        System.out.println();
	        
	        // Executing incoming query.	        
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery(sqlStatement);

	        // This while-loop adds the results to the arrayList.	        
	        while (rs.next()) {
	        	int id = rs.getInt("ID");
	        	String name = rs.getString("Name");
	        	String logo = rs.getString("Logo");
	        	String rating = rs.getString("AVG(Rating)");
	        	String email = rs.getString("Email");
	        	String phone = rs.getString("Phone");
	        	String street = rs.getString("Street");
	        	String zip = rs.getString("Zip");
	        	String city = rs.getString("City");
	        	AgencyExtended agency = new AgencyExtended(id, name, logo, rating, email, phone, street, zip, city);
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
	
	public static ArrayList<Rating> fetchRating(String sqlStatement) {
		ArrayList<Rating> result = new ArrayList<>();
		Connection c = null;
		Statement stmt = null;
	    
	    try {
	    	// Loading the driver!
	    	System.out.println("Loading JDBC driver...");
	    	Class.forName("org.sqlite.JDBC");
	    	System.out.println("Driver loaded successfully!");
	    	
	    	// Connecting to database!
	    	System.out.println("Attempting to connect to database...");
	    	c = DriverManager.getConnection("jdbc:sqlite:BeeHive");
	    	c.setAutoCommit(false);
	        System.out.println("Opened database successfully!");
	        System.out.println();
	        
	        // Executing incoming query.	        
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery(sqlStatement);

	        // This while-loop adds the results to the arrayList.	        
	        while (rs.next()) {
	        	String name = rs.getString("Name");
	        	String rating = rs.getString("Rating");
	        	String comment = rs.getString("Comment");
	        	Rating grade = new Rating(name, rating, comment);
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
	 * Method for returning specific attributes, for example for displaying all available species in the application. Also
	 * used for displaying all cities that we have agencies at. 
	 * 
	 * @param table, attribute
	 * @return ObservableList<Object>
	 */
	
	public static ObservableList<Object> fetchAttribute(String table, String column) {
		ObservableList<Object> result = FXCollections.observableArrayList(column, new Separator());
		String sqlStatement = "SELECT Distinct " + column + " FROM " + table + " ORDER BY " + column + ";";
		Connection c = null;
		Statement stmt = null;
	    
	    try {
	    	// Loading the driver!
	    	System.out.println("Loading JDBC driver...");
	    	Class.forName("org.sqlite.JDBC");
	    	System.out.println("Driver loaded successfully!");
	    	
	    	// Connecting to database!
	    	System.out.println("Attempting to connect to database...");
	    	c = DriverManager.getConnection("jdbc:sqlite:BeeHive");
	    	c.setAutoCommit(false);
	        System.out.println("Opened database successfully!");
	        System.out.println();
	        
	        // Executing incoming query.	        
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery(sqlStatement);

	        // This while-loop adds the results to the arrayList.	        
	        while (rs.next()) {
	        	String attribute = rs.getString(column);
	        	result.add(attribute);
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
	 * Foreign key (PRAGMA) constraints are activated for this DB connection, otherwise the constraints will not be enforced.
	 * 
	 * @param sqlStatement
	 * @return void
	 */
	
	public static void insertUpdateDelete(String sqlStatement) {
		Connection c = null;
		Statement stmt = null;
	    
	    try {
	    	// Loading the driver!
	    	System.out.println("Loading JDBC driver...");
	    	Class.forName("org.sqlite.JDBC");
	    	System.out.println("Driver loaded successfully!");
	    	
	    	// Connecting to database!
	    	System.out.println("Attempting to connect to database...");
	    	c = DriverManager.getConnection("jdbc:sqlite:BeeHive");
	    	c.setAutoCommit(false);
	        System.out.println("Opened database successfully!");
	        System.out.println();
	        
	        // Activate foreign key constraints before executing updates, deletions or inserts! VERY IMPORTANT!
	        // Executing incoming query.	        
	        stmt = c.createStatement();
	        stmt.executeUpdate(sqlStatement); 
	        
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
	
	private static String sqlStatement(String sqlMethod, String dbTableName, String agency, String species, 
									   String type, String age, String gender, String description){
		String sqlStatement = "";
		if(sqlMethod == null){
			sqlStatement = "SELECT * FROM " + dbTableName + " WHERE ";
		}
		return sqlStatement;
	}
}