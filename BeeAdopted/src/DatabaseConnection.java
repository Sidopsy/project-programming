
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Separator;

public class DatabaseConnection {

	public static ObservableList<Object> getListFromDatabase(String string) {
		ObservableList<Object> result = 
				FXCollections.observableArrayList(
						string,
						new Separator(),
			        "Option 1",
			        "Option 2",
			        "Option 3");
		return result;
	}
	
	public static String[] getAgencies(String location){
		Connection c = null;
	    Statement stmt = null;
	    String selectStatement = "SELECT Name, Logo FROM Agencies = Addresses WHERE City = " + location +";";
	    String[] ad = new String[2]; 	
	    
	    try {
	    	Class.forName("org.sqlite.JDBC"); // Loading the driver!
	    	c = DriverManager.getConnection("jdbc:sqlite:BeeHiveTest"); // Establishing a connection!
	    	c.setAutoCommit(false);
	        System.out.println("Opened database successfully");
	        System.out.println();
	        
	        
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery(selectStatement);
	        
	        while (rs.next()) {
	           String name = rs.getString("Name");
	           String logo = rs.getString("Logo");
	           
	           System.out.println("Name = " + name);
	           ad[0] = name;
	           
	           System.out.println("Gender = " + logo);
	           ad[1] = logo;
	           
	           System.out.println();
	        }
	        rs.close();
	        stmt.close();
	        c.close();
	        
	        System.out.println("End of DB extraction");
	    	
	    } catch ( Exception e ) { 
	    	System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    	System.exit(0);
	    }
	    
	    return ad;
	}	
	

	public static String[] getAd(int adID) {
//		Connection c = null;
//	    Statement stmt = null;
//	    String selectStatement = "SELECT * FROM Listings WHERE ID = " + adID +";";
//	    String[] ad = new String[3]; 	// 3 is an arbitrary number, just used to get 3 items of info from the DB. This should be
//	    								// an array of our own defined objects when we're done!
//	    
//	    try {
//	    	Class.forName("org.sqlite.JDBC"); // Loading the driver!
//	    	c = DriverManager.getConnection("jdbc:sqlite:BeeHiveTest"); // Establishing a connection!
//	    	c.setAutoCommit(false);
//	        System.out.println("Opened database successfully");
//	        
//	        DatabaseMetaData md = c.getMetaData();
//	        ResultSet test = md.getTables(null, null, "%", null);
//	        while (test.next()) {
//	          System.out.println(test.toString());
//	        }
//	        System.out.println();
//	        
//	        // We need to think of how to extract info from the DB, I don't seem to be able to concatenate string to what is inside
//	        // the citation marks below... If i remove the 1 and replace it with + adID + ";" it does not work! Maybe we wont make
//	        // searches that way, but it's worth a thought.
//	        
//	        stmt = c.createStatement();
//	        ResultSet rs = stmt.executeQuery(selectStatement);
//	       
//	        
//	        while (rs.next()) {
//	           String name = rs.getString("name");
//	           String gender = rs.getString("gender");
//	           String species = rs.getString("species");
//	           
//	           System.out.println("Name = " + name);
//	           ad[0] = name;
//	           
//	           System.out.println("Gender = " + gender);
//	           ad[1] = gender;
//	           
//	           System.out.println("Species = " + species);
//	           ad[2] = species;
//	           
//	           System.out.println();
//	        }
//	        rs.close();
//	        stmt.close();
//	        c.close();
//	        
//	        System.out.println("End of DB extraction");
//	    	
//	    } catch ( Exception e ) { 
//	    	System.err.println(e.getClass().getName() + ": " + e.getMessage());
//	    	System.exit(0);
//	    }
//	    
//	    return ad;
		return null;
	}

}
