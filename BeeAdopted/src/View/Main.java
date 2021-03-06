package View;
import java.util.ArrayList;
import org.controlsfx.control.RangeSlider;

import Control.Membership;
import Object.Ad;
import Object.Database;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Class for starting the application, it also contains the DSS's main functionality, the search function.
 * 
 * The application launch shows the location window where the user inputs his or her desired city for searching.
 * One is then shown a table containing ads in the specified location, clickable to show additional information.
 * 
 * @author Mattias Landkvist
 */

public class Main extends Application {

	private static Stage window;
	private static Scene scene1, scene2, scene3;
	private static BorderPane bpLayout1, bpLayout2, layoutViewAd;
	private static TableView<Ad> tvAd;
	private static ArrayList<Ad> searchResults = null;
	private static ObservableList<Object> obsListSpecies, obsListType, obsListGender, obsListAgencies;
	private static ObservableList<Ad> compareAds = FXCollections.observableArrayList();
	private static ChoiceBox<Object> cbCity, cbSpecies, cbType, cbGender, cbAgency;
	private static RangeSlider rslAge;
	private static String city, species, type, gender, agency;
	private static boolean firstSearch;
	private static Database db = new Database();
	private static int minAge = 0;
	private static int maxAge = 100;
	private static Label back, name;

	/**
	 * The main method, only used to start the application with the launch command from javaFX.
	 */
	public static void main(String[] args){
		launch(args);
	}


