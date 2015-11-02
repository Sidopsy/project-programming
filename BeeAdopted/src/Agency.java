import javafx.beans.property.SimpleStringProperty;

/**
 * Object for agencies, basic information for displaying in the application.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class Agency {
	public final SimpleStringProperty name, logo, rating;
	
	public Agency(String name, String logo, String rating) {
		this.name = new SimpleStringProperty(name);
		this.logo = new SimpleStringProperty(logo);
		this.rating = new SimpleStringProperty(rating);
	}
	
	public String getName(){
		return name.get();
	}
	
	public String getLogo(){
		return logo.get();
	}
	
	public String getRating(){
		return rating.get();
	}
	
	public String toString() {
		return name + " " + logo + " " + rating;
	}
}
