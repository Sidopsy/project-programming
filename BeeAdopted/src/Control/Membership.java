package Control;
import java.sql.SQLException;

import Object.AgencyExt;
import Object.Database;
import View.AdminPage;
import View.CreateAccount;
import View.MemberPage;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;

/**
 * Loginbox is a hidden function on the start page. Pressing "Go to agency pages" shows the user login field for their
 * email and password, as well as an option to create a new account. This class also verifies the integrity of the input
 * email and password. It makes sure that the information corresponds to a user in the database.
 * 
 * @author Madisen Whitfield
 * @refactoredBy Maans Thoernvik & Mattias Landkvist * Added some styling and migrated verification method into same class file.
 */

public class Membership {
	private static Database db = new Database();
	
	/**
	 * This method created the loginBox where a user can enter his or her email address and password. There is also an option
	 * for creating a new account.
	 * 
	 * @return HBox containing login fields and create account button.
	 */
	
	public static HBox loginBox(){
		HBox hbox = new HBox();
		Button btnShow, btnLogin, btnCreateAccount;
		TextField tfEmail = new TextField();
		PasswordField pfPassword = new PasswordField();
		Alert alert = new Alert(AlertType.INFORMATION);
		
		hbox.getStyleClass().add("hbox");
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER);

		// Below is every action taken when login is pressed.
		btnLogin = new Button("Login");
		btnLogin.setOnAction(e -> {
			String email = tfEmail.getText();		// retrieving text field information.
			String password = pfPassword.getText();
			
			try {
				if ((email.equals("admin")) && (password.equals("admin"))){		// Start of admin account login, this page was
					AdminPage.start();											// never finished.
				} else if(Membership.verify(email, password)) {
					AgencyExt agencyInfo = db.fetchAgencyExt(db.executeQuery("SELECT * FROM AgencyExtended WHERE "
												+ "Email = '" + email + "';")).get(0);
					db.closeConnection();	
					
					MemberPage.display(agencyInfo);			// Using the member info retrieved this opens the Member page
				} else {
					alert.setAlertType(AlertType.ERROR);	// An error is displayed if the information entered was wrong.
					alert.setTitle("Failiure");
					alert.setHeaderText("Login failed");
					alert.setContentText("Information entered was incorrect, please try again.");
					alert.showAndWait();
				}
			} catch (Exception e1) {
				System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
			} finally {
				try {
					if (!db.getConnection().isClosed()) {db.closeConnection();}	// Added finally block since the connection locked sometimes.
				} catch (SQLException e1) {
					System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
				}
			}
		});

		btnCreateAccount = new Button("Create account");
		btnCreateAccount.setOnAction(e -> {
			CreateAccount.display();
		});
		
		// This button shows the login fields once pressed.
		btnShow = new Button("Go to agency pages");
		btnShow.setOnAction(e -> {
			hbox.getChildren().removeAll(hbox.getChildren());
			hbox.getChildren().addAll(tfEmail, pfPassword, btnLogin, btnCreateAccount);
		});
		
		tfEmail.setPromptText("Enter your email");
		pfPassword.setPromptText("Enter your password");

		hbox.getChildren().add(btnShow);

		return hbox;
	}

	/**
	 * Validation method for input email and password.
	 * 
	 * @param two strings, "a" representing the email address and "b" the password.
	 * @return boolean stating if information entered was correct (true) or incorrect.
	 */
	
	public static boolean verify(String a, String b){
		String resultEmail, resultPassword, sql;
		resultEmail = "";
		resultPassword = "";
		sql = "SELECT Email, Password FROM Agencies WHERE Email == '" + a + "' AND Password == '" + b + "';";
		
		try {
			resultEmail = db.fetchResult(db.executeQuery(sql)).get(0).get(0);	// First arrayList, finding the email
			resultPassword = db.fetchResult(db.executeQuery(sql)).get(0).get(1);// First arrayList, finding the password
		} catch (Exception e) {
			System.err.println(">> ArrayList<AgencyExt> returned nothing...");
		} finally {
			db.closeConnection();
		}
		
		// We needed to add length() > 0 since the users could otherwise enter nothing and would then get an error.
		return((resultEmail.length() > 0) && (resultEmail.equals(a)) && 
			  (resultPassword.length() > 0) && (resultPassword.equals(b)));
	}
}