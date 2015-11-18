
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

	    	stmt = conn.createStatement();								
	    	ResultSet rs = stmt.executeQuery(sqlStatement);				// Executing incoming query.

	        while (rs.next()) {											// This while-loop adds the results to the arrayList. All column names must be matched.
	        	String agency = rs.getString("AgencyID");
	        	String picture = rs.getString("Picture");
	        	String name = rs.getString("Name");
	        	String gender = rs.getString("Gender");
	        	String species = rs.getString("Species");
	        	String type = rs.getString("Type");
	        	String age = rs.getString("Age");
	        	String description = rs.getString("Description");
	        	String startDate = rs.getString("StartDate");
	        	String endDate = rs.getString("EndDate");
	        	Ad ad = new Ad(0, agency, picture, name, gender, species, 0, type, age, description, 0, startDate, endDate);
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
		Connection conn = null;
		Statement stmt = null;
	    
		System.out.println("Attempting to retrieve simple agencies...");
		
	    try {
	    	Class.forName("org.sqlite.JDBC");	    					// Loading the driver!
	    	
	    	conn = DriverManager.getConnection("jdbc:sqlite:BeeHive");	// Connecting to database!
	    	conn.setAutoCommit(false);									// AutoCommit should never be used.	

	        stmt = conn.createStatement();								
	        ResultSet rs = stmt.executeQuery(sqlStatement);				// Executing incoming query.
	        
	        while (rs.next()) {											// This while-loop adds the results to the arrayList.
	        	String id = rs.getString("ID");
	        	String name = rs.getString("Name");
	        	String rating = rs.getString("AVG(Rating)");
	        	String logo = rs.getString("Logo");
	        	Agency agency = new Agency(id, logo, name, rating);
	        	result.add(agency);	// Each iteration of the loop an object is added to the ArrayList.
	        }
	        
			rs.close();				// It is good practice to always close all connections when the information has been retrieved.
			stmt.close();
			conn.close();
		
	    } catch (Exception e) { 
	    	System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    	System.exit(0);
	    }
	    
	    System.out.println("Retrieved simple agencies!");
	    
	    return result;
	}
	
	/**
	 * Method for fetching extended information about agencies for displaying in the application.
	 * 
	 * @param sqlStatement
	 * @return ArrayLisy<AgencyExtended>
	 */
	
	public static ArrayList<AgencyExt> fetchAgencyExt(String sqlStatement) {
		ArrayList<AgencyExt> result = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
	    
		System.out.println("Attempting to retrieve extended agencies...");
		
	    try {
	    	Class.forName("org.sqlite.JDBC");	    					// Loading the driver!
	    	
	    	conn = DriverManager.getConnection("jdbc:sqlite:BeeHive");	// Connecting to database!
	    	conn.setAutoCommit(false);									// AutoCommit should never be used.		        
	    	
	        stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(sqlStatement);				// Executing incoming query.
       
	        while (rs.next()) {											// This while-loop adds the results to the arrayList.
	        	String id = rs.getString("ID");
	        	String logo = rs.getString("Logo");
	        	String name = rs.getString("Name");
	        	String rating = rs.getString("AVG(Rating)");
	        	String email = rs.getString("Email");
	        	String phone = rs.getString("Phone");
	        	String street = rs.getString("Street");
	        	String zip = rs.getString("Zip");
	        	String city = rs.getString("City");
	        	AgencyExt agency = new AgencyExt(id, logo, name, rating, email, phone, street, zip, city);
	        	result.add(agency);	// Each iteration of the loop an object is added to the ArrayList.
	        }
	                
			rs.close();				// It is good practice to always close all connections when the information has been retrieved.
			stmt.close();		
			conn.close();

	    } catch (Exception e) { 
	    	System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    	System.exit(0);
	    }
	    
	    System.out.println("Retrieved extended agencies!");
	    
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
	
	public static ObservableList<Object> fetchAttribute(String table, String column, String priorColumn, String priorValue) {
		ObservableList<Object> result = FXCollections.observableArrayList(column, new Separator());
		String sqlStatement;
		if(priorColumn == null){
			sqlStatement = "SELECT Distinct " + column + " FROM " + table + " ORDER BY " + column + ";";
		} else {
			sqlStatement = "SELECT Distinct " + column + " FROM " + table + " WHERE " + priorColumn + " == '" + priorValue + "' ORDER BY " + column + ";";
		}
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