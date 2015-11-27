

import java.awt.Checkbox;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

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


<<<<<<< HEAD
    public static Stage window;
    public static Button login;
    public static Boolean isCorrect = false;

   
    
    
    public static void verifyLogin(TextField a, TextField b){
=======
	public static Stage window;
	public static Button login;
	// public static TextField nameInput;
	// public static TextField passwordInput;


	//public static Button LogInHere(Button b, TextField nameInput, TextField passwordInput){
	//login = new Button("Log In");
	//GridPane.setConstraints(login, 1, 2);
	//add action Listener, button click shit.
	//login.addEventHandler(login, handle());



	//verify(nameInput, passwordInput);
	//else 
	//return login;
	//}




	public static void verify(TextField a, TextField b){

		System.out.println("Verify is Working!");

		if  ((a.getText() == "") && (b.getText() == "")) {


			login.setOnAction(e -> InputPage.display());
			System.out.println("It's alive!");
			window.close();
		}
		else {
			window.close();
		}
	}

	/*(a.getText() == "beta@gmail.com") || 
    //}
    
      
  
    
    public static void verify(TextField a, TextField b){
>>>>>>> branch 'master' of https://github.com/Sidopsy/project-programming.git
		
    System.out.println("Verify is Working!");
   
    if  ((a.getText() == " ") && (b.getText() == "")) {
    		
    		
    		login.setOnAction(e -> InputPage.start());
    	    System.out.println("It's alive!");
    	    window.close();
    	}
    else {
    	window.close();
    }
    }
    
<<<<<<< HEAD
   
	
=======
    /*(a.getText() == "beta@gmail.com") || 
>>>>>>> branch 'master' of https://github.com/Sidopsy/project-programming.git
	(a.getText() == "charlie@gmail.com") ||
	(a.getText() == "delta@gmail.com") ||
	(a.getText() == "echo@gmail.com") ||
	(a.getText() == "omega@gmail.com")*/
>>>>>>> branch 'master' of https://github.com/Sidopsy/project-programming.git

	public static void start() {
		// TODO Auto-generated method stub

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

<<<<<<< HEAD
	        //Password Input
	        PasswordField passwordInput = new PasswordField();
	        (passwordInput).setPromptText("password");
	        GridPane.setConstraints(passwordInput, 1, 1);
	        
	        
	       
	      
	        //Login Button
	        login = new Button("Log In");
	        GridPane.setConstraints(login, 1, 3);
	        
	        //login.setOnAction(e -> verify(nameInput, passwordInput));
	        //login.setOnAction(e -> MemberPage.memberPage());
	        	//verify(nameInput,passwordInput);
	        	//if(isCorrect == true){
	        	//InputPage.display();
	        	
	        	/*try {String query = "select Email, Password from Agencies where Email=? and Password=abc123 ";
	        	PreparedStatement pst = (PreparedStatement) db.fetchResult((db.executeQuery(query)));
	        		
	        		//ArrayList<String> pst = db.fetchResult((db.executeQuery(query))).get(0);
	        		
	        		pst.setString(0,nameInput.getText());
	        		pst.setString(1, passwordInput.getText());
	        		ResultSet rs = pst.executeQuery();
	        		
	        		int count = 0;
	        		while(rs.next()) {
	        			count += count;
	        		}
	        		if (count == 1)	{
	        			JOptionPane.showMessageDialog( null, "Usename and Password is correct");
	        			login.setOnAction(e2 -> MemberPage.memberPage());
	        		}
	        		else if (count >1){
	        			JOptionPane.showMessageDialog(null, "Duplicate Username and Password");
	        		}
	        		else{
	        			JOptionPane.showMessageDialog( null, "Usename and Password is not correct. Please try again.");
	        		}
	        		rs.close();
	        		pst.close();
	        		
	        		}catch (Exception e1){
	        		 JOptionPane.showMessageDialog( null, e1);
	        	 }
	        	
	        	
	        	}
	        	
	        
	);*/
	        
	        //Create an Account
	        Button createAccount = new Button("Create an Account");
	        GridPane.setConstraints(createAccount, 1, 4);
	    	createAccount.setOnAction(e -> CreateAccount.go());
	    	
	    	
	       
	        
	        //Add everything to membership grid layout
	        membership.getChildren().addAll(nameLabel, nameInput, 
	        				passwordLabel, passwordInput, login, 
	        				createAccount);
	        
	       // Membership.verify(nameInput, passwordInput);
	        Scene scene = new Scene(membership, 300, 200);
	        window.setScene(scene);
	        window.show();

	        login.setOnAction(e -> MemberPage.memberPage());

	        
	        
	    }
			
=======
		//Password Label
		Label passwordLabel = new Label("Password:");
		GridPane.setConstraints(passwordLabel, 0, 1);

		//Password Input
		TextField passwordInput = new TextField();
		(passwordInput).setPromptText("password");
		GridPane.setConstraints(passwordInput, 1, 1);

		//Login Button
		login = new Button("Log In");
		GridPane.setConstraints(login, 1, 2);

		//login.setOnAction(e -> verify(nameInput, passwordInput));
		login.setOnAction(e -> InputPage.display());


		//Create an Account
		Button createAccount = new Button("Create an Account");
		GridPane.setConstraints(createAccount, 1, 3);
		createAccount.setOnAction(e -> CreateAccount.go());


		//Add everything to membership grip layout
		membership.getChildren().addAll(nameLabel, nameInput, passwordLabel, passwordInput, login, createAccount);

		// Membership.verify(nameInput, passwordInput);
		Scene scene = new Scene(membership, 300, 200);
		window.setScene(scene);
		window.show();


	}
>>>>>>> branch 'master' of https://github.com/Sidopsy/project-programming.git
}
<<<<<<< HEAD
	
	
=======

>>>>>>> branch 'master' of https://github.com/Sidopsy/project-programming.git


