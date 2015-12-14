import java.sql.SQLException;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;

/**
 * This class verifies the integrity of the input email and password. It makes sure that the information corresponds to a user
 * in the database.
 * 
 * @since 2015-12-03
 * @author Madisen Whitfield
 */

public class Membership {
	private static DBobject db = new DBobject();
	
	/**
	 * 
	 * @return
	 */
	public static HBox loginBox(){
		HBox hbox = new HBox();
		Button btnShow = new Button("Go to agency pages");
		TextField tfEmail = new TextField();
		PasswordField pfPassword = new PasswordField();
		Button btnLogin = new Button("Login");
		Alert alert = new Alert(AlertType.INFORMATION);

		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER);

		tfEmail.setPromptText("Enter your email");
		pfPassword.setPromptText("Enter your password");

		btnLogin.setOnAction(e -> {
			String email = tfEmail.getText();
			String password = pfPassword.getText();
			try {
				if ((email.equals("admin")) && (password.equals("admin"))){
					AdminPage.start();
				}
				else if(Membership.verify(email, password)) {
					System.out.println(">> Entered login correct");
					AgencyExt agencyInfo = db.fetchAgencyExt(db.executeQuery("SELECT * FROM AgencyExtended WHERE "
												+ "Email = '" + email + "';")).get(0);
					db.closeConnection();	
					MemberPage.display(agencyInfo);
					
				} else {
				
					System.out.println(">> Entered login incorrect");
					alert.setAlertType(AlertType.ERROR);
					alert.setTitle("Failiure");
					alert.setHeaderText("Login failed");
					alert.setContentText("Information entered was incorrect, please try again.");
					alert.showAndWait();
				}
			} catch (Exception e1) {
				System.err.println(e1.getMessage());
				System.err.println(">> There is most likely a problem with missing ratings, user cannot log in.");
			} finally {
				try {
					if (!db.getConnection().isClosed()) {
						db.closeConnection();
					}
				} catch (SQLException e1) {
					System.err.println(">> Error when closing connection");
				}
			}
		});


		Button btnCreateAccount = new Button("Create account");
		btnCreateAccount.setOnAction(e -> {
			CreateAccount.display();
		});


		btnShow.setOnAction(e -> {
			hbox.getChildren().removeAll(hbox.getChildren());
			hbox.getChildren().addAll(tfEmail, pfPassword, btnLogin, btnCreateAccount);
		});

		hbox.getChildren().add(btnShow);

		return hbox;
	}


	/**
	 * Validation method for input email and password.
	 * 
	 * @param two strings, a representing the email address and b the password.
	 * @return boolean stating if information entered was correct (true) or incorrect.
	 */
	
	public static boolean verify(String a, String b){
		String resultEmail, resultPassword, sql;
		resultEmail = "";
		resultPassword = "";
		sql = "SELECT Email, Password FROM Agencies WHERE Email == '" + a + "' AND Password == '" + b + "';";
		
		try {
			resultEmail = db.fetchResult(db.executeQuery(sql)).get(0).get(0);	// First arrayList, first item
			resultPassword = db.fetchResult(db.executeQuery(sql)).get(0).get(1);// First arrayList, second item
		} catch (Exception e) {
			System.err.println(">> ArrayList<AgencyExt> returned nothing...");
		} finally {
			db.closeConnection();
		}
		
		return((resultEmail.length() > 0) && (resultEmail.equals(a)) && 
			  (resultPassword.length() > 0) && (resultPassword.equals(b)));

	}
}