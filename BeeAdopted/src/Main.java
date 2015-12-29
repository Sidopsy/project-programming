import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Observable;

import org.controlsfx.control.RangeSlider;

import com.sun.xml.internal.fastinfoset.vocab.SerializerVocabulary;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
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
import javafx.util.Callback;

/**
 * Class for starting the application.
 * 
 * TODO: List all methods.
 * 
 * @author ML
 */

public class Main extends Application {

	private static Stage window;
	private static Scene scene1, scene2, scene3;
	private static BorderPane bpLayout1, bpLayout2, bpLayout3;
	private static TableView<Ad> tvAd;
	private static ArrayList<Ad> theSearch = null;
	private static ObservableList<Object> obsListSpecies, obsListType, obsListGender, obsListAgencies;
	private static ObservableList<Ad> compareAds = FXCollections.observableArrayList();
	private static ChoiceBox<Object> cbLocation, cbSpecies, cbType, cbGender, cbAgencies;
	private static RangeSlider slAge;
	private static String city, species, type, age, gender, agency;
	private static boolean firstSearch;
	private static DBobject db = new DBobject();
	private static String sqlStatement;
	private static Date today = Date.valueOf(LocalDate.now());
	private static int minAge = 0;
	private static int maxAge = 100;
	private static Label back, name;


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
		bpLayout1.setTop(getHeader());					// Top element of the BorderPane is retrieved, which is the iAdopt image.
		bpLayout1.setCenter(getCityChooser());		// Center element of BorderPane contains the dropdown vbox.
		bpLayout1.setBottom(Membership.loginBox());
		back.setVisible(false);
		scene1 = new Scene(bpLayout1,800,600);
		scene1.getStylesheets().add("style.css");