	/**
	 * Start method that is launched with Application.launch(args). This builds the main windows shown on launch which is
	 * the prompt for a location. This view also contains a button for logging into the MemberPages, the code for this is in
	 * getCityChooser().
	 * 
	 * @param Stage
	 */
	public void start(Stage primaryStage){
		window = primaryStage;
		window.setTitle("Marketplace");	

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
	 * This method returns a header for use as a top element of a BorderPane. The header consists 
	 * of the BeeAdopted logotype and a back arrow.
	 * 
	 * @return BorderPane with logotype and navigation arrow
	 */
	private BorderPane getHeader(){
		BorderPane header = new BorderPane();
		header.getStyleClass().add("header");

		back = new Label("<");
		back.setId("backbutton");
		back.getStyleClass().add("backbutton");
		back.setOnMouseClicked(e -> back());

		name = new Label("BeeAdopted");
		name.setId("beeadopted");
		name.getStyleClass().add("beeadopted");


		// All items created are added to the BorderPane.
		header.setLeft(back);
		header.setCenter(name);

		return header;
	}

	/**
	 * This method change the scene. Either from scene2 to scene1 or from any other scene to scene2. 
	 * When changed to scene2 the ObservableList<Ad> in tvAd is iterated through, changing pictures 
	 * to a size appropriate for showing in a TableView.
	 */
	public static void back(){
		if(window.getScene() == scene2) {
			cbCity.setValue("City");
			window.setScene(scene1);
		} else {
			for(int i = 0; i < tvAd.getItems().size(); i++){
				tvAd.getItems().get(i).getPicture().setFitHeight(50);
				tvAd.getItems().get(i).getPicture().setFitWidth(50);
			}
			window.setScene(scene2);
		}
	}


	/**
	 * Refresh the content of the tvAd TableView.
	 * 
	 * @author Måns Thörnvik
	 */
	private static void refreshTable() {
		ObservableList<Ad> adList = db.createObservableList(searchResults);

		tvAd.setItems(adList);
		tvAd.refresh();
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

		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(25);

		obsListSpecies = db.createObservableList("Species",db.fetchResult(
				db.executeQuery("SELECT DISTINCT Species FROM Ads, Agencies, Addresses "
						+ "WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID " + city
						+ "and EndDate >= DATE('NOW') ORDER BY Species;")));
		db.closeConnection();

		obsListType = db.createObservableList("Type",db.fetchResult(
				db.executeQuery("SELECT DISTINCT Type FROM Ads, Agencies, Addresses "
						+ "WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID " + city
						+ "and EndDate >= DATE('NOW') ORDER BY Type;")));
		db.closeConnection();

		obsListGender = db.createObservableList("Gender",db.fetchResult(
				db.executeQuery("SELECT DISTINCT Gender FROM Ads, Agencies, Addresses "
						+ "WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID " + city
						+ "and EndDate >= DATE('NOW') ORDER BY Gender;")));
		db.closeConnection();

		obsListAgencies = db.createObservableList("Agencies",db.fetchResult(
				db.executeQuery("SELECT DISTINCT Name FROM Agencies, Addresses "
						+ "WHERE Agencies.ID == Addresses.AgencyID " + city
						+ "ORDER BY Name;")));
		db.closeConnection();

		cbSpecies = new ChoiceBox<>(obsListSpecies);
		cbSpecies.setPrefWidth(150);
		cbSpecies.setValue(cbSpecies.getItems().get(0));
		cbSpecies.setOnAction(e -> {
			if(!((String)cbSpecies.getValue()).equals("Species")){
				species = (String) cbSpecies.getValue();

				obsListType = db.createObservableList("Type",db.fetchResult(
						db.executeQuery("SELECT Distinct Type FROM Ads "
								+ "WHERE Species == '" + species + "' ORDER BY Type;")));
				db.closeConnection();

				cbType.setItems(obsListType);
				cbType.setValue(cbType.getItems().get(0));

				type = "Type";	// Reset value of string "type" when a new species is selected
			} else {
				species = (String) cbSpecies.getValue();

				obsListType = db.createObservableList("Type",db.fetchResult(
						db.executeQuery("SELECT DISTINCT Type FROM Ads, Agencies, Addresses "
								+ "WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID " + city
								+ "and EndDate >= DATE('NOW') ORDER BY Type;")));
				db.closeConnection();

				cbType.setItems(obsListType);
				cbType.setValue(cbType.getItems().get(0));

				type = "Type";
			}
		});

		cbType = new ChoiceBox<>(obsListType);
		cbType.setPrefWidth(150);
		cbType.setValue(cbType.getItems().get(0));
		cbType.setOnAction(e -> type = (String) cbType.getValue());

		String minAgeStatement, maxAgeStatement;

		if(!city.equals("")){
			minAgeStatement = "SELECT MIN(Age) "
					+ "FROM Ads, Agencies, Addresses "
					+ "WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID " + city 
					+ "and EndDate >= DATE('NOW');";

			maxAgeStatement = "SELECT MAX(Age) "
					+ "FROM Ads, Agencies, Addresses "
					+ "WHERE Agencies.ID == Ads.AgencyID and Agencies.ID == Addresses.AgencyID " + city 
					+ "and EndDate >= DATE('NOW');";
		} else {
			minAgeStatement = "SELECT MIN(Age) "
					+ "FROM Ads "
					+ "WHERE EndDate >= DATE('NOW');";
			maxAgeStatement = "SELECT MAX(Age) "
					+ "FROM Ads "
					+ "WHERE EndDate >= DATE('NOW');";
		}		
		minAge = Integer.parseInt(db.fetchResult(db.executeQuery(minAgeStatement)).get(0).get(0));
		maxAge = Integer.parseInt(db.fetchResult(db.executeQuery(maxAgeStatement)).get(0).get(0));		
		db.closeConnection();

		rslAge = new RangeSlider();
		rslAge.setMin(minAge);
		rslAge.setMax(maxAge);
		rslAge.setShowTickLabels(true);
		rslAge.setShowTickMarks(true);
		rslAge.setSnapToTicks(true);
		rslAge.setBlockIncrement(3);
		rslAge.setPrefWidth(150);
		rslAge.setHighValue(maxAge);

		HBox secondaryFilters = new HBox();
		secondaryFilters.setAlignment(Pos.CENTER);
		secondaryFilters.getStyleClass().add("hbox");
		secondaryFilters.setPadding(new Insets(10, 10, 10, 10));
		secondaryFilters.setSpacing(75);

		cbGender = new ChoiceBox<>(obsListGender);
		cbGender.setPrefWidth(150);
		cbGender.setValue(cbGender.getItems().get(0));
		cbGender.setOnAction(e -> gender = (String) cbGender.getValue());

		cbAgency = new ChoiceBox<>(obsListAgencies);
		cbAgency.setPrefWidth(150);
		cbAgency.setValue(cbAgency.getItems().get(0));
		cbAgency.setOnAction(e -> {
			agency = InputPage.correctInput((String) cbAgency.getValue());
		});

		Button btnSearch = new Button("Search");
		btnSearch.setOnAction(e -> {
			search();
			refreshTable();
		});

		Button btnReset = new Button("Reset filters");
		btnReset.setOnAction(e -> {
			cbSpecies.setValue(cbSpecies.getItems().get(0));
			cbType.setValue(cbType.getItems().get(0));
			rslAge.setLowValue(minAge);
			rslAge.setHighValue(maxAge);
			cbGender.setValue(cbGender.getItems().get(0));
			cbAgency.setValue(cbAgency.getItems().get(0));
		});

		buttons.getChildren().addAll(btnReset, btnSearch);
		primaryFilters.getChildren().addAll(cbSpecies,cbType,rslAge,cbGender,cbAgency);
		filters.getChildren().addAll(primaryFilters, buttons);

		return filters;
	}

	/**
	 * This method send a search query to the database and saves it to the global variable searchResults. 
	 * Depending on what is specified in the global variables city, species, type, age, gender and agency
	 * the search returns different results.
	 */
	public static void search(){
		String searchStatement, searchSpecies, searchType, searchGender, searchAgency;

		// Null values are ignored as well as if the user left the cbs in their original posistions.
		if (species != null && species != "Species" && species != "Select all") {
			searchSpecies = "and Species == '" + species + "' ";
		} else {searchSpecies = "";}

		if (type != null && type != "Type" && type != "Select all") {
			searchType = "and Type == '" + type + "' ";
		} else {searchType = "";}

		if (gender != null && gender != "Gender" && gender != "Select all") {
			searchGender = "and Gender == '" + gender + "' ";
		} else {searchGender = "";}

		if (agency != null && agency != "Agencies" && agency != "Select all") {
			searchAgency = "and Agencies.Name == '" + agency + "' ";
		} else {searchAgency = "";}

		if (firstSearch == true) {	
			if (!city.equals("")) {
				searchStatement = "SELECT DISTINCT Ads.ID, Picture, Ads.Name, Species, Type, Gender, Age, Description, StartDate, EndDate, Ads.AgencyID, "
						+ "Agencies.Name as Agency, AVG(Rating) as Rating "
						+ "FROM Ads, Agencies, Addresses, Ratings "
						+ "WHERE Agencies.ID == Addresses.AgencyID and "
						+ "Agencies.ID == Ratings.AgencyID and "
						+ "Agencies.ID == Ads.AgencyID and " 
						+ "EndDate >= DATE('NOW') "
						+ city
						+ "GROUP BY Ads.ID "
						+ "ORDER BY Ads.ID;";
			} else {
				searchStatement = "SELECT DISTINCT Ads.ID, Picture, Ads.Name, Species, Type, Gender, Age, Description, StartDate, EndDate, Ads.AgencyID, "
						+ "Agencies.Name as Agency, AVG(Rating) as Rating "
						+ "FROM Ads, Agencies, Ratings "
						+ "WHERE Agencies.ID == Ratings.AgencyID and "
						+ "Agencies.ID == Ads.AgencyID and "
						+ "EndDate >= DATE('NOW') "
						+ "GROUP BY Ads.ID "
						+ "ORDER BY Ads.ID;";
			}
			firstSearch = false;
		} else {
			searchStatement =  "SELECT DISTINCT Ads.ID,Picture, Ads.Name, Species, Type, Gender, Age, Description, StartDate, EndDate, Ads.AgencyID, "
					+ "Agencies.Name as Agency,AVG(Rating) as Rating "
					+ "FROM Ads, Agencies, Ratings, Addresses "
					+ "WHERE Agencies.ID == Addresses.AgencyID and "
					+ "Agencies.ID == Ads.AgencyID and "
					+ "Agencies.ID == Ratings.AgencyID and "
					+ "EndDate >= DATE('NOW') "
					+ city
					+ searchSpecies
					+ searchType
					+ "and Age >= " + rslAge.getLowValue() + " and Age <= " + rslAge.getHighValue() + " "
					+ searchGender 
					+ searchAgency 
					+ "GROUP BY Ads.ID "
					+ "ORDER BY Ads.ID;";
		}
		searchResults = db.fetchAd(db.executeQuery(searchStatement));
		db.closeConnection();
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
		cbCity = new ChoiceBox<>(db.createObservableList("City",db.fetchResult(
				db.executeQuery("SELECT Distinct City FROM "
						+ "Addresses ORDER BY "
						+ "City;"))));
		db.closeConnection();

		cbCity.setValue(cbCity.getItems().get(0));	// Getting retrieved items from the DB.

		cbCity.setOnAction(e -> {
			if(!cbCity.getValue().equals("City")){
				if(!cbCity.getValue().equals("Select all")){
					city = " and City == '" + cbCity.getValue() + "'";		// What location was input?
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
		firstPage.getChildren().addAll(lblLocation, cbCity);
		firstPage.setAlignment(Pos.CENTER);

		return firstPage;
	}

	/**
	 * This method returns a VBox with a TableView<Ad> and a button. The button change the center layout to Compare.compareAds().
	 * 
	 * @return VBox with a TableView populated by n Ad objects from a database and a button.
	 */

	@SuppressWarnings("unchecked")
	public VBox getTableViewAndCompare(){
		VBox vbox = new VBox();
		vbox.getStyleClass().add("vbox");
		vbox.setAlignment(Pos.CENTER);

		tvAd = new TableView<Ad>();
		tvAd.setEditable(true);

		// Columns to be added to the TableView.
		TableColumn<Ad, String> pictureCol = new TableColumn<>("Picture");
		TableColumn<Ad, String> nameCol = new TableColumn<>("Name");
		TableColumn<Ad, String> speciesCol = new TableColumn<>("Species");
		TableColumn<Ad, String> typeCol = new TableColumn<>("Type");
		TableColumn<Ad, String> ratingCol = new TableColumn<>("Rating");
		TableColumn<Ad, Boolean> checkCol = new TableColumn<>("Compare");

		ObservableList<Ad> adList = db.createObservableList(searchResults);

		pictureCol.setCellValueFactory(new PropertyValueFactory<>("picture"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		speciesCol.setCellValueFactory(new PropertyValueFactory<>("species"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
		checkCol.setCellValueFactory(new PropertyValueFactory<>("check"));
		checkCol.setCellFactory(new Callback<TableColumn<Ad, Boolean>, TableCell<Ad,Boolean>>(){
			public TableCell<Ad, Boolean> call(TableColumn<Ad, Boolean> p) {
				final CheckBoxTableCell<Ad, Boolean> ctCell = new CheckBoxTableCell<>();
				ctCell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(Integer index) {
						return tvAd.getItems().get(index).getCheckProperty();
					}
				});
				return ctCell;
			}
		});

		tvAd.setRowFactory( tv -> {
			TableRow<Ad> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
					Ad ad = (Ad) row.getItem();
					try {
						layoutViewAd = new BorderPane();
						layoutViewAd.setTop(getHeader());
						layoutViewAd.setCenter(AdView.showAd(ad, ad.getAgencyID()));
						tvAd.refresh();
						scene3 = new Scene(layoutViewAd,800,600);
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
		tvAd.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		Button btnCompare = new Button("Compare Ads");
		btnCompare.setOnAction(e -> {
			getCheckedAds(adList);
			layoutViewAd = new BorderPane();
			layoutViewAd.setTop(getHeader());
			layoutViewAd.setCenter(Compare.compareAds(compareAds));
			tvAd.refresh();
			scene3 = new Scene(layoutViewAd,800,600);
			scene3.getStylesheets().add("style.css");
			window.setScene(scene3);
			compareAds.removeAll(compareAds);

		});

		vbox.getChildren().addAll(tvAd, btnCompare);
		return vbox;
	}

	/**
	 * @return VBox with the filter, the TableView and the compare Button.
	 */
	public VBox getAdSearchAndChooser(){
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(0));
		vbox.setSpacing(10);

		vbox.getChildren().addAll(getFilter(), getTableViewAndCompare());

		return vbox;
	}

	/**
	 * Ad every selected Ad to compareAds.
	 * @param ObservableList<Ad> to go through
	 */
	private void getCheckedAds(ObservableList<Ad> adList){
		for(int i = 0; i < adList.size(); i++){
			if(adList.get(i).getCheck()){
				compareAds.add(tvAd.getItems().get(i));
			}
		}
	}
}