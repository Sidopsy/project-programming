
/**
 * An object solely for the purpose of tracking statement parameters sent to the database.
 * 
 * When using PreparedStatements queries can be sent with question marks in pace of the condition values, these conditions
 * are intended to be saved in one of these objects and then read in order of their occurence, for example:
 * 
 * SELECT * FROM Table1 WHERE Name = ?;
 * dbParam object for the above has to have 1 as its position: (1, "Molly"). This will replace the question mark with Molly.
 * 
 * @author MTs
 */

public class dbParams {
	public int position;
	public String parameter;
	
	public dbParams(int pos, String param) {
		this.position = pos;
		this.parameter = param;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public String getParameter() {
		return this.parameter;
	}
	
	public String toString() {
		return position + ", " + parameter;
	}
}
