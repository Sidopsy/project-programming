package View;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import Control.InputValidation;
import Object.Ad;
import Object.AgencyExt;
import Object.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

/**
 * This class creates a new window and lets the user input information into the database through the GUI.
 * 
 * @author Mattias Landkvist, Måns Thörnvik & Yu Jet Hua
 */

public class InputPage {
	private static TextField tfName, tfAge, tfNewSpecies, tfNewType;
	private static TextArea taDescription;
	private static CheckBox chbNewSpecies, chbNewType, chbReActivate;

	private static ChoiceBox<Object> cbSpecies, cbType, cbGenders;
	private static ObservableList<Object> obsListType;

	private static File file = null;
	private static ImageView myImageView;
	private static FileChooser fileChooser;
	
	private static AgencyExt agency;
	private static Ad ad;

	private static Database db = new Database();

	/**
	 * Creates the view in which an ad can be created or updated.
	 * 
	 * @param boolean
	 * @return GridPane with option to input animals
	 * 
	 * @author Mattias Landkvist & Måns Thörnvik
	 */

	public static GridPane viewInputAd(boolean updateAd) {
		GridPane gridPane = new GridPane();
		
		gridPane.setHgap(7);
		gridPane.setVgap(7);
		gridPane.setPadding(new Insets(5, 5, 5, 5));
		gridPane.getColumnConstraints().setAll(new ColumnConstraints(250, 250, 250));
		gridPane.setAlignment(Pos.CENTER);

		addInputLabels(gridPane, updateAd);
		addInputTextFields(gridPane, updateAd);
		addInputBoxes(gridPane, updateAd);
		addInputButtons(gridPane, updateAd);


		return gridPane;
	}

	/**
	 * Updates the incoming GridPane with label for information about inputs.
	 * 
	 * @param GridPane to add labels to and a boolean
	 * @return GridPane containing the labels neccessary to guide the user.
	 * 
	 * @author Måns Thörnvik
	 */

