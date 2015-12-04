

import java.awt.Checkbox;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.sqlite.SQLiteConfig;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class Membership {
    public static Stage window;
    public static Button login;
    public static Boolean isCorrect = false;
    
	private static int id = 0;
	private static String logo = "";
	private static String name = "";
	private static String rating = "";
	private static String email = "";
	private static String phone = "";
	private static String street = "";
	private static String zip = "";
	private static String city = "";
    	
    static DBobject db = new DBobject();
	public static AgencyExt result = new AgencyExt(id, logo, name, rating, email, phone, street, zip, city);
	

	public static boolean verify(String a, String b){
		System.out.println(">> Executing query bitchez");
		String query = "SELECT Email, Password FROM Agencies WHERE Email == '" + a + "' AND Password == '" + b + "';";
		System.out.println(query);
		String resultEmail = "";
		String resultPass = "";
		String enteredEmail = a;
		String enteredPass = b;
		try {
			resultEmail = db.fetchResult(db.executeQuery(query)).get(0).get(0);
			resultPass = db.fetchResult(db.executeQuery(query)).get(0).get(1);
			db.closeConnection();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}		
		System.out.println(resultEmail);
		System.out.println(a);
		System.out.println(resultPass);
		System.out.println(b);
		return((a.length() > 0) && (resultEmail.equals(a)) && 
				((b.length()) > 0) && (resultPass.equals(b)));
		
}

	public static void display() {
	
		//AgencyExt result = new AgencyExt(id, logo, name, rating, email, phone, street, zip, city);
		
		window = new Stage();
		window.setTitle("BeeAdopted - Login");

		//GridPane with 10px padding around edge
		GridPane membership = new GridPane();
		membership.setPadding(new Insets(10, 10, 10, 10));
		membership.setVgap(8);
		membership.setHgap(10);

		//Name Label - constrains use (child, column, row)
		Label nameLabel = new Label("Username:");
		GridPane.setConstraints(nameLabel, 0, 0);

		//Name Input
		TextField nameInput = new TextField();
		(nameInput).setPromptText("username");
		GridPane.setConstraints(nameInput, 1, 0);
		
	        //Login Button
	        login = new Button("Log In");
	        GridPane.setConstraints(login, 1, 3);
	
		//Password Label
		Label passwordLabel = new Label("Password:");
		GridPane.setConstraints(passwordLabel, 0, 1);

		//Password Input
		PasswordField passwordInput = new PasswordField();
		(passwordInput).setPromptText("password");
		GridPane.setConstraints(passwordInput, 1, 1);

		//Login Button
		login = new Button("Log In");
		GridPane.setConstraints(login, 1, 2);
		
		//login.setOnAction(e -> verify(nameInput, passwordInput));
//		login.setOnAction(e -> {
//			if(Membership.verify(nameInput, passwordInput)) {
//				
//				String query = "SELECT Agencies.ID, Logo, Name, AVG(Rating), Email, Phone, Street, ZIP, City FROM "
//						   + "Agencies, Addresses, Ratings WHERE "
//						   + "Email = '" + nameInput.getText() + "'";
//				//String enteredName = nameInput.getText();
//				//AgencyExt result = new AgencyExt(id, logo, name, rating, email, phone, street, zip, city);
//				
//				
//				try{ result.equals(db.fetchResult(db.executeQuery(query)).get(0).get(0));
//					db.closeConnection();
//				}catch(Exception e1){
//					System.err.println(e.getClass().getName() + ": " + e1.getMessage());
//				};
//				
//				MemberPage.display();
//				
//			}
//			
//			
//		});

		//Create an Account
		Button createAccount = new Button("Create an Account");
		GridPane.setConstraints(createAccount, 1, 3);
		createAccount.setOnAction(e -> CreateAccount.display());


		//Add everything to membership grip layout
		membership.getChildren().addAll(nameLabel, nameInput, passwordLabel, passwordInput, login, createAccount);

		// Membership.verify(nameInput, passwordInput);
		Scene scene = new Scene(membership, 300, 200);
		window.setScene(scene);
		window.show();
		

	}
}

