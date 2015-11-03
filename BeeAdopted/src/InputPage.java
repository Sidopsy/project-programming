
import java.util.ArrayList;

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

	public static void display() {
		window = new Stage();
		StackPane head = new StackPane();
		Image image = new Image("iAdopt.png");
		ImageView header = new ImageView();
		header.setImage(image);
		head.getChildren().add(header);

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Input page");
		window.setMinWidth(250);

		// Inputs
		layout1 = new BorderPane();
		GridPane input = theInput();
		layout1.setTop(head);
		layout1.setCenter(input);
		sc1 = new Scene(layout1, 600, 400);

		// Adopt view
		layout2 = new BorderPane();
		GridPane acceptedInput = acceptedInput();
		layout2.setTop(head);
		layout2.setCenter(acceptedInput);
		sc2 = new Scene(layout2, 600, 400);

		window.setScene(sc1);
		window.showAndWait();
	}

	private static GridPane acceptedInput() {
		// TODO Auto-generated method stub
		return null;
	}

	private static GridPane theInput() {
		GridPane input = new GridPane();
		input.setHgap(40);
		input.setVgap(20);
		input.setPadding(new Insets(0, 10, 0, 10));
		
		tf = new TextField();
		tf.setPromptText("Name");
		tf.setMaxWidth(150);
		input.add(tf,0,0);
		
		age = new TextField();
		age.setPromptText("Name");
		age.setMaxWidth(150);
		input.add(age, 1, 0);
		
		ta = new TextArea();
		ta.setPromptText("Description");
		ta.setMaxWidth(150);
		input.add(ta, 0, 1);
		

		
		

		cb1 = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Agencies", "Name"));
		cb1.setValue(cb1.getItems().get(0));
		input.add(cb1, 0, 2);
		// cb1.setOnAction(e -> agency = (String) cb1.getValue());

		cb2 = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Species"));
		cb2.setValue(cb2.getItems().get(0));
		input.add(cb2, 1, 2);
		// cb2.setOnAction(e -> species = (String) cb2.getValue());

		cb3 = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Type"));
		cb3.setValue(cb3.getItems().get(0));
		input.add(cb3, 0, 3);
		// cb3.setOnAction(e -> type = (String) cb3.getValue());

		
		// cb4.setOnAction(e -> age = (String) cb4.getValue());

		cb5 = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Gender"));
		cb5.setValue(cb5.getItems().get(0));
		input.add(cb5, 1, 3);
		// cb5.setOnAction(e -> gender = (String) cb5.getValue());

		Label uploadText = new Label("Upload picture");
		input.add(uploadText, 0,4);
		
		Button bowse = new Button("Browse");
		input.add(bowse, 1, 4);

		
		btn = new Button("Save ad");
		input.add(btn, 0, 5);

	//	input.getChildren().addAll(cb1, cb2, cb3, cb4, cb5, tf, ta, btn);
		btn.setOnAction(e -> inputValues());

		return input;
	}

	private static void inputValues() {
		ArrayList<Agency> agencyID = DatabaseCommunication.fetchAgency("SELECT ID FROM Agencies WHERE Name == " + cb1.getValue() + ";");
		String insert = "INSERT INTO Ads (Name,Gender,Species,Type,Age,Description,AgencyID)";
		String values = 
				" VALUES ('" + tf.getText()  + "', '"+ cb2.getValue() + "', '" 
				+ cb3.getValue() + "', '" + cb4.getValue() + "', "
				+ cb5.getValue() + ", '" + ta.getText() + "', " + agencyID.get(0) + ");";
		
		DatabaseCommunication.insertUpdateDelete(insert + values);
		System.out.println(values);
	}

}
