import javafx.beans.property.SimpleStringProperty;

/**
 * Object for agencies, containing extended information about them in order for users to get more detailed information.
 * 
 * @author Maans Thoernvik
 * @since 10-26-15
 */

public class AgencyExt extends Agency {
	private final SimpleStringProperty email, phone, street, zip, city;
	
	public AgencyExt(int id, String name, String rating, String email, 
						  String phone, String street, String zip, String city) {
		super(id, name, rating);
		this.email = new SimpleStringProperty(email);
		this.phone = new SimpleStringProperty(phone);
		this.street = new SimpleStringProperty(street);
		this.zip = new SimpleStringProperty(zip);
		this.city = new SimpleStringProperty(city);
	}
	
	public int getID() {
		return super.getID();
	}
	
	public String getName() {
		return super.getName();
	}
	
	public String getRating() {
		return super.getRating();
	}
	
	public String getEmail() {
		return email.get();
	}
	
	public String getPhone() {
		return phone.get();
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
		return super.toString() + " " + email + " " + phone + " " + street + " " + zip + " " + city;
	}
}