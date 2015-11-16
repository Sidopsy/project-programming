import javafx.beans.property.SimpleStringProperty;

/**
 * Object for ratings, appropriate variables for displaying in the application. Name is for the Agency.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class Rating {
	private final SimpleStringProperty agency, rating, comment;
	
	public Rating(String name, String rating, String comment) {
		this.agency = new SimpleStringProperty(name);
		this.rating = new SimpleStringProperty(rating);
		this.comment = new SimpleStringProperty(comment);
	}
	
	public String getName(){
		return agency.get();
	}
	
	public String getRating(){
		return rating.get();
	}
	
	public String getComment(){
		return comment.get();
	}
		
	public String toString() {
		return agency + " " + rating + " " + comment;
	}
}
