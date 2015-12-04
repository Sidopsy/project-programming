
/**
 * This class verifies the integrity of the input email and password. It makes sure that the information corresponds to a user
 * in the database.
 * 
 * @author Madisen Whitfield
 */

public class Membership {
	private static DBobject db = new DBobject();

	/**
	 * Validation method for input email and password.
	 * 
	 * @param two strings, a representing the email address and b the password.
	 * @return boolean stating if information entered was correct (true) or incorrect.
	 */
	
	public static boolean verify(String a, String b){
		String resultEmail, resultPassword, sql;
		resultEmail = "";
		resultPassword = "";
		sql = "SELECT Email, Password FROM Agencies WHERE Email == '" + a + "' AND Password == '" + b + "';";
		
		try {
			resultEmail = db.fetchResult(db.executeQuery(sql)).get(0).get(0);	// First arrayList, first item
			resultPassword = db.fetchResult(db.executeQuery(sql)).get(0).get(1);// First arrayList, second item
			db.closeConnection();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}		
		
		return((resultEmail.length() > 0) && (resultEmail.equals(a)) && 
			  (resultPassword.length() > 0) && (resultPassword.equals(b)));

	}
}