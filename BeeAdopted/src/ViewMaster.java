
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
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

public class ViewMaster extends Application{

	private static ChoiceBox<Object> location, cb1,cb2,cb3,cb4;
	private static String city, species, type, age, gender;
	private static TextField tf1;
	private static  Button btn1;
	private static Stage window;
	private static Scene sc1, sc2, sc3, sc4;
	private static BorderPane layout1, layout2, layout3, layout4;
	private static ArrayList<Ad> theSearch;
	private static int adID;
	static boolean isSearch = false;



	//Launch the application
	public static void main(String[] args){
		launch(args);
	}    

	/**
	 * Start method that is launched 
	 * with Application.launch(args) 
	 * in main. Building the window.
	 * @param primaryStage
	 */
	public void start(Stage primaryStage){
		window = primaryStage;
		window.setTitle("Marketplace");
		//VBox head = theHead();
		//	Label header = new Label("This is the header");		

		//Start view - Location
		layout1 = new BorderPane();
		VBox vbox = startLocation();
		layout1.setTop(header());
		layout1.setCenter(vbox);
		sc1 = new Scene(layout1,800,600);

		//Main view
		layout3 = new BorderPane();
		search();
		layout3.setTop(header());
		layout3.setCenter(mainCenterView());
		sc3 = new Scene(layout3,800,600);

		window.setScene(sc1);
		window.show();
	}


	/**
	 * The header complete with a 
	 * image in the form of a VBox
	 * @return head as a VBox
	 */
	private VBox header(){
		VBox head = new VBox();
		Image image = new Image(getClass().getResourceAsStream("iAdopt.png"));
		ImageView header = new ImageView();
		header.setImage(image);
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> goBack());


		//		if(isOnView){
		//			HBox filter = mainFilter();
		//			head.getChildren().addAll(backButton, header, filter);			
		//		} else {
		head.getChildren().addAll(backButton,header);
		//		}

