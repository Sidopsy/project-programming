
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Observable;

import org.controlsfx.control.RangeSlider;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Class for starting the application.
 * 
 * TODO: List all methods.
 * 
 * @author ML
 */

public class ViewMaster extends Application {

	private static Stage window;
	private static Scene scene1, scene2, scene3;
	private static BorderPane bpLayout1, bpLayout2, bpLayout3;
	private static TableView<Ad> tvAd;
	private static ArrayList<Ad> theSearch = null;
	private static ObservableList<Object> obsListSpecies, obsListType, obsListGender, obsListAgencies;
	private static ChoiceBox<Object> cbLocation, cbSpecies, cbType, cbGender, cbAgencies;
	private static RangeSlider slAge;
	private static TextField tfDescription;
	private static Button btnSearch;
	private static String city, species, type, age, gender, description, agency;
	private static boolean firstSearch;
	private static DBobject database = new DBobject();
	private static String sqlStatement;
	private static LocalDate todayDate = LocalDate.now();
	private static Date today = Date.valueOf(todayDate);
	private static int minAge = 0;
	private static int maxAge = 100;


	/**
	 * The main method, only used to start the application with the launch command from javaFX.
	 * 
	 * @param args
	 */

	public static void main(String[] args){
		launch(args);
	}



	/**
	 * Start method that is launched with Application.launch(args). This builds the main windows shown on launch which is
	 * the prompt for a location. This view also contains a button for going to the input page, the code for this is in
	 * startLocation().
	 * 
	 * @param primaryStage
	 */

	public void start(Stage primaryStage){
		window = primaryStage;
		window.setTitle("Marketplace");	

		// Creating the window.
		bpLayout1 = new BorderPane();				// BorderPane layout is used.
		bpLayout1.setTop(Header.largeHeader());					// Top element of the BorderPane is retrieved, which is the iAdopt image.
		bpLayout1.setCenter(startLocation());		// Center element of BorderPane contains the dropdown vbox.
		scene1 = new Scene(bpLayout1, 800, 600);	

		// Setting the currently open window to show the scene created above.
		window.setScene(scene1);
		window.show();
	}

	/**
	 * This method returs a header for use as a top element of a BorderPane. The header consists of the iAdopt image.
	 * 
	 * @return Vbox header image and navigation buttons
	 */

	//	private StackPane header(){
	//
	//		// The StackPane to be returned and used as a header.
	//		StackPane header = new StackPane();
	//
	//		// Specifying an image and adding it to the ImageView that is later to be added to the StackPane.
	//		Image img = new Image(getClass().getResourceAsStream("BeeAdoptedLarge.png"));
	//		ImageView headerImg = new ImageView();	
	//
	//		headerImg.setImage(img);								// Adding the image to ImageView so that it can be added to the StackPane.
	//		headerImg.setOnMouseClicked(e -> backToStart());		// Click function to take the user back to the start page is also inserted into the header.
	//
	//		// Button for going back one step in the application.
	//		btnBack = new Button("Back");
	//		btnBack.setOnAction(e -> goBack());
	//		btnBack.setVisible(false);
	//
	//		// All items created are added to Vbox.
	//		header.getChildren().addAll(headerImg, btnBack);
	//		header.setAlignment(Pos.CENTER_LEFT);					// Alignment of the items to the center left so that the back button is in an obvious place.
	//
	//		return header;
	//	}

	/**
	 * This method returns a Vbox element containing a dropdown menu of all cities that agencies exist at and a button for
	 * going to the input page.
	 * 
	 * @return Vbox location dropdown menu and input page button
	 */

