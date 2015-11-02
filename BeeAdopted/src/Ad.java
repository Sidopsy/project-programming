import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Object for ads, appropriate variables for displaying in the application.
 * 
 * Getters are available for all class variables.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class Ad {
	private final SimpleStringProperty picture, name, gender, species, type, description, startDate, endDate;
	private final SimpleIntegerProperty age;

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
	
	public String getPicture(){
		return picture.get();
	}
	
	public String getName(){
		return name.get();
	}
	
	public String getGender(){
		return gender.get();
	}
	
	public String getSpecies(){
		return species.get();
	}
	
	public String getType(){
		return type.get();
	}
	
	public int getAge(){
		return age.get();
	}
	
	public String getDescription(){
		return description.get();
	}
	
	public String getStartDate(){
		return startDate.get();
	}
	
	public String getEndDate(){
		return endDate.get();
	}
		
	public String toString() {
		return picture + " " + name + " " + gender + " " + species + " " + 
				type + " " + age + " " + description + " " + startDate + " " + endDate;
	}
}
