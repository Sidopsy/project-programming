
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Observable;

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
	private static ObservableList<Object> obsListSpecies, obsListType, obsListAge, obsListGender, obsListAgencies;
	private static ChoiceBox<Object> cbLocation, cbSpecies, cbType, cbAge, cbGender, cbAgencies;
	private static TextField tfDescription;
	private static Agency chosenAgency = null;
	private static Button btnSearch, btnBack;
	private static String city, species, type, age, gender, description, agency;
	private static boolean firstSearch;
	private static boolean firstScene = true;
	private static DBobject database = new DBobject();
	private static String sqlStatement;
	private static LocalDate todayDate = LocalDate.now();
	private static Date today = Date.valueOf(todayDate);

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
		bpLayout1.setTop(header());					// Top element of the BorderPane is retrieved, which is the iAdopt image.
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
	
	private StackPane header(){

		// The StackPane to be returned and used as a header.
		StackPane header = new StackPane();
		
		// Specifying an image and adding it to the ImageView that is later to be added to the StackPane.
		Image img = new Image(getClass().getResourceAsStream("BeeAdoptedLarge.png"));
		ImageView headerImg = new ImageView();	

		headerImg.setImage(img);								// Adding the image to ImageView so that it can be added to the StackPane.
		headerImg.setOnMouseClicked(e -> backToStart());		// Click function to take the user back to the start page is also inserted into the header.

		// Button for going back one step in the application.
		btnBack = new Button("Back");
		btnBack.setOnAction(e -> goBack());
		btnBack.setVisible(false);

		// All items created are added to Vbox.
		header.getChildren().addAll(headerImg, btnBack);
		header.setAlignment(Pos.CENTER_LEFT);					// Alignment of the items to the center left so that the back button is in an obvious place.

		return header;
	}

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
		firstPage.setSpacing(40);

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
			System.out.println("Bobo");
			bpLayout2 = new BorderPane();				// Preparing for a new scene.
			bpLayout2.setTop(header());					// Setting the header as before.
			bpLayout2.setCenter(mainCenterView());		// Getting the center view for the next scene which now will show a list of agencies in the input location.
			btnBack.setVisible(true);
			scene2 = new Scene(bpLayout2, 800, 600);
			window.setScene(scene2);
			}
		});
		
		// Input page button to guide the user to the input section of the application.
		Button btnLogin = new Button("Login");
		btnLogin.setOnAction(e -> Membership.start());
		
		// Vbox is created using the items above.
		firstPage.getChildren().addAll(lblLocation, cbLocation, btnLogin);
		firstPage.setAlignment(Pos.CENTER);
		
		return firstPage;
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
			obsListSpecies = database.createObservableList("Species",database.fetchResult(database.executeQuery("SELECT Distinct Species FROM Ads ORDER BY Species;")));
			obsListType = database.createObservableList("Type",database.fetchResult(database.executeQuery("SELECT Distinct Type FROM Ads ORDER BY Type;")));
			obsListAge = database.createObservableList("Age",database.fetchResult(database.executeQuery("SELECT Distinct Age FROM Ads ORDER BY Age;")));
			obsListGender = database.createObservableList("Gender",database.fetchResult(database.executeQuery("SELECT Distinct Gender FROM Ads ORDER BY Gender;")));
			obsListAgencies = database.createObservableList("Agencies",database.fetchResult(database.executeQuery("SELECT Distinct Name FROM Agencies, Addresses WHERE Agencies.ID == Addresses.AgencyID and City == '" + city + "' ORDER BY Name;")));
			
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		cbSpecies = new ChoiceBox<>(obsListSpecies);
		cbSpecies.setValue(cbSpecies.getItems().get(0));
		cbSpecies.setOnAction(e -> {
			species = (String) cbSpecies.getValue();
			sqlStatement = "SELECT Distinct Type FROM Ads WHERE Species == '" + species + "' ORDER BY Type;";
			try {
				obsListType = database.createObservableList("Type",database.fetchResult(database.executeQuery(sqlStatement)));
				cbType.setItems(obsListType);
				cbType.setValue("Type");
//				cbType.setDisable(false);
//				cbAge.setDisable(true);
//				cbGender.setDisable(true);
//				tfDescription.setDisable(false);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		cbType = new ChoiceBox<>(obsListType);
		cbType.setValue(cbType.getItems().get(0));
//		cbType.setDisable(true);
		cbType.setOnAction(e -> {
			type = (String) cbType.getValue();
			sqlStatement = "SELECT Distinct Age FROM Ads WHERE Type == '" + type + "' ORDER BY Age;";
			try {
				obsListSpecies = database.createObservableList("Species",database.fetchResult(database.executeQuery("SELECT Distinct Species FROM Ads WHERE Type == '" + type + "' ORDER BY Species;")));
				obsListAge = database.createObservableList("Age",database.fetchResult(database.executeQuery(sqlStatement)));
				cbSpecies.setItems(obsListSpecies);
				cbSpecies.setValue("Species");
				cbAge.setItems(obsListAge);
				cbAge.setValue("Age");
//				cbAge.setDisable(false);
//				cbGender.setDisable(true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		cbAge = new ChoiceBox<>(obsListAge);
		cbAge.setValue(cbAge.getItems().get(0));
//		cbAge.setDisable(true);
		cbAge.setOnAction(e -> {
			age = (String) cbAge.getValue();
			sqlStatement = "SELECT Distinct Gender FROM Ads WHERE Age == '" + age + "' ORDER BY Gender;";
			try {
				obsListGender = database.createObservableList("Gender",database.fetchResult(database.executeQuery(sqlStatement)));
				cbGender.setItems(obsListGender);
				cbGender.setValue("Gender");
//				cbGender.setDisable(false);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		cbGender = new ChoiceBox<>(obsListGender);
		cbGender.setValue(cbGender.getItems().get(0));
//		cbGender.setDisable(true);
		cbGender.setOnAction(e -> gender = (String) cbGender.getValue());
		
		cbAgencies = new ChoiceBox<>(obsListAgencies);
		cbAgencies.setValue(cbAgencies.getItems().get(0));
		cbAgencies.setOnAction(e -> agency = (String) cbAgencies.getValue());

		tfDescription = new TextField();
		tfDescription.setPromptText("Description");
//		tfDescription.setDisable(true);

		btnSearch = new Button("Search");
		btnSearch.setOnAction(e -> {
			search();
			bpLayout3 = new BorderPane();
			bpLayout3.setTop(header());
			bpLayout3.setCenter(mainCenterView());
			btnBack.setVisible(true);
			tvAd.refresh();
			scene3 = new Scene(bpLayout3,800,600);
			window.setScene(scene3);
		});

		hbox.getChildren().addAll(cbSpecies,cbType,cbAge,cbGender,cbAgencies,tfDescription,btnSearch);

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
		if (species != null && species != "Species"){
			searchSpecies = " and Species == '" + species + "'";
		} else {
			searchSpecies = "";
		}
		
		if (type != null && type != "Type"){
			searchType = "and Type == '" + type + "'";
		} else {
			searchType = "";
		}
		
		if (age != null && age != "Age"){
			searchAge = " and Age == " + age;
		} else {
			searchAge = "";
		}
		
		if (gender != null && gender != "Gender"){
			searchGender = " and Gender == '" + gender + "'";;
		} else {
			searchGender = "";
		}
		
		if (agency != null && agency != "Agency"){
			searchAgency = " and Agencies.Name as Agency == '" + agency + "'";
		} else {
			searchAgency = "";
		}
		
		if (firstSearch == true){	
			searchStatement = "SELECT Distinct Ads.ID,Picture,Ads.Name,Species,Type,Gender,Age,Description,StartDate,EndDate,Ads.AgencyID,Agencies.Name as Agency,(SELECT AVG(Rating) FROM Ads,Agencies,Ratings,Addresses WHERE Agencies.ID = Ratings.AgencyID and Agencies.ID = Addresses.AgencyID and City = '" + city + "') as Rating FROM Ads,Agencies,Addresses,Ratings WHERE Agencies.ID = Addresses.AgencyID and Agencies.ID = Ratings.AgencyID and Agencies.ID = Ads.AgencyID and City = '" + city + "' and EndDate >= '" + today + "' ORDER BY Ads.ID;";
			firstSearch = false;
		} else {
			searchStatement =  "SELECT Distinct Ads.ID,Picture,Ads.Name,Species,Type,Gender,Age,Description,StartDate,EndDate,Ads.AgencyID,Agencies.Name as Agency,(SELECT AVG(Rating)"
					+ "Ads,Agencies,Ratings,Addresses WHERE "
					+ "Agencies.ID == Addresses.AgencyID and "
					+ "Agencies.ID == Ads.AgencyID and "
					+ "Agencies.ID == Ratings.AgencyID and "
					+ "City = '" + city + "' and "
					+ "EndDate >= " + today +
					searchSpecies + 
					searchType + 
					searchAge + 
					searchGender + ";";
			
		}
		
		try {
			theSearch = database.fetchAd(database.executeQuery(searchStatement));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	//	System.out.println(searchStatement);
	//	System.out.println(theSearch.toString());
	}
	
	/**
	 * This method 
	 * 
	 * @return TableView<Ad>
	 */
	
	public TableView<Ad> searchResults(){
		tvAd = new TableView<Ad>();

		// Columns to be added to the TableView.
		TableColumn<Ad, String> pictureCol = new TableColumn<>("Picture");
		TableColumn<Ad, String> speciesCol = new TableColumn<>("Species");
		TableColumn<Ad, String> typeCol = new TableColumn<>("Type");
		TableColumn<Ad, String> genderCol = new TableColumn<>("Gender");
		TableColumn<Ad, String> endCol = new TableColumn<>("End date");
		TableColumn<Ad, String> agencyCol = new TableColumn<>("Agency");
		TableColumn<Ad, String> ratingCol = new TableColumn<>("Rating");

		pictureCol.setMinWidth(110);
		speciesCol.setMinWidth(110);
		typeCol.setMinWidth(110);
		genderCol.setMinWidth(110);
		endCol.setMinWidth(110);
		agencyCol.setMinWidth(110);
		ratingCol.setMinWidth(110);
		
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

		return tvAd;
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
			bpLayout2.setTop(header());
			bpLayout2.setCenter(mainCenterView());
			btnBack.setVisible(false);
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
		btnBack.setVisible(false);
		cbLocation.setValue("City");
		window.setScene(scene1);	
		}
}