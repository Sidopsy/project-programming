
import java.awt.Event;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public abstract class Membership {


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
	(a.getText() == "charlie@gmail.com") ||
	(a.getText() == "delta@gmail.com") ||
	(a.getText() == "echo@gmail.com") ||
	(a.getText() == "omega@gmail.com")*/

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

}



