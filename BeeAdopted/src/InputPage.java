
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
	static TextField tf;
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
		VBox input = theInput();
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

	private static VBox theInput() {
		VBox input = new VBox();
		input.setPadding(new Insets(0, 10, 0, 10));
		input.setSpacing(20);

		cb1 = new ChoiceBox<>(DatabaseConnection.getListFromDatabase("Agency"));
		cb1.setValue(cb1.getItems().get(0));
		// cb1.setOnAction(e -> agency = (String) cb1.getValue());

		cb2 = new ChoiceBox<>(DatabaseConnection.getListFromDatabase("Species"));
		cb2.setValue(cb2.getItems().get(0));
		// cb2.setOnAction(e -> species = (String) cb2.getValue());

		cb3 = new ChoiceBox<>(DatabaseConnection.getListFromDatabase("Type"));
		cb3.setValue(cb3.getItems().get(0));
		// cb3.setOnAction(e -> type = (String) cb3.getValue());

		cb4 = new ChoiceBox<>(DatabaseConnection.getListFromDatabase("Age"));
		cb4.setValue(cb4.getItems().get(0));
		// cb4.setOnAction(e -> age = (String) cb4.getValue());

		cb5 = new ChoiceBox<>(DatabaseConnection.getListFromDatabase("Gender"));
		cb5.setValue(cb5.getItems().get(0));
		// cb5.setOnAction(e -> gender = (String) cb5.getValue());

		tf = new TextField();
		tf.setPromptText("Name");

		ta = new TextArea();
		ta.setPromptText("Description");

		btn = new Button("Save ad");

		input.getChildren().addAll(cb1, cb2, cb3, cb4, cb5, tf, ta, btn);
		btn.setOnAction(e -> inputValues());

		return input;
	}

	private static void inputValues() {
		String insert = "INSERT INTO LISTINGS (ID,Picture,Name,Gender,Species,Type,Age,Description,Start date,End date)";
		String values = 
				"VALUES (" + tf.getText()  + ", "+ cb2.getValue() + ", " 
				+ cb3.getValue() + ", " + cb4.getValue() + ", "
				+ cb5.getValue() + ", " + ta.getText() + ");";
		System.out.println(values);
	}

}