		// Setting the currently open window to show the scene created above.
		window.setScene(scene1);
		window.show();
	}

	/**
	 * This method returs a header for use as a top element of a BorderPane. The header consists of the iAdopt image.
	 * 
	 * @return Vbox header image and navigation buttons
	 */
	private BorderPane getHeader(){

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
	 * Go to start view.
	 */
	public static void back(){
		if(window.getScene() == scene2) {
			cbLocation.setValue("City");
			window.setScene(scene1);
		} else {
			window.setScene(scene2);
		}
	}


	/**
	 * This method contains filter options for searches. 
	 * 
	 * @return Vbox displaying filers
	 */

	public VBox getFilter(){
		VBox filters = new VBox();
		filters.getStyleClass().add("filter");
		filters.setAlignment(Pos.CENTER);

		HBox primaryFilters = new HBox();
		primaryFilters.setAlignment(Pos.CENTER);
		primaryFilters.getStyleClass().add("filter");
		primaryFilters.setPadding(new Insets(10, 10, 10, 10));
		primaryFilters.setSpacing(75);

		obsListSpecies = db.createObservableList("Species",db.fetchResult(db.executeQuery("SELECT Distinct Species FROM Ads, Agencies, Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID " + city + " and EndDate >= '" + today + "' ORDER BY Species;")));
		obsListType = db.createObservableList("Type",db.fetchResult(db.executeQuery("SELECT Distinct Type FROM Ads, Agencies, Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID " + city + " and EndDate >= '" + today + "' ORDER BY Type;")));
		obsListGender = db.createObservableList("Gender",db.fetchResult(db.executeQuery("SELECT Distinct Gender FROM Ads, Agencies, Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID " + city + " and EndDate >= '" + today + "' ORDER BY Gender;")));
		obsListAgencies = db.createObservableList("Agencies",db.fetchResult(db.executeQuery("SELECT Distinct Name FROM Agencies, Addresses WHERE Agencies.ID == Addresses.AgencyID " + city + " ORDER BY Name;")));


		cbSpecies = new ChoiceBox<>(obsListSpecies);
		cbSpecies.setPrefWidth(150);
		cbSpecies.setValue(cbSpecies.getItems().get(0));
		cbSpecies.setOnAction(e -> {
			if(!((String)cbSpecies.getValue()).equals("Species")){
				species = (String) cbSpecies.getValue();
				sqlStatement = "SELECT Distinct Type FROM Ads WHERE Species == '" + species + "' ORDER BY Type;";

				obsListType = db.createObservableList("Type",db.fetchResult(db.executeQuery(sqlStatement)));

				cbType.setItems(obsListType);
				cbType.setValue("Type");
			}
		});

		cbType = new ChoiceBox<>(obsListType);
		cbType.setPrefWidth(150);
		cbType.setValue(cbType.getItems().get(0));
		cbType.setOnAction(e -> {
			type = (String) cbType.getValue();
		});

		String minAgeStatement;
		String maxAgeStatement;
		if(city != "Select all" && city != "City"){
			minAgeStatement = "SELECT MIN(Age) FROM Ads,Agencies,Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID " + city + " and EndDate >= '" + today + "';";
			maxAgeStatement = "SELECT MAX(Age) FROM Ads,Agencies,Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID " + city + " and EndDate >= '" + today + "';";
		} else {
			minAgeStatement = "SELECT MIN(Age) FROM Ads,Agencies,Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID and EndDate >= '" + today + "';";
			maxAgeStatement = "SELECT MAX(Age) FROM Ads,Agencies,Addresses WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID and EndDate >= '" + today + "';";
		}

		minAge = Integer.parseInt(db.fetchResult(db.executeQuery(minAgeStatement)).get(0).get(0));
		maxAge = Integer.parseInt(db.fetchResult(db.executeQuery(maxAgeStatement)).get(0).get(0));
		System.out.println(minAge);
		System.out.println(maxAge);


		Label ageLabel = new Label("Age");
		slAge = new RangeSlider();
		slAge.setMin(minAge);
		slAge.setMax(maxAge);
		slAge.setShowTickLabels(true);
		slAge.setShowTickMarks(true);
		slAge.setSnapToTicks(true);
		slAge.setBlockIncrement(3);
		slAge.setPrefWidth(150);
		slAge.setHighValue(maxAge);
		//	slAge.setShowTickMarks(true);
		//slAge.setMajorTickUnit(15);
		//	slAge.setMinorTickCount(maxAge/2);
		//	slAge.setBlockIncrement(5);



		HBox secondaryFilters = new HBox();
		secondaryFilters.setAlignment(Pos.CENTER);
		secondaryFilters.getStyleClass().add("hbox");
		secondaryFilters.setPadding(new Insets(10, 10, 10, 10));
		secondaryFilters.setSpacing(75);

		cbGender = new ChoiceBox<>(obsListGender);
		cbGender.setPrefWidth(150);
		cbGender.setValue(cbGender.getItems().get(0));
		cbGender.setOnAction(e -> gender = (String) cbGender.getValue());

		cbAgencies = new ChoiceBox<>(obsListAgencies);
		cbAgencies.setPrefWidth(150);
		cbAgencies.setValue(cbAgencies.getItems().get(0));
		cbAgencies.setOnAction(e -> agency = (String) cbAgencies.getValue());

		Button btnSearch = new Button("Search");
		btnSearch.setOnAction(e -> {
			tvAd.getChildrenUnmodifiable().removeAll(theSearch);
			search();
			bpLayout2 = new BorderPane();
			bpLayout2.setTop(getHeader());
			bpLayout2.setCenter(getAdSearchAndChooser());
			tvAd.refresh();
			scene2 = new Scene(bpLayout2,800,600);
			scene2.getStylesheets().add("style.css");
			window.setScene(scene2);
		});


		primaryFilters.getChildren().addAll(cbSpecies,cbType,slAge,cbGender,cbAgencies);

		filters.getChildren().addAll(primaryFilters, btnSearch);



		return filters;
	}

	/**
	 * This method has two functions. When you select an agency in scene 2 this method gets all ads for that specific agency. After this, you can specify conditions
	 * for which ads you want to view.
	 */
	public static void search(){

		// Search string to be sent to DB along with small parts of the statement.
		String searchStatement, searchSpecies, searchType, searchAge, searchGender, searchAgency;

		// Null values are ignored as well as if the user left the cbs in their original posistions.
		if (species != null && species != "Species" && species != "Select all"){
			searchSpecies = " and Species == '" + species + "'";
		} else {
			searchSpecies = "";
		}

		if (type != null && type != "Type" && type != "Select all"){
			searchType = " and Type == '" + type + "'";
		} else {
			searchType = "";

		}

		if (age != null && age != "Age"){
			searchAge = " and Age >= " + slAge.getMin() + " and Age <= " + slAge.getMax();
		} else {
			searchAge = "";
		}

		if (gender != null && gender != "Gender" && gender != "Select all"){
			searchGender = " and Gender == '" + gender + "'";
		} else {
			searchGender = "";
		}

		if (agency != null && agency != "Agency" && agency != "Select all"){
			searchAgency = " and Agencies.Name as Agency == '" + agency + "'";
		} else {
			searchAgency = "";
		}

		if (firstSearch == true){	
			if(city != "Select all"){
				searchStatement = "SELECT Distinct Ads.ID,Picture,Ads.Name,Species,Type,Gender,Age,Description,StartDate,EndDate,Ads.AgencyID,Agencies.Name as Agency, AVG(Rating) as Rating FROM "
						+ "Ads,Agencies,Addresses,Ratings WHERE "
						+ "Agencies.ID = Addresses.AgencyID and "
						+ "Agencies.ID = Ratings.AgencyID and "
						+ "Agencies.ID = Ads.AgencyID and " 
						+ "EndDate >= '" + today + "' "
						+ city
						+ " GROUP BY Ads.ID;";
			} else {
				searchStatement = "SELECT Distinct Ads.ID,Picture,Ads.Name,Species,Type,Gender,Age,Description,StartDate,EndDate,Ads.AgencyID,Agencies.Name as Agency, AVG(Rating) as Rating FROM "
						+ "Ads,Agencies,Ratings WHERE "
						+ "Agencies.ID == Ratings.AgencyID and "
						+ "Agencies.ID == Ads.AgencyID and "
						+ "EndDate >= '" + today 
						+ "' Group BY Ads.ID;";
			}
			firstSearch = false;
		} else {
			System.out.println("Bob0: " + searchSpecies + searchType + searchAge + searchGender);
			searchStatement =  "SELECT Distinct Ads.ID,Picture,Ads.Name,Species,Type,Gender,Age,Description,StartDate,EndDate,Ads.AgencyID,Agencies.Name as Agency,AVG(Rating) as Rating FROM "
					+ "Ads,Agencies,Ratings,Addresses WHERE "
					+ "Agencies.ID == Addresses.AgencyID and "
					+ "Agencies.ID == Ads.AgencyID and "
					+ "Agencies.ID == Ratings.AgencyID and "
					+ "EndDate >= '" + today + "' and "
					+ "Age >= " + slAge.getLowValue() + " and Age <= " + slAge.getHighValue()
					+ city
					+ searchSpecies
					+ searchType
					+ searchAge 
					+ searchGender 
					+ searchAgency 
					+ " GROUP BY Agencies.ID"
					+ " ORDER BY Ads.ID;";

		}

		theSearch = db.fetchAd(db.executeQuery(searchStatement));
		species = null;
		type = null;
		age = null;
		gender = null;
		agency = null;
		System.out.println(searchStatement);
		System.out.println(theSearch.toString());

	}
	
	/**
	 * This method returns a Vbox element containing a dropdown menu of all cities that agencies exist at and a button for
	 * going to the input page.
	 * 
	 * @return Vbox location dropdown menu and input page button
	 */
	private VBox getCityChooser() {

		// The Vbox to be returned.
		VBox firstPage = new VBox();
		firstPage.getStyleClass().add("start-pane");

		// Displayed above the choicebox for cities to let the user know what to do.
		Label lblLocation = new Label("Where are you?");

		// This inputs information into the choicebox, namely the cities in which there are agencies.
		cbLocation = new ChoiceBox<>(db.createObservableList("City",db.fetchResult(
				db.executeQuery("SELECT Distinct City FROM "
						+ "Addresses ORDER BY "
						+ "City;"))));
		db.closeConnection();

		cbLocation.setValue(cbLocation.getItems().get(0));	// Getting retrieved items from the DB.

		cbLocation.setOnAction(e -> {
			if(!cbLocation.getValue().equals("City")){
				if(!cbLocation.getValue().equals("Select all")){
					city = " and City == '" + cbLocation.getValue() + "'";		// What location was input?
					System.out.println(city);
				} else {
					city = "";
				}

				firstSearch = true;												
				search();
				bpLayout2 = new BorderPane();				// Preparing for a new scene.
				bpLayout2.setTop(getHeader());		// Setting the header as before.
				bpLayout2.setCenter(getAdSearchAndChooser());		// Getting the center view for the next scene which now will show a list of agencies in the input location.
				scene2 = new Scene(bpLayout2,800,600);
				scene2.getStylesheets().add("style.css");
				window.setScene(scene2);
			}
		});


		// Vbox is populated with the label, the choicebox and the content from loginBox()
		firstPage.getChildren().addAll(lblLocation, cbLocation);
		firstPage.setAlignment(Pos.CENTER);

		return firstPage;
	}

	/**
	 * This method 
	 * 
	 * @return VBox with a tableview populated by n Ad objects from a database and a button.
	 */
	public VBox getTableViewAndCompare(){
		VBox vbox = new VBox();
		vbox.getStyleClass().add("vbox");
		vbox.setAlignment(Pos.CENTER);

		tvAd = new TableView<Ad>();
		tvAd.setEditable(true);
		//tvAd.setMinWidth(0);

		// Columns to be added to the TableView.
		TableColumn<Ad, String> pictureCol = new TableColumn<>("Picture");
		TableColumn<Ad, String> nameCol = new TableColumn<>("Name");
		TableColumn<Ad, String> speciesCol = new TableColumn<>("Species");
		TableColumn<Ad, String> typeCol = new TableColumn<>("Type");
		TableColumn<Ad, String> ratingCol = new TableColumn<>("Rating");
		TableColumn<Ad, Boolean> checkCol = new TableColumn<>("Compare");

		ObservableList<Ad> adList = db.createObservableList(theSearch);

		pictureCol.setCellValueFactory(new PropertyValueFactory<>("picture"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		speciesCol.setCellValueFactory(new PropertyValueFactory<>("species"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
		checkCol.setCellValueFactory(new PropertyValueFactory<>("check"));
		checkCol.setCellFactory(new Callback<TableColumn<Ad, Boolean>, TableCell<Ad,Boolean>>(){
			public TableCell<Ad, Boolean> call(TableColumn<Ad, Boolean> p) {
			final CheckBoxTableCell<Ad, Boolean> ctCell = new CheckBoxTableCell<>();
			final BooleanProperty selected = new SimpleBooleanProperty();
			ctCell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {

				@Override
				public ObservableValue<Boolean> call(Integer index) {
					
					return tvAd.getItems().get(index).getCheckProperty();
				}
				

			});
			System.out.println("Cell index: " + ctCell.getIndex());
			if(tvAd.getItems().get(ctCell.getIndex() + 1).getCheck() == true){
				compareAds.add(tvAd.getItems().get(ctCell.getIndex() +1 ));
//			} 
//			else  if (tvAd.getItems().get(ctCell.getIndex() + 1).getCheck() == false){
//				compareAds.remove(compareAds.indexOf(tvAd.getItems().get(ctCell.getIndex() + 1)));
			} else {
				return ctCell;
			}
			return ctCell;
		}

	});
//		checkCol.setCellFactory(new Callback<TableColumn<Ad, Boolean>, TableCell<Ad,Boolean>>(){
//			public TableCell<Ad, Boolean> call(TableColumn<Ad, Boolean> p) {
//				final CheckBoxTableCell<Ad, Boolean> ctCell = new CheckBoxTableCell<>();
//				final BooleanProperty selected = new SimpleBooleanProperty();
//				ctCell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
//
//					@Override
//					public ObservableValue<Boolean> call(Integer index) {
//						return selected;
//					}
//
//				});
//				selected.addListener(new ChangeListener<Boolean>() {
//
//					@Override
//					public void changed(ObservableValue<? extends Boolean> obs, Boolean wasSelected, Boolean isSelected) {
//						if (ctCell.selectedProperty().get() != true){
//							compareAds.add(tvAd.getItems().get(ctCell.getIndex()));							
//						} else {
//							compareAds.remove(compareAds.indexOf(tvAd.getItems().get(ctCell.getIndex())));
//						}
//						System.out.println(isSelected);
//						
//
//					}
//
//				});
//				return ctCell;
//			}
//
//		});

		tvAd.setRowFactory( tv -> {
			TableRow<Ad> row = new TableRow<>();

			row.setOnMouseClicked(event -> {

				if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
					Ad ad = (Ad) row.getItem();
					
						try {
							bpLayout3 = new BorderPane();
							bpLayout3.setTop(getHeader());
							bpLayout3.setCenter(AdView.showAd(ad, ad.getAgencyID()));
							tvAd.refresh();
							scene3 = new Scene(bpLayout3,800,600);
							scene3.getStylesheets().add("style.css");
							window.setScene(scene3);
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					
				}
			});
			return row ;
		});


		tvAd.setItems(adList);

		tvAd.getColumns().addAll(pictureCol, nameCol, speciesCol, typeCol, ratingCol,checkCol);
		//vbox.getChildren().add(tvAd);
		tvAd.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		Button btnCompare = new Button("Compare Ads");
		btnCompare.setOnAction(e -> {
		//	getCheckedAds();
			bpLayout3 = new BorderPane();
			bpLayout3.setTop(getHeader());
			bpLayout3.setCenter(Compare.compareAds(compareAds));
			tvAd.refresh();
			scene3 = new Scene(bpLayout3,800,600);
			scene3.getStylesheets().add("style.css");
			window.setScene(scene3);
			
			
		});

		vbox.getChildren().addAll(tvAd, btnCompare);
		return vbox;
	}
	
	public VBox getAdSearchAndChooser(){
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(0));
		vbox.setSpacing(10);

		vbox.getChildren().addAll(getFilter(), getTableViewAndCompare());

		return vbox;
	}
	
//	private void getCheckedAds(){
//		System.out.println("Length: " + tvAd.getItems().toArray().length);
//		int i = 1;
//		while(true){
//			compareAds.add(tvAd.getItems().get(i));
////			else {
////				try {
////					compareAds.remove(compareAds.indexOf(tvAd.getItems().get(i)));
////				} catch (NullPointerException e) {
////					System.out.println("Bop bop");
////				}
////			}
//		}
//	}
}