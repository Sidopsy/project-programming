
import java.sql.*;
import java.util.ArrayList;

import org.sqlite.SQLiteConfig;

import javafx.beans.property.SimpleStringProperty;
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
	private Connection connect = null;
	public PreparedStatement stmt = null;
	public ResultSet resultSet = null;
	private final String dbType;
	private final String dbName;
	private final String dbDriver;

	/**
	 * Just an empty constructor call to load the driver and start connecting to the database.
	 */

	public DBobject() {
		this.dbType = "jdbc:sqlite:";
		this.dbName = "BeeHive";
		this.dbDriver = "org.sqlite.JDBC";
		
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
		resultSet.close();
		
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
		resultSet = stmt.executeQuery();
		
		connect.commit();
		
		return resultSet;
	}

	/**
	 * Execute single updates to the database, use batch if more than one insert, delect or update is to be carried out at one
	 * time.
	 * 
	 * @param String update
	 * @throws SQLException
	 */
	
	public void executeUpdate(String update) throws SQLException {
		System.out.println(">> Executing update");
		stmt = connect.prepareStatement(update);
		stmt.executeUpdate();
		
		connect.commit();
	}
	
	/**
	 * Adds update to batch to be executed at a later point.
	 * 
	 * NOT IN WORKING CONDITION!
	 * 
	 * @throws SQLException
	 */
	
	public void addBatch(String update) {
		System.out.println(">> Adding update to batch");
		try {
			stmt = connect.prepareStatement(update);
			stmt.addBatch();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
	}
	
	/**
	 * Executes updates to the database that are batched into the prepared statement.
	 * 
	 * NOT IN WORKING CONDITION!
	 * 
	 * @throws Exception
	 */

	public void executeBatch() throws SQLException {
		System.out.println(">> Executing batch update(s)");
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		
			connect.commit();
		
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
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
		System.out.println(">> Closing ResultSet");
		if (resultSet != null) {
			resultSet.close();
		}
		else {
			System.out.println(">> Nothing in ResultSet, cannot close");
		}
		
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
	
	public int getColumnCount(ResultSet input) throws SQLException {
	    int iOutput = 0;
	   	ResultSetMetaData rsMetaData = input.getMetaData();
	   	iOutput = rsMetaData.getColumnCount();
	   	
	    return iOutput;
	}
	
	/**
	 * Returns a string array with all column names in the input Result Set.
	 * 
	 * @param ResultSet input
	 * @return String[] with all column names
	 */

	public String[] metaDataNames(ResultSet input) throws SQLException {
		ResultSetMetaData rsMeta = input.getMetaData();				// Getting all metaData from ResultSet.
		String[] nameArray = new String[getColumnCount(input)];		// Counting columns to determine size of the array, starts at 1 so no need for -1.
		
		for (int i = 1; i <= nameArray.length; i++) {				
			nameArray[i - 1] = rsMeta.getColumnName(i);				// ResultSet metaData starts at 1 when counting columns, if 7 columns are present it is then not
		}															// counted for 0 to 6 but 1 to 7.

		return nameArray;
	}

	/**
	 * Returns a string array with all column types in the input Result Set. If returned array is null, an error occured.
	 * 
	 * @param ResultSet input
	 * @return String[] with all column types
	 */
	
	public String[] metaDataTypes(ResultSet input) throws SQLException {
		ResultSetMetaData rsMeta = input.getMetaData();
		String[] typeArray = new String[getColumnCount(input)];
		
		for (int i = 1; i <= typeArray.length; i++) {
			typeArray[i - 1] = rsMeta.getColumnTypeName(i);
		}
		
		return typeArray;
	}

	/**
	 * Takes in input a ResultSet and outputs information in the form of an ArrayList of ArrayLists.
	 * 
	 * @param ResultSet
	 * @return ArrayList<ArrayList<String>>
	 * @throws SQLException
	 */
	
	public ArrayList<ArrayList<String>> fetchResult(ResultSet input) throws SQLException {
		ArrayList<ArrayList<String>> resultList = new ArrayList<>();
		int columnCount = getColumnCount(input);
		
		while (input.next()) {
			ArrayList<String> result = new ArrayList<>();
			for (int index = 1; index <= columnCount; index++) {
				result.add(input.getString(index));
			}
			resultList.add(result);
		}

		return resultList;
	}
	
	public ArrayList<Ad> fetchAd(ResultSet input) throws SQLException {
		ArrayList<Ad> result = new ArrayList<>();
	
	        while (input.next()) {											// This while-loop adds the results to the arrayList. All column names must be matched.
	        	int id = input.getInt("Id");
	        	String picture = input.getString("Picture");
	        	String name = input.getString("Name");
	        	String gender = input.getString("Gender");
	        	String species = input.getString("Species");
	        	String type = input.getString("Type");
	        	int age = input.getInt("Age");
	        	String description = input.getString("Description");
	        	String startDate = input.getString("StartDate");
	        	String endDate = input.getString("EndDate");
	        	int agencyId = input.getInt("AgencyID");
	        	String agencyName = input.getString("Agency");
	        	String rating = input.getString("Rating");
	        	
	        	Ad ad = new Ad(id,picture,name,gender,species,type,age,description,startDate,endDate,agencyId,agencyName,rating);
	        	result.add(ad);	// Each iteration of the loop an object is added to the ArrayList.
	        }
	        
	  
	    return result;
	}
	
	public ArrayList<Agency> fetchAgency(ResultSet input) throws SQLException {
		ArrayList<Agency> result = new ArrayList<>();
	        while (input.next()) {											// This while-loop adds the results to the arrayList.
	        	int id = input.getInt("ID");
	        	String name = input.getString("Name");
	        	String rating = input.getString("AVG(Rating)");
	        	String logo = input.getString("Logo");
	        	Agency agency = new Agency(id, logo, name, rating);
	        	result.add(agency);	// Each iteration of the loop an object is added to the ArrayList.
	        }
	    return result;
	}
	
	/**
	 * Method for fetching extended information about agencies for displaying in the application.
	 * 
	 * @param input
	 * @return ArrayLisy<AgencyExtended>
	 * @throws SQLException 
	 */
	
	protected ArrayList<AgencyExt> fetchAgencyExt(ResultSet input) throws SQLException {
		ArrayList<AgencyExt> result = new ArrayList<>();
	        while (input.next()) {											// This while-loop adds the results to the arrayList.
	        	int id = input.getInt("ID");
	        	String logo = input.getString("Logo");
	        	String name = input.getString("Name");
	        	String rating = input.getString("AVG(Rating)");
	        	String email = input.getString("Email");
	        	String phone = input.getString("Phone");
	        	String street = input.getString("Street");
	        	String zip = input.getString("Zip");
	        	String city = input.getString("City");
	        	AgencyExt agency = new AgencyExt(id, logo, name, rating, email, phone, street, zip, city);
	        	result.add(agency);	// Each iteration of the loop an object is added to the ArrayList.
	        }
	    return result;
	}
	
	/**
	 * Method for fetching appropriate information about ratings for displaying in the application.
	 * 
	 * @param sqlStatement
	 * @return ArrayList<Rating>
	 * @throws SQLException 
	 */
	
	public static ArrayList<Rating> fetchRating(ResultSet input) throws SQLException {
		ArrayList<Rating> result = new ArrayList<>();
	        while (input.next()) {
	        	String name = input.getString("Name");
	        	String rating = input.getString("Rating");
	        	String comment = input.getString("Comment");
	        	Rating grade = new Rating(name, rating, comment);
	        	result.add(grade);
	        }
	    return result;
	}
	
	/**
	 * Method for creating observable lists using ArrayList<ArrayList<String>>, ordinarily after obtaining the resulting 2D array-
	 * list from a query.
	 * 
	 * @param ArrayList<ArrayList<String>>
	 * @returns ObservableList<Object>
	 */
	
	public ObservableList<Object> createObservableList(String columnName, ArrayList<ArrayList<String>> input) {
		ObservableList<Object> resultList = FXCollections.observableArrayList(columnName, new Separator(), "Select all" , new Separator());
		
		for (int i = 0; input.size() > i; i++) {
			ArrayList<String> fetch = input.get(i);
			for (int j = 0; fetch.size() > j; j++) {
				resultList.add(fetch.get(j));
			}
		}
		return resultList;
	}

	public ObservableList<Ad> createObservableList(ArrayList<Ad> input) {
		// TODO Auto-generated method stub
		return FXCollections.observableArrayList(input);
	}

}
