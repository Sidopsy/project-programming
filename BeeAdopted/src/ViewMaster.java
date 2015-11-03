
import java.util.ArrayList;
import java.util.Observable;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
	private static Scene sc1, sc2, sc3, sc4;
	private static BorderPane layout1, layout2, layout3, layout4;
	private static TableView<Ad> table = new TableView<Ad>();
	private static ArrayList<Ad> theSearch = null;
	private static ObservableList<Object> specieses, typeses, ageses, genderers;
	private static ChoiceBox<Object> location, cb1, cb2, cb3, cb4;
	private static TextField tf1;
	private static Agency chosenAgency = null;
	private static Button btn1;
	private static String city, species, type, age, gender, description;
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
		VBox vbox = startLocation();		// Getting the locations to show in a dropdown menu.

		window = primaryStage;
		window.setTitle("Marketplace");	

		// Creating the window.
		layout1 = new BorderPane();			// BorderPane layout is used.
		layout1.setTop(header());			// Top element of the BorderPane is retrieved, which is the iAdopt image.
		layout1.setCenter(vbox);			// Center element of BorderPane contains the dropdown vbox.
		sc1 = new Scene(layout1,800,600);
		
		// Setting the currently open window to show the scene created above.
		window.setScene(sc1);
		window.show();
	}

	/**
	 * This method returs a header for use as a top element of a BorderPane. The header consists of the iAdopt image.
	 * 
	 * @return Vbox header image and navigation buttons
	 */
	
	private VBox header(){

		VBox head = new VBox();				// The Vbox to be returned.
		Image image = new Image(getClass().getResourceAsStream("BeeAdopted.png"));	// Specifying header image.
		ImageView header = new ImageView();	
		header.setImage(image);				// Adding image to ImageView to be able to be viewed.

		// Buttons for navigation inside the header element.
		Button backButton = new Button("Back");
		Button startButton = new Button("Start");
		backButton.setOnAction(e -> goBack());
		startButton.setOnAction(e -> backToStart());
		
		// Adding the buttons to a Hbox to be displayed horizontally.
		HBox buttons = new HBox();
		buttons.getChildren().addAll(backButton, startButton);

		// All items created are added to Vbox.
		head.getChildren().addAll(buttons,header);

		return head;
	}

	/**
	 * This method returns a Vbox element containing a dropdown menu of all cities that agencies exist at and a button for
	 * going to the input page.
	 * 
	 * @return Vbox location dropdown menu and input page button
	 */
	
	private VBox startLocation(){
		VBox vbox = new VBox();							// The Vbox to be returned.
		vbox.setPadding(new Insets(175));
		vbox.setSpacing(40);

		// This creates the dropdown menu and a label to specify what type of information the user should input.
		Label label = new Label("Where are you?");
		location = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Addresses", "City", null, null));
		location.setValue(location.getItems().get(0));
		
		//TODO - save location and goto sc2
		
		// The dropdown has a guiding function that sends the user to a list of agencies in the specified location.
		location.setOnAction(e -> {
			city = (String) location.getValue();		// What location was input?
			layout2 = new BorderPane();					// Preparing for a new scene.
			layout2.setTop(header());					// Setting the header as before.
			layout2.setCenter(startAgencies());			// Getting the center view for the next scene.
			sc2 = new Scene(layout2,800,600);
			window.setScene(sc2);
		});
		
		// Input page button, guess what it does.
		Button inputPage = new Button("Go to input page");
		inputPage.setOnAction(e -> InputPage.display());
		
		// Vbox is created using the items above.
		vbox.getChildren().addAll(label, location, inputPage);
		
		return vbox;
	}

	/**
	 * This method retrieves agencies based on the global variable city that is altered through the method startLocation().
	 * 
	 * @return TableView<Agency> that shows agencies
	 */
	
	//TODO - present agencies on location (see below)

	private TableView<Agency> startAgencies(){
		String sqlStatement = "SELECT Agencies.ID,Name,Logo,AVG(Rating) FROM "
								+ "Agencies,Addresses,Ratings WHERE "
								+ "Agencies.ID = Addresses.AgencyID and "
								+ "Agencies.ID = Ratings.AgencyID and "
								+ "Addresses.City == '" + city + "';";
		
		// For viewing the agencies
		TableView<Agency> agencyTable = new TableView<>();
		agencyTable.setEditable(true);
		
		TableColumn<Agency, String> logoCol = new TableColumn<>("Logo");
		TableColumn<Agency, String> agencyCol = new TableColumn<>("Agency");
		TableColumn<Agency, String> ratingCol = new TableColumn<>("Rating");
		ObservableList<Agency> agencyList = FXCollections.observableArrayList
											(DatabaseCommunication.fetchAgency(sqlStatement));

		agencyCol.setCellValueFactory(new PropertyValueFactory<Agency,String>("name"));
		agencyCol.setMinWidth(300);
		logoCol.setCellValueFactory(new PropertyValueFactory<Agency,String>("logo"));
		logoCol.setMinWidth(250);
		ratingCol.setCellValueFactory(new PropertyValueFactory<Agency,String>("rating"));
		ratingCol.setMinWidth(250);
		
		agencyTable.setRowFactory( tv -> {
			TableRow<Agency> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
					chosenAgency = row.getItem();
					firstSearch = true;
					search();
					layout3 = new BorderPane();
					layout3.setTop(header());
					layout3.setCenter(mainCenterView());
					sc3 = new Scene(layout3,800,600);
					window.setScene(sc3);
				}
			});
			return row ;
		});
		
		agencyTable.setItems(agencyList);
		agencyTable.getColumns().addAll(logoCol, agencyCol, ratingCol);
		
		return agencyTable;
	}


	//TODO - Get ChoiceBox.setValue to work

	/**
	 * Search filter module
	 * @return hbox with filter
	 */
	
	public HBox filter(){
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.setSpacing(10);
		
		specieses = DatabaseCommunication.fetchAttribute("Ads", "Species", null, null);
		typeses = DatabaseCommunication.fetchAttribute("Ads", "Type", null, null);
		ageses = DatabaseCommunication.fetchAttribute("Ads", "Age", null, null);
		genderers = DatabaseCommunication.fetchAttribute("Ads", "Gender", null, null);
		
		cb1 = new ChoiceBox<>(specieses);
		cb1.setValue(cb1.getItems().get(0));
		cb1.setOnAction(e -> {
			species = (String) cb1.getValue();
			typeses = DatabaseCommunication.fetchAttribute("Ads", "Type", "Species", (String) cb1.getValue());
			cb2.setItems(typeses);
			cb2.setValue("Type");
		});

		cb2 = new ChoiceBox<>(typeses);
		cb2.setValue(cb2.getItems().get(0));
		cb2.setOnAction(e -> {
			type = (String) cb2.getValue();
			ageses = DatabaseCommunication.fetchAttribute("Ads", "Age", "Type", (String) cb2.getValue());
			cb3.setItems(ageses);
			cb3.setValue("Age");
		});

		cb3 = new ChoiceBox<>(ageses);
		cb3.setValue(cb3.getItems().get(0));
		cb3.setOnAction(e -> {
			age = (String) cb3.getValue();
			genderers = DatabaseCommunication.fetchAttribute("Ads", "Gender", "Age", (String) cb3.getValue());
			cb4.setItems(genderers);
			cb4.setValue("Gender");
		});

		cb4 = new ChoiceBox<>(genderers);
		cb4.setValue(cb4.getItems().get(0));
		cb4.setOnAction(e -> gender = (String) cb4.getValue());

		tf1 = new TextField();
		tf1.setPromptText("Description");

		btn1 = new Button("Search");
		btn1.setOnAction(e -> {
			search();
			layout4 = new BorderPane();
			layout4.setTop(header());
			layout4.setCenter(mainCenterView());
			table.refresh();
			sc4 = new Scene(layout4,800,600);
			window.setScene(sc4);
			//			table.refresh();
			//			layout3.requestLayout();
		});

		hbox.getChildren().addAll(cb1,cb2,cb3,cb4,tf1,btn1);

		return hbox;

	}


	//TODO - present search results (see below)
	public TableView<Ad> searchResults(){
		

		TableColumn<Ad, String> pictureCol = new TableColumn<>("Picture");
		TableColumn<Ad, String> speciesCol = new TableColumn<>("Species");
		TableColumn<Ad, String> typeCol = new TableColumn<>("Type");
		TableColumn<Ad, String> genderCol = new TableColumn<>("Gender");
		pictureCol.setMinWidth(190);
		speciesCol.setMinWidth(190);
		typeCol.setMinWidth(190);
		genderCol.setMinWidth(190);
		
	//	if(theSearch != null){
			ObservableList<Ad> adList = FXCollections.observableArrayList(theSearch);

			pictureCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("picture"));
			speciesCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("species"));
			typeCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("type"));
			genderCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("gender"));

			table.setRowFactory( tv -> {
				TableRow<Ad> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
						Ad ad = row.getItem();
						ViewAd.display(ad.getName(), ad);;
					}
				});
				return row ;
			});

			table.setItems(adList);
	//	}
		table.getColumns().addAll(pictureCol, speciesCol, typeCol, genderCol);

		return table;
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
		if(window.getScene() == sc2){
			window.setScene(sc1);
		} else if (window.getScene() == sc3){
			window.setScene(sc2);
		} else if (window.getScene() == sc4){
			firstSearch = true;
			search();
			layout3 = new BorderPane();
			layout3.setTop(header());
			layout3.setCenter(mainCenterView());
			sc3 = new Scene(layout3,800,600);
			window.setScene(sc3);
		} else {
			window.setScene(sc1);
		}
	}

	/**
	 * Go to start view.
	 */

	public void backToStart(){
		window.setScene(sc1);
	}

	/**
	 * Search the database for the chosen values
	 */
	
	public static void search(){
		String searchStatement, s, t, a, g, d;
		if(species != null && species != "Species"){
			s = " and Species == '" + species + "'";
		} else {
			s = "";
		}
		if(type != null && type != "Type"){
			t = "and Type == '" + type + "'";
		} else {
			t = "";
		}
		if(age != null && age != "Age"){
			a = " and Age == " + age;
		} else {
			a = "";
		}
		if(gender != null && gender != "Gender"){
			g = " and Gender == '" + gender + "'";;
		} else {
			g = "";
		}
		if(firstSearch == true){	
			searchStatement = "SELECT * FROM Ads, Agencies WHERE Agencies.ID == " + chosenAgency.getID() +";";
			firstSearch = false;
		} else {
			searchStatement = "SELECT * FROM Ads, Agencies WHERE "
					+ "Agencies.ID == " + chosenAgency.getID() + s + t + a + g + ";";

		}

		theSearch = DatabaseCommunication.fetchAd(searchStatement);
		System.out.println(searchStatement);
		System.out.println(theSearch.toString());
	}
}