package Object;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

import org.sqlite.SQLiteConfig;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;

/**
 * This is an object that handles the Database communication. There are also static methods to handle ResultSet metaData for generic
 * result handling.
 * 
 * @since 2015-11-16
 * @author Maans Thoernvik
 */

public class Database {
	private static Connection connect = null;
	private static PreparedStatement stmt = null;
	private static ResultSet resultSet = null;
	private final String dbType;
	private final String dbName;
	private final String dbDriver;

	/**
	 * An empty constructor call to load the driver.
	 */

	public DBobject() {
		this.dbType = "jdbc:sqlite:";
		this.dbName = "BeeHive";
		this.dbDriver = "org.sqlite.JDBC";

		System.out.println(">> Loading driver");
		try {
			Class.forName(dbDriver);
		} catch (Exception e) {System.err.println(e.getClass().getName() + ": " + e.getMessage());}
	}

	/**
	 * Setting the connection to the database based on the dbName and Type, foreign keys are also activated here using the SQLiteConfig object.
	 * 
	 */

	public void setConnection() {
		System.out.println(">> Setting connection to DB");
		try {
			SQLiteConfig dbProperties = new SQLiteConfig();
			dbProperties.enforceForeignKeys(true);

			connect = DriverManager.getConnection(dbType + dbName, dbProperties.toProperties());
			connect.setAutoCommit(false);

		} catch (Exception e) {System.err.println(e.getClass().getName() + ": " + e.getMessage());}
	}

	/**
	 * This method checks if foreign keys are enforced for this connection, which they always should be. The console will also show information about the foreign key
	 * status. setConnection() should always enforde foreign keys when the connection is started. Check the SQLiteConfig in case of problems.
	 * 
	 * This method is not currently in use but is left if need for it arises in the future.
	 * 
	 * @return boolean
	 * @throws SQLException
	 */

	public boolean foreignKeysOn() {
		setConnection();
		System.out.println(">> Checking foreign keys...");
		int foreignKeyStatus = 2;

		try {
			stmt = connect.prepareStatement("PRAGMA foreign_keys;");
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				foreignKeyStatus = resultSet.getInt("foreign_keys");
			}
			resultSet.close();
		} catch (SQLException e) {System.err.println(e.getClass().getName() + ": " + e.getMessage());}

