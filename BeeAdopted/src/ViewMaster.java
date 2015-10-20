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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ViewMaster extends Application{
	ChoiceBox<String> cb1,cb2,cb3,cb4;
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
		StackPane head = new StackPane();
	//	Label header = new Label("This is the header");
		Image image = new Image(getClass().getResourceAsStream("iAdopt.png"));
		ImageView header = new ImageView();
		header.setImage(image);
		
		head.getChildren().add(header);

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

	

	//TODO - present agencies on location (see below)
	private GridPane startAgencies() {
		GridPane grid = new GridPane();
		grid.setHgap(200);
		grid.setVgap(40);
		grid.setPadding(new Insets(200,100,200,100));

		Text agencyAttribute = new Text("Agency");
		grid.add(agencyAttribute, 0, 0); 

		Text logoAttribute = new Text("Logo");
		grid.add(logoAttribute, 1, 0);

		Button agency1 = new Button("Agency 1");
		grid.add(agency1, 0, 1);
		agency1.setOnAction(e -> window.setScene(sc3));

		Button agency2 = new Button("Agency 2");
		grid.add(agency2, 0, 2);
		agency2.setOnAction(e -> window.setScene(sc3));

		Button agency3 = new Button("Agency 3");
		grid.add(agency3, 0, 3);
		agency3.setOnAction(e -> window.setScene(sc3));

		//TODO - present agencies on location
		
		return grid;

	}



	//TODO - Get ChoiceBox.setValue to work
	public HBox mainFilter(){
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);

		cb1 = new ChoiceBox<String>(DatabaseConnection.getListFromDatabase("Species"));
		cb1.setValue("Species");
		
		cb2 = new ChoiceBox<String>(getListFromDatabase("Type"));
		cb2.setValue("Type");
		
		cb3 = new ChoiceBox<String>(getListFromDatabase("Age"));
		cb3.setValue("Age");
		
		cb4 = new ChoiceBox<String>(getListFromDatabase("Gender"));
		cb4.setValue("Type");
		
		tf1 = new TextField();
		
		btn1 = new Button("Search");

		hbox.getChildren().addAll(cb1,cb2,cb3,cb4,tf1,btn1);

		return hbox;

	}

	//TODO - Here we get some values from the database with the attribute as the input
	private ObservableList<String> getListFromDatabase(String attribute) {
		ObservableList<String> result = 
			    FXCollections.observableArrayList(
			        "Option 1",
			        "Option 2",
			        "Option 3"
			    );
		return result;
	}




	public VBox startLocation(){
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(275));
		vbox.setSpacing(40);

		Label label = new Label("Where are you?");
		ChoiceBox<String> location = new ChoiceBox<String>(getListFromDatabase("Location"));
		location.setValue("Location");
		location.setOnAction(e -> window.setScene(sc2));

		vbox.getChildren().addAll(label, location);
		return vbox;
	}

	//TODO - present search results (see below)
	public GridPane mainResults(){
		GridPane grid = new GridPane();
		grid.setHgap(40);
		grid.setVgap(40);
		grid.setPadding(new Insets(0,100,0,100));

		HBox filter = mainFilter();
		grid.add(filter, 0, 0); 

		Button testAd = new Button("The test ad");
		grid.add(testAd, 0, 1);
		testAd.setOnAction(e -> ViewAd.display("Test ad", 0));
		
		
		//TODO - present search results
		
		return grid;
	}

	//TODO - Query the database with global values from ChoiceBox and TextField
	public void search(){
		window.setScene(sc2);
	}
}