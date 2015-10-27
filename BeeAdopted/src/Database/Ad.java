package Database;

/**
 * Object for ads, appropriate variables for displaying in the application.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class Ad {
	public final String picture, name, gender, species, type, description, startDate, endDate;
	public final int age;

	public Ad(String picture, String name, String gender, String species, String type, 
			  int age, String description, String start, String end) {
		this.picture = picture;
		this.name = name;
		this.gender = gender;
		this.species = species;
		this.type = type;
		this.age = age;
		this.description = description;
		this.startDate = start;
		this.endDate = end;
	}
	
	public String toString() {
		return this.picture + " " + this.name + " " + this.gender + " " + this.species + " " + 
				this.type + " " + this.age + " " + this.description + " " + this.startDate + " " + this.endDate;
	}
}