	private static GridPane addInputLabels(GridPane inputGridPane, boolean updateAd) {
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
	 * @param initialized GridPane to add text fields to and a boolean
	 * @return GridPane containing the textfields neccessary for inputing information about ads.
	 * 
	 * @author Måns Thörnvik
	 */

	private static GridPane addInputTextFields(GridPane inputGridPane, boolean updateAd) {
		GridPane gridPane = inputGridPane;
		ContextMenu strings = new ContextMenu();

		tfName = new TextField();
		tfName.setPromptText("...");
		if (updateAd) {tfName.setText(ad.getName());}
		tfName.setContextMenu(strings);
		tfName.setMinWidth(150);
		tfName.setMaxWidth(150);
		tfName.setOnKeyReleased(e -> InputValidation.validateInputTextFieldString(tfName));

		tfAge = new TextField();
		tfAge.setPromptText("...");
		if (updateAd) {
			String age = "";
			int intAge = ad.getAge();
			age += intAge;
			tfAge.setText(age);
		}
		tfAge.setMinWidth(50);
		tfAge.setMaxWidth(50);
		tfAge.setOnKeyReleased(e -> InputValidation.validateInputAge(tfAge));

		taDescription = new TextArea();
		taDescription.setPromptText("Description...");
		if (updateAd) {taDescription.setText(ad.getDescription());}
		taDescription.setMinSize(500, 150);
		taDescription.setMaxSize(500, 150);
		taDescription.setOnKeyReleased(e -> InputValidation.validateInputTextArea(taDescription));

		tfNewSpecies = new TextField();				// TF for new species
		tfNewSpecies.setMinWidth(150);				// Setting size for TF
		tfNewSpecies.setMaxWidth(150);
		tfNewSpecies.setVisible(false);				// TF not visible by default
		tfNewSpecies.setPromptText("Species...");	// Prompt text for TF to be displayed in the background
		tfNewSpecies.setOnKeyReleased(e -> {
			InputValidation.validateInputTextFieldString(tfNewSpecies);
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
		tfNewType.setMinWidth(150);
		tfNewType.setMaxWidth(150);
		tfNewType.setVisible(false);
		tfNewType.setPromptText("Type...");
		tfNewType.setOnKeyReleased(e -> InputValidation.validateInputTextFieldString(tfNewType));

		gridPane.add(tfName, 0, 0);
		gridPane.add(tfAge, 0, 0);
		gridPane.add(taDescription, 0, 3);
		gridPane.add(tfNewSpecies, 0, 2);			// Adding CHB for new species to gridpane, column 0, row 3
		gridPane.add(tfNewType, 1, 2);
		GridPane.setColumnSpan(taDescription, 2);
		GridPane.setMargin(tfAge, 			new Insets(0, 0, 0, 160));
		GridPane.setMargin(tfNewType, 		new Insets(0, 0, 0, -17));

		return gridPane;
	}

	/**
	 * Updates the incoming GridPane with boxes for input information.
	 * 
	 * @param initialized GridPane to add boxes to and a boolean.
	 * @return GridPane containing the boxes (choice and check) neccessary for inputing information about ads.
	 * 
	 * @author Måns Thörnvik
	 */

	private static GridPane addInputBoxes(GridPane inputGridPane, boolean updateAd) {
		GridPane gridPane = inputGridPane;

		ObservableList<Object> genders = FXCollections.observableArrayList("...", new Separator());
		genders.add("Male");
		genders.add("Female");
		cbGenders = new ChoiceBox<>(genders);
		if (updateAd) {cbGenders.setValue(ad.getGender());
		} else {cbGenders.setValue(cbGenders.getItems().get(0));}
		cbGenders.setMinWidth(100);
		cbGenders.setMaxWidth(100);
		cbGenders.setOnAction(e -> InputValidation.validateChoiceBox(cbGenders));

		chbNewType = new CheckBox("New");			// Checkboxes for adding new species and types
		chbNewSpecies = new CheckBox("New");

		chbNewSpecies.setOnAction(e -> {
			if (chbNewSpecies.isSelected()) {
				cbSpecies.setValue("Species");		// Reset value of Species CB
				cbSpecies.setVisible(false);		// Hide CB

				tfNewSpecies.setVisible(true);		// TextField appears

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

				cbType.setValue("Type");			// Setting value to Standard before showing, no need to retrieve values from 
													// DB since checkbox was pressed at this point and the above if has been run.
				cbType.setDisable(true);			// Disable choosing type before species specified
				cbType.setVisible(true);			// Show CB again for types

				chbNewType.setSelected(false);		// Setting type CHB unselected
				chbNewType.setDisable(true);		// Disables new type until species chosen
			}
		});

		if (updateAd) {
		} else {chbNewType.setDisable(true);}		// Checkbox disables by default if a new ad is to be entered
		
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
		
		if (updateAd) {cbSpecies.setValue(ad.getSpecies());
		} else {cbSpecies.setValue("Species");}
		cbSpecies.setMinWidth(150);					// Setting size of the CB
		cbSpecies.setMaxWidth(150);
		cbSpecies.setOnAction(e -> {				// On action (selection) it should...
			InputValidation.validateChoiceBox(cbSpecies);
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
		if (updateAd) {
			cbType = new ChoiceBox<>(db.createObservableList("Type", db.fetchResult(
				db.executeQuery("SELECT DISTINCT Type "
						+ "FROM Ads "
						+ "WHERE Species = '" + ad.getSpecies() + "' "
						+ "ORDER BY Type;"))));
			db.closeConnection();
			
			cbType.setValue(ad.getType());
			cbType.setDisable(false);				
		} else {
			cbType = new ChoiceBox<>(db.createObservableList("Type", db.fetchResult(
				db.executeQuery("SELECT DISTINCT Type "
						+ "FROM Ads "
						+ "ORDER BY Type;"))));
			db.closeConnection();
			
			cbType.setValue("Type");
			cbType.setDisable(true);				// Type CB is disabled by default when a new ad is to be added
		}
		cbType.setMinWidth(150);					// Setting size of type CB
		cbType.setMaxWidth(150);
		cbType.setOnAction(e -> {
		try {
			InputValidation.validateChoiceBox(cbType);
		} catch (Exception error) {}			// When Type is activated by selecting a species, validate points to null
		});											// which throws an error, catch lets us ignore it since it is not function critical.

		chbReActivate = new CheckBox("Re-activate?");
		chbReActivate.setVisible(false);
		chbReActivate.setStyle("-fx-text-fill: red;");
		if (updateAd) {	
			if (InputValidation.checkEndAfterToday(ad)) {chbReActivate.setVisible(true);} 
		}
		
		gridPane.add(cbGenders, 1, 0);
		gridPane.add(cbSpecies, 0, 2);				// Adding CB for species to gridpane, column 0, row 3
		gridPane.add(cbType, 1, 2);
		gridPane.add(chbNewSpecies, 0, 2);
		gridPane.add(chbNewType, 1, 2);
		gridPane.add(chbReActivate, 1, 5);
		GridPane.setMargin(cbGenders, 		new Insets(0, 0, 0, 143));
		GridPane.setMargin(cbType, 			new Insets(0, 0, 0, -17));
		GridPane.setMargin(chbNewSpecies, 	new Insets(0, 0, 0, 155));	// Displacing CHB for new species 110 pixels to the left, to appear after the species CB/TF
		GridPane.setMargin(chbNewType, 		new Insets(0, 0, 0, 137));
		GridPane.setMargin(chbReActivate, new Insets(0, 0, 0, -120));

		return gridPane;
	}

	/**
	 * Updates the incoming GridPane with boxes for input information.
	 * 
	 * @param initialized GridPane to add buttons to and a boolean.
	 * @return GridPane containing the buttons neccessary for inputing information about ads.
	 * 
	 * @author Måns Thörnvik
	 */

	private static GridPane addInputButtons(GridPane inputGridPane, boolean updateAd) {
		GridPane gridPane = inputGridPane;
		Button btnAddPicture, btnSaveAd;
		Alert alert;

		myImageView = new ImageView();
		if (updateAd) {myImageView = ad.getPicture();}
		myImageView.setPreserveRatio(false);
		myImageView.setFitWidth(100);
		myImageView.setFitHeight(100);

		btnAddPicture = new Button("Upload picture");
		btnAddPicture.setMinSize(50, 50);
		btnAddPicture.setOnAction(loadPicture);		// Button for adding pictures guides you to a new window

		alert = new Alert(AlertType.INFORMATION);

		btnSaveAd = new Button("Save");
		btnSaveAd.setMinWidth(110);
		btnSaveAd.setMaxWidth(110);
		btnSaveAd.setOnAction(e -> {
			if (InputValidation.validateAdInfo(tfName, tfAge, cbGenders, cbSpecies, cbType, 
											   tfNewSpecies, tfNewType, taDescription)) {
				System.out.println(">> Input values OK");
				
				if (updateAd) {
					updateMemberAd();
				} else {
					inputAdValues();
				}
			
				if (file != null) {	
					try {
						if (updateAd) {
							inputUpdatePicture(ad.getID(), file, true);
							
							alert.setAlertType(AlertType.INFORMATION);
							alert.setTitle("Success");
							alert.setHeaderText("Advertisement has been updated");
							if (chbReActivate.isSelected()) {
								alert.setContentText("Your advertisement is now updated and visible to all system users for 90 days.");
							} else {
								alert.setContentText("Your advertisement now has its updated information visible.");
							}
							alert.showAndWait();
						} else {
							inputUpdatePicture(0, file, true);
							
							alert.setAlertType(AlertType.INFORMATION);
							alert.setTitle("Success");
							alert.setHeaderText("Advertisement has been submitted");
							alert.setContentText("Your advertisement is now visible to all system users for 90 days.");
							alert.showAndWait();
						}
					} catch (Exception e1) {
						System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
					}
					file = null;
				}
				MemberPage.refreshTable();
			
				MemberPage.back();
			} else {
				System.out.println(">> Input values incorrect");
				alert.setAlertType(AlertType.ERROR);
				alert.setTitle("Failiure");
				alert.setHeaderText("Information could not be saved");
				alert.setContentText("You need to input correct values into all fields before saving.");
				alert.showAndWait();
			}
		});

		gridPane.add(btnAddPicture, 0, 4);
		gridPane.add(myImageView, 1, 4);
		gridPane.add(btnSaveAd, 0, 5);
		GridPane.setMargin(myImageView, new Insets(0, 0, 0, -120));

		return gridPane;
	}

	/**
	 * Eventhandler for choosing a PNG file through FileChooser and displaying it in the same window.
	 * 
	 * @author Yu Jet Hua
	 */

	private static EventHandler<ActionEvent> loadPicture = new EventHandler<ActionEvent>() {

		public void handle(ActionEvent t) {
			fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
			fileChooser.getExtensionFilters().addAll(extFilterPNG);
			file = fileChooser.showOpenDialog(null);
			try {
				BufferedImage bufferedImage = ImageIO.read(file);
				Image image = SwingFXUtils.toFXImage(bufferedImage, null);
				if (image != null) {myImageView.setImage(image);}
			} catch (Exception ex) {
				System.err.println(">> No image was chosen or another error was encountered...");
			}
		}
	};
	
	/**
	 * Upload a picture to a specific ad/agency by making the outputStream of the image into a ByteArray and then inserting
	 * arrays of 1024 byte until the image has been completely uploaded into the database.
	 * 
	 * @param int, File and a boolean.
	 * 
	 * @author Yu Jet Hua
	 * @throws Exception
	 */

	public static void inputUpdatePicture(int inputID, File file, boolean inputAd) {
		byte[] image = null;
		int result = 0;
		int id = 0;

		try {				
			FileInputStream inputStream = new FileInputStream(file);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
		
			for (int readNum; (readNum = inputStream.read(buffer)) != -1;) {
				outputStream.write(buffer, 0, readNum);
				System.out.println(">> Read " + readNum + " bytes,");
			}
			image = outputStream.toByteArray();
			inputStream.close();
			outputStream.close();
			if (inputAd) {
				if (inputID == 0) {
					id = Integer.parseInt(db.fetchResult(db.executeQuery("SELECT ID FROM Ads ORDER BY ID DESC;")).get(0).get(0));
					db.closeConnection();
					
				} else {id = inputID;}
				result = db.updatePicture("UPDATE Ads SET Picture = ? WHERE ID = ?;", image, id);
			} else {
				if (inputID == 0) {
					id = Integer.parseInt(db.fetchResult(db.executeQuery("SELECT ID FROM Agencies ORDER BY ID DESC;")).get(0).get(0));
					db.closeConnection();
					
				} else {id = inputID;}
				result = db.updatePicture("UPDATE Agencies SET Logo = ? WHERE ID = ?;", image, id);
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try {
				if (!db.getConnection().isClosed()) {db.closeConnection();}
			} catch (SQLException e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}
		if (result > 0) {System.out.println(">> Image Uploaded successfully");}
	}

	/**
	 * Method for finalizing and inputing values into the database, based on what has been entered into the input page GUI.
	 * Values entered into TextFields and ChoiceBoxes are first validated before submitted with an INSERT.
	 * 
	 * @author Mattias Landkvist & Måns Thörnvik
	 */

	private static void inputAdValues() {
		String name, age, gender, description, species, type;

		name = correctInput(tfName.getText().trim());	// Trimming TFs with possibility of spaces
		age = tfAge.getText();
		if (age.length() > 1 && age.charAt(0) == '0') {age = age.substring(1);}
		gender = (String) cbGenders.getValue();
		description = correctInput(taDescription.getText().trim());	// Trimming TAs with possibility of spaces
		if (InputValidation.validateChoiceBox(cbSpecies)) {	// If gets executed when the default value of CB is NOT chosen
			species = (String) cbSpecies.getValue();
		} else {species = correctInput(tfNewSpecies.getText().trim());}
		if (InputValidation.validateChoiceBox(cbType)) {
			type = (String) cbType.getValue();
		} else {type = correctInput(tfNewType.getText().trim());}
		String insert = "INSERT INTO Ads (Name, Species, Type, Gender, Age, Description, AgencyID) ";
		String values = "VALUES ('" + name + "', '" + species + "', '" + type + "', '" + gender + "', " + age
				+ ", '" + description + "', " + agency.getID() + ");";		// Exchange 1 with the agencies ID.
		System.out.println(">> " + insert + "\n" + ">> " + values);
		db.executeUpdate(insert + values);
	}
	
	/**
	 * Method for finalizing and inputing values into the database, based on what has been entered into the agency input 
	 * page GUI.
	 * 
	 * @param TextField, TextField, TextField, PasswordField, TextField, TextField, TextField.
	 * 
	 * @author Mattias Landkvist & Måns Thörnvik
	 */
	
	public static void inputAgencyValues(TextField tfName, TextField tfPhone, TextField tfEmail, PasswordField pfPassword, 
										 TextField tfStreet, TextField tfZip, TextField tfCity)  {
		String name, phone, email, password, street, zip, city;
		
		name = correctInput(tfName.getText().trim());
		email = tfEmail.getText();
		phone = tfPhone.getText();
		password = pfPassword.getText();
		String insertInfo = "INSERT INTO Agencies (Name, Phone, Email, Password)";
		String valuesInfo = " VALUES ('" + name  + "', '"+ phone + "', '" + email + "', '" + password + "');";
		System.out.println(">> " + insertInfo + valuesInfo);
		db.executeUpdate(insertInfo + valuesInfo);
		
		int agencyID = Integer.parseInt(db.fetchResult(db.executeQuery("SELECT ID FROM "
																	 + "Agencies ORDER BY "
																	 + "ID DESC;")).get(0).get(0));
		db.closeConnection();
		street = correctInput(tfStreet.getText().trim());
		zip = tfZip.getText();
		city = correctInput(tfCity.getText().trim());
		String insertAddress = "INSERT INTO Addresses (Street, ZIP, City, AgencyID)";
		String valuesAddress = " VALUES ('" + street + "', '" + zip + "', '" + city + "', " + agencyID + ");";
		System.out.println(">> " + insertAddress + valuesAddress);
		db.executeUpdate(insertAddress + valuesAddress); 
		// Default rating inserted so that agencies can log in upon account creation.
		db.executeUpdate("INSERT INTO Ratings (Rating, Comment, AgencyID) VALUES (0, 'N/A', " + agencyID + ");");
	}
	
	/**
	 * Updates the input Ad with the information in the filled out fields. If CHB re-activate has been checked, the ad will
	 * set its start date to todays date and its end date to 90 days in the future. Updates the ad table when done.
	 * 
	 * @author Måns Thörnvik
	 */
	
	public static void updateMemberAd() {
		String updateAd, values, where;
		updateAd = "UPDATE Ads SET ";
		values = "";
		where = " WHERE ID = " + ad.getID() + ";";
		
		values += "Name = '" + correctInput(tfName.getText().trim()) + "', ";			// Trimming TFs with possibility of spaces
		values += "Age = " + tfAge.getText() + ", ";
		values += "Gender = '" + (String) cbGenders.getValue() + "', ";
		values += "Description = '" + correctInput(taDescription.getText().trim()) + "', ";	// Trimming TAs with possibility of spaces
		if (InputValidation.validateChoiceBox(cbSpecies)) {				 				// If gets executed when the default value of CB is NOT chosen
			values += "Species = '" + (String) cbSpecies.getValue() + "', ";
		} else {values += "Species = '" + correctInput(tfNewSpecies.getText().trim()) + "', ";}

		if (InputValidation.validateChoiceBox(cbType)) {
			values += "Type = '" + (String) cbType.getValue() + "'";
		} else {values += "Type = '" + correctInput(tfNewType.getText().trim()) + "'";}
		if (chbReActivate.isSelected()) {
			values += ", StartDate = date('NOW'), EndDate = date('NOW', '+90 days')";
		}
		updateAd += values + where;
		System.out.println(">> " + updateAd.substring(0, 10) + ": " + ad.getID());
		db.executeUpdate(updateAd);
	}
	
	/**
	 * This method updates new input information from the TextFields after the "Save" button has been pressed, assuming
	 * any new, valid information has been entered.
	 * 
	 * @author Måns Thörnvik
	 */

	public static void updateMemberInfo(AgencyExt inputAgency, TextField name, TextField phone, TextField email, 
										TextField street, TextField zip, TextField city) {
		String updateInfo, updateAddress, valuesInfo, valuesAddress, whereInfo, whereAddress;
		updateInfo = "UPDATE Agencies SET ";
		updateAddress = "UPDATE Addresses SET ";
		valuesInfo = "";
		valuesAddress = "";
		whereInfo = " WHERE ID = " + inputAgency.getID() + ";";
		whereAddress = " WHERE Addresses.AgencyID = " + inputAgency.getID() + ";";
	
		valuesInfo += "Name = '" + correctInput(name.getText()) + "', ";
		valuesInfo += "Phone = '" + phone.getText() + "', ";
		valuesInfo += "Email = '" + email.getText() + "'";
		updateInfo += valuesInfo + whereInfo;
		System.out.println(">> INFO " + updateInfo.substring(0, 10) + ": " + inputAgency.getID());
		db.executeUpdate(updateInfo);
		
		valuesAddress += "Street = '" + correctInput(street.getText().trim()) + "', ";
		valuesAddress += "ZIP = '" + zip.getText() + "', ";
		valuesAddress += "City = '" + correctInput(city.getText().trim()) + "'";
		updateAddress += valuesAddress + whereAddress;
		System.out.println(">> ADDRESS " + updateAddress.substring(0, 10) + ": " + inputAgency.getID());
		db.executeUpdate(updateAddress);
	}
	
	/**
	 * This method updates the users password information from the TextFields after the "Save" button has been pressed, assuming
	 * any new, valid information has been entered.
	 * 
	 * @author Måns Thörnvik
	 */
	
	public static void updateMemberPassword(AgencyExt inputAgency, PasswordField password) {
		String updatePassword, value, where;
		updatePassword = "UPDATE Agencies SET ";
		value = "Password = '" + password.getText() + "'";
		where = " WHERE ID = " + inputAgency.getID() + ";";
		
		updatePassword += value + where;
		System.out.println(">> PASSWORD " + updatePassword.substring(0, 10) + ": " + inputAgency.getID());
		db.executeUpdate(updatePassword);
	}
	
	/**
	 * Check for ' in the input to fix them
	 * 
	 * @param String to check
	 * 
	 * @author Måns Thörnvik
	 */
	
	public static String correctInput(String input) {
		String correctedInput = input;
		
		for (int index = 0; index < correctedInput.length(); index++) {
			String temp = "";
			if (index < correctedInput.length() - 1) {	
				if ((correctedInput.charAt(index) == '\'') &&
					(correctedInput.charAt(index - 1) != '\'') &&
					(correctedInput.charAt(index + 1) != '\'')) {
					temp = correctedInput.substring(0, index) + "''";
					correctedInput = temp + correctedInput.substring(index + 1);
				} 
			} else if ((correctedInput.charAt(index) == '\'') && 
					correctedInput.charAt(index - 1) != '\'') {
				correctedInput += "'";
			}
		}
		
		return correctedInput;
	}
	
	/**
	 * Delete an ad.
	 * 
	 * @param Ad to delete
	 * 
	 * @author Måns Thörnvik
	 */
	
	public static void deleteMemberAd(Ad ad) {
		System.out.println(">> DELETE FROM Ads: " + ad.getID());
		db.executeUpdate("DELETE FROM Ads WHERE ID = " + ad.getID() + ";");		
	}
	
	/**
	 * @return AgencyExt from global variable agency.
	 * 
	 * @author Måns Thörnvik
	 */
	
	public static AgencyExt getAgency() {
		return agency;
	}
	
	/**
	 * Set global variable agency to input AgencyExt
	 * 
	 * @param AgencyExt
	 * 
	 * @author Måns Thörnvik
	 */
	
	public static void setAgency(AgencyExt inputAgency) {
		agency = inputAgency;
	}
	
	/**
	 * @return Ad from global variable ad.
	 * 
	 * @author Måns Thörnvik
	 */
	
	public static Ad getAd() {
		return ad;
	}
	
	/**
	 * Set global variable ad to input Ad
	 * 
	 * @param Ad
	 * 
	 * @author Måns Thörnvik
	 */
	
	public static void setAd(Ad inputAd) {
		ad = inputAd;
	}
}