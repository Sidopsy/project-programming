
/**
 * Object for agencies, basic information for displaying in the application.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class Agency {
	public final String name, logo, rating;
	
	public Agency(String name, String logo, String rating) {
		this.name = name;
		this.logo = logo;
		this.rating = rating;
	}
	
	public String toString() {
		return name + " " + logo + " " + rating;
	}
}
