
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
 * This is an object that will handle the Database communication. There are also static methods to handle ResultSet metaData for
 * generic result handling.
 * 
 * @since 2015-11-16
 * @author M��ns Th��rnvik
 */

public class DBobject {
	private static Connection connect = null;
	private static PreparedStatement stmt = null;
	private static ResultSet resultSet = null;
	private final String dbType;
	private final String dbName;
	private final String dbDriver;

	/**
	 * Just an empty constructor call to load the driver.
	 */

	public DBobject() {
		this.dbType = "jdbc:sqlite:";
		this.dbName = "BeeHive";
		//this.dbName = "//drive.google.com/file/d/0B8ZOX8oToxRGdnNsMi16ZDl2bVU/";
		this.dbDriver = "org.sqlite.JDBC";

		System.out.println(">> Loading driver");
		try {
			Class.forName(dbDriver);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Setting the connection to the database based on the dbName and Type, foreign keys are also activated here using the SQLiteConfig.
	 * 
	 */

	public void setConnection() {
		System.out.println(">> Setting connection to DB");
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
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		if (foreignKeyStatus == 1) {
			System.out.println(">> Foreign keys activated!");
			return true;
		} else {
			System.out.println(">> Foreign keys not active.");
			return false;
		}
	}

	/**
	 * Execute queries to the database.
	 * 
	 * @throws SQLException
	 */

	public ResultSet executeQuery(String query) {
		setConnection();
		System.out.println(">> Executing query");

		try {
			stmt = connect.prepareStatement(query);
			resultSet = stmt.executeQuery();

			connect.commit();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return resultSet;
	}

	/**
	 * Execute single updates to the database, use batch if more than one insert, delect or update is to be carried out at one
	 * time.
	 * 
	 * @param String update
	 * @throws SQLException
	 */

	public void executeUpdate(String update){
		setConnection();
		System.out.println(">> " + update.substring(0, 10));

		try {
			stmt = connect.prepareStatement(update);
			stmt.executeUpdate();

			connect.commit();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} 

		closeConnection();
	}

	/**
	 * 
	 */

	public int updatePicture(String update, byte[] parameters, int agencyID) {
		int result = 0;
		setConnection();
		System.out.println(">> Picture update " + update.substring(7, 10) + ": " + agencyID);
		try {
			stmt = connect.prepareStatement(update);
			stmt.setBytes(1, parameters);
			stmt.setInt(2, agencyID);
			result = stmt.executeUpdate();
			connect.commit();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		closeConnection();

		return result;
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
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return resultList;
	}

	/**
	 * 
	 * @param input
	 * @return
	 * @throws IOException 
	 */

	public ArrayList<Ad> fetchAd(ResultSet input) {
		ArrayList<Ad> result = new ArrayList<>();

		try {
			while (input.next()) {
				BufferedImage bufferedImage = null;
				InputStream fis = null;
				Image picture;
				int id = input.getInt("Id");
				try {
					fis = input.getBinaryStream("Picture");  //It happens that the 3rd column in my database is where the image is stored (a BLOB)
					bufferedImage = javax.imageio.ImageIO.read(fis);  //create the BufferedImaged
					picture = SwingFXUtils.toFXImage(bufferedImage, null);
				} catch (NullPointerException e) {
					System.err.println(">> No picture available for this advertisement");
					picture = new Image("PlaceholderSmall.png");
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
				result.add(ad);	// Each iteration of the loop an object is added to the ArrayList.
			}
		} catch (SQLException | IOException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} 
		return result;
	}

	/**
	 * Method for fetching information about agencies for displaying in the application.
	 * 
	 * @param input
	 * @return ArrayLisy<AgencyExtended>
	 */

	public ArrayList<Agency> fetchAgency(ResultSet input) {
		ArrayList<Agency> result = new ArrayList<>();
		try {    	
			while (input.next()) {											// This while-loop adds the results to the arrayList.
				int id = input.getInt("ID");
				String name = input.getString("Name");
				String rating = input.getString("AVG(Rating)");
				String logo = input.getString("Logo");
				Agency agency = new Agency(id, logo, name, rating);
				result.add(agency);	// Each iteration of the loop an object is added to the ArrayList.
			}
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return result;
	}

	/**
	 * Method for fetching extended information about agencies for displaying in the application.
	 * 
	 * @param input
	 * @return ArrayLisy<AgencyExtended>
	 */

	public ArrayList<AgencyExt> fetchAgencyExt(ResultSet input) {
		ArrayList<AgencyExt> result = new ArrayList<>();
		try {	
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
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return result;
	}

	/**
	 * Method for creating observable lists using ArrayList<ArrayList<String>>, ordinarily after obtaining the resulting 3D array-
	 * list from a query.
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
	 * 
	 * @throws SQLException
	 */

	public void closeConnection() {
		try {
			if (resultSet.isClosed()) {}
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