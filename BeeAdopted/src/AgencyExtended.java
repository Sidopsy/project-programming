
/**
 * Object for agencies, containing extended information about agencies for users to get more detailed information.
 * 
 * Added orgNo as String because of formatting reasons.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class AgencyExtended extends Agency {
	public final String email, phone, street, zip, city;
	
	public AgencyExtended(String name, String logo, String rating, String email, 
							String phone, String street, String zip, String city) {
		super(name, logo, rating);
		this.email = email;
		this.phone = phone;
		this.street = street;
		this.zip = zip;
		this.city = city;
	}
		
	public String toString() {
		return super.toString() + " " + email + " " + phone + " " + street + " " + zip + " " + city;
	}
}
