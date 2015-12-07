import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This class creates a new window in which the user can input information to add a new agency.
 * 
 * @since 2015-12-03
 * @author Madisen Whitfield
 * @refactor Måns Thörnvik: Re-styled the save function to be done with one button click instead of two.
 */

public class CreateAccount {
	private static Stage window;

	private static TextField tfName, tfCity, tfStreet, tfZip, tfPhone, tfEmail;
	private static PasswordField pfPassword;

	private static DBobject db = new DBobject();

	/**
	 * Displays a window in which the user can enter information for a *NEW* agency.
	 */

	public static void display(){
		Scene sceneCA;
		
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setVgap(8);
		gridPane.setHgap(10);
		
		window = new Stage();
		window.setTitle("BeeAdopted - Create An Account");

		addAgencyLabels(gridPane);
		addAgencyTextFields(gridPane);
		addAgencyButtons(gridPane);
		
		sceneCA = new Scene(gridPane, 650, 300);
		
		window.setScene(sceneCA);
		window.show();
	}

	/**
	 * Updates the incoming GridPane with labels for member fillout form information.
	 * 
	 * @param initialized GridPane to add labels to.
	 * @return GridPane containing the labels neccessary for showing information about agency input fields.
	 */
	
	private static GridPane addAgencyLabels(GridPane inputGridPane) {
		GridPane gridPane = inputGridPane;
		Label lblName, lblAddress, lblCity, lblStreet, lblZip, 
				lblPhone, lblEmail, lblPassword, lblUploadPicture;
		
		
		lblName = new Label("Name:");
		lblAddress = new Label("Address:");
		lblCity = new Label("City:");
		lblStreet = new Label("Street:");
		lblZip = new Label("Zip-Code");
		lblPhone = new Label("Phone:");
		lblEmail = new Label("Email:");
		lblPassword = new Label("Password:");
		lblUploadPicture = new Label("Upload Picture");
		
		gridPane.getChildren().addAll(lblName, lblAddress, lblCity, lblStreet, lblZip, 
									lblPhone, lblEmail, lblPassword, lblUploadPicture);
		GridPane.setConstraints(lblName, 0, 0);
		GridPane.setConstraints(lblAddress, 2, 0);
		GridPane.setConstraints(lblCity, 3, 0);
		GridPane.setConstraints(lblStreet, 3, 2);
		GridPane.setConstraints(lblZip, 3, 4);
		GridPane.setConstraints(lblPhone, 1, 0);
		GridPane.setConstraints(lblEmail, 1, 2);
		GridPane.setConstraints(lblPassword, 1, 4);
		GridPane.setConstraints(lblUploadPicture, 1, 6);
		
		
		return gridPane;
	}

	/**
	 * Updates the incoming GridPane with TextFields for member input.
	 * 
	 * @param initialized GridPane to add TextFields to.
	 * @return GridPane containing the TextFields neccessary for inputing agency information.
	 */
	
	private static GridPane addAgencyTextFields(GridPane inputGridPane) {
		GridPane gridPane = inputGridPane;
		
		
		tfName = new TextField();
		tfName.setPromptText("Personal or Business Name");
		tfName.setOnKeyReleased(e -> {
			InputValidation.validateInputName(tfName);
		});
		
		tfPhone = new TextField();
		tfPhone.setPromptText("Phone Number");
		tfPhone.setOnKeyReleased(e -> {
			InputValidation.validateInputPhone(tfPhone);
		});
		
		tfEmail = new TextField();
		tfEmail.setPromptText("Email: Also Your Username");
		tfEmail.setOnKeyReleased(e -> {
			InputValidation.validateInputEmail(tfEmail);
		});
		
		tfStreet = new TextField();
		tfStreet.setPromptText("Street Name");
		tfStreet.setOnKeyReleased(e -> {
			InputValidation.validateInputStreet(tfStreet);
		});
		
		tfZip = new TextField();
		tfZip.setPromptText("Zip");
		tfZip.setOnKeyReleased(e -> {
			InputValidation.validateInputZip(tfZip);
		});
		
		tfCity = new TextField();
		tfCity.setPromptText("City");
		tfCity.setOnKeyReleased(e -> {
			InputValidation.validateInputCity(tfCity);
		});
		
		pfPassword = new PasswordField();
		pfPassword.setPromptText("Choose a Password");
		pfPassword.setOnKeyReleased(e -> {
			InputValidation.validateInputPassword(pfPassword);
		});

		gridPane.getChildren().addAll(tfName, tfPhone, tfEmail, pfPassword, tfStreet, tfZip, tfCity); 
		GridPane.setConstraints(tfName, 0, 1);
		GridPane.setConstraints(tfPhone, 1, 1);
		GridPane.setConstraints(tfEmail, 1, 3);
		GridPane.setConstraints(pfPassword, 1, 5);
		GridPane.setConstraints(tfStreet, 3, 3);
		GridPane.setConstraints(tfZip, 3, 5);
		GridPane.setConstraints(tfCity, 3, 1);

		
		return gridPane;
	}

	/**
	 * Updates the incoming GridPane with buttons for member input.
	 * 
	 * @param initialized GridPane to add buttons to.
	 * @return GridPane containing the labels neccessary for saving and inputing member information.
	 */
	
	private static GridPane addAgencyButtons(GridPane inputGridPane) {
		GridPane gridPane = inputGridPane;
		Button btnSave, btnUploadLogo;
		Alert alert;
		
		
		alert = new Alert(AlertType.INFORMATION);
		
		btnSave = new Button("Save");
		btnSave.setOnAction(e -> {
			if (InputValidation.validateMemberInfo(tfName, tfPhone, tfEmail, tfStreet, tfZip, tfCity) &&
				InputValidation.validateInputPassword(pfPassword)) {
				
				InputPage.inputAgencyValues(tfName, tfPhone, tfEmail, pfPassword, tfStreet, tfZip, tfCity);	
				
				alert.setAlertType(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText("Account has been created");
				alert.setContentText("You are now able to log in and add advertisements to the application.");
				alert.showAndWait();
				window.close();
			} else {
				alert.setAlertType(AlertType.ERROR);
				alert.setTitle("Failiure");
				alert.setHeaderText("Account could not be created");
				alert.setContentText("One or more fields have not been filled out correctly.");
				alert.showAndWait();
			}
		});

		btnUploadLogo = new Button("Browse");
		btnUploadLogo.setOnAction(InputPage.btnLoadEventListener);
		
		gridPane.getChildren().addAll(btnSave, btnUploadLogo);
		GridPane.setConstraints(btnSave, 1, 7);
		GridPane.setConstraints(btnUploadLogo, 3, 6);
		
		
		return gridPane;
	}
}