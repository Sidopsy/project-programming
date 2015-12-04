
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CreateAccount {
	
	public static Stage createAccountWindow;
	
	
	 static DBobject db = new DBobject();
	
	 static TextField nameInput = new TextField();
	 static TextField cityInput = new TextField();
	 static TextField streetInput = new TextField();
	 static TextField zipInput = new TextField();
	 static TextField phoneInput = new TextField();
	 static TextField emailInput = new TextField();
	 static TextField passwordInput = new TextField();


	private static String rain;
	
	public static void display(){
		
		createAccountWindow = new Stage();
		createAccountWindow.setTitle("Bee Adopted - Create An Account");
		
		GridPane account = new GridPane();
        account.setPadding(new Insets(10, 10, 10, 10));
        account.setVgap(8);
        account.setHgap(10);

        Label nameLabel = new Label("Name:");
        GridPane.setConstraints(nameLabel, 0, 0);
        
        //Name Input
       
        (nameInput).setPromptText("Personal or Business Name");
        GridPane.setConstraints(nameInput, 0, 1);
        
        Label addressLabel = new Label("Address:");
        GridPane.setConstraints(addressLabel, 2, 0);

        //Address Input (City, Street, zip)
        //City Label
        Label cityLabel = new Label("City:");
        GridPane.setConstraints(cityLabel, 3, 0);
        //City Input
       
        (cityInput).setPromptText("City");
        GridPane.setConstraints(cityInput, 3, 1);
        
        Label streetLabel = new Label("Street:");
        GridPane.setConstraints(streetLabel, 3, 2);
        //Street Input
        
        (streetInput).setPromptText("Street Name");
        GridPane.setConstraints(streetInput, 3, 3);
        
        //Zip-code Label
        Label zipLabel = new Label("Zip-Code");
        GridPane.setConstraints(zipLabel, 3, 4);
        
        (zipInput).setPromptText("Zip");
        GridPane.setConstraints(zipInput, 3, 5);
        
        //Phone Label
        Label phoneLabel = new Label("Phone:");
        GridPane.setConstraints(phoneLabel, 1, 0);
        //Phone Input
        
        (phoneInput).setPromptText("Phone Number");
        GridPane.setConstraints(phoneInput, 1, 1);
        
        //Email Label
        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 1, 2);
        //Email Input
        
        (emailInput).setPromptText("Email: Also Your Username");
        GridPane.setConstraints(emailInput, 1, 3);
        
        //Password label
        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 1, 4);
        //PasswordInput
        
        (passwordInput).setPromptText("Choose a Password");
        GridPane.setConstraints(passwordInput, 1, 5);
        
        Button nameAndPI = new Button("Save Name and Personal Info");
        GridPane.setConstraints(nameAndPI, 1, 7);
        
        Label uploadText = new Label("Upload Picture");
		GridPane.setConstraints(uploadText, 1, 6);

		Button browse = new Button("Browse");
		browse.setOnAction(InputPage.btnLoadEventListener);
		GridPane.setConstraints(browse, 3, 6);
		
		Button saveAddress = new Button("Save Address");
		GridPane.setConstraints(saveAddress, 3, 7);
		
        account.getChildren().addAll(nameLabel, nameInput, addressLabel, 
        							streetLabel, streetInput, cityLabel, 
        							cityInput, zipLabel, zipInput, phoneLabel, 
        							phoneInput, emailLabel, emailInput, 
        							passwordLabel, passwordInput, nameAndPI, saveAddress,
        							uploadText, browse);
		
        
        Scene createAccountScene = new Scene(account, 650, 300);
		createAccountWindow.setScene(createAccountScene);
        createAccountWindow.show();
        nameAndPI.setOnAction(e -> saveNewAccountPartOne());
        saveAddress.setOnAction(e -> saveNewAccountPartTwo());
	}
	
	private static void saveNewAccountPartOne()  {
		//System.out.println(agencyID);
		String name = nameInput.getText();
		String email = emailInput.getText();
		String phone = phoneInput.getText();
		String password = passwordInput.getText();
		
	
		String insert = "INSERT INTO Agencies (Name, Email, Phone, Password)";
		String values = 
				" VALUES ('" + name  + "', '"+ email + "', '" 
						+ phone + "', '" + password + "');";
		System.out.println(insert + values);
		db.executeUpdate(insert + values);
		
		
		
		
			
		
		
			
		
	}

	private static void saveNewAccountPartTwo(){
		
		
		String street = streetInput.getText();
		String zip = zipInput.getText();
		String city = cityInput.getText();
		
		
		int agencyId = Integer.parseInt(db.fetchResult(db.executeQuery("SELECT ID FROM Agencies ORDER BY ID DESC;")).get(0).get(0));
		db.closeConnection();
		String insertAdress = "INSERT INTO Addresses (AgencyID, Street, Zip, City)";
		String valuesAdress = 
				" VALUES (" + agencyId + ", '" + street + "', '" + zip + "', '" + city + "');";
		//could the error be because we are trying to insert an id that already exists?

		System.out.println(insertAdress + valuesAdress);
		db.executeUpdate(insertAdress + valuesAdress);
	}
	
}
