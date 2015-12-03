import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Page for showing member information and allowing for updates of both the specific member's personal information as well
 * as advertisements submitted by him/her.
 * 
 * @author Måns Thörnvik for functionality & Madisen Whitfield for partial design.
 */

public class MemberPage {

	private static Stage window;
	private static Scene sceneMP, sceneUpdateAd;
	private static BorderPane layoutMP, layoutUpdateAd;
	
	private static TextField tfName, tfPhone, tfEmail, tfStreet, tfZip, tfCity;
	private static PasswordField pfPassword, pfConfirmPassword;
	
	private static TextField tfAdName, tfAdAge, tfAdNewSpecies, tfAdNewType;
	private static ChoiceBox<Object> cbAdGenders, cbAdSpecies, cbAdType;
	private static TextArea taAdDescription;
	private static CheckBox chbNewSpecies, chbNewType, chbReActivate;
	
	private static AgencyExt agencyInfo;
	
	private static ObservableList<Object> obsListType;
	
	private static String selectAgency = "SELECT Agencies.ID, Logo, Name, AVG(Rating), Email, Phone, Street, ZIP, City FROM "
									   + "Agencies, Addresses, Ratings WHERE "
									   + "Agencies.ID = Addresses.AgencyID and "
									   + "Agencies.ID = Addresses.AgencyID and "
									   + "Agencies.ID = 1;";
	
	private static DBobject db = new DBobject();

	/**
	 * Displays the member page when called. This page lets the user see information about the account and allows for
	 * alteration of this information as well as advertisements associated with this particular user.
	 * 
	 */

	public static void display() {
//		agencyInfo = agency;			// This is where the agency should come in.
		
		window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Your Account");
		window.setMinWidth(250);

		layoutMP = new BorderPane();
		
		layoutMP.setTop(Header.smallHeader());
		layoutMP.setCenter(viewMemberInfo());
		layoutMP.setBottom(viewMemberAds());
		
		sceneMP = new Scene(layoutMP, 600, 750);

		window.setScene(sceneMP);
		window.showAndWait();
	}

	/**
	 * Creates a gridpane in which user/member information is shown.
	 * 
	 * @return GridPane outfitted with fields showing the user his or her information.
	 */
	
	private static GridPane viewMemberInfo() {
		GridPane gridPane = new GridPane();
		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		ColumnConstraints col3 = new ColumnConstraints();
		
		
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setAlignment(Pos.BOTTOM_CENTER);
		gridPane.setPadding(new Insets(5, 5, 5, 5));
		
		
		col1.setHalignment(HPos.RIGHT);
		col1.setPercentWidth(33);
		col2.setHalignment(HPos.LEFT);
		col2.setPercentWidth(33);
		col3.setHalignment(HPos.LEFT);
		col3.setPercentWidth(33);
		gridPane.getColumnConstraints().addAll(col1, col2, col3);
		
		
		agencyInfo = db.fetchAgencyExt(db.executeQuery(selectAgency)).get(0);	// Loads the current user's information.
		db.closeConnection();
		
		
		addMemberLabels(gridPane);				// Calls a method to add labels to the members view.
		addMemberTextFields(gridPane);			// Calls a method to add text fields to the members view.
		addMemberButtons(gridPane);				// Calls a method to add buttons to the members view.
		
		
		return gridPane;
	}
	
	/**
	 * Updates the incoming GridPane with labels for member information.
	 * 
	 * @param initialized GridPane to add labels to.
	 * @return GridPane containing the labels neccessary for showing member information.
	 */
	
	public static GridPane addMemberLabels(GridPane inputGridPane) {
		GridPane gridPane = inputGridPane;
		Label lblAgencyInfo, lblPassword, lblAdInfo;
		
		
		lblAgencyInfo = new Label("Your information:");
		
		lblPassword = new Label("Change password:");
		
		lblAdInfo = new Label("Click an item below to edit");
		lblAdInfo.setFont(Font.font("Lucida Grande", FontWeight.BOLD, 13));
		
		gridPane.add(lblAgencyInfo, 0, 0);
		gridPane.add(lblPassword, 2, 0);
		gridPane.add(lblAdInfo, 1, 4);
		GridPane.setMargin(lblAgencyInfo, new Insets(-45, 43, 0, 0));
		GridPane.setMargin(lblPassword, new Insets(-45, 0, 0, 0));
		GridPane.setMargin(lblAdInfo, new Insets(10, 0, 0, 0));	
		
		
		return gridPane;
	}
	
	/**
	 * Updates the incoming GridPane with TextFields for member information.
	 * 
	 * @param initialized GridPane to add TextFields to.
	 * @return GridPane containing the TextFields neccessary for showing and changing member information.
	 */
	
