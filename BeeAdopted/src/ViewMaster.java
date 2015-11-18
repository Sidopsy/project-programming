
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

public class ViewMaster extends Application{

	private static Stage window;
	private static Scene scene1, scene2, scene3, scene4;
	private static BorderPane bpLayout1, bpLayout2, bpLayout3, bpLayout4;
	private static TableView<Ad> tvAd = new TableView<Ad>();
	private static ArrayList<Ad> theSearch = null;
	private static ChoiceBox<Object> cbLocation, cbSpecies, cbType, cbAge, cbGender;
	private static TextField tfDescription;
	private static Agency chosenAgency = null;
	private static Button btnSearch;
	private static String city, species, type, age, gender, description;
	private static ObservableList<Object> obsListSpecies, obsListType, obsListAge, obsListGender;
	private static boolean firstSearch;

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
		Button btnBack = new Button("Back");
		
		btnBack.setOnAction(e -> goBack());

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
	
	private VBox startLocation(){
		
		// The Vbox to be returned.
		VBox firstPage = new VBox();
		
		firstPage.setPadding(new Insets(0));
		firstPage.setSpacing(40);

		// Displayed above the choicebox for cities to let the user know what to do.
		Label lblLocation = new Label("Where are you?");

		// This inputs information into the choicebox, namely the cities in which there are agencies.
		cbLocation = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Addresses", "City", null, null));
		cbLocation.setValue(cbLocation.getItems().get(0));	// Getting retrieved items from the DB.
		
		cbLocation.setOnAction(e -> {
			city = (String) cbLocation.getValue();		// What location was input?
			bpLayout2 = new BorderPane();				// Preparing for a new scene.
			bpLayout2.setTop(header());					// Setting the header as before.
			bpLayout2.setCenter(startAgencies());		// Getting the center view for the next scene which now will show a list of agencies in the input location.
			scene2 = new Scene(bpLayout2, 800, 600);
			window.setScene(scene2);
		});
		
		// Input page button to guide the user to the input section of the application.
		Button btnInput = new Button("Go to input page");
		btnInput.setOnAction(e -> Membership.start());
		
		// Vbox is created using the items above.
		firstPage.getChildren().addAll(lblLocation, cbLocation, btnInput);
		firstPage.setAlignment(Pos.CENTER);
		
		return firstPage;
	}

	/**
	 * This method retrieves agencies based on the global variable city that is altered through the method startLocation().
	 * 
	 * @return TableView<Agency> that shows agencies
	 */

	private TableView<Agency> startAgencies(){
		
		// For viewing the agencies in table form.
		TableView<Agency> tableAgency = new TableView<>();

		tableAgency.setEditable(true);												// Needed to be able to alter the table.
		tableAgency.setRowFactory(tv -> {											// Adding actions to each of the tables rows.
			TableRow<Agency> rowAgency = new TableRow<>();							
			
			rowAgency.setOnMouseClicked(event -> {									// The tables rows respond to mouse clicks.
				if ( (event.getClickCount() == 1) && (!rowAgency.isEmpty()) ) {
					chosenAgency = rowAgency.getItem();								// Reading what agency was chosen.
					System.out.println("Agency number " + chosenAgency.getID() + "was chosen.");
					firstSearch = true;												
					search();
					bpLayout3 = new BorderPane();
					bpLayout3.setTop(header());
					bpLayout3.setCenter(mainCenterView());
					scene3 = new Scene(bpLayout3, 800, 600);
					window.setScene(scene3);
				}
			});
			return rowAgency;
		});

		// Nearly static sqlStatement for retrieving info on all agencies in the selected city. City is the only thing that changes from time to time.
		String sqlStatement = "SELECT Agencies.ID,Name,Logo,AVG(Rating) FROM "
								+ "Agencies,Addresses,Ratings WHERE "
								+ "Agencies.ID = Addresses.AgencyID and "
								+ "Agencies.ID = Ratings.AgencyID and "
								+ "Addresses.City == '" + city + "';";
		
		// Columns to be shown in the TableView.
		TableColumn<Agency, String> colLogo = new TableColumn<>("Logo");
		TableColumn<Agency, String> colName = new TableColumn<>("Agency");
		TableColumn<Agency, String> colRating = new TableColumn<>("Rating");

		colLogo.setCellValueFactory(new PropertyValueFactory<Agency, String>("logo"));
		colLogo.setMinWidth(250);
		colName.setCellValueFactory(new PropertyValueFactory<Agency, String>("name"));
		colName.setMinWidth(300);
		colRating.setCellValueFactory(new PropertyValueFactory<Agency, String>("rating"));
		colRating.setMinWidth(250);
		
		// Retrieving all agencies from the database.
		ObservableList<Agency> listAgency = FXCollections.observableArrayList
											(DatabaseCommunication.fetchAgency(sqlStatement));

		// Setting the table to display the ObservableList and use the columns created above.
		tableAgency.setItems(listAgency);
		tableAgency.getColumns().addAll(colLogo, colName, colRating);
		
		return tableAgency;
	}
	
	/**
	 * This method contains filter options for searches. 
	 * 
	 * @return Hbox displaying filers
	 */
	