		return head;
	}


	/**
	 * Go to previous scene.
	 */
	private void goBack() {
		if(window.getScene() == sc2){
			window.setScene(sc1);
		} else if (window.getScene() == sc3 || window.getScene() == sc4){
			window.setScene(sc2);
		} else {
			window.setScene(sc1);
		}
	}

	/**
	 * A VBox element where a location 
	 * can be choosed from a ChoiceBox 
	 * item. The choosen value gets saved 
	 * when pressed.
	 * 
	 * Also holds an option to go to the 
	 * input page.
	 * 
	 * @return vbox
	 */
	public VBox startLocation(){
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(175));
		vbox.setSpacing(40);

		Label label = new Label("Where are you?");
		location = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Addresses", "City"));
		location.setValue(location.getItems().get(0));
		//TODO - save location and goto sc2
		location.setOnAction(e ->  {
			city = (String) location.getValue();
			System.out.println(city);
			//Start view - Agencies
			layout2 = new BorderPane();
			layout2.setTop(header());
			layout2.setCenter(startAgencies());
			sc2 = new Scene(layout2,800,600);
			window.setScene(sc2);
		});

		Button inputPage = new Button("Go to input page");
		inputPage.setOnAction(e -> InputPage.display());

		vbox.getChildren().addAll(label, location, inputPage);
		return vbox;
	}

	//TODO - present agencies on location (see below)
	private VBox startAgencies() {
		VBox grid = new VBox();
		String thisLocation = this.city;
		String sqlStatement = "SELECT Name,Logo,AVG(Rating) FROM Agencies,Addresses,Ratings WHERE Agencies.ID = Addresses.AgencyID and Agencies.ID = Ratings.AgencyID and Addresses.City == '" + thisLocation + "';";
		grid.setPadding(new Insets(10,10,10,10));

		ObservableList<Agency> agencyList = FXCollections.observableArrayList(DatabaseCommunication.fetchAgency(sqlStatement));
		System.out.println(agencyList.toString());
		TableView<Agency> table = new TableView<>();
		table.setEditable(true);
		TableColumn<Agency, String> logoCol = new TableColumn<>("Logo");
		TableColumn<Agency, String> agencyCol = new TableColumn<>("Agency");
		TableColumn<Agency, String> ratingCol = new TableColumn<>("Rating");

		agencyCol.setCellValueFactory(new PropertyValueFactory<Agency,String>("name"));
		logoCol.setCellValueFactory(new PropertyValueFactory<Agency, String>("logo"));
		ratingCol.setCellValueFactory(new PropertyValueFactory<Agency,String>("rating"));

		agencyCol.setOnEditCommit(e -> System.out.println("Hi agency!"));
		logoCol.setOnEditCommit(e -> System.out.println("Hi logo!"));
		ratingCol.setOnEditCommit(e -> System.out.println("Hi rating!"));

		table.setItems(agencyList);
		table.getColumns().addAll(logoCol, agencyCol, ratingCol);


		//A test Agency, here we will have a list of clickable agencies
		Button testAgency = new Button("Agency in " + thisLocation);
		testAgency.setOnAction(e -> window.setScene(sc3));

		//Return to start page
		Button backToStart = new Button("Go back to the start page");
		backToStart.setOnAction(e -> backToStart());

		grid.getChildren().addAll(table, testAgency, backToStart);
		//	String[] agencies = DatabaseConnection.getAgencies(thisLocation);
		//TODO - present agencies on location
		// ACTIVE OBJECT???

		return grid;

	}

	//TODO - Get ChoiceBox.setValue to work
	public HBox mainFilter(){
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.setSpacing(10);

		cb1 = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Species"));
		cb1.setValue(cb1.getItems().get(0));
		cb1.setOnAction(e -> species = (String) cb1.getValue());

		cb2 = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Type"));
		cb2.setValue(cb2.getItems().get(0));
		cb2.setOnAction(e -> type = (String) cb2.getValue());

		cb3 = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Age"));
		cb3.setValue(cb3.getItems().get(0));
		cb3.setOnAction(e -> age = (String) cb3.getValue());

		cb4 = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Gender"));
		cb4.setValue(cb4.getItems().get(0));
		cb4.setOnAction(e -> gender = (String) cb4.getValue());

		tf1 = new TextField();
		tf1.setPromptText("Description");

		btn1 = new Button("Search");
		btn1.setOnAction(e -> {
			isSearch = true;
			layout4 = new BorderPane();
			layout4.setTop(header());
			layout4.setCenter(mainCenterView());
			System.out.println(theSearch.toString());
			sc4 = new Scene(layout4,800,600);
			window.setScene(sc4);
		});

		hbox.getChildren().addAll(cb1,cb2,cb3,cb4,tf1,btn1);

		return hbox;

	}

	//TODO - present search results (see below)
	public VBox mainResults(){
		VBox grid = new VBox();
		grid.setPadding(new Insets(10,10,10,10));


		TableView<Ad> table = new TableView<Ad>();
		TableColumn<Ad, String> pictureCol = new TableColumn<>("Picture");
		TableColumn<Ad, String> speciesCol = new TableColumn<>("Species");
		TableColumn<Ad, String> typeCol = new TableColumn<>("Type");
		TableColumn<Ad, String> genderCol = new TableColumn<>("Gender");



		ObservableList<Ad> adList = FXCollections.observableArrayList(theSearch);
		
		pictureCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("picture"));
		speciesCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("species"));
		typeCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("type"));
		genderCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("gender"));
		
		table.setItems(adList);

		


		table.getColumns().addAll(pictureCol, speciesCol, typeCol, genderCol);

		Button testAd = new Button("The test ad");
		testAd.setOnAction(e -> ViewAd.display("Test ad", 1));

		Button backToStart = new Button("Go back to the start page");
		backToStart.setOnAction(e -> backToStart());


		grid.getChildren().addAll(table, testAd, backToStart);

		//TODO - present search results

		return grid;
	}

	public VBox mainCenterView(){
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(10);

		vbox.getChildren().addAll(mainFilter(), mainResults());

		return vbox;
	}

	public void backToStart(){
		window.setScene(sc1);
	}

	//TODO - Query the database with global values from ChoiceBox and TextField
	public static void search(){
		String startStatement = "SELECT * FROM Ads;";
		
		if(!isSearch){
			theSearch = DatabaseCommunication.fetchAd(startStatement); 
		} else {
			String searchStatement = "SELECT * FROM Ads WHERE "
					+ "Species == '" + species + "' and "
					+ "Type == '" + type + "' and "
					+ "Age == '" + age + "' and " 
					+ "Gender == '" + gender + "' and " 
					+ "Description == '%" + tf1.getText() + "%' ;"; 
			theSearch = DatabaseCommunication.fetchAd(searchStatement); 
			System.out.println(theSearch.toString());
		}
	}
}