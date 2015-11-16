
import java.sql.*;
import java.util.ArrayList;

import org.sqlite.SQLiteConfig;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Separator;

/**
 * This is an object that will handle the Database communication. There are also static methods to handle ResultSet metaData for
 * generic result handling.
 * 
 * @since 2015-11-16
 * @author MTs
 */

public class DBobject {
	public Connection connect = null;
	public PreparedStatement stmt = null;
	public ResultSet resultSet = null;
	public final String dbType;
	public final String dbName;
	public final String dbDriver;
	public final int dbTimeOut;

	/*
	 * Just an empty constructor call to load the driver and start connecting to the database.
	 */

	public DBobject() {
		this.dbType = "jdbc:sqlite:";
		this.dbName = "BeeHive";
		this.dbDriver = "org.sqlite.JDBC";
		this.dbTimeOut = 30;
		
		System.out.println(">> Loading driver");
		try {
			Class.forName(dbDriver);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		setConnection();
	}

	/**
	 * Setting the connection to the database based on the dbName and Type, foreign keys are also activated here using the SQLiteConfig.
	 * 
	 */
	
	public void setConnection() {
		System.out.println(">> Connecting to database");
		try {
			SQLiteConfig dbProperties = new SQLiteConfig();
			dbProperties.enforceForeignKeys(true);
			
			connect = DriverManager.getConnection(dbType + dbName, dbProperties.toProperties());
			connect.setAutoCommit(false);

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * This method checks if foreign keys are enforced for this connection, which they always should be. The console will also show information about the foreign key
	 * status. setConnection() should always enforde foreign keys when the connection is started. Check the SQLiteConfig in case of problems.
	 * 
	 * @return boolean
	 * @throws SQLException
	 */
	
	public boolean foreignKeysOn() throws SQLException {
		int foreignKeyStatus = 2;
		
		stmt = connect.prepareStatement("PRAGMA foreign_keys;");
		resultSet = stmt.executeQuery();
		
		while (resultSet.next()) {
			foreignKeyStatus = resultSet.getInt("foreign_keys");
		}

		return foreignKeyStatus == 1;
	}
	
	/**
	 * Execute queries to the database.
	 * 
	 * @throws SQLException
	 */

	public ResultSet executeQuery(String query) throws SQLException {
		System.out.println(">> Executing query");
		stmt = connect.prepareStatement(query);
		ResultSet resultSet = stmt.executeQuery();
		
		connect.commit();
		
		return resultSet;
	}

	/**
	 * Adds update to batch to be executed at a later point.
	 * 
	 * @throws SQLException
	 */
	
	public void addBatch(String update) throws SQLException {
		System.out.println(">> Adding update to batch");
		stmt = connect.prepareStatement(update);
		
		stmt.addBatch();
	}
	
	/**
	 * Executes updates to the database that are batched into the prepared statement.
	 * 
	 * @throws Exception
	 */

	public void executeBatch() throws SQLException {
		System.out.println(">> Executing batch update(s)");
		stmt.executeBatch();
		
		connect.commit();
	}

	/**
	 * Commits current transaction if updates have been made (?). Not sure if this method is neccessary yet.
	 * 
	 * @throws SQLException
	 */
	
	public void commit() throws SQLException {
		connect.commit();
	}
	
	/**
	 * Closing connection to currently connected database, if one is connected
	 * at all. Otherwise, do nothing. You should always commit or roll back before closing.
	 * 
	 * @throws SQLException
	 */

	public void closeConnection() throws SQLException {
		System.out.println(">> Closing statement");
		if (stmt != null) {
			stmt.close();
		} 
		else {
			System.out.println(">> No statement established, cannot close");
		}

		System.out.println(">> Closing connection");
		if (connect != null) {
			connect.close();
		} 
		else {
			System.out.println(">> No database connected, cannot close");
		}
	}
	
	/**
	 * Returns an integer representing the number of columns in the ResultSet. If an error occurs, -1 is returned.
	 * 
	 * @param ResultSet input
	 * @return Integer representing number of ResultSet columns
	 */
	
	public int getColumnCount(ResultSet input) {
	    int iOutput = 0;
	   
	    try {
	    	ResultSetMetaData rsMetaData = input.getMetaData();
	    	iOutput = rsMetaData.getColumnCount();
	    } catch (Exception e) {
	    	System.out.println(e);
	    	return -1;
	    }
	    return iOutput;
	}
	
	/**
	 * Returns a string array with all column names in the input Result Set.
	 * 
	 * @param ResultSet input
	 * @return String[] with all column names
	 */

	public static String[] metaDataNames(ResultSet input) {
		String exceptionArray[] = null;									// Every possible outcome needs to return an array, this is for if there is a problem.
		
		try {
			ResultSetMetaData rsMeta = input.getMetaData();				// Getting all metaData from ResultSet.
			String nameArray[] = new String[rsMeta.getColumnCount()];	// Counting columns to determine size of the array, starts at 1 so no need for -1.
			for (int i = 1; i <= nameArray.length; i++) {				
				nameArray[i - 1] = rsMeta.getColumnName(i);				// ResultSet metaData starts at 1 when counting columns, if 7 columns are present it is then not
			}															// counted for 0 to 6 but 1 to 7.
			return nameArray;
		} catch (Exception e) {
			System.out.println(e);
			return exceptionArray;
		}
	}

	/**
	 * Returns a string array with all column types in the input Result Set. If returned array is null, an error occured.
	 * 
	 * @param ResultSet input
	 * @return String[] with all column types
	 */
	
	public static String[] metaDataTypes(ResultSet input) {
		String exceptionArray[] = null;
		
		try {
			ResultSetMetaData rsMeta = input.getMetaData();
			String typeArray[] = new String[rsMeta.getColumnCount()];
			for (int i = 1; i <= typeArray.length; i++) {
				typeArray[i - 1] = rsMeta.getColumnTypeName(i);
			}
			return typeArray;
		} catch (Exception e) {
			System.out.println(e);
			return exceptionArray;
		}
	}

	/**
	 * Takes in unput a ResultSet and outputs information in the form of an ArrayList.
	 * 
	 * @param ResultSet
	 * @return ArrayList<ShownObject>
	 * @throws SQLException
	 */
	
	public static ArrayList<ShownObject> createAddress(ResultSet input) throws SQLException {
		ArrayList<ShownObject> resultList = new ArrayList<ShownObject>();

		while (input.next()) {
			String street = input.getString("Street");
			String zip = input.getString("ZIP");
			String city = input.getString("City");
			ShownObject address = new ShownObject(street, zip, city);
			resultList.add(address);
		}

		return resultList;
	}
	
	/**
	 * Takes in unput a ResultSet and outputs information in the form of an ArrayList.
	 * 
	 * @param ResultSet
	 * @return ArrayList<ShownObject>
	 * @throws SQLException
	 */

	public static ArrayList<ShownObject> createRating(ResultSet input) throws SQLException {
		ArrayList<ShownObject> resultList = new ArrayList<ShownObject>();

		while (input.next()) {
			String name = input.getString("Name");
			String stars = input.getString("Rating");
			String comment = input.getString("Comment");
			ShownObject rating = new ShownObject(name, stars, comment);
			resultList.add(rating);
		}

		return resultList;
	}

	/**
	 * Takes in unput a ResultSet and outputs information in the form of an ArrayList.
	 * 
	 * @param ResultSet
	 * @return ArrayList<ShownObject>
	 * @throws SQLException
	 */
	
	public static ArrayList<ShownObject> createAgency(ResultSet input) throws SQLException {
		ArrayList<ShownObject> resultList = new ArrayList<ShownObject>();

		while (input.next()) { // This while-loop adds the results to the
								// arrayList.
			String name = input.getString("Name");
			String rating = input.getString("AVG(Rating)");
			String logo = input.getString("Logo");
			ShownObject agency = new ShownObject(logo, name, rating);
			resultList.add(agency); // Each iteration of the loop an object is added
								// to the ArrayList.
		}

		return resultList;
	}

	/**
	 * Takes in unput a ResultSet and outputs information in the form of an ArrayList.
	 * 
	 * @param ResultSet
	 * @return ArrayList<ShownObject>
	 * @throws SQLException
	 */
	
	public static ArrayList<ShownObject> createAgencyExt(ResultSet input) throws SQLException {
		ArrayList<ShownObject> resultList = new ArrayList<ShownObject>();

		while (input.next()) { 	// This while-loop adds the results to the
								// arrayList.
			String logo = input.getString("Logo");
			String name = input.getString("Name");
			String rating = input.getString("AVG(Rating)");
			String email = input.getString("Email");
			String phone = input.getString("Phone");
			String street = input.getString("Street");
			String zip = input.getString("Zip");
			String city = input.getString("City");
			ShownObject agency = new ShownObject(logo, name, rating, email, phone, street, zip, city);
			resultList.add(agency); // Each iteration of the loop an object is added
								// to the ArrayList.
		}

		return resultList;
	}
	
	/**
	 * Takes in unput a ResultSet and outputs information in the form of an ArrayList.
	 * 
	 * @param ResultSet
	 * @return ArrayList<ShownObject>
	 * @throws SQLException
	 */

	public static ArrayList<ShownObject> createAd(ResultSet input) throws SQLException {
		ArrayList<ShownObject> resultList = new ArrayList<ShownObject>();

		while (input.next()) { 	// This while-loop adds the results to the
								// arrayList. All column names must be matched.
			String picture = input.getString("Picture");
			String name = input.getString("Name");
			String gender = input.getString("Gender");
			String species = input.getString("Species");
			String type = input.getString("Type");
			String age = input.getString("Age");
			String description = input.getString("Description");
			String startDate = input.getString("StartDate");
			String endDate = input.getString("EndDate");
			ShownObject ad = new ShownObject(picture, name, gender, species, type, age, description, startDate, endDate);
			resultList.add(ad); // Each iteration of the loop an object is added to
								// the ArrayList.
		}

		return resultList;
	}

	/**
	 * Method for returning specific attributes, for example for displaying all
	 * available species in the application. Also used for displaying all cities
	 * that we have agencies at.
	 * 
	 * @param table,
	 *            attribute
	 * @return ObservableList<Object>
	 */

	public static ObservableList<Object> fetchAttribute(String table, String column, 
														String priorColumn, String priorValue) {
		
		ObservableList<Object> resultList = FXCollections.observableArrayList(column, new Separator());
		String sqlStatement;
		
		if (priorColumn == null) {
			sqlStatement = "SELECT Distinct " + column + " FROM " + table + " ORDER BY " + column + ";";
		} 
		else {
			sqlStatement = "SELECT Distinct " + column + " FROM " + table + " WHERE " + priorColumn + 
						   " == '" + priorValue + "' ORDER BY " + column + ";";
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
				resultList.add(attribute);
			}

			// Closing result sets and statements.
			rs.close();
			stmt.close();
			c.close();

			// Catching any and all exceptions, printing an error message.
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
		return resultList;
	}
}
