import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.sun.glass.events.ViewEvent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Page for showing member information and allowing for updates of both the specific member's personal information as well
 * as advertisements submitted by him/her.
 * 
 * @since 2015-11-25
 * @author M��ns Th��rnvik for functionality & Madisen Whitfield for partial design.
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
	private static ImageView myImageView;
	
	private static TableView<Ad> table;
	private static AgencyExt loggedInAgency;
	
	private static File file;
	
	private static ObservableList<Object> obsListType;
	
	private static DBobject db = new DBobject();
	private static Label back,name;

	/**
	 * Displays the member page when called. This page lets the user see information about the account and allows for
	 * alteration of this information as well as advertisements associated with this particular user.
	 * 
	 */

	public static void display(AgencyExt agencyInfo) {
		loggedInAgency = agencyInfo;	// This is where the agency should come in.
		
		window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Your Account");
		window.setMinWidth(250);

		layoutMP = new BorderPane();
		
		layoutMP.setTop(header());
		layoutMP.setCenter(viewMemberLogoAndInfo());
		layoutMP.setBottom(viewMemberAds());
		sceneMP = new Scene(layoutMP, 700, 750);
		sceneMP.getStylesheets().add("style.css");

		window.setScene(sceneMP);
		window.setOnCloseRequest(e -> {
			window.close();
		});
		window.showAndWait();
	}

	
	/**
	 * This method returns a header for use as a top element of a BorderPane.
	 * @return BorderPane header and navigation button
	 * @author Mattias Landkvist
	 */
	private static BorderPane header(){

		// The StackPane to be returned and used as a header.
		BorderPane header = new BorderPane();
		header.getStyleClass().add("header");

		back = new Label("<");
		back.setId("backbutton");
		back.getStyleClass().add("backbutton");
		back.setOnMouseClicked(e -> back());

		name = new Label("BeeAdopted");
		name.setId("beeadopted");
		name.getStyleClass().add("beeadopted");


		// All items created are added to HBox.
		header.setLeft(back);
		header.setCenter(name);					// Alignment of the items to the center left so that the back button is in an obvious place.

		return header;
	}
	
	/**
	 * Go back to sceneMP. If already in sceneMP, close window.
	 * @author Mattias Landkvist
	 */
	private static void back() {
		if(window.getScene() == sceneUpdateAd) {;
			window.setScene(sceneMP);
		} else {
			window.close();
		}
	}
	
	/**
	 * @return VBox with viewMemberLogo() and viewMemberInfo()
	 * @author Mattias Landkvist
	 */
	private static VBox viewMemberLogoAndInfo() {
		VBox vbox = new VBox();
		vbox.getChildren().addAll(viewMemberLogo(), viewMemberInfo());
		return vbox;
	}

	/**
	 * Insert logo for agency
	 * @return
	 */

	private static HBox viewMemberLogo() {
		HBox hBox = new HBox();
		hBox.getStyleClass().add("member-box");
		Button btnUpdateLogo, btnSaveLogo;
		
		myImageView = new ImageView();
		myImageView.setPreserveRatio(false);
		myImageView.setFitWidth(100);
		myImageView.setFitHeight(100);
		
		hBox.setAlignment(Pos.CENTER);
		
		try{
			BufferedImage image = null;
			InputStream inputStream = null;
			inputStream = db.executeQuery("SELECT Logo FROM Agencies WHERE ID = " + loggedInAgency.getID() + ";").getBinaryStream("Logo");
			db.closeConnection();
			image = javax.imageio.ImageIO.read(inputStream);
			if(image != null){
				Image newPic = SwingFXUtils.toFXImage(image, null);
				myImageView.setImage(newPic);
			} 
			inputStream.close();
		} catch (Exception e) {System.err.println(">> Error while loading image, or no image selected");
		} finally {
			try {
				if (!db.getConnection().isClosed()) {db.closeConnection();}
			} catch (SQLException e) {System.err.println(">> Error when closing connection");}
		}
		
		btnUpdateLogo = new Button("Change logo");
		btnUpdateLogo.setAlignment(Pos.CENTER_RIGHT);
		btnUpdateLogo.setOnAction(loadPicture);
		
		btnSaveLogo = new Button("Save logo");
		btnSaveLogo.setAlignment(Pos.CENTER_RIGHT);
		btnSaveLogo.setOnAction(e -> {
			if (file != null) {	
				try {
					InputPage.inputUpdatePicture(loggedInAgency.getID(), file, false);
				} catch (Exception e1) {
					System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
				} finally {
					layoutMP.setTop(viewMemberLogo());
				}
				file = null;
			} else {}
		});
			
		hBox.getChildren().addAll(myImageView, btnUpdateLogo, btnSaveLogo);
		
		return hBox;
	}
	
	/**
	 * Creates a gridpane in which user/member information is shown.
	 * 
	 * @return GridPane outfitted with fields showing the user his or her information.
	 */
	
	private static GridPane viewMemberInfo() {
		GridPane gridPane = new GridPane();
		gridPane.getStyleClass().add("member-box");
		gridPane.setAlignment(Pos.CENTER);

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
	
	private static GridPane addMemberLabels(GridPane inputGridPane) {
		GridPane gridPane = inputGridPane;
		Label lblAgencyInfo, lblPassword, lblAdInfo;
		
		lblAgencyInfo = new Label("Your information:");
		
		lblPassword = new Label("Change password:");
		
		lblAdInfo = new Label("Click an item below to edit");
		lblAdInfo.setFont(Font.font("Lucida Grande", FontWeight.BOLD, 13));
		lblAdInfo.setAlignment(Pos.BOTTOM_CENTER);
		
		gridPane.add(lblAgencyInfo, 0, 0);
		gridPane.add(lblPassword, 2, 0);
		gridPane.add(lblAdInfo, 1, 4);
		
		
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
		tfName.setText(loggedInAgency.getName());
		tfName.setOnKeyReleased(e -> {
			InputValidation.validateInputName(tfName);
		});
		
		tfPhone = new TextField();
		tfPhone.setEditable(false);
		tfPhone.setText(loggedInAgency.getPhone());
		tfPhone.setPromptText("Phone...");
		tfPhone.setOnKeyReleased(e -> {
			InputValidation.validateInputPhone(tfPhone);
		});
		
		tfEmail = new TextField();
		tfEmail.setEditable(false);
		tfEmail.setText(loggedInAgency.getEmail());
		tfEmail.setPromptText("Email...");
		tfEmail.setOnKeyReleased(e -> {
			InputValidation.validateInputEmail(tfEmail);
		});
		
		tfStreet = new TextField();
		tfStreet.setEditable(false);
		tfStreet.setText(loggedInAgency.getStreet());
		tfStreet.setPromptText("Street...");
		tfStreet.setOnKeyReleased(e -> {
			InputValidation.validateInputStreet(tfStreet);
		});
		
		tfZip = new TextField();
		tfZip.setEditable(false);
		tfZip.setText(loggedInAgency.getZip());
		tfZip.setPromptText("ZIP code...");
		tfZip.setOnKeyReleased(e -> {
			InputValidation.validateInputZip(tfZip);
		});
		
		tfCity = new TextField();
		tfCity.setEditable(false);
		tfCity.setText(loggedInAgency.getCity());
		tfCity.setPromptText("City...");
		tfCity.setOnKeyReleased(e -> {
			InputValidation.validateInputCity(tfCity);
		});
		
		gridPane.add(tfName, 0, 0);
		gridPane.add(tfPhone, 0, 1);
		gridPane.add(tfEmail, 0, 2);
		gridPane.add(tfStreet, 1, 0);
		gridPane.add(tfZip, 1, 1);
		gridPane.add(tfCity, 1, 2);
		
		pfPassword = new PasswordField();
		pfPassword.setPromptText("Password...");
		
		pfConfirmPassword = new PasswordField();
		pfConfirmPassword.setPromptText("Confirm...");
		
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
	
	private static GridPane addMemberButtons(GridPane inputGridPane) {
		GridPane gridPane = inputGridPane;
		Button btnEditInfo, btnSaveInfo, btnSavePassword, btnInputPage;
		Alert alert;
		
		alert = new Alert(AlertType.ERROR);
		
		btnEditInfo = new Button("Edit");
		btnSaveInfo = new Button("Save");

		btnEditInfo.setOnAction(e -> {
			btnEditInfo.setVisible(false);
			btnSaveInfo.setVisible(true);
			
			tfName.setEditable(true);
			tfPhone.setEditable(true);
			tfEmail.setEditable(true);
			tfStreet.setEditable(true);
			tfZip.setEditable(true);
			tfCity.setEditable(true);
		});

		btnSaveInfo.setVisible(false);
		btnSaveInfo.setOnAction(e -> {
			if (InputValidation.validateMemberInfo(tfName, tfPhone, tfEmail, 
													tfStreet, tfZip, tfCity)) {
				
				btnSaveInfo.setVisible(false);
				btnEditInfo.setVisible(true);
				
				InputPage.updateMemberInfo(loggedInAgency, tfName, tfPhone, tfEmail, 
											tfStreet, tfZip, tfCity);
				
				loggedInAgency = db.fetchAgencyExt(
						db.executeQuery("SELECT * "
							+ "FROM AgencyExtended "
							+ "WHERE ID == " + loggedInAgency.getID() + ";")).get(0);
				db.closeConnection();
				
				resetColorMemberFields();
				resetInfoMemberFields();
			} else {
				alert.setHeaderText("Changes could not be saved");
				alert.setContentText("Input changes are not valid and cannot be saved.");
				alert.showAndWait();
			}
		});	
				
		btnSavePassword = new Button("Save");
		btnSavePassword.setOnAction(e -> {
			InputValidation.validateInputPassword(pfPassword);
			InputValidation.validateInputPassword(pfConfirmPassword);
			if (InputValidation.validateInputPassword(pfPassword) && 
				pfPassword.getText().equals(pfConfirmPassword.getText())) {
				
				InputPage.updateMemberPassword(loggedInAgency, pfPassword);
				
				pfPassword.clear();
				pfConfirmPassword.clear();
			}
		});
		
		btnInputPage = new Button("New ad");
		btnInputPage.setOnAction(e -> {
			InputPage.setAgency(loggedInAgency);

			layoutUpdateAd = new BorderPane();
			layoutUpdateAd.setTop(header());
			layoutUpdateAd.setCenter(InputPage.viewInputAd(false));

			sceneUpdateAd = new Scene(layoutUpdateAd, 700, 750);
			sceneUpdateAd.getStylesheets().add("style.css");

			window.setScene(sceneUpdateAd);
		});
		
		gridPane.add(btnEditInfo, 0, 3);
		gridPane.add(btnSaveInfo, 0, 3);
		gridPane.add(btnSavePassword, 2, 2);
		gridPane.add(btnInputPage, 2, 4);
		
		
		return gridPane;
	}
	
	/**
	 * This method is used to generate a TableView of ads, clickable by the user to display additional information about
	 * it, as well as being able to alter it.
	 * 
	 * @return HBox containing a TableView of all the members associated advertisements.
	 */
	
	@SuppressWarnings("unchecked")
	private static TableView<Ad> viewMemberAds() {
		table = new TableView<Ad>();		
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
		+ "WHERE Agencies.ID = Ratings.AgencyID "
		+ "and Agencies.ID = " + loggedInAgency.getID() + ") as Rating "
		+ "FROM Ads, Agencies, Addresses, Ratings "
		+ "WHERE Agencies.ID = Addresses.AgencyID "
		+ "and Agencies.ID = Ratings.AgencyID "
		+ "and Agencies.ID = Ads.AgencyID "
		+ "and Agencies.ID = " + loggedInAgency.getID() + " "
		+ "ORDER BY StartDate DESC;";
		ArrayList<Ad> alAgencyAds = db.fetchAd(db.executeQuery(sql));
		db.closeConnection();
		ObservableList<Ad> olAgencyAds = db.createObservableList((alAgencyAds));
		
		table.setEditable(true);
		table.setRowFactory(e -> {
			TableRow<Ad> tableRow = new TableRow<>();
			tableRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!tableRow.isEmpty()) ) {
					Ad ad = (Ad) tableRow.getItem();
					
					InputPage.setAd(ad);
					
					layoutUpdateAd = new BorderPane();
					layoutUpdateAd.setTop(header());
					layoutUpdateAd.setCenter(InputPage.viewInputAd(true));

					sceneUpdateAd = new Scene(layoutUpdateAd, 700, 750);
					sceneUpdateAd.getStylesheets().add("style.css");

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
		
		table.setItems(olAgencyAds);
		table.getColumns().addAll(tcName, tcGender, tcSpecies, tcType, tcStartDate, tcEndDate);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		
		return table;
	}
	
	/**
	 * This method is used to refresh the table when an insert has been made.
	 */
	
	public static void refreshTable() {
		String sql = "SELECT Distinct Ads.ID, Picture, Ads.Name, Species, Type, Gender, Age, Description, "
		+ "StartDate, EndDate, Ads.AgencyID, Agencies.Name as Agency, "
		+ "(SELECT AVG(Rating) "
		+ "FROM Agencies, Ratings "
		+ "WHERE Agencies.ID = Ratings.AgencyID "
		+ "and Agencies.ID = " + loggedInAgency.getID() + ") as Rating "
		+ "FROM Ads, Agencies, Addresses, Ratings "
		+ "WHERE Agencies.ID = Addresses.AgencyID "
		+ "and Agencies.ID = Ratings.AgencyID "
		+ "and Agencies.ID = Ads.AgencyID "
		+ "and Agencies.ID = " + loggedInAgency.getID() + " "
		+ "ORDER BY StartDate DESC;";
		ArrayList<Ad> alAgencyAds = db.fetchAd(db.executeQuery(sql));
		db.closeConnection();
		ObservableList<Ad> olAgencyAds = db.createObservableList((alAgencyAds));
		
		table.setItems(olAgencyAds);
		table.refresh();
	}
	
	/**
	 * Upload a new picture to add to the ad
	 */
	
	public static EventHandler<ActionEvent> loadPicture = new EventHandler<ActionEvent>() {
		
		public void handle(ActionEvent t) {
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
			fileChooser.getExtensionFilters().addAll(extFilterPNG);
			file = fileChooser.showOpenDialog(null);
			try {
				BufferedImage bufferedImage = ImageIO.read(file);
				Image image = SwingFXUtils.toFXImage(bufferedImage, null);
				if (image != null) {myImageView.setImage(image);}
			} catch (Exception ex) {System.err.println(">> No image was chosen or another error was encountered...");}
		}
	};
	
	/**
	 * Get info into member text fields, extracted from logged in agency.
	 */
	
	public static void resetInfoMemberFields() {
		tfName.setText(loggedInAgency.getName());
		tfName.setEditable(false);
		tfPhone.setText(loggedInAgency.getPhone());
		tfPhone.setEditable(false);
		tfEmail.setText(loggedInAgency.getEmail());
		tfEmail.setEditable(false);
		tfStreet.setText(loggedInAgency.getStreet());
		tfStreet.setEditable(false);
		tfZip.setText(loggedInAgency.getZip());
		tfZip.setEditable(false);
		tfCity.setText(loggedInAgency.getCity());
		tfCity.setEditable(false);
	}
	
	/**
	 * Resets to remove error colors from member info text fields.
	 */
	
	private static void resetColorMemberFields() {
		tfName.setStyle("-fx-box-border: teal;");
		tfPhone.setStyle("-fx-box-border: teal;");
		tfEmail.setStyle("-fx-box-border: teal;");
		tfStreet.setStyle("-fx-box-border: teal;");
		tfZip.setStyle("-fx-box-border: teal;");
		tfCity.setStyle("-fx-box-border: teal;");
	}	
}