	private static GridPane addMemberTextFields(GridPane inputGridPane) {
		GridPane gridPane = inputGridPane;
		
		
		tfName = new TextField();
		tfName.setEditable(false);			// Cannot edit Agency name?
		tfName.setText(agencyInfo.getName());
		tfName.setMinWidth(150);
		tfName.setMaxWidth(150);
		
		
		tfPhone = new TextField();
		tfPhone.setEditable(false);
		tfPhone.setText(agencyInfo.getPhone());
		tfPhone.setPromptText("Phone...");
		tfPhone.setMinWidth(150);
		tfPhone.setMaxWidth(150);
		tfPhone.setOnKeyReleased(e -> {
			validateInputPhone(tfPhone);
		});
		
		
		tfEmail = new TextField();
		tfEmail.setEditable(false);
		tfEmail.setText(agencyInfo.getEmail());
		tfEmail.setPromptText("Email...");
		tfEmail.setMinWidth(150);
		tfEmail.setMaxWidth(150);
		tfEmail.setOnKeyReleased(e -> {
			validateInputEmail(tfEmail);
		});
		
		
		tfStreet = new TextField();
		tfStreet.setEditable(false);
		tfStreet.setText(agencyInfo.getStreet());
		tfStreet.setPromptText("Street...");
		tfStreet.setMinWidth(150);
		tfStreet.setMaxWidth(150);
		tfStreet.setOnKeyReleased(e -> {
			validateInputStreet(tfStreet);
		});
		
		
		tfZip = new TextField();
		tfZip.setEditable(false);
		tfZip.setText(agencyInfo.getZip());
		tfZip.setPromptText("ZIP code...");
		tfZip.setMinWidth(100);
		tfZip.setMaxWidth(100);
		tfZip.setOnKeyReleased(e -> {
			validateInputZip(tfZip);
		});
		
		
		tfCity = new TextField();
		tfCity.setEditable(false);
		tfCity.setText(agencyInfo.getCity());
		tfCity.setPromptText("City...");
		tfCity.setMinWidth(150);
		tfCity.setMaxWidth(150);
		tfCity.setOnKeyReleased(e -> {
			validateInputCity(tfCity);
		});
		
		gridPane.add(tfName, 0, 0);
		gridPane.add(tfPhone, 0, 1);
		gridPane.add(tfEmail, 0, 2);
		gridPane.add(tfStreet, 1, 0);
		gridPane.add(tfZip, 1, 1);
		gridPane.add(tfCity, 1, 2);
		
		
		pfPassword = new PasswordField();
		pfPassword.setPromptText("Password...");
		pfPassword.setMinWidth(100);
		pfPassword.setMaxWidth(100);
		
		
		pfConfirmPassword = new PasswordField();
		pfConfirmPassword.setPromptText("Confirm...");
		pfConfirmPassword.setMinWidth(100);
		pfConfirmPassword.setMaxWidth(100);
		
		gridPane.add(pfPassword, 2, 0);
		gridPane.add(pfConfirmPassword, 2, 1);
		
		
		return gridPane;
	}
	
	/**
	 * Updates the incoming GridPane with buttons for member information.
	 * 
	 * @param initialized GridPane to add buttons to.
	 * @return GridPane containing the buttons neccessary for showing and changing member information.
	 */
	