	public HBox filter(){
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.setSpacing(10);
		
		obsListSpecies = DatabaseCommunication.fetchAttribute("Ads", "Species", null, null);
		obsListType = DatabaseCommunication.fetchAttribute("Ads", "Type", null, null);
		obsListAge = DatabaseCommunication.fetchAttribute("Ads", "Age", null, null);
		obsListGender = DatabaseCommunication.fetchAttribute("Ads", "Gender", null, null);
		
		cbSpecies = new ChoiceBox<>(obsListSpecies);
		cbSpecies.setValue(cbSpecies.getItems().get(0));
		cbSpecies.setOnAction(e -> {
			species = (String) cbSpecies.getValue();
			obsListType = DatabaseCommunication.fetchAttribute("Ads", "Type", "Species", (String) cbSpecies.getValue());
			cbType.setItems(obsListType);
			cbType.setValue("Type");
			cbType.setDisable(false);
			cbAge.setDisable(true);
			cbGender.setDisable(true);
			tfDescription.setDisable(false);
		});

		cbType = new ChoiceBox<>(obsListType);
		cbType.setValue(cbType.getItems().get(0));
		cbType.setDisable(true);
		cbType.setOnAction(e -> {
			type = (String) cbType.getValue();
			obsListAge = DatabaseCommunication.fetchAttribute("Ads", "Age", "Type", (String) cbType.getValue());
			cbAge.setItems(obsListAge);
			cbAge.setValue("Age");
			cbAge.setDisable(false);
			cbGender.setDisable(true);
		});

		cbAge = new ChoiceBox<>(obsListAge);
		cbAge.setValue(cbAge.getItems().get(0));
		cbAge.setDisable(true);
		cbAge.setOnAction(e -> {
			age = (String) cbAge.getValue();
			obsListGender = DatabaseCommunication.fetchAttribute("Ads", "Gender", "Age", (String) cbAge.getValue());
			cbGender.setItems(obsListGender);
			cbGender.setValue("Gender");
			cbGender.setDisable(false);
		});

		cbGender = new ChoiceBox<>(obsListGender);
		cbGender.setValue(cbGender.getItems().get(0));
		cbGender.setDisable(true);
		cbGender.setOnAction(e -> gender = (String) cbGender.getValue());

		tfDescription = new TextField();
		tfDescription.setPromptText("Description");
		tfDescription.setDisable(true);

		btnSearch = new Button("Search");
		btnSearch.setOnAction(e -> {
			search();
			bpLayout4 = new BorderPane();
			bpLayout4.setTop(header());
			bpLayout4.setCenter(mainCenterView());
			tvAd.refresh();
			scene4 = new Scene(bpLayout4,800,600);
			window.setScene(scene4);
		});

		hbox.getChildren().addAll(cbSpecies,cbType,cbAge,cbGender,tfDescription,btnSearch);

		return hbox;
	}
	
	/**
	 * This method has two functions. When you select an agency in scene 2 this method gets all ads for that specific agency. After this, you can specify conditions
	 * for which ads you want to view.
	 */
	
	public static void search(){
		
		// Search string to be sent to DB along with small parts of the statement.
		String searchStatement, searchSpecies, searchType, searchAge, searchGender;
		
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
		
		if (firstSearch == true){	
			searchStatement = "SELECT * FROM "
								+ "Ads, Agencies WHERE "
								+ "Agencies.ID = Ads.AgencyID and "
								+ "Agencies.ID == " + chosenAgency.getID() + ";";
			firstSearch = false;
		} else {
			searchStatement = "SELECT * FROM "
								+ "Ads, Agencies WHERE "
								+ "Agencies.ID = Ads.AgencyID and "
								+ "Agencies.ID == " + chosenAgency.getID() + 
								searchSpecies + 
								searchType + 
								searchAge + 
								searchGender + ";";
			
		}
		
		theSearch = DatabaseCommunication.fetchAd(searchStatement);
		System.out.println(searchStatement);
		System.out.println(theSearch.toString());
	}
	
	/**
	 * This method 
	 * 
	 * @return TableView<Ad>
	 */
	
	public TableView<Ad> searchResults(){

		// Columns to be added to the TableView.
		TableColumn<Ad, String> pictureCol = new TableColumn<>("Picture");
		TableColumn<Ad, String> speciesCol = new TableColumn<>("Species");
		TableColumn<Ad, String> typeCol = new TableColumn<>("Type");
		TableColumn<Ad, String> genderCol = new TableColumn<>("Gender");
		
		pictureCol.setMinWidth(190);
		speciesCol.setMinWidth(190);
		typeCol.setMinWidth(190);
		genderCol.setMinWidth(190);
		
		ObservableList<Ad> adList = FXCollections.observableArrayList(theSearch);

		pictureCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("picture"));
		speciesCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("species"));
		typeCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("type"));
		genderCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("gender"));

		tvAd.setRowFactory( tv -> {
			TableRow<Ad> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
		
				if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
					Ad ad = row.getItem();
					ViewAd.display(ad.getName(), ad, chosenAgency);;
				}
			});
			return row ;
		});

		tvAd.setItems(adList);
			
		tvAd.getColumns().addAll(pictureCol, speciesCol, typeCol, genderCol);

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
			window.setScene(scene1);
		} else if (window.getScene() == scene3){
			window.setScene(scene2);
		} else if (window.getScene() == scene4){
			firstSearch = true;
			search();
			bpLayout3 = new BorderPane();
			bpLayout3.setTop(header());
			bpLayout3.setCenter(mainCenterView());
			scene3 = new Scene(bpLayout3,800,600);
			window.setScene(scene3);
		} else {
			window.setScene(scene1);
		}
	}

	/**
	 * Go to start view.
	 */

	public void backToStart(){
		window.setScene(scene1);
	}
}