	private VBox startLocation() {

		// The Vbox to be returned.
		VBox firstPage = new VBox();

		firstPage.setPadding(new Insets(0));
		firstPage.setSpacing(75);

		// Displayed above the choicebox for cities to let the user know what to do.
		Label lblLocation = new Label("Where are you?");

		// This inputs information into the choicebox, namely the cities in which there are agencies.
		try {
			cbLocation = new ChoiceBox<>(database.createObservableList("City",database.fetchResult(database.executeQuery("SELECT Distinct City FROM  Addresses ORDER BY City;"))));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cbLocation.setValue(cbLocation.getItems().get(0));	// Getting retrieved items from the DB.

		cbLocation.setOnAction(e -> {
			if(!cbLocation.getValue().equals("Table")){
				city = (String) cbLocation.getValue();		// What location was input?
				firstSearch = true;												
				search();
				bpLayout2 = new BorderPane();				// Preparing for a new scene.
				bpLayout2.setTop(Header.largeHeader());		// Setting the header as before.
				bpLayout2.setCenter(mainCenterView());		// Getting the center view for the next scene which now will show a list of agencies in the input location.
				Header.toggleBackButton();
				scene2 = new Scene(bpLayout2);
				window.setScene(scene2);
			}
		});

		// Vbox is populated with the label, the choicebox and the content from loginBox()
		firstPage.getChildren().addAll(lblLocation, cbLocation,new Separator(), loginBox());
		firstPage.setAlignment(Pos.CENTER);

		return firstPage;
	}

	private static HBox loginBox(){
		HBox hbox = new HBox();
		//	hbox.setPadding(new Insets(20));
		hbox.setSpacing(100);
		hbox.setAlignment(Pos.CENTER);
		Button btnShow = new Button("Go to agency pages");
		TextField tfEmail = new TextField();
		PasswordField pfPassword = new PasswordField();

		tfEmail.setPromptText("Enter your email");
		pfPassword.setPromptText("Enter your password");
		Button btnLogin = new Button("Login");
		btnLogin.setOnAction(e -> {
			InputPage.display();
		});


		btnShow.setOnAction(e -> {
			hbox.getChildren().removeAll(hbox.getChildren());
			hbox.getChildren().addAll(tfEmail,pfPassword,btnLogin);
		});

		hbox.getChildren().add(btnShow);

		return hbox;
	}


	//	

	/**
	 * This method contains filter options for searches. 
	 * 
	 * @return Hbox displaying filers
	 */

	public HBox filter(){
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.setSpacing(10);

		try {
			obsListSpecies = database.createObservableList("Species",database.fetchResult(database.executeQuery("SELECT Distinct Species FROM Ads, Agencies, Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID and City == '" + city + "' and EndDate >= '" + today + "' ORDER BY Species;")));
			obsListType = database.createObservableList("Type",database.fetchResult(database.executeQuery("SELECT Distinct Type FROM Ads, Agencies, Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID and City == '" + city + "' and EndDate >= '" + today + "' ORDER BY Type;")));
			obsListGender = database.createObservableList("Gender",database.fetchResult(database.executeQuery("SELECT Distinct Gender FROM Ads, Agencies, Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID and City == '" + city + "' and EndDate >= '" + today + "' ORDER BY Gender;")));
			obsListAgencies = database.createObservableList("Agencies",database.fetchResult(database.executeQuery("SELECT Distinct Name FROM Agencies, Addresses WHERE Agencies.ID == Addresses.AgencyID and City == '" + city + "' ORDER BY Name;")));

		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		cbSpecies = new ChoiceBox<>(obsListSpecies);
		cbSpecies.setValue(cbSpecies.getItems().get(0));
		cbSpecies.setOnAction(e -> {
			if(!((String)cbSpecies.getValue()).equals("Species")){
				species = (String) cbSpecies.getValue();
				sqlStatement = "SELECT Distinct Type FROM Ads WHERE Species == '" + species + "' ORDER BY Type;";
				try {
					obsListType = database.createObservableList("Type",database.fetchResult(database.executeQuery(sqlStatement)));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				cbType.setItems(obsListType);
				cbType.setValue("Type");
			}
		});

		cbType = new ChoiceBox<>(obsListType);
		cbType.setValue(cbType.getItems().get(0));
		cbType.setOnAction(e -> {
			type = (String) cbType.getValue();
			cbSpecies.setValue(cbSpecies.getItems().get(0));
		});

		VBox ageBox = new VBox();
		String minAgeStatement;
		String maxAgeStatement;
		ageBox.setPadding(new Insets(0));
		ageBox.setSpacing(2);
		if(city != "Select all"){
			minAgeStatement = "SELECT MIN(Age) FROM Ads,Agencies,Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID and  City == '" + city + "' and EndDate >= " + today + ";";
			maxAgeStatement = "SELECT MAX(Age) FROM Ads,Agencies,Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID and  City == '" + city + "' and EndDate >= " + today + ";";
		} else {
			minAgeStatement = "SELECT MIN(Age) FROM Ads,Agencies,Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID and EndDate >= " + today + ";";
			maxAgeStatement = "SELECT MAX(Age) FROM Ads,Agencies,Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID and EndDate >= " + today + ";";
		}
		try {
			minAge = Integer.parseInt(database.fetchResult(database.executeQuery(minAgeStatement)).get(0).get(0));
			maxAge = Integer.parseInt(database.fetchResult(database.executeQuery(maxAgeStatement)).get(0).get(0));
			System.out.println(minAge);
			System.out.println(maxAge);

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Label ageLabel = new Label("Age");
		slAge = new RangeSlider();
		slAge.setMin(minAge);
		slAge.setMax(maxAge = 100);
	//	slAge.setShowTickLabels(true);
	//	slAge.setShowTickMarks(true);
		//slAge.setMajorTickUnit(15);
	//	slAge.setMinorTickCount(maxAge/2);
	//	slAge.setBlockIncrement(5);
		ageBox.getChildren().addAll(ageLabel, slAge);


		//		cbAge = new ChoiceBox<>(obsListAge);
		//		cbAge.setValue(cbAge.getItems().get(0));
		//	//	cbAge.setDisable(true);
		//		cbAge.setOnAction(e -> {
		//			age = (String) cbAge.getValue();
		//			sqlStatement = "SELECT Distinct Gener FROM Ads WHERE Age == '" + age + "' ORDER BY Gender;";
		//			try {
		//				obsListGender = database.createObservableList("Gender",database.fetchResult(database.executeQuery(sqlStatement)));
		//			} catch (Exception e1) {
		//				// TODO Auto-generated catch block
		//				e1.printStackTrace();
		//			}
		//			cbGender.setItems(obsListGender);
		//			cbGender.setValue("Gender");
		//		//	cbGender.setDisable(false);
		//		});


		cbGender = new ChoiceBox<>(obsListGender);
		cbGender.setValue(cbGender.getItems().get(0));
		cbGender.setOnAction(e -> gender = (String) cbGender.getValue());

		cbAgencies = new ChoiceBox<>(obsListAgencies);
		cbAgencies.setValue(cbAgencies.getItems().get(0));
		cbAgencies.setOnAction(e -> agency = (String) cbAgencies.getValue());

		tfDescription = new TextField();
		tfDescription.setPromptText("Description");

		btnSearch = new Button("Search");
		btnSearch.setOnAction(e -> {
			search();
			bpLayout3 = new BorderPane();
			bpLayout3.setTop(Header.largeHeader());
			bpLayout3.setCenter(mainCenterView());
			tvAd.refresh();
			scene3 = new Scene(bpLayout3,800,600);
			window.setScene(scene3);
		});

		hbox.getChildren().addAll(cbSpecies,cbType,slAge,cbGender,cbAgencies,tfDescription,btnSearch);

		return hbox;
	}

	/**
	 * This method has two functions. When you select an agency in scene 2 this method gets all ads for that specific agency. After this, you can specify conditions
	 * for which ads you want to view.
	 */

	public static void search(){

		// Search string to be sent to DB along with small parts of the statement.
		String searchStatement, searchSpecies, searchType, searchAge, searchGender, searchAgency;

		// Null values are ignored as well as if the user left the cbs in their original posistions.
		if (species != null && species != "Species" || species != "Select all"){
			searchSpecies = " and Species == '" + species + "'";
		} else {
			searchSpecies = "";
		}

		if (type != null && type != "Type"){
			searchType = " and Type == '" + type + "'";
		} else {
			searchType = "";
		}

		if (age != null && age != "Age"){
			searchAge = " and Age >= " + slAge.getMin() + " and Age <= " + slAge.getMax();
		} else {
			searchAge = "";
		}

		if (gender != null && gender != "Gender"){
			searchGender = " and Gender == '" + gender + "'";
		} else {
			searchGender = "";
		}

		if (agency != null && agency != "Agency"){
			searchAgency = " and Agencies.Name as Agency == '" + agency + "'";
		} else {
			searchAgency = "";
		}

		if (firstSearch == true){	
			if(city != "Select all"){
				searchStatement = "SELECT Distinct Ads.ID,Picture,Ads.Name,Species,Type,Gender,Age,Description,StartDate,EndDate,Ads.AgencyID,Agencies.Name as Agency,(SELECT AVG(Rating) FROM Ads,Agencies,Ratings,Addresses WHERE Agencies.ID = Ratings.AgencyID and Agencies.ID = Addresses.AgencyID and City = '" + city + "') as Rating FROM Ads,Agencies,Addresses,Ratings WHERE Agencies.ID = Addresses.AgencyID and Agencies.ID = Ratings.AgencyID and Agencies.ID = Ads.AgencyID and City = '" + city + "' and EndDate >= '" + today + "' ORDER BY Ads.ID;";
			} else {
				searchStatement = "SELECT Distinct Ads.ID,Picture,Ads.Name,Species,Type,Gender,Age,Description,StartDate,EndDate,Ads.AgencyID,Agencies.Name as Agency,(SELECT (SELECT AVG(Rating) FROM Agencies,Ratings WHERE Agencies.ID == Ratings.AgencyID GROUP BY Agencies.ID	) AS Rating FROM Ads,Agencies,Ratings,Addresses WHERE Agencies.ID = Ratings.AgencyID and Agencies.ID = Addresses.AgencyID) as Rating FROM Ads,Agencies,Addresses,Ratings WHERE Agencies.ID = Addresses.AgencyID and Agencies.ID = Ratings.AgencyID and Agencies.ID = Ads.AgencyID and EndDate >= '" + today + "' ORDER BY Ads.ID;";
			}
			firstSearch = false;
		} else {
			System.out.println("Bob0: " + searchSpecies + searchType + searchAge + searchGender);
			searchStatement =  "SELECT Distinct Ads.ID,Picture,Ads.Name,Species,Type,Gender,Age,Description,StartDate,EndDate,Ads.AgencyID,Agencies.Name as Agency,(SELECT AVG(Rating)"
					+ "Ads,Agencies,Ratings,Addresses WHERE "
					+ "Agencies.ID == Addresses.AgencyID and "
					+ "Agencies.ID == Ads.AgencyID and "
					+ "Agencies.ID == Ratings.AgencyID and "
					+ "City = '" + city + "' and "
					+ "EndDate >= '" + today + "'" +
					searchSpecies + 
					searchType + 
					searchAge + 
					searchGender + ";";

		}

		try {
			theSearch = database.fetchAd(database.executeQuery(searchStatement));
			System.out.println(searchStatement);
			System.out.println(theSearch.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * This method 
	 * 
	 * @return TableView<Ad>
	 */

	public VBox searchResults(){
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10,10,10,10));
		vbox.setSpacing(2);

		tvAd = new TableView<Ad>();
		tvAd.setEditable(true);
		//tvAd.setMinWidth(0);

		// Columns to be added to the TableView.
		TableColumn<Ad, String> pictureCol = new TableColumn<>("Picture");
		TableColumn<Ad, String> speciesCol = new TableColumn<>("Species");
		TableColumn<Ad, String> typeCol = new TableColumn<>("Type");
		TableColumn<Ad, String> genderCol = new TableColumn<>("Gender");
		TableColumn<Ad, String> endCol = new TableColumn<>("End date");
		TableColumn<Ad, String> agencyCol = new TableColumn<>("Agency");
		TableColumn<Ad, String> ratingCol = new TableColumn<>("Rating");

		pictureCol.setMinWidth(115);
		speciesCol.setMinWidth(115);
		typeCol.setMinWidth(115);
		genderCol.setMinWidth(115);
		endCol.setMinWidth(115);
		agencyCol.setMinWidth(115);
		ratingCol.setMinWidth(115);

		ObservableList<Ad> adList = database.createObservableList(theSearch);

		pictureCol.setCellValueFactory(new PropertyValueFactory<>("picture"));
		speciesCol.setCellValueFactory(new PropertyValueFactory<>("species"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
		endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		agencyCol.setCellValueFactory(new PropertyValueFactory<>("agencyName"));
		ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

		tvAd.setRowFactory( tv -> {
			TableRow<Ad> row = new TableRow<>();
			row.setOnMouseClicked(event -> {

				if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
					Ad ad = (Ad) row.getItem();
					ViewAd.display(ad.getName(), ad);
				}
			});
			return row ;
		});

		tvAd.setItems(adList);

		tvAd.getColumns().addAll(pictureCol, speciesCol, typeCol, genderCol,endCol, agencyCol, ratingCol);

		vbox.getChildren().add(tvAd);
		return vbox;
	}

	public VBox mainCenterView(){
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(0));
		vbox.setSpacing(10);

		vbox.getChildren().addAll(filter(), searchResults());

		return vbox;
	}

	/**
	 * Go to previous scene.
	 */
	private void goBack(){
		if(window.getScene() == scene2){
			cbLocation.setValue("City");
			window.setScene(scene1);
		} else if (window.getScene() == scene3){
			firstSearch = true;
			search();
			bpLayout2 = new BorderPane();
			bpLayout2.setTop(Header.largeHeader());
			bpLayout2.setCenter(mainCenterView());
			Header.toggleBackButton();
			scene2 = new Scene(bpLayout2,800,600);
			window.setScene(scene2);
		} else {
			cbLocation.setValue("City");
			window.setScene(scene1);
		}
	}


	/**
	 * Go to start view.
	 */

	public void backToStart(){
		Header.toggleBackButton();
		cbLocation.setValue("City");
		window.setScene(scene1);	
	}
}