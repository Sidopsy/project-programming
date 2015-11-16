import javafx.beans.property.SimpleStringProperty;

/**
 * Object for showing appropriate information about agencies addresses.
 * 
 * @author MTs
 */

public class Address {
	private final SimpleStringProperty street, zip, city;
	
	public Address(String street, String zip, String city) {
		this.street = new SimpleStringProperty(street);
		this.zip = new SimpleStringProperty(zip);
		this.city = new SimpleStringProperty(city);
	}
	
	public String getStreet() {
		return street.get();
	}
	
	public String getZip() {
		return zip.get();
	}
	
	public String getCity() {
		return city.get();
	}
	
	public String toString() {
		return street + " " + zip + " " + city;
	}
	
}
	