import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class creates a new window and lets the user input information into the database throught the GUI.
 * 
 * @author 		Mattias Landkvist
 * @refactored 	M??ns Th??rnvik
 * 				Added new layout touches and input restrictions on all input fields.
 */

public class InputPage {
	private static Stage window;
	private static Scene sceneInput;
	private static BorderPane layoutInput;

	private static TextField tfName, tfAge, tfNewSpecies, tfNewType;
	private static TextArea taDescription;
	private static CheckBox chbNewSpecies, chbNewType;

	private static ChoiceBox<Object> cbAgencies, cbSpecies, cbType, cbGenders;
	private static ObservableList<Object> obsListType;

	private static File picture;
	private static File file;
	private static ImageView myImageView;
	private static FileChooser filechooser;
	private static AgencyExt agency;
	
	private static DBobject db = new DBobject();

	/**
	 * Starting the input page by displaying a small header and letting the user specify input.
	 * 
	 */

	public static void display(AgencyExt agencyInfo){
		window = new Stage();

		agency = agencyInfo;
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Input page");
		window.setMinWidth(250);

		layoutInput = new BorderPane();
		layoutInput.setTop(Header.smallHeader());
		layoutInput.setCenter(viewInputAd());

		sceneInput = new Scene(layoutInput, 600, 550);

		window.setScene(sceneInput);
		window.showAndWait();
	}
	
	/**
	 * 
	 * 
	 * @return GridPane with option to input animals
	 */

	public static GridPane viewInputAd() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(7);
		gridPane.setVgap(7);
		gridPane.setPadding(new Insets(5, 5, 5, 5));
		gridPane.getColumnConstraints().setAll(new ColumnConstraints(250, 250, 250));
		gridPane.setAlignment(Pos.CENTER);


		addInputLabels(gridPane);
		addInputTextFields(gridPane);
		addInputBoxes(gridPane);
		addInputButtons(gridPane);


