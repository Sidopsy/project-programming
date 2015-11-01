
/**
 * Object for ratings, appropriate variables for displaying in the application.
 * 
 * TODO change from int to char.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class Rating {
	public final String name, rating, comment;
	
	public Rating(String name, String rating, String comment) {
		this.name = name;
		this.rating = rating;
		this.comment = comment;
	}
	
	public String toString() {
		return name + " " + rating + " " + comment;
	}
}
