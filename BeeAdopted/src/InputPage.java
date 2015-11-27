import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class creates a new window and lets the user input information into the database throught the GUI.
 * 
 * @author 		Mattias Landkvist
 * @refactored 	Måns Thörnvik
 * 				Added new layout touches and input restrictions on all input fields.
 */

public class InputPage {
	private static Stage window;
	private static Scene sceneInput, sceneInputConfirmation;
	private static BorderPane layoutInput, layoutInputConfirmation;

	private static TextField tfName, tfAge, tfNewSpecies, tfNewType, tfEmail, tfPhone, tfStreet, tfZip, tfCity;
	private static TextArea taDescription;
	private static PasswordField pfPassword;

	private static ChoiceBox<Object> cbAgencies, cbSpecies, cbType, cbGenders;
	private static ObservableList<Object> obsListType;

	private static File picture;

	private static DBobject db = new DBobject();
	
	/**
	 * Starting the input page by displaying a small header and letting the user specify input.
	 * 
	 */

	public static void start() {
		window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Input page");
		window.setMinWidth(250);

		layoutInput = new BorderPane();
		layoutInput.setTop(Header.smallHeader());
		layoutInput.setCenter(inputAnimalView());

		sceneInput = new Scene(layoutInput, 600, 550);

		window.setScene(sceneInput);
		window.showAndWait();
	}

	/**
	 * This prompt should be used by system admins since they are the only ones
	 * inputing agencies as well as animals.
	 * 
	 * @return a horizontal box with two options, either to input an animal or
	 *         an agency
	 */

