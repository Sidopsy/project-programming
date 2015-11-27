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

public class CreateAccount {
	
	public static Stage createAccountWindow;
	
	public static void go(){
		
		createAccountWindow = new Stage();
		createAccountWindow.setTitle("Bee Adopted - Create An Account");
		
		GridPane account = new GridPane();
        account.setPadding(new Insets(10, 10, 10, 10));
        account.setVgap(8);
        account.setHgap(10);

        Label nameLabel = new Label("Name:");
        GridPane.setConstraints(nameLabel, 0, 0);
        //Name Input
        TextField nameInput = new TextField();
        (nameInput).setPromptText("Personal or Business Name");
        GridPane.setConstraints(nameInput, 0, 1);
        
        Label addressLabel = new Label("Address:");
        GridPane.setConstraints(addressLabel, 2, 0);

        //Address Input (City, Street, zip)
        //City Label
        Label cityLabel = new Label("City:");
        GridPane.setConstraints(cityLabel, 3, 0);
        //City Input
        TextField cityInput = new TextField();
        (cityInput).setPromptText("city");
        GridPane.setConstraints(cityInput, 3, 1);
        
        Label streetLabel = new Label("Street:");
        GridPane.setConstraints(streetLabel, 3, 2);
        //Street Input
        TextField streetInput = new TextField();
        (streetInput).setPromptText("street name");
        GridPane.setConstraints(streetInput, 3, 3);
        
        //Zip-code Label
        Label zipLabel = new Label("Zip-Code");
        GridPane.setConstraints(zipLabel, 3, 4);
        TextField zipInput = new TextField();
        (zipInput).setPromptText("Zip");
        GridPane.setConstraints(zipInput, 3, 5);
        
      //Phone Label
        Label phoneLabel = new Label("Phone:");
        GridPane.setConstraints(phoneLabel, 1, 0);
        //Phone Input
        TextField phoneInput = new TextField();
        (phoneInput).setPromptText("phone number");
        GridPane.setConstraints(phoneInput, 1, 1);
        
        //Email Label
        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 1, 2);
        //Email Input
        TextField emailInput = new TextField();
        (emailInput).setPromptText("enter your email: this is also your username");
        GridPane.setConstraints(emailInput, 1, 3);
        
        //Password label
        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 1, 4);
        //PasswordInput
        TextField passwordInput = new TextField();
        (passwordInput).setPromptText("choose a password");
        GridPane.setConstraints(passwordInput, 1, 5);
        
        Button create = new Button("Create My Account");
        GridPane.setConstraints(create, 0, 7);
        
        Label uploadText = new Label("Upload Picture");
		GridPane.setConstraints(uploadText, 1, 6);

		Button bowse = new Button("Browse");
		bowse.setOnAction(e -> FileChooserExample.display());
		GridPane.setConstraints(bowse, 3, 6);
		
        account.getChildren().addAll(nameLabel, nameInput, addressLabel, 
        							streetLabel, streetInput, cityLabel, 
        							cityInput, zipLabel, zipInput, phoneLabel, 
        							phoneInput, emailLabel, emailInput, 
        							passwordLabel, passwordInput, create,
        							uploadText, bowse);
		Scene createAccountScene = new Scene(account, 650, 300);
        createAccountWindow.setScene(createAccountScene);
        createAccountWindow.show();
        Membership.window.close();
	}

}
