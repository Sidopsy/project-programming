
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class for showing an Ad in a separate window. This window contains more information about said Ad as well as an option
 * to press an Adopt button. The Adopt button should remove the Ad from being listed in the application and prompt the
 * user to input feedback on the transaction with a 0 to 5 rating and a comment.
 * 
 * @author ML
 */

public class ViewAd {
	static Stage window;
	static Scene sceneAd, sceneAdopt;
	static BorderPane bpLayoutAd, bpLayoutAdopt;
	private static DBobject db = new DBobject();

	public static void display(String title, Ad ad){
		window = new Stage();

		// Sets Modality and window title.
		window.initModality(Modality.APPLICATION_MODAL);	// Application modal makes sure that input to the app is only
		window.setTitle(title);								// entered to the currently open window. This enum needs to be
		window.setMinWidth(250);							// set before anything is shown in this stage.

		// This is the Ad view of this Stage.
		bpLayoutAd = new BorderPane();
		bpLayoutAd.setTop(header());						// Show the iAdopt image.
		bpLayoutAd.setCenter(showAd(ad, ad.getAgencyID()));			// Center of the BorderPane will show the Ad.
		sceneAd = new Scene(bpLayoutAd,600,550);
		sceneAd.getStylesheets().add("table.css");

		// This is the view of the Ad once adopt is pressed.
		bpLayoutAdopt = new BorderPane();
		GridPane viewAdopt = adopted();
		bpLayoutAdopt.setTop(header());						// Show the iAdopt image.
		bpLayoutAdopt.setCenter(viewAdopt);					// Center of the BorderPane will show the Adopted layout.
		sceneAdopt = new Scene(bpLayoutAdopt,600,550);

		// Ad scene will be shown first and the App awaits instructions.
		window.setScene(sceneAd);
		window.showAndWait();
	}

	/**
	 * Places an image in a stackpane and returns the stackpane once called.
	 * 
	 * @return header as stackpane
	 */

	private static StackPane header(){

		// StackPane to be returned.
		StackPane head = new StackPane();

		// Adding the image to the StackPane.
		Image image = new Image("BeeAdoptedSmall.png");
		ImageView header = new ImageView();
		header.setImage(image);
		head.getChildren().add(header);

		return head;
	}

	/**
	 * This method returns a Hbox with Ad and Agency information about the currently viewed Ad. 
	 * 
	 * @param Ad, Agency
	 * @return Hbox containing Ad and Agency information
	 */

	private static HBox showAd(Ad ad, int agencyID){

		// Creating the Hbox to be returned.
		HBox hbox = new HBox();
		hbox.getStyleClass().add("hbox");
		hbox.setPadding(new Insets(10));
		hbox.setSpacing(10);

		// Calling additional methods to retrieve the information.
		hbox.getChildren().addAll(getAd(ad),getAgency(agencyID));

		return hbox;
	}

	/**
	 * This method returns a GridPane containing the information on a specific Ad that was clicked in a previous window.
	 * A button for adopting said item will be available as well as a close button that will close the entire window.
	 * 
	 * @param Ad
	 * @return GridPane
	 */
	
	private static VBox getAd(Ad ad) {

		// GridPane to be returned.
		VBox vbox = new VBox();
		vbox.getStyleClass().add("hbox");
		vbox.setPrefSize(350, 400);
		vbox.setSpacing(10);										// Vertical gaps between columns.
		vbox.setPadding(new Insets(2,2,2,2));				// Setting the padding around the content.
		vbox.setStyle("-fx-border-style: solid;"
				+ "-fx-border-width: 1;"
				+ "-fx-border-color: black");
		
		
		Image picture = new Image("PlaceholderBig.png");
		ImageView animalPicture = new ImageView(picture);
		
		Label species = new Label("Species: " + ad.getSpecies());
		Text nameAgeGenderType= new Text(ad.getName() + " is a " + ad.getAge() + "year old " + ad.getGender().toLowerCase() + " " + ad.getType().toLowerCase());
		Text description = new Text("Description: " + ad.getDescription());
		description.autosize();

		
		// Buttons for adopting or closing the ad.
		Button adoptButton = new Button("Adopt");
		Button closeButton = new Button("Close the window");

		// Adding all items to the VBox
		vbox.getChildren().addAll(animalPicture,species,nameAgeGenderType, description, adoptButton, closeButton);
		
		// Associating actions with the buttons.
		adoptButton.setOnAction(e -> window.setScene(sceneAdopt));
		closeButton.setOnAction(e -> window.close());



		return vbox;
	}

	/**
	 * This method returns a Vbox containing information about the Agency that has the currently viewed Ad. 
	 * 
	 * @param agency
	 * @return Vbox with agency information
	 */

	private static VBox getAgency(int agencyID){

		// The Vbox to be returned.
		VBox vbox = new VBox();
		vbox.getStyleClass().add("hbox");
		vbox.setPrefSize(250, 400);
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(10);
		vbox.setStyle("-fx-border-style: solid;"
				+ "-fx-border-width: 1;"
				+ "-fx-border-color: black");

		String sqlStatement = "SELECT Agencies.ID,Name,Logo,AVG(Rating),Email,Phone,Street,ZIP,City FROM "
				+ "Agencies, Ratings, Addresses WHERE "
				+ "Agencies.ID = Ratings.AgencyID and "
				+ "Agencies.ID = Addresses.AgencyID and "
				+ "Agencies.ID == " + agencyID + ";";

		// Saving all information about the Agency in extended format.
		
			AgencyExt agencyExtended = db.fetchAgencyExt(db.executeQuery(sqlStatement)).get(0);
			System.out.println(agencyExtended);

			Image picture = new Image("PlaceholderSmall.png");
			ImageView agencyPicture = new ImageView(picture);
			
			// Transfering the Agency information into labels.
			Label name = new Label("Name: " + agencyExtended.getName());
			Label rating = new Label("Rating: " + agencyExtended.getRating());
			Label email = new Label("Email: " + agencyExtended.getEmail());
			Label phone = new Label("Phone: " + agencyExtended.getPhone());
			Label street = new Label("Street: " + agencyExtended.getStreet());
			Label zip = new Label("ZIP: " + agencyExtended.getZip());
			Label city = new Label("City: " + agencyExtended.getCity());

			// Adding all Agency information to the Vbox.
			vbox.getChildren().addAll(agencyPicture, name, rating, email, phone, street, zip, city);

		

		System.out.println("Shits not working");
		return vbox;
	}

	/**
	 * This method returns a GridPane containing an appropriate message one should recieve when pressing the Adopt button 
	 * for a specific Ad. 
	 * 
	 * @return GridPane
	 */

	private static GridPane adopted() {

		// GridPane in which the Adopted view will be shown.
		GridPane grid = new GridPane();
		grid.setHgap(10);											// Horizontal gaps between columns.
		grid.setVgap(10);											// Vertical gaps between columns.
		grid.setPadding(new Insets(0,10,0,10));						// Setting the padding around the content.

		// Label containing information to the shown to the user when pressing Adopt.
		Label label = new Label("CONGRATULATIONS!");				// Friendly message shown when Adopt is pressed.
		grid.add(label, 0, 0);

		// Same function as in other scene. Closes the window.
		Button closeButton = new Button("Close the window");
		grid.add(closeButton, 4, 4);
		closeButton.setOnAction(e -> window.close());				// Closes the window.

		return grid;		
	}
}