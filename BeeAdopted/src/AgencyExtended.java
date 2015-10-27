
/**
 * Object for agencies, containing extended information about agencies for users to get more detailed information.
 * 
 * Added orgNo as String because of formatting reasons.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class AgencyExtended extends Agency {
	public final String orgNo, email, phone, street, zip, city;
	
	public AgencyExtended(String name, String logo, String orgNo, String email, 
							String phone, String street, String zip, String city) {
		super(name, logo);
		this.orgNo = orgNo;
		this.email = email;
		this.phone = phone;
		this.street = street;
		this.zip = zip;
		this.city = city;
	}
		
	public String toString() {
		return super.toString() + " " + orgNo + " " + email + " " + phone + " " + street + " " + zip + " " + city;
	}
}
