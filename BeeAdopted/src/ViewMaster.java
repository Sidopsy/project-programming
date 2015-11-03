
import java.util.ArrayList;

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

public class ViewMaster extends Application{

	private static ChoiceBox<Object> location, cb1,cb2,cb3,cb4;
	private static String city, species, type, age, gender;
	private static TextField tf1;
	private static  Button btn1;
	private static Stage window;
	private static Scene sc1, sc2, sc3, sc4;
	private static BorderPane layout1, layout2, layout3, layout4;
	private static ArrayList<Ad> theSearch;

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
		
		Button startButton = new Button("Start");
		startButton.setOnAction(e -> backToStart());
		
		HBox buttons = new HBox();
		buttons.getChildren().addAll(backButton, startButton);

		head.getChildren().addAll(buttons,header);

		return head;
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
	private VBox startLocation(){
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

	private TableView<Agency> startAgencies() {
		String thisLocation = city;
		String sqlStatement = "SELECT Name,Logo,AVG(Rating) FROM Agencies,Addresses,Ratings WHERE Agencies.ID = Addresses.AgencyID and Agencies.ID = Ratings.AgencyID and Addresses.City == '" + thisLocation + "';";

		TableView<Agency> table = new TableView<>();
		ObservableList<Agency> agencyList = FXCollections.observableArrayList(DatabaseCommunication.fetchAgency(sqlStatement));

		table.setEditable(true);
		TableColumn<Agency, String> logoCol = new TableColumn<>("Logo");
		TableColumn<Agency, String> agencyCol = new TableColumn<>("Agency");
		TableColumn<Agency, String> ratingCol = new TableColumn<>("Rating");

		agencyCol.setCellValueFactory(new PropertyValueFactory<Agency,String>("name"));
		logoCol.setCellValueFactory(new PropertyValueFactory<Agency, String>("logo"));
		ratingCol.setCellValueFactory(new PropertyValueFactory<Agency,String>("rating"));

		table.setRowFactory( tv -> {
			TableRow<Agency> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
					window.setScene(sc3);
				}
			});
			return row ;
		});

		table.setItems(agencyList);
		table.getColumns().addAll(logoCol, agencyCol, ratingCol);

		return table;
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
			//isSearch = true;
			search();
			layout4 = new BorderPane();
			layout4.setTop(header());
			layout4.setCenter(mainCenterView());
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
		TableView<Ad> table = new TableView<Ad>();

		TableColumn<Ad, String> pictureCol = new TableColumn<>("Picture");
		TableColumn<Ad, String> speciesCol = new TableColumn<>("Species");
		TableColumn<Ad, String> typeCol = new TableColumn<>("Type");
		TableColumn<Ad, String> genderCol = new TableColumn<>("Gender");

		ObservableList<Ad> adList = FXCollections.observableArrayList(theSearch);

		pictureCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("picture"));
		pictureCol.setMinWidth(190);
		speciesCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("species"));
		speciesCol.setMinWidth(190);
		typeCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("type"));
		typeCol.setMinWidth(190);
		genderCol.setCellValueFactory(new PropertyValueFactory<Ad,String>("gender"));
		genderCol.setMinWidth(190);

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
	private void goBack() {
		if(window.getScene() == sc2){
			window.setScene(sc1);
		} else if (window.getScene() == sc3){
			window.setScene(sc2);
		} else if (window.getScene() == sc4){
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
		String startStatement;
		String searchStatement;
		//		if(theSearch == null){
		//			theSearch = DatabaseCommunication.fetchAd(startStatement); 
		//		} else {
		if(tf1 == null){	
			searchStatement = "SELECT * FROM Ads;";
		} else {
			searchStatement = "SELECT * FROM Ads WHERE "
					+ "Species == '" + species + "' and "
					+ "Type == '" + type + "' and "
					+ "Age == " + age + " and " 
					+ "Gender == '" + gender + "';";
		}
		theSearch = DatabaseCommunication.fetchAd(searchStatement);
		System.out.println(searchStatement);
		System.out.println(theSearch.toString());
		//		}
	}
}