import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Object for ads, appropriate variables for displaying in the application.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class Ad {
	public final SimpleStringProperty picture, name, gender, species, type, description, startDate, endDate;
	public final SimpleIntegerProperty age;

	public Ad(String picture, String name, String gender, String species, String type, 
				int age, String description, String start, String end) {
		this.picture = new SimpleStringProperty(picture);
		this.name = new SimpleStringProperty(name);
		this.gender = new SimpleStringProperty(gender);
		this.species = new SimpleStringProperty(species);
		this.type = new SimpleStringProperty(type);
		this.age = new SimpleIntegerProperty(age);
		this.description = new SimpleStringProperty(description);
		this.startDate = new SimpleStringProperty(start);
		this.endDate = new SimpleStringProperty(end);
	}
	
	public String toString() {
		return picture + " " + name + " " + gender + " " + species + " " + 
				type + " " + age + " " + description + " " + startDate + " " + endDate;
	}
}
