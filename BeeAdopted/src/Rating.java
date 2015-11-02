import javafx.beans.property.SimpleStringProperty;

/**
 * Object for ratings, appropriate variables for displaying in the application. Name is for the Agency.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class Rating {
	private final SimpleStringProperty name, rating, comment;
	
	public Rating(String name, String rating, String comment) {
		this.name = new SimpleStringProperty(name);
		this.rating = new SimpleStringProperty(rating);
		this.comment = new SimpleStringProperty(comment);
	}
	
	public String getName(){
		return name.get();
	}
	
	public String getRating(){
		return rating.get();
	}
	
	public String getComment(){
		return comment.get();
	}
		
	public String toString() {
		return name + " " + rating + " " + comment;
	}
}