	private static HBox chooseInput() {
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10, 10, 10, 10));
		hBox.setSpacing(10);

		Button btnAgencyInput = new Button("New agency?");
		Button btnAnimalInput = new Button("New animal?");

		btnAgencyInput.setOnAction(e -> {
			layoutInputConfirmation = new BorderPane();
			layoutInputConfirmation.setTop(Header.smallHeader());
			layoutInputConfirmation.setCenter(inputAgencyView());
			sceneInputConfirmation = new Scene(layoutInputConfirmation, 600, 550);
			window.setScene(sceneInputConfirmation);
		});

		btnAnimalInput.setOnAction(e -> {
			layoutInputConfirmation = new BorderPane();
			layoutInputConfirmation.setTop(Header.smallHeader());
			layoutInputConfirmation.setCenter(inputAnimalView());
			sceneInputConfirmation = new Scene(layoutInputConfirmation, 600, 550);
			window.setScene(sceneInputConfirmation);
		});

		hBox.getChildren().addAll(btnAgencyInput, btnAnimalInput);
		hBox.setAlignment(Pos.CENTER);

		return hBox;
	}

	/**
	 * 
	 * 
	 * @return GridPane with option to input animals
	 */

	private static GridPane inputAnimalView() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(7);
		gridPane.setVgap(7);
		gridPane.setPadding(new Insets(5, 5, 5, 5));
		gridPane.getColumnConstraints().setAll(new ColumnConstraints(250, 250, 250));
		gridPane.setAlignment(Pos.CENTER);

		Label lblName, lblAge, lblGender;

		Button btnAddPicture, btnSaveAd;

		CheckBox chbNewSpecies, chbNewType;

		
		// Adding items to the first row of the GridPane.
		lblName = new Label("Name");
		lblAge = new Label("Age");
		lblGender = new Label("Gender");

		gridPane.add(lblName, 0, 0);
		gridPane.add(lblAge, 0, 0);
		gridPane.add(lblGender, 1, 0);
		GridPane.setMargin(lblName, new Insets(-45, 0, 0, 0));
		GridPane.setMargin(lblAge, new Insets(-45, 0, 0, 160));
		GridPane.setMargin(lblGender, new Insets(-45, 0, 0, 143));

		tfName = new TextField();
		tfName.setPromptText("...");
		tfName.setMinWidth(150);
		tfName.setMaxWidth(150);

		tfName.setOnKeyReleased(e -> {
			validateInputString(tfName);
		});
		
		tfAge = new TextField();
		tfAge.setPromptText("...");
		tfAge.setMinWidth(50);
		tfAge.setMaxWidth(50);

		tfAge.setOnKeyReleased(e -> {
			validateInputInteger(tfAge);
		});
		
		ObservableList<Object> genders = FXCollections.observableArrayList("...", new Separator());
		genders.add("Male");
		genders.add("Female");
		cbGenders = new ChoiceBox<>(genders);
		cbGenders.setValue(cbGenders.getItems().get(0));
		cbGenders.setMinWidth(100);
		cbGenders.setMaxWidth(100);

		gridPane.add(tfName, 0, 0);
		gridPane.add(tfAge, 0, 0);
		gridPane.add(cbGenders, 1, 0);
		GridPane.setMargin(tfAge, new Insets(0, 0, 0, 160));
		GridPane.setMargin(cbGenders, new Insets(0, 0, 0, 143));

		
		// Items are added to row two in the GridPane
		taDescription = new TextArea();
		taDescription.setPromptText("Description...");
		taDescription.setMinSize(500, 150);
		taDescription.setMaxSize(500, 150);
		
		taDescription.setOnKeyReleased(e -> {
			validateInputTextArea(taDescription);
		});
		
		GridPane.setColumnSpan(taDescription, 2);
		gridPane.add(taDescription, 0, 2);

		
		// Items added to row three of the GridPane:
		/*
		 * What to do with the agencies...
		 */
		cbAgencies = new ChoiceBox<>(db.createObservableList("Agency", db.fetchResult(
									 db.executeQuery("SELECT Distinct Name FROM "
														  + "Agencies ORDER BY "
														  + "Name;"))));
		db.closeConnection();

		cbAgencies.setValue(cbAgencies.getItems().get(0));

		
		chbNewType = new CheckBox("New");			// Checkboxes for adding new species and types
		chbNewSpecies = new CheckBox("New");

		chbNewSpecies.setOnAction(e -> {
			if (chbNewSpecies.isSelected()) {
				cbSpecies.setValue("Species");		// Reset value of Species CB
				cbSpecies.setVisible(false);		// Hide CB
				
				tfNewSpecies.setVisible(true);		// TextField appears
				
				// Loading all types into type CB when TF has been used
				obsListType = db.createObservableList("Type", db.fetchResult(
							  db.executeQuery("SELECT Distinct Type FROM "
									  			   + "Ads ORDER BY "
									  			   + "Type;")));
				db.closeConnection();

				cbType.setItems(obsListType);		// Loading type list into type CB
				cbType.setValue("Type");			// Setting default value to "Type"
				
				tfNewType.clear();					// Clearing any added value from the TF
				tfNewType.setVisible(false);		// Reset to only show choice box, if TF for new type was visible
				
				cbType.setDisable(true);			// Disable choosing type before species specified
				cbType.setVisible(true);			// Show CB again for types
					
				cbType.setItems(obsListType);		// All Types added into Type CB
				cbType.setValue("Type");			// Set value to Type
				
				chbNewType.setSelected(false);		// Setting CHB unselected
				chbNewType.setDisable(true);		// Checking new type is disabled until species chosen
			} else {								// You dont need to retrieve a list from the DB here since it must have already gotten it, box was selected, right?
				tfNewSpecies.clear();				// Species TF cleared
				tfNewSpecies.setVisible(false);		// Hide TF
				
				cbSpecies.setVisible(true);			// Show CB again
				
				tfNewType.clear();					// Clearing any added value from the type TF
				tfNewType.setVisible(false);		// Hide type TF
				
				cbType.setValue("Type");			// Setting value to Standard before showing, no need to retrieve values from DB since checkbox was pressed at this point and the above if has been run.
				cbType.setDisable(true);			// Disable choosing type before species specified
				cbType.setVisible(true);			// Show CB again for types
				
				chbNewType.setSelected(false);		// Setting type CHB unselected
				chbNewType.setDisable(true);		// Disables new type until species chosen
			}
		});
		
		chbNewType.setDisable(true);				// Checkbox disables by default
		chbNewType.setOnAction(e -> {
			if (chbNewType.isSelected()) {
				cbType.setValue("Type");			// Resetting value to Type
				cbType.setVisible(false);			// Hiding CB for showing TF
				
				tfNewType.setVisible(true);			// Showing TF
			} else {
				tfNewType.clear();					// Clearing TF when box unchecked
				tfNewType.setVisible(false);		// Hiding TF
				
				cbType.setVisible(true);			// Setting CB visible again when unchecked
			}
		});
		
		
		// Preparing a list of all species in the DB
		cbSpecies = new ChoiceBox<>(db.createObservableList("Species", db.fetchResult(
									db.executeQuery("SELECT Distinct Species FROM "
														 + "Ads ORDER BY "
														 + "Species;"))));
		db.closeConnection();

		cbSpecies.setValue("Species");				// Setting default value to "Species"
		cbSpecies.setMinWidth(100);					// Setting size of the CB
		cbSpecies.setMaxWidth(100);
		cbSpecies.setOnAction(e -> {				// On action (selection) it should...
			if ((String) cbSpecies.getValue() != "Species") {	// If it has changed from "Species", do:
				// Update the list of types by sorting our the types belonging to other species
				obsListType = db.createObservableList("Type", db.fetchResult(
							  db.executeQuery("SELECT Distinct Type FROM "
									  			   + "Ads WHERE "
									  			   + "Species == '" + (String) cbSpecies.getValue() + "' ORDER BY "
									  			   + "Type;")));
				db.closeConnection();

				cbType.setItems(obsListType);		// Add the updated list to the CB with types
				cbType.setValue("Type");			// Setting default value of Type CB to "Type"
				cbType.setDisable(false);			// Enabling type CB
				
				chbNewType.setDisable(false);		// Enabling use of new type CHB
			} else {
				cbType.setValue("Type");			// If species is selected, type CB is also resetted
				cbType.setDisable(true);			// Disable use of type CB until an OK value is chosen for species
				
				chbNewType.setDisable(true);		// Cannot add new type until species has been chosen
			}
		});

		
		// Standard list of types
		cbType = new ChoiceBox<>(db.createObservableList("Type", db.fetchResult(
								 db.executeQuery("SELECT Distinct Type FROM "
													  + "Ads ORDER BY "
													  + "Type;"))));
		db.closeConnection();

		cbType.setValue("Type");					// Setting default value of type CB to "Type"
		cbType.setMinWidth(100);					// Setting size of type CB
		cbType.setMaxWidth(100);
		cbType.setDisable(true);					// Type CB is disables by default

		
		tfNewSpecies = new TextField();				// TF for new species
		tfNewSpecies.setMinWidth(100);				// Setting size for TF
		tfNewSpecies.setMaxWidth(100);
		tfNewSpecies.setVisible(false);				// TF not visible by default
		tfNewSpecies.setPromptText("Species...");	// Prompt text for TF to be displayed in the background
		
		tfNewSpecies.setOnKeyReleased(e -> {
			validateInputString(tfNewSpecies);
			if (tfNewSpecies.getLength() != 0) {	// This actually becomes true when 2 characters have been entered, for some reason
				cbType.setDisable(false);			// The type CB should no longer be disabled
				chbNewType.setDisable(false);		// CHB should no longer be disables
			} else if (tfNewSpecies.getLength() == 0) {	// As soon as text goes below 2 chars:
				tfNewType.clear();					// Clear the TF of new type if it had been in use
				tfNewType.setVisible(false);		// Set new type TF not visible
				
				cbType.setValue("Type");			// Set value of type CB back to Type and
				cbType.setDisable(true);			// Disable type CB again
				cbType.setVisible(true);			// Make type CB visible again, if it had been deactivated and TF for new type used
				
				chbNewType.setSelected(false);		// Deselects the CHB for new type if it was in use
				chbNewType.setDisable(true);		// Disables option for new typ CHB
			}
		});

		
		tfNewType = new TextField();				// TF for new types
		tfNewType.setMinWidth(100);
		tfNewType.setMaxWidth(100);
		tfNewType.setVisible(false);
		tfNewType.setPromptText("Type...");

		tfNewType.setOnKeyReleased(e -> {
			validateInputString(tfNewType);
		});
		
		
		gridPane.add(cbSpecies, 0, 3);				// Adding CB for species to gridpane, column 0, row 3
		gridPane.add(tfNewSpecies, 0, 3);			// Adding CHB for new species to gridpane, column 0, row 3

		gridPane.add(cbType, 1, 3);
		gridPane.add(tfNewType, 1, 3);

		gridPane.add(chbNewSpecies, 0, 3);
		GridPane.setMargin(chbNewSpecies, new Insets(0, 0, 0, 110));	// Displacing CHB for new species 110 pixels to the left, to appear after the species CB/TF

		gridPane.add(chbNewType, 1, 3);
		GridPane.setMargin(chbNewType, new Insets(0, 0, 0, 110));

		
		// The following items are added to row four
		btnAddPicture = new Button("Upload picture");
		btnAddPicture.setMinSize(50, 50);
		btnAddPicture.setOnAction(e -> picture = FileChooserExample.display());	// Button for adding pictures guides you to a new window
		gridPane.add(btnAddPicture, 0, 4);

		
		// This item is added to the last row (5)
		btnSaveAd = new Button("Save ad");
		btnSaveAd.setMinWidth(100);
		btnSaveAd.setMaxWidth(100);
		
		btnSaveAd.setOnAction(e -> {
			if (validateAdInputs()) {	
				inputAdValues();
				layoutInputConfirmation = new BorderPane();
				layoutInputConfirmation.setTop(Header.smallHeader());
				sceneInputConfirmation = new Scene(layoutInputConfirmation, 600, 550);
				window.setScene(sceneInputConfirmation);
			} else {}
		});
		gridPane.add(btnSaveAd, 0, 5);

		
		return gridPane;
	}

	private static GridPane inputAgencyView() {
		GridPane input = new GridPane();
		input.setHgap(40);
		input.setVgap(20);
		input.setPadding(new Insets(10, 10, 10, 10));

		Button btnSaveAgency;
		
		tfName = new TextField();
		tfName.setPromptText("Name");
		tfName.setMaxWidth(150);
		input.add(tfName, 0, 0);

		tfEmail = new TextField();
		tfEmail.setPromptText("Email");
		tfEmail.setMaxWidth(150);
		input.add(tfEmail, 1, 0);

		tfPhone = new TextField();
		tfPhone.setPromptText("Phone number");
		tfPhone.setMaxWidth(150);
		input.add(tfPhone, 0, 1);

		pfPassword = new PasswordField();
		pfPassword.setPromptText("Password");
		pfPassword.setMaxWidth(150);
		input.add(pfPassword, 1, 1);

		tfStreet = new TextField();
		tfStreet.setPromptText("Street");
		tfStreet.setMaxWidth(150);
		input.add(tfStreet, 0, 2);

		tfZip = new TextField();
		tfZip.setPromptText("ZIP");
		tfZip.setMaxWidth(150);
		input.add(tfZip, 0, 3);

		tfCity = new TextField();
		tfCity.setPromptText("City");
		tfCity.setMaxWidth(150);
		input.add(tfCity, 1, 3);

		Label uploadText = new Label("Upload logotype");
		input.add(uploadText, 0, 4);

		Button bowse = new Button("Browse");
		bowse.setOnAction(e -> FileChooserExample.display());
		input.add(bowse, 1, 4);

		btnSaveAgency = new Button("Save agency");
		input.add(btnSaveAgency, 0, 5);

		// input.getChildren().addAll(cb1, cb2, cb3, cb4, cb5, tf, ta, btn);
		btnSaveAgency.setOnAction(e -> inputAgencyValues());

		input.setAlignment(Pos.CENTER);

		return input;
	}
	
	/**
	 * Used to validate all input fields at once. This only checks that allowed valued have been entered into CBs and TFs.
	 * 
	 * @return boolean representing if all text inputs are correct and shows the program that it is OK to go ahead and send the
	 * information to the database.
	 */
	
	private static boolean validateAdInputs() {
		if ((validateChoiceBox(cbSpecies)) &&			// Species and types have been chosen manually, both "new" TFs are validated
			(validateChoiceBox(cbType))) {	
			
			return (validateInputString(tfName)) && 
				   (validateInputInteger(tfAge)) && 
				   !(validateChoiceBox(cbGenders)) &&	// "!" because the validation of CBs check whether they are in their original position or not.
				   (validateInputTextArea(taDescription)) && 
				   (validateInputString(tfNewSpecies)) && 
				   (validateInputString(tfNewType)); 
		} else if (validateChoiceBox(cbSpecies)) {		// Species is in its original position, TF for new species is validated.
			
			return (validateInputString(tfName)) && 
				   (validateInputInteger(tfAge)) && 
				   !(validateChoiceBox(cbGenders)) &&
				   (validateInputTextArea(taDescription)) &&
				   (validateInputString(tfNewSpecies));
		} else if (validateChoiceBox(cbType)) {			// Type is in its original position, TF for new type is validated.
			
			return (validateInputString(tfName)) && 
				   (validateInputInteger(tfAge)) && 
				   !(validateChoiceBox(cbGenders)) &&
				   (validateInputTextArea(taDescription)) &&
				   (validateInputString(tfNewType));
		} else {										// Nethier CBs were in their original states, none of the "new" TFs are validated.
			
			return (validateInputString(tfName)) && 
				   (validateInputInteger(tfAge)) && 
				   !(validateChoiceBox(cbGenders)) &&
				   (validateInputTextArea(taDescription));
		}
	}
				
	/**
	 * Performs a check whether a choice box is in its default position, stating information about it's use.
	 *
	 * @return boolean representing if parameter choice box is in its original position, true for "YES" and false for "NO".
	 */
	
	private static boolean validateChoiceBox(ChoiceBox<Object> cb) {
		return (cb.getValue() == cb.getItems().get(0));		
	}
	
	/**
	 * Ensures that text fields only contain alphabetical characters and or spaces, otherwise it will change the borde color to red.
	 * 
	 * @param text field that should only contain numbers, spaces are not accepted 
	 */
	
	private static boolean validateInputString(TextField tf) {
		if (tf.getLength() < 30) {										// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!(Character.isAlphabetic(tf.getText().charAt(index))) && !(tf.getText().charAt(index) == ' ')) {
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;										// Loop is broken, a character that is not alphabetical was found.
				} else if ((index == tf.getLength() - 1)) {				// Everything was scanned and all was alphabetic, return true
					tf.setStyle("-fx-box-border: teal;");
					return true;
				} else {												// Do we need this else?
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
				}
			}
		} else {														// Number of chars too many
			tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
		return false;
	}

	/**
	 * Ensures that text fields only contain digits, otherwise it will change the borde color to red.
	 * 
	 * @param text field that should only contain numbers, spaces are not accepted
	 */
	
	private static boolean validateInputInteger(TextField tf) {
		for (int index = 0; index < tf.getLength(); index++)	{
			if (!Character.isDigit(tf.getText().charAt(index))) {		// "Not a digit" was encountered
				tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
				return false;											// Loop is broken, a character that is not numerical was found.
			} else if ((index == tf.getLength() - 1) && Integer.parseInt(tf.getText()) < 100) {
				tf.setStyle("-fx-box-border: teal;");
				return true;
			} else {													// Do we need this else?
				tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			}
		}
		return false;
	}
	
	/**
	 * Not currently in use as you may want to add a long text including characters such as !, ?, / and so on, including numbers.
	 * Does check for description length as the DB has a limit of 255 characters.
	 * 
	 * @param text area with the description of the animal.
	 */
	
	private static boolean validateInputTextArea(TextArea ta) {
		if (ta.getLength() > 254) {
			ta.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		} else {
			ta.setStyle("-fx-box-border: teal;");
			return true;
		}
	}

	/**
	 * TBD
	 */
	
	private static void inputAgencyValues() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String password = pfPassword.getText();
        String street = tfStreet.getText();
        String zip = tfZip.getText();
        String city = tfCity.getText();

        String insert = "INSERT INTO Agencies (Name,Email,Phone,Password)";
        String values = " VALUES ('" + name  + "', '"+ email + "', '"
                        + phone + "', '" + password + "', '"
                        + street + "', '" + zip + "', '" + city + "');";
        System.out.println(values);
        
        db.executeUpdate(insert + values);

        int agencyId = db.fetchAgency(db.executeQuery("SELECT ID FROM Agencies ORDER BY ASC(ID)")).get(0).getID();

        String insertAdress = "INSERT INTO Addresses (AgencyId,Street,Zip,City) ";
        String valuesAdress = "VALUES ('" + password + "', '" + street + "', "
        							+ "'" + zip + "', '" + city + "');";

        System.out.println(values);
        db.executeUpdate(insertAdress + valuesAdress);

  
    }

	/**
	 * Method for finalizing and inputing values into the database, based on what was entered into the input page GUI.
	 * @throws  
	 * 
	 */
	
	private static void inputAdValues() {
		String name, age, gender, description, species, type, agencyID;
		
//		ArrayList<Agency> agency;
		
		// The Agencies.ID = 1 in this statement should be replaced by the >>logged in<< agencies ID.
//		String sqlStatement = "SELECT Agencies.ID, Name, AVG(Rating), Logo FROM "
//								+ "Agencies, Ratings WHERE "
//								+ "Agencies.ID = Ratings.AgencyID and Agencies.ID = 1;";
//
//		try {
//			agency = db.fetchAgency(db.executeQuery(sqlStatement));
//			db.closeConnection();
//		} catch (SQLException e) {
//			System.out.println(e);
//		}
		
		name = tfName.getText().trim();					// Trimming TFs with possibility of spaces
		age = tfAge.getText();
		gender = (String) cbGenders.getValue();
		description = taDescription.getText().trim();	// Trimming TAs with possibility of spaces
			
		if (!validateChoiceBox(cbSpecies)) {			// If gets executed when the default value of CB is NOT chosen
			species = (String) cbSpecies.getValue();
		} else {
			species = tfNewSpecies.getText().trim();
		}

		if (!validateChoiceBox(cbType)) {
			type = (String) cbType.getValue();
		} else {
			type = tfNewType.getText().trim();
		}

		String insert = "INSERT INTO Ads (Name, Species, Type, Gender, Age, Description, AgencyID) ";
		String values = "VALUES ('" + name + "', '" + species + "', '" + type + "', '" + gender + "', " + age
								+ ", '" + description + "', " + 1 + ");";		// Exchange 1 with the agencies ID.
		System.out.println(">> " + insert + "\n" + ">> " + values);
		
		db.executeUpdate(insert + values);
		db.closeConnection();
	}
}