		if (foreignKeyStatus == 1) {				// Database returns a "1" if foreign keys are activated and a "0" if not.
			System.out.println(">> Foreign keys activated!");
			return true;
		} else {
			System.out.println(">> Foreign keys not active.");
			return false;
		}
	}

	/**
	 * Execute queries to the database. Note that this method SETS the connection but never closes it. Remember to use
	 * closeConnection() when the ResultSet has been read and the information has been gathered.
	 * 
	 * @param Query to be sent to the database.
	 * @return ResultSet containing the result of the query.
	 */

	public ResultSet executeQuery(String query) {
		setConnection();
		System.out.println(">> Executing query");
	
		try {
			stmt = connect.prepareStatement(query);
			resultSet = stmt.executeQuery();
	
			connect.commit();
		} catch (SQLException e) {System.err.println(e.getClass().getName() + ": " + e.getMessage());}
	
		return resultSet;
	}

	/**
	 * Execute single updates to the database. Closes connection automatically when update has been executed.
	 * 
	 * @param String update command.
	 */

	public void executeUpdate(String update){
		setConnection();
		System.out.println(">> " + update.substring(0, 10));

		try {
			stmt = connect.prepareStatement(update);
			stmt.executeUpdate();

			connect.commit();
		} catch (SQLException e) {System.err.println(e.getClass().getName() + ": " + e.getMessage());} 

		closeConnection();
	}

	/**
	 * Method for updating pictures to existing entries in the database.
	 */

	public int updatePicture(String update, byte[] parameters, int id) {
		int result = 0;							// Used for letting the caller know whether the update was successful.
		setConnection();
		System.out.println(">> Picture update " + update.substring(7, 10) + ": " + id);
		
		try {
			stmt = connect.prepareStatement(update);
			stmt.setBytes(1, parameters);		// Loading the byte array to the update to be sent.
			stmt.setInt(2, id);					// ID of the entry to be updated.
			result = stmt.executeUpdate();
			
			connect.commit();
		} catch (Exception e) {System.err.println(e.getClass().getName() + ": " + e.getMessage());}
		
		closeConnection();

		return result;
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
		} catch (Exception e) {System.err.println(e.getClass().getName() + ": " + e.getMessage());}
		
		return iOutput;
	}

	/**
	 * Takes in input a ResultSet and outputs information in the form of an ArrayList of ArrayLists.
	 * 
	 * @param ResultSet
	 * @return ArrayList<ArrayList<String>>
	 */

	public ArrayList<ArrayList<String>> fetchResult(ResultSet input) {
		ArrayList<ArrayList<String>> resultList = new ArrayList<>();
		try {	
			int columnCount = getColumnCount(input);

			while (input.next()) {
				ArrayList<String> result = new ArrayList<>();
				for (int index = 1; index <= columnCount; index++) {
					result.add(input.getString(index));
				}
				resultList.add(result);
			}
		} catch (SQLException e) {System.err.println(e.getClass().getName() + ": " + e.getMessage());}

		return resultList;
	}

	/**
	 * Takes in input a ResultSet and reads the inforamtion into an ArrayList of Ad objects.
	 * 
	 * @param ResultSet
	 * @return ArrayList<Ad> 
	 */

	public ArrayList<Ad> fetchAd(ResultSet input) {
		ArrayList<Ad> result = new ArrayList<>();

		try {
			while (input.next()) {
				BufferedImage bufferedImage = null;
				InputStream fis = null;
				Image picture;
				int id = input.getInt("Id");
				try {														// Trying to read an image from the ResultSet.
					fis = input.getBinaryStream("Picture"); 
					bufferedImage = javax.imageio.ImageIO.read(fis);
					picture = SwingFXUtils.toFXImage(bufferedImage, null);
				} catch (NullPointerException | IOException e) {			// Null is returned when no picture is available,
					picture = new Image("PlaceholderBig.png");			// default placeholder is used in this case.
				}
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
				double rating = Double.parseDouble(input.getString("Rating"));

				Ad ad = new Ad(id,picture,name,gender,species,type,age,description,startDate,endDate,agencyId,agencyName,rating);
				result.add(ad);							// Each iteration of the loop an object is added to the ArrayList.
			}
		} catch (SQLException e) {System.err.println(e.getClass().getName() + ": " + e.getMessage());} 
		
		return result;
	}

	/**
	 * Method for fetching information about agencies for displaying in the application.
	 * 
	 * @param ResultSet
	 * @return ArrayList<AgencyExtended>
	 */

	public ArrayList<Agency> fetchAgency(ResultSet input) {
		ArrayList<Agency> result = new ArrayList<>();
		try {    	
			while (input.next()) {						// This while-loop adds the results to the arrayList.
				int id = input.getInt("ID");
				String name = input.getString("Name");
				String rating = input.getString("AVG(Rating)");
				Agency agency = new Agency(id, name, rating);
				result.add(agency);						// Each iteration of the loop an object is added to the ArrayList.
			}
		} catch (SQLException e) {System.err.println(e.getClass().getName() + ": " + e.getMessage());}
		
		return result;
	}

	/**
	 * Method for fetching extended information about agencies for displaying in the application.
	 * 
	 * @param ResultSet
	 * @return ArrayList<AgencyExtended>
	 */

	public ArrayList<AgencyExt> fetchAgencyExt(ResultSet input) {
		ArrayList<AgencyExt> result = new ArrayList<>();
		try {	
			while (input.next()) {						// This while-loop adds the results to the arrayList.
				int id = input.getInt("ID");
				String name = input.getString("Name");
				String rating = input.getString("AVG(Rating)");
				String email = input.getString("Email");
				String phone = input.getString("Phone");
				String street = input.getString("Street");
				String zip = input.getString("Zip");
				String city = input.getString("City");
				AgencyExt agency = new AgencyExt(id, name, rating, email, phone, street, zip, city);
				result.add(agency);						// Each iteration of the loop an object is added to the ArrayList.
			}
		} catch (SQLException e) {System.err.println(e.getClass().getName() + ": " + e.getMessage());}
		return result;
	}

	/**
	 * Method for creating observable lists using ArrayList<ArrayList<String>>, ordinarily after obtaining the resulting 3D array-
	 * list from a query. This ObservableList has the Select All option, applicable for some ChoiceBoxes.
	 * 
	 * @param ArrayList<ArrayList<String>>
	 * @returns ObservableList<Object>
	 */

	public ObservableList<Object> createSelectAllObservableList(String columnName, ArrayList<ArrayList<String>> input) {
		ObservableList<Object> resultList = FXCollections.observableArrayList(columnName, new Separator(), "Select all" , new Separator());

		for (int i = 0; input.size() > i; i++) {
			ArrayList<String> fetch = input.get(i);
			for (int j = 0; fetch.size() > j; j++) {
				resultList.add(fetch.get(j));
			}
		}
		return resultList;
	}
	
	/**
	 * Additional ObservableList method since not all OBSLists should have the select all option. Otherwise, this method performs the same task.
	 * 
	 * @param ArrayList<ArrayList<String>
	 * @returns ObservableList<Object>
	 */
	
	public ObservableList<Object> createObservableList(String columnName, ArrayList<ArrayList<String>> input) {
		ObservableList<Object> resultList = FXCollections.observableArrayList(columnName, new Separator());

		for (int i = 0; input.size() > i; i++) {
			ArrayList<String> fetch = input.get(i);
			for (int j = 0; fetch.size() > j; j++) {
				resultList.add(fetch.get(j));
			}
		}
		return resultList;
	}
	
	/**
	 * Method for creating observable lists using ArrayList<Ad>, ordinarily after obtaining the resulting array-
	 * list from a query.
	 * 
	 * @param ArrayList<Ad>
	 * @returns ObservableList<Object>
	 */

	public ObservableList<Ad> createObservableList(ArrayList<Ad> input) {
		return FXCollections.observableArrayList(input);
	}

	/**
	 * Closing connection to currently connected database, if one is connected at all. Otherwise, do nothing.
	 * You should always close a connection after you've retrieved information/updated.
	 */

	public void closeConnection() {
		try {								// Firstly checks if the ResultSet, connection and statement are not already closed,
			if (resultSet.isClosed()) {}	// then takes the appropriate measures.
			else {resultSet.close();}
			if (stmt.isClosed()) {} 
			else {stmt.close();}
			if (connect.isClosed()) {} 
			else {this.getConnection().close();}
		}catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} 
		System.out.println(">> Closed DB connection");
	}

	public Connection getConnection() {
		return connect;
	}
}