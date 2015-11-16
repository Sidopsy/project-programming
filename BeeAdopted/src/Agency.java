
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Object for agencies, basic information for displaying in the application.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class Agency {
	private final SimpleStringProperty id, logo, name, rating;
	
	public Agency(String id, String logo, String name, String rating) {
		this.id = new SimpleStringProperty(id);
		this.logo = new SimpleStringProperty(logo);
		this.name = new SimpleStringProperty(name);
		this.rating = new SimpleStringProperty(rating);
	}
	
	public String getID() {
		return id.get();
	}
	
	public String getLogo() {
		return logo.get();
	}
	
	public String getName() {
		return name.get();
	}
		
	public String getRating() {
		return rating.get();
	}
	
	public String toString() {
		return logo + " " + name + " " + rating;
	}
}
