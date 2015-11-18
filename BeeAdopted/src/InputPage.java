
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;


import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InputPage {
	static Stage window;
	static Scene scene1, scene2, scene3 ;
	static BorderPane layout1, layout2;
	static ChoiceBox<Object> cbAgencies, cbSpecies, cbType, cbGender;
	static TextField tfName, tfAge, tfEmail,tfPhone,tfStreet,tfZip,tfCity;
	static PasswordField pfPassword;
	static TextArea taDescription;
	static Button btnInput, btnMoreSpecies, btnMoreTypes;
	static ObservableList<Object> obsListType, obsListGender;
	static Optional<String> result;
	static String name,species,newSpecies,type,newType,gender,age,description,agencyID;
	static File picture;

	public static void display(){
		window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Input page");
		window.setMinWidth(250);

		// Inputs
		layout1 = new BorderPane();
		layout1.setTop(header());
		layout1.setCenter(chooseInput());
		scene1 = new Scene(layout1, 600, 550);

		window.setScene(scene1);
		window.showAndWait();
	}
	

	private static HBox chooseInput() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.setSpacing(10);
		
		Button agencyInput = new Button("New agency?");
		Button animalInput = new Button("New animal?");
		
		agencyInput.setOnAction(e -> {
			layout2 = new BorderPane();
			layout2.setTop(header());
			layout2.setCenter(inputAgencyView());
			scene2 = new Scene(layout2, 600, 550);
			window.setScene(scene2);
		});
		
		animalInput.setOnAction(e -> {
			layout2 = new BorderPane();
			layout2.setTop(header());
			layout2.setCenter(inputAnimalView());
			scene2 = new Scene(layout2, 600, 550);
			window.setScene(scene2);
		});
		
		hbox.getChildren().addAll(agencyInput, animalInput);
		hbox.setAlignment(Pos.CENTER);
		
		return hbox;
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
		GridPane input = new GridPane();
		input.setHgap(40);
		input.setVgap(20);
		input.setPadding(new Insets(10, 10, 10, 10));
		
		Label victory = new Label("Victory");
		input.add(victory, 0, 0);
		
		return input;
	}

	private static GridPane inputAnimalView() {
		GridPane input = new GridPane();
		input.setHgap(40);
		input.setVgap(20);
		input.setPadding(new Insets(10, 10, 10, 10));
		
		tfName = new TextField();
		tfName.setPromptText("Name");
		tfName.setMaxWidth(150);
		input.add(tfName,0,0);
		
		tfAge = new TextField();
		tfAge.setPromptText("Age");
		tfAge.setMaxWidth(150);
		input.add(tfAge, 1, 0);
		
		taDescription = new TextArea();
		taDescription.setPromptText("Description");
		taDescription.setMaxWidth(150);
		input.add(taDescription, 0, 1);
	
		cbAgencies = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Agencies", "Name", null, null));
		cbAgencies.setValue(cbAgencies.getItems().get(0));
		input.add(cbAgencies, 0, 2);
		// cb1.setOnAction(e -> agency = (String) cb1.getValue());

		
		HBox speciesHBox = new HBox();
		//hbox.setPadding(new Insets(10, 10, 10, 10));
		speciesHBox.setSpacing(4);
		cbSpecies = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Species", null, null));
		cbSpecies.setValue(cbSpecies.getItems().get(0));
		cbSpecies.setOnAction(e -> {
			obsListType = DatabaseCommunication.fetchAttribute("Ads", "Type", "Species", (String)cbSpecies.getValue());
			cbType.setItems(obsListType);
			cbType.setValue("Type");
		});
		// cb2.setOnAction(e -> species = (String) cb2.getValue());
		
		btnMoreSpecies = new Button("+");
		btnMoreSpecies.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("New species");
			dialog.setHeaderText("Custom species");
			dialog.setContentText("Please enter the name of the species:");
			
			Optional<String> result = dialog.showAndWait();
			
			if (result.isPresent()) {
				newSpecies = result.get();
			}

		});
		speciesHBox.getChildren().addAll(cbSpecies, btnMoreSpecies);
		input.add(speciesHBox, 1, 2);
		
		
		HBox typeHBox = new HBox();
		//hbox.setPadding(new Insets(10, 10, 10, 10));
		typeHBox.setSpacing(4);
		
		cbType = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Type", null, null));
		cbType.setValue(cbType.getItems().get(0));
		//cb3.setOnAction(e -> {
		//	gender = DatabaseCommunication.fetchAttribute("Ads", "Gender", "Type", (String)cb3.getValue());
		//	cb5.setItems(gender);
		//	cb5.setValue("Gender");
		//});
		
		btnMoreTypes = new Button("+");
		btnMoreTypes.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("New type");
			dialog.setHeaderText("Custom type");
			dialog.setContentText("Please enter the name of the type:");
			
			Optional<String> result = dialog.showAndWait();
			
			if (result.isPresent()) {
				newType = result.get();
			}

		});
		typeHBox.getChildren().addAll(cbType,btnMoreTypes);
		input.add(typeHBox, 0, 3);
		
		cbGender = new ChoiceBox<>(DatabaseCommunication.fetchAttribute("Ads", "Gender", null, null));
		cbGender.setValue(cbGender.getItems().get(0));
		input.add(cbGender, 1, 3);


		Label uploadText = new Label("Upload picture");
		input.add(uploadText, 0,4);
		
		Button bowse = new Button("Browse");
		bowse.setOnAction(e -> picture = FileChooserExample.display());
		input.add(bowse, 1, 4);

		
		btnInput = new Button("Save ad");
		input.add(btnInput, 0, 5);

	//	input.getChildren().addAll(cb1, cb2, cb3, cb4, cb5, tf, ta, btn);
		btnInput.setOnAction(e -> {
			inputAnimalValues();
			layout2 = new BorderPane();
			layout2.setTop(header());
			layout2.setCenter(acceptedInput());
			scene2 = new Scene(layout2, 600, 550);
			window.setScene(scene2);
		});

		input.setAlignment(Pos.CENTER);
		
		return input;
	}
	
	private static GridPane inputAgencyView() {
		GridPane input = new GridPane();
		input.setHgap(40);
		input.setVgap(20);
		input.setPadding(new Insets(10, 10, 10, 10));
		
		tfName = new TextField();
		tfName.setPromptText("Name");
		tfName.setMaxWidth(150);
		input.add(tfName,0,0);
		
		tfEmail = new TextField();
		tfEmail.setPromptText("Email");
		tfEmail.setMaxWidth(150);
		input.add(tfEmail, 1, 0);
		
		tfPhone = new TextField();
		tfPhone.setPromptText("Phone number");
		tfPhone.setMaxWidth(150);
		input.add(tfPhone, 0, 1);
		
		pfPassword = new PasswordField();
		pfPassword.setPromptText("Password");
		pfPassword.setMaxWidth(150);
		input.add(pfPassword,1,1);
		
		tfStreet = new TextField();
		tfStreet.setPromptText("Street");
		tfStreet.setMaxWidth(150);
		input.add(tfStreet, 0, 2);
		
		tfZip = new TextField();
		tfZip.setPromptText("ZIP");
		tfZip.setMaxWidth(150);
		input.add(tfZip, 0, 3);
		
		tfCity = new TextField();
		tfCity.setPromptText("City");
		tfCity.setMaxWidth(150);
		input.add(tfCity, 1, 3);


		Label uploadText = new Label("Upload logotype");
		input.add(uploadText, 0,4);
		
		Button bowse = new Button("Browse");
		bowse.setOnAction(e -> FileChooserExample.display());
		input.add(bowse, 1, 4);

		
		btnInput = new Button("Save agency");
		input.add(btnInput, 0, 5);

	//	input.getChildren().addAll(cb1, cb2, cb3, cb4, cb5, tf, ta, btn);
		btnInput.setOnAction(e -> inputAgencyValues());

		input.setAlignment(Pos.CENTER);
		
		return input;
	}
	
	private static void inputAgencyValues() {
		// TODO Auto-generated method stub

	}

	private static void inputAnimalValues() {
		System.out.println("BOB");
		ArrayList<Agency> agencyID = DatabaseCommunication.fetchAgency("SELECT Agencies.ID,Name,AVG(Rating),Logo FROM Agencies,Ratings WHERE Agencies.ID = Ratings.AgencyID and Agencies.Name = '" + (String)cbAgencies.getValue() + "';");
		System.out.println(agencyID);
		name = tfName.getText();
		if(newSpecies == null){
			species = (String)cbSpecies.getValue();
		} else {
			species = newSpecies;
		}
		if(newType == null){
			type = (String)cbType.getValue();
		} else {
			type = newType;
		}
		gender = (String)cbGender.getValue();
		age = tfAge.getText();
		description = taDescription.getText();
		
		
		String insert = "INSERT INTO Ads (Name,Species,Type,Gender,Age,Description,AgencyID)";
		String values = 
				" VALUES ('" + name  + "', '"+ species + "', '" 
				+ type + "', '" + gender + "', "
				+ age + ", '" + description + "', " + agencyID.get(0).getID() + ");";
		
		DatabaseCommunication.insertUpdateDelete(insert + values);
		
		System.out.println(values);
	}

}