	public static GridPane addMemberButtons(GridPane inputGridPane) {
		GridPane gridPane = inputGridPane;
		Button btnEditInfo, btnSaveInfo, btnCancelEdit, btnSavePassword, btnInputPage;
		Alert alert;
		
		alert = new Alert(AlertType.ERROR);
		
		btnEditInfo = new Button("Edit");
		btnSaveInfo = new Button("Save");
		btnCancelEdit = new Button("Cancel");
		
		btnEditInfo.setMinWidth(50);
		btnEditInfo.setMaxWidth(50);
		btnEditInfo.setOnAction(e -> {
			btnEditInfo.setVisible(false);
			btnSaveInfo.setVisible(true);
			btnCancelEdit.setVisible(true);
			
			tfPhone.setEditable(true);
			
			tfEmail.setEditable(true);
			
			tfStreet.setEditable(true);
			
			tfZip.setEditable(true);
			
			tfCity.setEditable(true);
		});
		
		btnSaveInfo.setMinWidth(50);
		btnSaveInfo.setMaxWidth(50);
		btnSaveInfo.setVisible(false);
		btnSaveInfo.setOnAction(e -> {
			if ((validateInputPhone(tfPhone)) && 
				(validateInputEmail(tfEmail)) && 
				(validateInputStreet(tfStreet)) && 
				(validateInputZip(tfZip)) && 
				(validateInputCity(tfCity))) {
				
				btnSaveInfo.setVisible(false);
				btnCancelEdit.setVisible(false);
				btnEditInfo.setVisible(true);
				
				updateMemberInfo();
				
				tfPhone.setText(agencyInfo.getPhone());
				tfPhone.setEditable(false);
			
				tfEmail.setText(agencyInfo.getEmail());
				tfEmail.setEditable(false);
			
				tfStreet.setText(agencyInfo.getStreet());
				tfStreet.setEditable(false);
			
				tfZip.setText(agencyInfo.getZip());
				tfZip.setEditable(false);
			
				tfCity.setText(agencyInfo.getCity());
				tfCity.setEditable(false);
			} else {
				alert.setHeaderText("Changes could not be saved");
				alert.setContentText("Input changes are not valid and cannot be saved.");
				alert.showAndWait();
			}
		});
		
		btnCancelEdit.setMinWidth(70);
		btnCancelEdit.setMaxWidth(70);
		btnCancelEdit.setVisible(false);
		btnCancelEdit.setOnAction(e -> {
			btnSaveInfo.setVisible(false);
			btnCancelEdit.setVisible(false);
			btnEditInfo.setVisible(true);
			
			resetMemberTextFields();
			
			tfPhone.setText(agencyInfo.getPhone());
			tfPhone.setEditable(false);
		
			tfEmail.setText(agencyInfo.getEmail());
			tfEmail.setEditable(false);
		
			tfStreet.setText(agencyInfo.getStreet());
			tfStreet.setEditable(false);
		
			tfZip.setText(agencyInfo.getZip());
			tfZip.setEditable(false);
		
			tfCity.setText(agencyInfo.getCity());
			tfCity.setEditable(false);
		});
		

		btnSavePassword = new Button("Save");
		btnSavePassword.setMinWidth(50);
		btnSavePassword.setMaxWidth(50);
		btnSavePassword.setOnAction(e -> {
			validateInputPassword(pfPassword);
			validateInputPassword(pfConfirmPassword);
			if (validateInputPassword(pfPassword) && 
				pfPassword.getText().equals(pfConfirmPassword.getText())) {
				
				updateMemberPassword();
				
				pfPassword.clear();
				pfConfirmPassword.clear();
			}
		});
		
		
		btnInputPage = new Button("New ad");
		btnInputPage.setOnAction(e -> {
			InputPage.display();
		});
		
		
		gridPane.add(btnEditInfo, 0, 3);
		gridPane.add(btnSaveInfo, 0, 3);
		gridPane.add(btnCancelEdit, 0, 3);
		gridPane.add(btnSavePassword, 2, 2);
		gridPane.add(btnInputPage, 2, 4);
		GridPane.setHalignment(btnEditInfo, HPos.LEFT);
		GridPane.setMargin(btnEditInfo, new Insets(0, 0, 0, 38));
		GridPane.setHalignment(btnSaveInfo, HPos.LEFT);
		GridPane.setMargin(btnSaveInfo, new Insets(0, 0, 0, 38));
		GridPane.setHalignment(btnCancelEdit, HPos.LEFT);
		GridPane.setMargin(btnCancelEdit, new Insets(0, 0, 0, 100));
		
		
		return gridPane;
	}
	
	/**
	 * This method is used to generate a TableView of ads, clickable by the user to display additional information about
	 * it, as well as being able to alter it.
	 * 
	 * @return HBox containing a TableView of all the members associated advertisements.
	 */
	