		return gridPane;
	}

	/**
	 * Updates the incoming GridPane with label for information about inputs.
	 * 
	 * @param initialized GridPane to add labels to.
	 * @return GridPane containing the labels neccessary to guide the user.
	 */

	public static GridPane addInputLabels(GridPane inputGridPane) {
		GridPane gridPane = inputGridPane;
		Label lblName, lblAge, lblGender;


		lblName = new Label("Name");
		lblAge = new Label("Age");
		lblGender = new Label("Gender");


		gridPane.add(lblName, 0, 0);
		gridPane.add(lblAge, 0, 0);
		gridPane.add(lblGender, 1, 0);
		GridPane.setMargin(lblName, 	new Insets(-45, 0, 0, 0));
		GridPane.setMargin(lblAge, 		new Insets(-45, 0, 0, 160));
		GridPane.setMargin(lblGender, 	new Insets(-45, 0, 0, 143));


		return gridPane;
	}

	/**
	 * Updates the incoming GridPane with textfields for input information.
	 * 
	 * @param initialized GridPane to add text fields to.
	 * @return GridPane containing the textfields neccessary for inputing information about ads.
	 */

	public static GridPane addInputTextFields(GridPane inputGridPane) {
		GridPane gridPane = inputGridPane;

		ContextMenu strings = new ContextMenu();

		tfName = new TextField();
		tfName.setPromptText("...");
		tfName.setContextMenu(strings);
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


		taDescription = new TextArea();
		taDescription.setPromptText("Description...");
		taDescription.setMinSize(500, 150);
		taDescription.setMaxSize(500, 150);
		taDescription.setOnKeyReleased(e -> {
			validateInputTextArea(taDescription);
		});


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


		gridPane.add(tfName, 0, 0);
		gridPane.add(tfAge, 0, 0);
		GridPane.setColumnSpan(taDescription, 2);
		gridPane.add(taDescription, 0, 3);
		gridPane.add(tfNewSpecies, 0, 2);			// Adding CHB for new species to gridpane, column 0, row 3
		gridPane.add(tfNewType, 1, 2);
		GridPane.setMargin(tfAge, 		new Insets(0, 0, 0, 160));
		GridPane.setMargin(tfNewType, 		new Insets(0, 0, 0, -97));


		return gridPane;
	}

	/**
	 * Updates the incoming GridPane with boxes for input information.
	 * 
	 * @param initialized GridPane to add boxes to.
	 * @return GridPane containing the boxes (choice and check) neccessary for inputing information about ads.
	 */

	public static GridPane addInputBoxes(GridPane inputGridPane) {
		GridPane gridPane = inputGridPane;


		ObservableList<Object> genders = FXCollections.observableArrayList("...", new Separator());
		genders.add("Male");
		genders.add("Female");
		cbGenders = new ChoiceBox<>(genders);
		cbGenders.setValue(cbGenders.getItems().get(0));
		cbGenders.setMinWidth(100);
		cbGenders.setMaxWidth(100);
		cbGenders.setOnAction(e -> {
			validateChoiceBox(cbGenders);
		});


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


		cbSpecies = new ChoiceBox<>(db.createObservableList("Species", db.fetchResult(
				db.executeQuery("SELECT Distinct Species FROM "
						+ "Ads ORDER BY "
						+ "Species;"))));
		db.closeConnection();
		cbSpecies.setValue("Species");				// Setting default value to "Species"
		cbSpecies.setMinWidth(100);					// Setting size of the CB
		cbSpecies.setMaxWidth(100);
		cbSpecies.setOnAction(e -> {				// On action (selection) it should...
			validateChoiceBox(cbSpecies);
			if ((String) cbSpecies.getValue() != "Species") {	// If it has changed from "Species", do:
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


		cbType = new ChoiceBox<>(db.createObservableList("Type", db.fetchResult(
				db.executeQuery("SELECT Distinct Type FROM "
						+ "Ads ORDER BY "
						+ "Type;"))));
		db.closeConnection();
		cbType.setValue("Type");					// Setting default value of type CB to "Type"
		cbType.setMinWidth(100);					// Setting size of type CB
		cbType.setMaxWidth(100);
		cbType.setDisable(true);					// Type CB is disables by default
		cbType.setOnAction(e -> {
			try {
				validateChoiceBox(cbType);
			} catch (Exception error) {}			// When Type is activated by selecting a species, validate points to null
		});											// which throws and error, catch lets us ignore it.


		gridPane.add(cbGenders, 1, 0);
		gridPane.add(cbSpecies, 0, 2);				// Adding CB for species to gridpane, column 0, row 3
		gridPane.add(cbType, 1, 2);
		gridPane.add(chbNewSpecies, 0, 2);
		gridPane.add(chbNewType, 1, 2);
		GridPane.setMargin(cbGenders, 		new Insets(0, 0, 0, 143));
		GridPane.setMargin(cbType, 			new Insets(0, 0, 0, -97));
		GridPane.setMargin(chbNewSpecies, 	new Insets(0, 0, 0, 105));	// Displacing CHB for new species 110 pixels to the left, to appear after the species CB/TF
		GridPane.setMargin(chbNewType, 		new Insets(0, 0, 0, 8));


		return gridPane;
	}

	/**
	 * Updates the incoming GridPane with boxes for input information.
	 * 
	 * @param initialized GridPane to add buttons to.
	 * @return GridPane containing the buttons neccessary for inputing information about ads.
	 */

	public static GridPane addInputButtons(GridPane inputGridPane) {
		GridPane gridPane = inputGridPane;
		Button btnAddPicture, btnSaveAd, btnClose;
		Alert alert;

		
		myImageView = new ImageView();
		myImageView.setPreserveRatio(false);
		myImageView.setFitWidth(100);
		myImageView.setFitHeight(100);
		gridPane.add(myImageView, 1, 4);


		btnAddPicture = new Button("Upload picture");
		btnAddPicture.setMinSize(50, 50);
		btnAddPicture.setOnAction(btnLoadEventListener);	// Button for adding pictures guides you to a new window
		gridPane.add(btnAddPicture, 0, 4);


		alert = new Alert(AlertType.INFORMATION);

		btnSaveAd = new Button("Save ad");
		btnSaveAd.setMinWidth(110);
		btnSaveAd.setMaxWidth(110);
		btnSaveAd.setOnAction(e -> {
			if (validateAdInfo(tfName, tfAge, cbGenders, cbSpecies, cbType, 
					tfNewSpecies, tfNewType, taDescription)) {

				System.out.println(">> Input values OK");
				inputAdValues();
				try {
					upload();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				alert.setAlertType(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText("Advertisement has been submitted");
				alert.setContentText("Your advertisement is now visible to all system users for 90 days.");
				alert.showAndWait();
				window.close();
			} else {
				System.out.println(">> Input values incorrect");
				alert.setAlertType(AlertType.ERROR);
				alert.setTitle("Failiure");
				alert.setHeaderText("Information could not be saved");
				alert.setContentText("You need to input correct values into all fields before saving.");
				alert.showAndWait();
			}
		});

		btnClose = new Button("Close");
		btnClose.setOnAction(e -> {
			window.close();
		});


		gridPane.add(btnSaveAd, 0, 5);
		gridPane.add(btnClose, 0, 5);
		GridPane.setMargin(btnClose, new Insets(0, 0, 0, 115));


		return gridPane;
	}

	/**
	 * Eventhandler for Image input into database.
	 */

	static EventHandler<ActionEvent> btnLoadEventListener
	= new EventHandler<ActionEvent>(){

		@Override
		public void handle(ActionEvent t) {
			FileChooser fileChooser = new FileChooser();

			//Set extension filters
			FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
			fileChooser.getExtensionFilters().addAll(extFilterPNG);

			//Show open file dialog
			file = fileChooser.showOpenDialog(null);

			try {
				BufferedImage bufferedImage = ImageIO.read(file);

				Image image = SwingFXUtils.toFXImage(bufferedImage, null);

				if (image != null) {
					myImageView.setImage(image);
				}
			} catch (IOException ex) {
				Logger.getLogger(InputPage.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
	};
	
	/**
	 * Det här bara funkar, ingen vet varför.
	 * 
	 * @throws Exception
	 */

	public static void upload() throws Exception {
		byte[] animal_image = null;
		int s = 0;
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];

		try {
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
				System.out.println("read " + readNum + " bytes,");
			}
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		animal_image = bos.toByteArray();
		
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:BeeHive");
		
		PreparedStatement ps = conn.prepareStatement("UPDATE Ads SET Picture = ? WHERE ID == " + db.fetchResult(db.executeQuery("SELECT ID FROM Ads ORDER BY ID DESC")).get(0).get(0));
		db.closeConnection();
		ps.setBytes(1, animal_image);
		s = ps.executeUpdate();

		if (s > 0) {
			System.out.print("Image Uploaded");
		}

		ps.close();
		conn.close();
	}


	public static FileChooser getFilechooser() {
		return filechooser;
	}

	public static void setFilechooser(FileChooser filechooser) {
		InputPage.filechooser = filechooser;
	}

	/**
	 * Used to validate all input fields at once. This only checks that allowed valued have been entered into CBs and TFs.
	 * 
	 * @return boolean representing if all text inputs are correct and shows the program that it is OK to go ahead and send the
	 * information to the database.
	 */

	public static boolean validateAdInfo(TextField name, TextField age, ChoiceBox<Object> gender, ChoiceBox<Object> species, ChoiceBox<Object> type, 
			TextField newSpecies, TextField newType, TextArea description) {

		validateChoiceBox(gender);
		validateInputInteger(age);
		validateInputTextArea(description);
		if ((!validateChoiceBox(species)) &&			// Species and types have been chosen manually, both "new" TFs are validated
				(!validateChoiceBox(type))) {	

			return (validateInputString(name)) && 
					(validateInputInteger(age)) && 
					(validateChoiceBox(gender)) &&	
					(validateInputTextArea(description)) && 
					(validateInputString(newSpecies)) && 
					(validateInputString(newType)); 

		} else if (!validateChoiceBox(species)) {		// Species is in its original position, TF for new species is validated.

			return (validateInputString(name)) && 
					(validateInputInteger(age)) && 
					(validateChoiceBox(gender)) &&
					(validateInputTextArea(description)) &&
					(validateInputString(newSpecies));

		} else if (!validateChoiceBox(type)) {			// Type is in its original position, TF for new type is validated.

			return (validateInputString(name)) && 
					(validateInputInteger(age)) && 
					(validateChoiceBox(gender)) &&
					(validateInputTextArea(description)) &&
					(validateInputString(newType));

		} else {										// Nethier CBs were in their original states, none of the "new" TFs are validated.

			return (validateInputString(name)) && 
					(validateInputInteger(age)) && 
					(validateChoiceBox(gender)) &&
					(validateInputTextArea(description));
		}
	}

	/**
	 * Performs a check whether a choice box is in its default position, stating information about it's use.
	 *
	 * @param ChoiceBox for either a species, gender or type.
	 * @return boolean representing if parameter choice box is in its original position, true if it is NOT - since this is the
	 * not the desired position, another should be chosen. False means that the CB value has not been changed.
	 */

	public static boolean validateChoiceBox(ChoiceBox<Object> cb) {
		if ((cb.getValue().equals("Species")) || (cb.getValue().equals("...")) || (cb.getValue().equals("Type"))) {
			cb.setStyle("-fx-focus-color: red ; -fx-border-color: red;");
			return false;
		} else {
			cb.setStyle("-fx-box-border: teal;");
			return true;
		}
	}

	/**
	 * Ensures that text fields only contain alphabetical characters and or spaces, otherwise it will change the borde color to red.
	 * 
	 * @param TextField that should only contain letters, spaces also accepted.
	 * @return boolean representing if the input inside the TextField is within it's allowed limits. No more than 30 or less
	 * than one character(s), only letters and or spaces are allowed.
	 */

	public static boolean validateInputString(TextField tf) {
		if ((tf.getLength() <= 30) && (tf.getLength() > 0)) {										// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!(Character.isAlphabetic(tf.getText().charAt(index))) && !(tf.getText().charAt(index) == ' ')) {
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;										// Loop is broken, a character that is not alphabetical was found
				} else {}
			}
			tf.setStyle("-fx-box-border: teal;");
			return true;
		} else {														// Number of chars too many
			tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}

	/**
	 * Ensures that text fields only contain digits, otherwise it will change the borde color to red.
	 * 
	 * @param TextField that should only contain numbers, spaces are not accepted.
	 * @return boolean representing if the input inside the TextField is within it's allowed limits. Not larger than 100 or
	 * less than 1, only digits are allowed.
	 */

	public static boolean validateInputInteger(TextField tf) {
		if ((tf.getLength() <= 2) && (tf.getLength() > 0)) {	
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!Character.isDigit(tf.getText().charAt(index))) {		// "Not a digit" was encountered
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;											// Loop is broken, a character that is not numerical was found.
				} else {}
			}
			tf.setStyle("-fx-box-border: teal;");
			return true;
		} else {
			tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}

	/**
	 * Not currently in use as you may want to add a long text including characters such as !, ?, / and so on, including numbers.
	 * Does check for description length as the DB has a limit of 255 characters.
	 * 
	 * @param TextArea
	 * @return boolean representing if the input inside the TextArea is within it's allowed limits. No more than 255 or less
	 * than zero characters, any input is accepted.
	 */

	public static boolean validateInputTextArea(TextArea ta) {
		if ((ta.getLength() <= 255) && (ta.getLength() > 0)) {
			ta.setStyle("-fx-box-border: teal;");
			return true;
		} else {
			ta.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}

	/**
	 * Method for finalizing and inputing values into the database, based on what has been entered into the input page GUI.
	 * Values entered into TextFields and ChoiceBoxes are first validated before submitted with an INSERT.
	 * 
	 * TODO Add agency.getID() instead of a static "1" when Madisen has finished the login function.
	 */

	private static void inputAdValues() {
		String name, age, gender, description, species, type;


		name = tfName.getText().trim();					// Trimming TFs with possibility of spaces
		age = tfAge.getText();
		if (age.length() > 1 && age.charAt(0) == '0') {
			age = age.substring(1);
		}
		gender = (String) cbGenders.getValue();
		description = taDescription.getText().trim();	// Trimming TAs with possibility of spaces

		if (validateChoiceBox(cbSpecies)) {				// If gets executed when the default value of CB is NOT chosen
			species = (String) cbSpecies.getValue();
		} else {
			species = tfNewSpecies.getText().trim();
		}

		if (validateChoiceBox(cbType)) {
			type = (String) cbType.getValue();
		} else {
			type = tfNewType.getText().trim();
		}
		

		String insert = "INSERT INTO Ads (Name, Species, Type, Gender, Age, Description, AgencyID) ";
		String values = "VALUES ('" + name + "', '" + species + "', '" + type + "', '" + gender + "', " + age
				+ ", '" + description + "', " + agency.getID() + ");";		// Exchange 1 with the agencies ID.
		System.out.println(">> " + insert + "\n" + ">> " + values);

		db.executeUpdate(insert + values);
	}
}