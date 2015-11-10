
import java.sql.*;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Separator;

/**
 * This is an object that will handle the Database communication.
 * 
 * @author MTs
 */

public class dbCommands {
	public Connection connect = null;
	public PreparedStatement stmt = null;
	public final String dbType;
	public final String dbName;
	public final String dbDriver;
	public final int dbTimeOut;

	/*
	 * Just an empty constructor call to start the connection with the database.
	 */

	public dbCommands() {
		this.dbType = "jdbc:sqlite:";
		this.dbName = "BeeHive2";
		this.dbDriver = "org.sqlite.JDBC";
		this.dbTimeOut = 30;

		setConnection();
	}

	/**
	 * 
	 * 
	 * @return
	 */

	public void setConnection() {
		try {
			Class.forName(dbDriver);

			connect = DriverManager.getConnection(dbType + dbName);
			connect.setAutoCommit(false);

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Execute queries to the database.
	 * 
	 * @throws Exception
	 */

	public ResultSet executeQuery() throws Exception {
		return stmt.executeQuery();
	}

	/**
	 * Executes updates to the database.
	 * 
	 * @throws Exception
	 */

	public void executeUpdate() throws Exception {
		stmt.executeBatch();
	}

	public String[] metaDataNames(ResultSet rs) {
		String sArr[] = null;
		try {
			ResultSetMetaData rm = rs.getMetaData();
			String sArray[] = new String[rm.getColumnCount()];
			for (int ctr = 1; ctr <= sArray.length; ctr++) {
				String s = rm.getColumnName(ctr);
				sArray[ctr - 1] = s;
			}
			return sArray;
		} catch (Exception e) {
			System.out.println(e);
			return sArr;
		}
	}

	public String[] metaDataTypes(ResultSet rs) {
		String sArr[] = null;
		try {
			ResultSetMetaData rm = rs.getMetaData();
			String sArray[] = new String[rm.getColumnCount()];
			for (int ctr = 1; ctr <= sArray.length; ctr++) {
				String s = rm.getColumnTypeName(ctr);
				sArray[ctr - 1] = s;
			}
			return sArray;
		} catch (Exception e) {
			System.out.println(e);
			return sArr;
		}
	}

	/**
	 * Closing connection to currently connected database, if one is connected
	 * at all. Otherwise, do nothing.
	 */

	public void closeConnection() {
		if (stmt != null) {
			try {
				stmt.close();

			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				System.exit(0);
			}
		} else {
			System.out.println("No statement established, cannot close.");
		}

		if (connect != null) {
			try {
				connect.close();

			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				System.exit(0);
			}
		} else {
			System.out.println("No database connected, cannot close.");
		}
	}
}