	@SuppressWarnings("unchecked")
	public static HBox viewMemberAds() {
		HBox hbTable = new HBox();
		TableView<Ad> table = new TableView<Ad>();		
		TableColumn<Ad, String> tcName 		=	new TableColumn<>("Name");
		TableColumn<Ad, String> tcGender 	=	new TableColumn<>("Gender");
		TableColumn<Ad, String> tcSpecies 	= 	new TableColumn<>("Species");
		TableColumn<Ad, String> tcType 		=	new TableColumn<>("Type");
		TableColumn<Ad, String> tcStartDate = 	new TableColumn<>("Submitted");
		TableColumn<Ad, String> tcEndDate 	= 	new TableColumn<>("Expires");
		String sql = "SELECT Distinct Ads.ID, Picture, Ads.Name, Species, Type, Gender, Age, Description, "
				   + "StartDate, EndDate, Ads.AgencyID, Agencies.Name as Agency, "
				   + "(SELECT AVG(Rating) "
				   + "FROM Agencies, Ratings "
				   + "WHERE Agencies.ID = Ratings.AgencyID and "
				   + "Agencies.ID = " + agencyInfo.getID() + ") as Rating "
				   + "FROM Ads, Agencies, Addresses, Ratings "
				   + "WHERE Agencies.ID = Addresses.AgencyID and "
				   + "Agencies.ID = Ratings.AgencyID and "
				   + "Agencies.ID = Ads.AgencyID and "
				   + "Agencies.ID = " + agencyInfo.getID() + " "
				   + "ORDER BY StartDate DESC;";
		ArrayList<Ad> alAgencyAds = db.fetchAd(db.executeQuery(sql));
		db.closeConnection();
		ObservableList<Ad> olAgencyAds = db.createObservableList((alAgencyAds));
		
		
		hbTable.setPadding(new Insets(5, 5, 5, 5));
		hbTable.setMinSize(590, 350);
		hbTable.setMaxSize(500, 350);
		
		
		table.setEditable(true);
		table.setMinSize(590, 340);
		table.setMaxSize(590, 340);
		table.setRowFactory(e -> {
			TableRow<Ad> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!tableRow.isEmpty()) ) {
					Ad ad = (Ad) tableRow.getItem();
					
					layoutUpdateAd = new BorderPane();
					layoutUpdateAd.setTop(Header.smallHeader());
					layoutUpdateAd.setCenter(viewUpdateMemberAd(ad));

					sceneUpdateAd = new Scene(layoutUpdateAd, 600, 550);

					window.setScene(sceneUpdateAd);
				}
			});
			return tableRow ;
		});
		

		tcName.setCellValueFactory(		new PropertyValueFactory<>("Name"));
		tcGender.setCellValueFactory(	new PropertyValueFactory<>("Gender"));
		tcSpecies.setCellValueFactory(	new PropertyValueFactory<>("Species"));
		tcType.setCellValueFactory(		new PropertyValueFactory<>("Type"));
		tcStartDate.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
		tcEndDate.setCellValueFactory(	new PropertyValueFactory<>("EndDate"));
		
		tcName.setPrefWidth(100);
		tcGender.setPrefWidth(65);
		tcSpecies.setPrefWidth(105);
		tcType.setPrefWidth(115);
		tcStartDate.setPrefWidth(80);
		tcEndDate.setPrefWidth(80);
		
		
		table.setItems(olAgencyAds);
		table.getColumns().addAll(tcName, tcGender, tcSpecies, tcType, tcStartDate, tcEndDate);
		
		
		hbTable.getChildren().add(table);
		
		
		return hbTable;
	}
	
	/**
	 * This method generates a view for when an ad in the members Ad TableView is pressed.
	 * 
	 * @param the Ad that was clicked in the members TableView.
	 * @return GridPane outfitted with information about the clicked ad, as well as the option for the user to alter its
	 * information.
	 */
	
	public static GridPane viewUpdateMemberAd(Ad ad) {
		GridPane gridPane = new GridPane();
		
		
		gridPane.setHgap(7);
		gridPane.setVgap(7);
		gridPane.setPadding(new Insets(5, 5, 5, 5));
		gridPane.getColumnConstraints().setAll(new ColumnConstraints(250, 250, 250));
		gridPane.setAlignment(Pos.CENTER);
		
		
		addAdLabels(gridPane, ad);
		addAdTextFields(gridPane, ad);
		addAdBoxes(gridPane, ad);
		addAdButtons(gridPane, ad);
		
	
		return gridPane;
	}
	
	/**
	 * Updates the incoming GridPane with Labels for Ad information.
	 * 
	 * @param initialized GridPane to add Labels to.
	 * @return GridPane containing the labels neccessary for updating Ad information.
	 */
	
	public static GridPane addAdLabels(GridPane inputGridPane, Ad ad) {
		GridPane gridPane = inputGridPane;
		Label lblName, lblAge, lblGender, lblActiveInfo;
		
		
		lblName = new Label("Name");
		lblAge = new Label("Age");
		lblGender = new Label("Gender");
		lblActiveInfo = new Label("This ad has been removed from the site");
		lblActiveInfo.setVisible(false);
		
		if (checkEndAfterToday(ad)) {		// Comparing today's date with that of the ad, sets label and CHB visible depending
			lblActiveInfo.setVisible(true);	// on the result.
		} else {}
		
		gridPane.add(lblName, 0, 0);
		gridPane.add(lblAge, 0, 0);
		gridPane.add(lblGender, 1, 0);
		gridPane.add(lblActiveInfo, 1, 4);		
		GridPane.setMargin(lblName, 	new Insets(-45, 0, 0, 0));
		GridPane.setMargin(lblAge, 		new Insets(-45, 0, 0, 160));
		GridPane.setMargin(lblGender, 	new Insets(-45, 0, 0, 143));
		GridPane.setMargin(lblActiveInfo, new Insets(-45, 0, 0, 0));
		
		
		return gridPane;
	}
	
	/**
	 * Updates the incoming GridPane with TextFields for Ad information.
	 * 
	 * @param initialized GridPane to add TextFields to.
	 * @return GridPane containing the TextFields neccessary for updating Ad information.
	 */
	
	public static GridPane addAdTextFields(GridPane inputGridPane, Ad ad) {
		GridPane gridPane = inputGridPane;
		
		
		tfAdName = new TextField();
		tfAdName.setText(ad.getName());
		tfAdName.setMinWidth(150);
		tfAdName.setMaxWidth(150);
		tfAdName.setOnKeyReleased(e -> {
			InputPage.validateInputString(tfAdName);
		});
		
		
		tfAdAge = new TextField();
		tfAdAge.setText(ad.getAge() + ""); // need "" since its and integer
		tfAdAge.setMinWidth(50);
		tfAdAge.setMaxWidth(50);
		tfAdAge.setOnKeyReleased(e -> {
			InputPage.validateInputInteger(tfAdAge);
		});
		
		
		taAdDescription = new TextArea();
		taAdDescription.setText(ad.getDescription());
		taAdDescription.setMinSize(500, 150);
		taAdDescription.setMaxSize(500, 150);
		taAdDescription.setOnKeyReleased(e -> {
			InputPage.validateInputTextArea(taAdDescription);
		});
		
		
		tfAdNewSpecies = new TextField();				// TF for new species
		tfAdNewSpecies.setMinWidth(100);				// Setting size for TF
		tfAdNewSpecies.setMaxWidth(100);
		tfAdNewSpecies.setVisible(false);				// TF not visible by default
		tfAdNewSpecies.setPromptText("Species...");	// Prompt text for TF to be displayed in the background
		tfAdNewSpecies.setOnKeyReleased(e -> {
			InputPage.validateInputString(tfAdNewSpecies);
			if (tfAdNewSpecies.getLength() != 0) {	// This actually becomes true when 2 characters have been entered, for some reason
				cbAdType.setDisable(false);			// The type CB should no longer be disabled
				chbNewType.setDisable(false);		// CHB should no longer be disables
			} else if (tfAdNewSpecies.getLength() == 0) {	// As soon as text goes below 2 chars:
				tfAdNewType.clear();					// Clear the TF of new type if it had been in use
				tfAdNewType.setVisible(false);		// Set new type TF not visible
				
				cbAdType.setValue("Type");			// Set value of type CB back to Type and
				cbAdType.setDisable(true);			// Disable type CB again
				cbAdType.setVisible(true);			// Make type CB visible again, if it had been deactivated and TF for new type used
				
				chbNewType.setSelected(false);		// Deselects the CHB for new type if it was in use
				chbNewType.setDisable(true);		// Disables option for new typ CHB
			}
		});

		
		tfAdNewType = new TextField();				// TF for new types
		tfAdNewType.setMinWidth(100);
		tfAdNewType.setMaxWidth(100);
		tfAdNewType.setVisible(false);
		tfAdNewType.setPromptText("Type...");
		tfAdNewType.setOnKeyReleased(e -> {
			InputPage.validateInputString(tfAdNewType);
		});
		
		
		gridPane.add(tfAdName, 0, 0);
		gridPane.add(tfAdAge, 0, 0);
		GridPane.setColumnSpan(taAdDescription, 2);
		gridPane.add(taAdDescription, 0, 3);
		gridPane.add(tfAdNewSpecies, 0, 2);			// Adding CHB for new species to gridpane, column 0, row 3
		gridPane.add(tfAdNewType, 1, 2);
		GridPane.setMargin(tfAdAge, 	new Insets(0, 0, 0, 160));
		GridPane.setMargin(tfAdNewType, 		new Insets(0, 0, 0, -97));
		
		
		return gridPane;
	}
	
	/**
	 * Updates the incoming GridPane with boxes (check and choice) for Ad information.
	 * 
	 * @param initialized GridPane to add Boxes to.
	 * @return GridPane containing the Boxes neccessary for updating Ad information.
	 */
	
	public static GridPane addAdBoxes(GridPane inputGridPane, Ad ad) {
		GridPane gridPane = inputGridPane;
		
		
		ObservableList<Object> genders;
		cbAdGenders = new ChoiceBox<>(genders = FXCollections.observableArrayList("Male", new Separator()));
		genders.add("Female");
		cbAdGenders.setValue(ad.getGender());
		cbAdGenders.setMinWidth(100);
		cbAdGenders.setMaxWidth(100);
		cbAdGenders.setOnAction(e -> {
			InputPage.validateChoiceBox(cbAdGenders);
		});
		
		chbNewType = new CheckBox("New");			// Checkboxes for adding new species and types
		chbNewSpecies = new CheckBox("New");
		
		
		cbAdSpecies = new ChoiceBox<>(db.createObservableList("Species", db.fetchResult(
				db.executeQuery("SELECT Distinct Species FROM "
							  + "Ads ORDER BY "
							  + "Species;"))));
		db.closeConnection();
		
		cbAdType = new ChoiceBox<>(db.createObservableList("Type", db.fetchResult(
				 db.executeQuery("SELECT Distinct Type FROM "
							   + "Ads WHERE Species == '" + ad.getSpecies() + "' "
							   + "ORDER BY Type;"))));
		db.closeConnection();
		
		
		chbNewSpecies.setOnAction(e -> {
			if (chbNewSpecies.isSelected()) {
				cbAdSpecies.setValue("Species");		// Reset value of Species CB
				cbAdSpecies.setVisible(false);		// Hide CB
				
				tfAdNewSpecies.setVisible(true);		// TextField appears
				
				// Loading all types into type CB when TF has been used
				obsListType = db.createObservableList("Type", db.fetchResult(
							  db.executeQuery("SELECT Distinct Type FROM "
									  		+ "Ads ORDER BY "
									  		+ "Type;")));
				db.closeConnection();


				cbAdType.setItems(obsListType);		// Loading type list into type CB
				cbAdType.setValue("Type");			// Setting default value to "Type"
				
				tfAdNewType.clear();					// Clearing any added value from the TF
				tfAdNewType.setVisible(false);		// Reset to only show choice box, if TF for new type was visible
				
				cbAdType.setDisable(true);			// Disable choosing type before species specified
				cbAdType.setVisible(true);			// Show CB again for types
				
				chbNewType.setSelected(false);		// Setting CHB unselected
				chbNewType.setDisable(true);		// Checking new type is disabled until species chosen
			} else {								// You dont need to retrieve a list from the DB here since it must have already gotten it, box was selected, right?
				tfAdNewSpecies.clear();				// Species TF cleared
				tfAdNewSpecies.setVisible(false);		// Hide TF
				
				cbAdSpecies.setVisible(true);			// Show CB again
				
				tfAdNewType.clear();					// Clearing any added value from the type TF
				tfAdNewType.setVisible(false);		// Hide type TF
				
				cbAdType.setValue("Type");			// Setting value to Standard before showing, no need to retrieve values from DB since checkbox was pressed at this point and the above if has been run.
				cbAdType.setDisable(true);			// Disable choosing type before species specified
				cbAdType.setVisible(true);			// Show CB again for types
				
				chbNewType.setSelected(false);		// Setting type CHB unselected
				chbNewType.setDisable(true);		// Disables new type until species chosen
			}
		});
		
		
		chbNewType.setDisable(false);				// Checkbox disables by default
		chbNewType.setOnAction(e -> {
			if (chbNewType.isSelected()) {
				cbAdType.setValue("Type");			// Resetting value to Type
				cbAdType.setVisible(false);			// Hiding CB for showing TF
				
				tfAdNewType.setVisible(true);			// Showing TF
			} else {
				tfAdNewType.clear();					// Clearing TF when box unchecked
				tfAdNewType.setVisible(false);		// Hiding TF
				
				cbAdType.setVisible(true);			// Setting CB visible again when unchecked
			}
		});

		
		cbAdSpecies.setValue(ad.getSpecies());			// Setting default value to "Species"
		cbAdSpecies.setMinWidth(100);					// Setting size of the CB
		cbAdSpecies.setMaxWidth(100);
		cbAdSpecies.setOnAction(e -> {				// On action (selection) it should...
			InputPage.validateChoiceBox(cbAdSpecies);
			if ((String) cbAdSpecies.getValue() != "Species") {	// If it has changed from "Species", do:
				obsListType = db.createObservableList("Type", db.fetchResult(
							  db.executeQuery("SELECT Distinct Type FROM "
									  	    + "Ads WHERE "
									  		+ "Species == '" + (String) cbAdSpecies.getValue() + "' ORDER BY "
									  		+ "Type;")));
				db.closeConnection();

				
				cbAdType.setItems(obsListType);		// Add the updated list to the CB with types
				cbAdType.setValue("Type");			// Setting default value of Type CB to "Type"
				cbAdType.setDisable(false);			// Enabling type CB
				
				chbNewType.setDisable(false);		// Enabling use of new type CHB
			} else {
				cbAdType.setValue("Type");			// If species is selected, type CB is also resetted
				cbAdType.setDisable(true);			// Disable use of type CB until an OK value is chosen for species
				
				chbNewType.setDisable(true);		// Cannot add new type until species has been chosen
			}
		});


		cbAdType.setValue(ad.getType());			// Setting default value of type CB to "Type"
		cbAdType.setMinWidth(100);					// Setting size of type CB
		cbAdType.setMaxWidth(100);
		cbAdType.setDisable(false);					// Type CB is disables by default
		cbAdType.setOnAction(e -> {
			try {
				InputPage.validateChoiceBox(cbAdType);
			} catch (Exception error) {}
		});
		
		
		chbReActivate = new CheckBox("Re-activate?");
		chbReActivate.setVisible(false);
		
		if (checkEndAfterToday(ad)) {
			chbReActivate.setVisible(true);
		} else {}
		
		
		gridPane.add(cbAdGenders, 1, 0);
		gridPane.add(cbAdSpecies, 0, 2);				// Adding CB for species to gridpane, column 0, row 3
		gridPane.add(cbAdType, 1, 2);
		gridPane.add(chbNewSpecies, 0, 2);
		gridPane.add(chbNewType, 1, 2);
		gridPane.add(chbReActivate, 1, 4);
		GridPane.setMargin(cbAdGenders, 	new Insets(0, 0, 0, 143));
		GridPane.setMargin(cbAdType, 			new Insets(0, 0, 0, -97));
		GridPane.setMargin(chbNewSpecies, 	new Insets(0, 0, 0, 105));	// Displacing CHB for new species 110 pixels to the left, to appear after the species CB/TF
		GridPane.setMargin(chbNewType, 		new Insets(0, 0, 0, 8));
		
		
		return gridPane;
	}
	
	/**
	 * Updates the incoming GridPane with buttons for Ad information.
	 * 
	 * @param initialized GridPane to add buttons to.
	 * @return GridPane containing the Buttons neccessary for updating Ad information.
	 */
	
	public static GridPane addAdButtons(GridPane inputGridPane, Ad ad) {
		GridPane gridPane = inputGridPane;
		Button btnAddPicture, btnSaveAd, btnBack;
		Alert alert;
		String reActivateMessage = "Your advertisement has been successfully updated, and will be visible for the next 90 days.";
		
		
		btnAddPicture = new Button("Upload picture");
		btnAddPicture.setMinSize(110, 50);
		btnAddPicture.setMaxSize(110, 50);
		btnAddPicture.setOnAction(InputPage.btnLoadEventListener);
		
		
		alert = new Alert(AlertType.INFORMATION);
		

		btnSaveAd = new Button("Save ad");
		btnSaveAd.setMinWidth(110);
		btnSaveAd.setMaxWidth(110);
		btnSaveAd.setOnAction(e -> {
			if (validateAdUpdate()) {
				System.out.println(">> Update values OK");
				updateMemberAd(ad);
				alert.setAlertType(AlertType.INFORMATION);		// Needed to set alert back to info after an error.
				alert.setTitle("Success");
				alert.setHeaderText("Advertisement has been updated");
				alert.setContentText("Your advertisement has been successfully updated.");
				if (chbReActivate.isSelected()) {
					alert.setContentText(reActivateMessage);
				}
				alert.showAndWait();
				window.setScene(sceneMP);
			} else {
				System.out.println(">> Update values incorrect");
				alert.setAlertType(AlertType.ERROR);
				alert.setTitle("Failiure");
				alert.setHeaderText("Changes could not be saved");
				alert.setContentText("You need to input correct values into all fields before saving.");
				alert.showAndWait();
			}
		});
		
		
		btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(sceneMP);
		});
		
		
		gridPane.add(btnAddPicture, 0, 4);
		gridPane.add(btnSaveAd, 0, 5);
		gridPane.add(btnBack, 0, 5);
		GridPane.setMargin(btnBack, new Insets(0, 0, 0, 115));
		
		
		return gridPane;
	}
	
	/**
	 * Resets to remove error colors from member info text fields.
	 */
	
	public static void resetMemberTextFields() {
		tfPhone.setStyle("-fx-box-border: teal;");
		tfEmail.setStyle("-fx-box-border: teal;");
		tfStreet.setStyle("-fx-box-border: teal;");
		tfZip.setStyle("-fx-box-border: teal;");
		tfCity.setStyle("-fx-box-border: teal;");
	}
	
	/**
	 * Validate phone no.
	 * 
	 * @param TextField
	 * @return boolean if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputPhone(TextField tf) {
		if ((tf.getLength() <= 10) && (tf.getLength() >= 8)) {										// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!Character.isDigit(tf.getText().charAt(index))) {
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
	 * Validate email TextField.
	 * 
	 * @param TextField
	 * @return boolean if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputEmail(TextField tf) {
		if ((tf.getLength() <= 50) && (tf.getLength() >= 8) && (tf.getText().contains("@"))) {										// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++) {
				if (!(Character.isDigit(tf.getText().charAt(index))) && 
					!(Character.isAlphabetic(tf.getText().charAt(index))) &&
					!(tf.getText().charAt(index) == '@') &&
					!(tf.getText().charAt(index) == '.')) {
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;
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
	 * Validate street TextField.
	 * 
	 * @param TextFielw
	 * @return boolean if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputStreet(TextField tf) {
		if ((tf.getLength() <= 30) && (tf.getLength() >= 1)) {										// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!(Character.isDigit(tf.getText().charAt(index))) && 
					!(Character.isAlphabetic(tf.getText().charAt(index))) && 
					!(tf.getText().charAt(index) == ' ')) {
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
	 * Validate zip TextField.
	 * 
	 * @param TextField
	 * @return boolean if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputZip(TextField tf) {
		if (tf.getLength() == 5) {										// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!Character.isDigit(tf.getText().charAt(index))) {
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
	 * Validate city TextField.
	 * 
	 * @param TextField
	 * @return boolean if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputCity(TextField tf) {
		if ((tf.getLength() < 30) && (tf.getLength() >= 1)) {										// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!(Character.isAlphabetic(tf.getText().charAt(index))) && 
					!(tf.getText().charAt(index) == ' ')) {
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
	 * This method updates new input information from the TextFields after the "Save" button has been pressed, assuming
	 * any new, valid information has been entered.
	 * 
	 */

	public static void updateMemberInfo() {
		String updateInfo, updateAddress, valuesInfo, valuesAddress, whereInfo, whereAddress;
		updateInfo = "UPDATE Agencies SET ";
		updateAddress = "UPDATE Addresses SET ";
		valuesInfo = "";
		valuesAddress = "";
		whereInfo = " WHERE ID = " + agencyInfo.getID() + ";";
		whereAddress = " WHERE Addresses.AgencyID = " + agencyInfo.getID() + ";";
	
		/*
		 * Updating agency table
		 */
	
		if (validateInputPhone(tfPhone)) {
			valuesInfo += "Phone = '" + tfPhone.getText() + "'";
		} else {}
	
		if (validateInputEmail(tfEmail)) {
			if (valuesInfo.contains("=")) {
				valuesInfo += ", ";
			} else {}
			valuesInfo += "Email = '" + tfEmail.getText() + "'";
		} else {}
	
		if (valuesInfo.contains("=")) {
			updateInfo += valuesInfo + whereInfo;
			System.out.println(">> Here comes an info update:");
			System.out.println(">> " + updateInfo);
			db.executeUpdate(updateInfo);
		} else {
			System.out.println(">> Entered info values not OK or non-existant");
		}
	
		/*
		 * Updating address table
		 */
	
		if (validateInputStreet(tfStreet)) {
			valuesAddress += "Street = '" + tfStreet.getText().trim() + "'";
		} else {}
	
		if (validateInputZip(tfZip)) {
			if (valuesAddress.contains("=")) {
				valuesAddress += ", ";
			} else {}
			valuesAddress += "ZIP = '" + tfZip.getText() + "'";
		} else {}
	
		if (validateInputCity(tfCity)) {
			if (valuesAddress.contains("=")) {
				valuesAddress += ", ";
			} else {}
			valuesAddress += "City = '" + tfCity.getText().trim() + "'";
		} else {}
	
		if (valuesAddress.contains("=")) {
			updateAddress += valuesAddress + whereAddress;
			System.out.println(">> Here comes an address update:");
			System.out.println(">> " + updateAddress);
			db.executeUpdate(updateAddress);
		} else {
			System.out.println(">> Entered address values not OK or non-existant");
		}
	
		agencyInfo = db.fetchAgencyExt(db.executeQuery(selectAgency)).get(0);	// refresh current agency information
		db.closeConnection();
	
		resetMemberTextFields();
	}
	
	/**
	 * Reads the endDate from the ad object and compares it to todays date.
	 * 
	 * @param Ad
	 * @return true if todays date is later than the compared ad's date.
	 */
	
	public static boolean checkEndAfterToday(Ad ad) {
		String endDate = ad.getEndDate();
		GregorianCalendar todaysDate = new GregorianCalendar();
		todaysDate.setTimeZone(TimeZone.getTimeZone("CET"));

		int year = Integer.parseInt(endDate.substring(0, 4));
		int month = Integer.parseInt(endDate.substring(5, 7));
		int day = Integer.parseInt(endDate.substring(7));
		
		
		return todaysDate.after(new GregorianCalendar(year, month, day));
	}
	
	/**
	 * This method is purely for convenience, it calls the total validation of the Input page. Ensures that all entered values for Ad updates are within their limits.
	 * 
	 * @return boolean representing whether the input values are OK (true).
	 */
	
	public static boolean validateAdUpdate() {
		return InputPage.validateAdInputs(tfAdName, tfAdAge, cbAdGenders, cbAdSpecies, cbAdType, 
										tfAdNewSpecies, tfAdNewType, taAdDescription);
	}
	
	/**
	 * Updates the input Ad with the information in the filled out fields. If CHB re-activate has been checked, the ad will
	 * set its start date to todays date and its end date to 90 days in the future. Updates the ad table when done.
	 * 
	 * @param Ad
	 */
	
	public static void updateMemberAd(Ad ad) {
		String updateAd, values, where;
		updateAd = "UPDATE Ads SET ";
		values = "";
		where = " WHERE ID = " + ad.getID() + ";";
		
		
		values += "Name = '" + tfAdName.getText().trim() + "', ";					// Trimming TFs with possibility of spaces
		values += "Age = " + tfAdAge.getText() + ", ";
		values += "Gender = '" + (String) cbAdGenders.getValue() + "', ";
		values += "Description = '" + taAdDescription.getText().trim() + "', ";	 	// Trimming TAs with possibility of spaces
			
		if (InputPage.validateChoiceBox(cbAdSpecies)) {				 				// If gets executed when the default value of CB is NOT chosen
			values += "Species = '" + (String) cbAdSpecies.getValue() + "', ";
		} else {values += "Species = '" + tfAdNewSpecies.getText().trim() + "', ";}

		if (InputPage.validateChoiceBox(cbAdType)) {
			values += "Type = '" + (String) cbAdType.getValue() + "'";
		} else {values += "Type = '" + tfAdNewType.getText().trim() + "'";}
		
		if (chbReActivate.isSelected()) {
			values += ", StartDate = date('NOW'), EndDate = date('NOW', '+90 days')";
		}
		
		updateAd += values + where;

		System.out.println(">> Here comes an Ad update:");
		System.out.println(">> " + updateAd);
		
		db.executeUpdate(updateAd);
		layoutMP.setBottom(viewMemberAds());
	}
	
	/**
	 * Validate password PasswordField.
	 * 
	 * @param PasswordField
	 * @return boolean if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputPassword(PasswordField pf) {
		if ((pf.getLength() <= 20) && (pf.getLength() >= 5)) {										// Checking number of characters in TF
			for (int index = 0; index < pf.getLength(); index++)	{
				if (!(Character.isDigit(pf.getText().charAt(index))) && 
					!(Character.isAlphabetic(pf.getText().charAt(index)))) {
					pf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;										// Loop is broken, a character that is not alphabetical was found
				} else {}
			}
			pf.setStyle("-fx-box-border: teal;");
			return true;
		} else {														// Number of chars too many
			pf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}
	
	/**
	 * This method updates the users password information from the TextFields after the "Save" button has been pressed, assuming
	 * any new, valid information has been entered.
	 */
	
	public static void updateMemberPassword() {
		String updatePassword, value, where;
		updatePassword = "UPDATE Agencies SET ";
		value = "Password = '" + pfPassword.getText() + "'";
		where = " WHERE ID = " + agencyInfo.getID() + ";";
		
		updatePassword += value + where;
		
		System.out.println(">> Here comes a password update:");
		System.out.println(">> " + updatePassword);
		
		db.executeUpdate(updatePassword);
	}
}