
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InputPage {
	static Stage window;
	static Scene sc1, sc2;
	static BorderPane layout1, layout2;
	static ChoiceBox<Object> cb1, cb2, cb3, cb4, cb5;
	static TextField tf, age;
	static TextArea ta;
	static Button btn;
	static ObservableList<Object> type, gender;

	public static void display(){
		window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Input page");
		window.setMinWidth(250);

		// Inputs
		layout1 = new BorderPane();
		GridPane input = theInput();
		layout1.setTop(header());
		layout1.setCenter(input);
		sc1 = new Scene(layout1, 600, 550);

		// Adopt view
		layout2 = new BorderPane();
		GridPane acceptedInput = acceptedInput();
		layout2.setTop(header());
		layout2.setCenter(acceptedInput);
		sc2 = new Scene(layout2, 600, 550);

		window.setScene(sc1);
		window.showAndWait();
	}
	
	private static StackPane header(){
		StackPane head = new StackPane();
		Image image = new Image("BeeAdoptedSmall.png");
		ImageView header = new ImageView();
		header.setImage(image);
		head.getChildren().add(header);
		return head;
	}

	private static GridPane acceptedInput() {
		// TODO Auto-generated method stub
		return null;
	}

	private static GridPane theInput() {
		GridPane input = new GridPane();
		input.setHgap(40);
		input.setVgap(20);
		input.setPadding(new Insets(10, 10, 10, 10));
		
		tf = new TextField();
		tf.setPromptText("Name");
		tf.setMaxWidth(150);
		input.add(tf,0,0);
		
		age = new TextField();
		age.setPromptText("Age");
		age.setMaxWidth(150);
		input.add(age, 1, 0);
		
		ta = new TextArea();
		ta.setPromptText("Description");
		ta.setMaxWidth(150);
		input.add(ta, 0, 1);
	
		cb1 = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Agencies", "Name", null, null));
		cb1.setValue(cb1.getItems().get(0));
		input.add(cb1, 0, 2);
		// cb1.setOnAction(e -> agency = (String) cb1.getValue());

		cb2 = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Species", null, null));
		cb2.setValue(cb2.getItems().get(0));
		cb2.setOnAction(e -> {
			type = DatabaseCommunication.fetchAttribute("Ads", "Type", "Species", (String)cb2.getValue());
			cb3.setItems(type);
			cb3.setValue("Type");
		});
		input.add(cb2, 1, 2);
		// cb2.setOnAction(e -> species = (String) cb2.getValue());

		cb3 = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Type", null, null));
		cb3.setValue(cb3.getItems().get(0));
		cb3.setOnAction(e -> {
			gender = DatabaseCommunication.fetchAttribute("Ads", "Gender", "Type", (String)cb3.getValue());
			cb5.setItems(gender);
			cb5.setValue("Gender");
		});
		input.add(cb3, 0, 3);
		
		cb5 = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Gender", null, null));
		cb5.setValue(cb5.getItems().get(0));
		input.add(cb5, 1, 3);


		Label uploadText = new Label("Upload picture");
		input.add(uploadText, 0,4);
		
		Button bowse = new Button("Browse");
		bowse.setDisable(true);
		input.add(bowse, 1, 4);

		
		btn = new Button("Save ad");
		input.add(btn, 0, 5);

	//	input.getChildren().addAll(cb1, cb2, cb3, cb4, cb5, tf, ta, btn);
		btn.setOnAction(e -> inputValues());

		return input;
	}

	private static void inputValues() {
		System.out.println("BOB");
		ArrayList<Agency> agencyID = DatabaseCommunication.fetchAgency("SELECT Agencies.ID,Name,AVG(Rating),Logo FROM Agencies,Ratings WHERE Agencies.ID = Ratings.AgencyID and Agencies.Name = '" + (String)cb1.getValue() + "';");
		System.out.println(agencyID);
		String insert = "INSERT INTO Ads (Name,Species,Type,Gender,Age,Description,AgencyID)";
		String values = 
				" VALUES ('" + tf.getText()  + "', '"+ cb2.getValue() + "', '" 
				+ cb3.getValue() + "', '" + cb5.getValue() + "', "
				+ age.getText() + ", '" + ta.getText() + "', " + agencyID.get(0).getID() + ");";
		
		DatabaseCommunication.insertUpdateDelete(insert + values);
		System.out.println(values);
	}

}
