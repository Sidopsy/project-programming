
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBtests {
	
	public static void main(String[] args) throws SQLException {
		String sql = "SELECT * FROM Ads;";		
		DBobject db = new DBobject();
		
		// Trying it all, creating a resultset with a query and seeing what I can create with the new shownObject.
		ResultSet result = db.executeQuery(sql);
		ArrayList<ShownObject> list = DBobject.createAd(result);
		
		for (ShownObject print : list) {
			System.out.println(print);
		}
		
		// Trying out the metaDataHandler
//		ResultSet rs = db.executeQuery(sql);
//		String[] names = CreateObjects.metaDataNames(rs);
//		String[] types = CreateObjects.metaDataTypes(rs);
//		
//		for (int i = 0; i < names.length; i++) {
//			System.out.println(names[i]);
//			System.out.println(types[i]);
//		}
//		db.closeConnection();

			
		// Trying out multiple >queries< in a row to see if it works with PreparedStatements
//		ResultSet rs = db.executeQuery(sql);
//		
//		while (rs.next()) {
//			int id = rs.getInt("ID");
//			System.out.println(id);
//		}
//		
//		sql = "SELECT * FROM Ads;";
//		
//		rs = db.executeQuery(sql);
//		
//		while (rs.next()) {
//			int id = rs.getInt("ID");
//			System.out.println(id);
//		}
		
		// Trying our paramererized queries for re-usability.
//		sql = "SELECT * FROM Addresses WHERE City = ?;";
//		String parameter = "Stockholm";
//		db.stmt = db.connect.prepareStatement(sql);
//		db.stmt.setString(1, parameter);
//		ResultSet rs = db.stmt.executeQuery();
//		
//		while (rs.next()) {
//			int ID = rs.getInt("AgencyID");
//			String street = rs.getString("Street");
//			String ZIP = rs.getString("ZIP");
//			String City = rs.getString("City");
//			System.out.println("" + ID + street + ZIP + City);
//		}
//		db.closeConnection();
		
		
		// This code determines if foreign keys have been activated for the current transaction.
//		db.foreignKeysOn();
//		db.closeConnection();
			
	}
}