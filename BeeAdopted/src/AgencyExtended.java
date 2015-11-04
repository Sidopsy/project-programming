import javafx.beans.property.SimpleStringProperty;

/**
 * Object for agencies, containing extended information about agencies for users to get more detailed information.
 * 
 * Added orgNo as String because of formatting reasons.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class AgencyExtended extends Agency {
	private final SimpleStringProperty email, phone, street, zip, city;
	
	public AgencyExtended(int id, String logo, String name, String rating, String email, 
							String phone, String street, String zip, String city) {
		super(id, logo, name, rating);
		this.email = new SimpleStringProperty(email);
		this.phone = new SimpleStringProperty(phone);
		this.street = new SimpleStringProperty(street);
		this.zip = new SimpleStringProperty(zip);
		this.city = new SimpleStringProperty(city);
	}
	
	// TODO get the super calls to work!
	
	public int getID(){
		return super.getID();
	}
	
	public String getLogo(){
		return super.getLogo();
	}
	
	public String getName(){
		return super.getName();
	}
	
	public String getRating(){
		return super.getRating();
	}
	
	public String getEmail(){
		return email.get();
	}
	
	public String getPhone(){
		return phone.get();
	}
	
	public String getStreet(){
		return street.get();
	}
	
	public String getZip(){
		return zip.get();
	}
	
	public String getCity(){
		return city.get();
	}
			
	public String toString() {
		return super.toString() + " " + email + " " + phone + " " + street + " " + zip + " " + city;
	}
}
