
/**
 * Object for agencies, basic information for displaying in the application.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class Agency {
	public final String name, logo;
	
	public Agency(String name, String logo) {
		this.name = name;
		this.logo = logo;
	}
	
	public String toString() {
		return this.name + " " + this.logo;
	}
}
