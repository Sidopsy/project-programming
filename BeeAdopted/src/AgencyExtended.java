
/**
 * Object for agencies, containing extended information about agencies for users to get more detailed information.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class AgencyExtended extends Agency {
	public final int orgNo;
	public final String email;
	public final String phone;
	
	public AgencyExtended(String name, String logo, int orgNo, String email, String phone) {
		super(name, logo);
		this.orgNo = orgNo;
		this.email = email;
		this.phone = phone;
	}
}
