import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ViewMaster extends Application{
	
	ChoiceBox<Object> location, cb1,cb2,cb3,cb4;
	String city, species, type, age, gender;
	TextField tf1;
	Button btn1;
	Stage window;
	Scene sc1, sc2, sc3, sc4;
	BorderPane layout1, layout2, layout3, layout4;
	int adID;

	public static void main(String[] args){
		launch(args);
	}    

	public void start(Stage primaryStage){
		window = primaryStage;
		window.setTitle("Marketplace");
		VBox head = theHead();
	//	Label header = new Label("This is the header");
		

		//Start view - Location
		layout1 = new BorderPane();
		VBox vbox = startLocation();
		layout1.setTop(head);
		layout1.setCenter(vbox);
		sc1 = new Scene(layout1,800,600);

		//Start view - Agencies
		layout2 = new BorderPane();
		GridPane grid = startAgencies();
		layout2.setTop(head);
		layout2.setCenter(grid);
		sc2 = new Scene(layout2,800,600);

		//Main view
		layout3 = new BorderPane();
		GridPane results = mainResults();
		layout3.setTop(head);
		layout3.setCenter(results);
		sc3 = new Scene(layout3,800,600);
		
		

		window.setScene(sc1);
		window.show();


	}



	private VBox theHead() {
		VBox head = new VBox();
		Image image = new Image(getClass().getResourceAsStream("iAdopt.png"));
		ImageView header = new ImageView();
		header.setImage(image);
		
		if(window.getScene() == sc3){
			HBox filter = mainFilter();
			head.getChildren().addAll(header, filter);			
		} else {
			head.getChildren().add(header);
		}
		
		return head;
	}

	public VBox startLocation(){
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(275));
		vbox.setSpacing(40);

		Label label = new Label("Where are you?");
		location = new ChoiceBox<>(DatabaseConnection.getListFromDatabase("Location"));
		location.setValue(location.getItems().get(0));
		//TODO - save location and goto sc2
		location.setOnAction(e ->  {
				city = (String) location.getValue();
				System.out.println(city);
				window.setScene(sc2);
		});
		
		Button inputPage = new Button("Go to input page");
		inputPage.setOnAction(e -> InputPage.display());

		vbox.getChildren().addAll(label, location, inputPage);
		return vbox;
	}

	

	//TODO - present agencies on location (see below)
	private GridPane startAgencies() {
		GridPane grid = new GridPane();
		String thisLocation = this.city;
		grid.setHgap(200);
		grid.setVgap(40);
		grid.setPadding(new Insets(200,100,200,100));

		//Column name Agency (this will probably not be in 1.0)
		Text agencyAttribute = new Text("Agency");
		grid.add(agencyAttribute, 0, 0); 

		//Column name Logo (this will probably not be in 1.0)
		Text logoAttribute = new Text("Logo");
		grid.add(logoAttribute, 1, 0);
		
//		Button getLocation = new Button("Get location");
//		grid.add(getLocation, 0, 1);
//		getLocation.setOnAction(e -> thisLocation = this.city);
//		
		
		
		//A test Agency, here we will have a list of clickable agencies
		Button testAgency = new Button("Agency in" + thisLocation);
		grid.add(testAgency, 0, 2);
		testAgency.setOnAction(e -> window.setScene(sc3));
		
		//Return to start page
		Button backToStart = new Button("Go back to the start page");
		grid.add(backToStart, 1, 3);
		backToStart.setOnAction(e -> backToStart());

	//	String[] agencies = DatabaseConnection.getAgencies(thisLocation);
		//TODO - present agencies on location
		// ACTIVE OBJECT???
		
		return grid;

	}



	//TODO - Get ChoiceBox.setValue to work
	public HBox mainFilter(){
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(10, 100, 10, 100));
		hbox.setSpacing(10);

		cb1 = new ChoiceBox<>(DatabaseConnection.getListFromDatabase("Species"));
		cb1.setValue(cb1.getItems().get(0));
		cb1.setOnAction(e -> species = (String) cb1.getValue());
		
		cb2 = new ChoiceBox<>(DatabaseConnection.getListFromDatabase("Type"));
		cb2.setValue(cb2.getItems().get(0));
		cb2.setOnAction(e -> type = (String) cb2.getValue());
		
		cb3 = new ChoiceBox<>(DatabaseConnection.getListFromDatabase("Age"));
		cb3.setValue(cb3.getItems().get(0));
		cb3.setOnAction(e -> age = (String) cb3.getValue());
		
		cb4 = new ChoiceBox<>(DatabaseConnection.getListFromDatabase("Gender"));
		cb4.setValue(cb4.getItems().get(0));
		cb4.setOnAction(e -> gender = (String) cb4.getValue());
		
		tf1 = new TextField();
		tf1.setPromptText("Description");
		
		btn1 = new Button("Search");
		btn1.setOnAction(e -> search());

		hbox.getChildren().addAll(cb1,cb2,cb3,cb4,tf1,btn1);

		return hbox;

	}

//	//TODO - Here we get some values from the database with the attribute as the input
//	private ObservableList<String> getListFromDatabase(String attribute) {
//		ObservableList<String> result = 
//			    FXCollections.observableArrayList(
//			        "Option 1",
//			        "Option 2",
//			        "Option 3"
//			    );
//		return result;
//	}



	//TODO - present search results (see below)
	public GridPane mainResults(){
		GridPane grid = new GridPane();
		grid.setHgap(40);
		grid.setVgap(40);
		grid.setPadding(new Insets(0,100,0,100));


		Button testAd = new Button("The test ad");
		grid.add(testAd, 0, 1);
		testAd.setOnAction(e -> ViewAd.display("Test ad", 1));
		
		Button backToStart = new Button("Go back to the start page");
		grid.add(backToStart, 1, 3);
		backToStart.setOnAction(e -> backToStart());

		
		//TODO - present search results
		
		return grid;
	}
	
	public void backToStart(){
		window.setScene(sc1);
	}

	//TODO - Query the database with global values from ChoiceBox and TextField
	public void search(){
		String searchStatement = species + ", " + type + ", " + age + ", " + gender + ", " + tf1.getText();
		System.out.println(searchStatement);
		window.setScene(sc3);
	}
}