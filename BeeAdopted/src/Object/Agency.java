package Object;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Object for agencies, basic information for displaying in the application.
 * As of now, these objects are not in use.
 * 
 * @author Maans Thoernvik
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
