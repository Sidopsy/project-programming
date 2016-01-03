package Object;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.controlsfx.control.Rating;

/**
 * Object for ratings, appropriate variables for displaying in the application. Name is for the Agency.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class RatingExt extends Rating{
	private final SimpleIntegerProperty agency;
	private final SimpleStringProperty comment;
	private final SimpleDoubleProperty rating;
	
	public RatingExt(int agencyId, double rating) {
		super();
		this.setPartialRating(true);
		this.setRating(rating);
		this.agency = new SimpleIntegerProperty(agencyId);
		this.rating = new SimpleDoubleProperty(rating);
		this.comment = new SimpleStringProperty("");
	}
	
	public RatingExt(int agencyId, double rating, String comment) {
		super();
		this.setPartialRating(true);
		this.setRating(rating);
		this.agency = new SimpleIntegerProperty(agencyId);
		this.rating = new SimpleDoubleProperty(rating);
		this.comment = new SimpleStringProperty(comment);
	}
	
	public int getName(){
		return agency.get();
	}
	
	public String getComment(){
		return comment.get();
	}
	
	public String toString() {
		return agency + " " + rating + " " + comment;
	}
}
