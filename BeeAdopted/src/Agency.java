
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Object for agencies, basic information for displaying in the application.
 * 
 * @author Maans Thoernvik
 * @since 10-26-15
 */

public class Agency {
	private final SimpleStringProperty name, rating;
	private final SimpleIntegerProperty id;
	
	public Agency(int id, String name, String rating) {
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.rating = new SimpleStringProperty(rating);
	}
	
	public int getID() {
		return id.get();
	}
	
	public String getName() {
		return name.get();
	}
		
	public String getRating() {
		return rating.get();
	}
	
	public String toString() {
		return name + " " + rating;
	}
}
