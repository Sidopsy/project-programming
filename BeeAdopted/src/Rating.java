
/**
 * Object for ratings, appropriate variables for displaying in the application.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class Rating {
	public final int rating;
	public final String comment;
	
	public Rating(int rating, String comment) {
		this.rating = rating;
		this.comment = comment;
	}
	
	public String toString() {
		return this.rating + " " + this.comment;
	}
}
