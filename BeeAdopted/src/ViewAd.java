
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.DatatypeConverter;

import org.controlsfx.control.Rating;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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
	public static ImageView animalPicture;
	private static DBobject db = new DBobject();

	public static void display(String title, Ad ad) throws IOException, SQLException {
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
		sceneAdopt.getStylesheets().add("table.css");

		// Ad scene will be shown first and the App awaits instructions.
		window.setScene(sceneAd);
		window.showAndWait();
	}

	/**
	 * Places an image in a stackpane and returns the stackpane once called.
	 * 
	 * @return header as stackpane
	 */

	private static BorderPane header(){

		// The StackPane to be returned and used as a header.
		BorderPane header = new BorderPane();
		header.getStyleClass().add("header");

		Label back = new Label("X");
		back.setId("backbutton");
		back.getStyleClass().add("backbutton");
		back.setOnMouseClicked(e -> window.close());

		Label name = new Label("BeeAdopted");
		name.setId("beeadopted");
		name.getStyleClass().add("beeadopted");


		
		header.setLeft(back);
		header.setCenter(name);

		return header;
	}

	/**
	 * This method returns a Hbox with Ad and Agency information about the currently viewed Ad. 
	 * 
	 * @param Ad, Agency
	 * @return Hbox containing Ad and Agency information
	 */

	private static HBox showAd(Ad ad, int agencyID) throws IOException, SQLException {

		// Creating the Hbox to be returned.
		HBox hbox = new HBox();
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

	private static VBox getAd(Ad ad) throws IOException, SQLException {

		// GridPane to be returned.
		VBox vbox = new VBox();
		vbox.setPrefSize(350, 400);
		vbox.setSpacing(10);										// Vertical gaps between columns.
		vbox.setPadding(new Insets(2,2,2,2));				// Setting the padding around the content.
		vbox.setStyle("-fx-border-style: solid;"
				+ "-fx-border-width: 1;"
				+ "-fx-border-color: black");

		animalPicture = new ImageView();
		animalPicture.setPreserveRatio(false);
		animalPicture.setFitWidth(200);
		animalPicture.setFitHeight(200);

		// Labels for displaying information about said Ad.
		Label species = new Label("Species: " + ad.getSpecies());
		Text nameAgeGenderType= new Text(ad.getName() + " is a " + ad.getAge() + "year old " + ad.getGender().toLowerCase() + " " + ad.getType().toLowerCase());
		Text description = new Text("Description: " + ad.getDescription());
		description.autosize();


		java.sql.Connection conn = DriverManager.getConnection("jdbc:sqlite:BeeHive");
		db.closeConnection();

		BufferedImage image = null;  //Buffered image coming from database
		InputStream fis = null; //Inputstream

		try{

			ResultSet databaseResults;  //Returned results from DB

			Statement stmt = conn.createStatement(); //Create the SQL statement

			databaseResults = stmt.executeQuery("SELECT Picture FROM Ads WHERE ID = " + ad.getID() + ";"); //Execute query
			db.closeConnection();

			fis = databaseResults.getBinaryStream("Picture");  //It happens that the 3rd column in my database is where the image is stored (a BLOB)

			image = javax.imageio.ImageIO.read(fis);  //create the BufferedImaged


			if(image != null){
				Image newPic = SwingFXUtils.toFXImage(image, null);
				animalPicture.setImage(newPic);
			} 
		} catch (Exception e){
			//print error if caught
		}

		// Buttons for adopting or closing the ad.
		Button adoptButton = new Button("Adopt");
		Button closeButton = new Button("Close the window");

		// Associating actions with the buttons.
		adoptButton.setOnAction(e -> window.setScene(sceneAdopt));
		closeButton.setOnAction(e -> window.close());


		vbox.getChildren().addAll(animalPicture,species,nameAgeGenderType, description, adoptButton, closeButton);


		return vbox;
	}
	
	public static byte[] toByteArray(String s) {
	    return DatatypeConverter.parseHexBinary(s);
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
		Label label = new Label("Please rate the agency!");				// Friendly message shown when Adopt is pressed.
		grid.add(label, 0, 0);
		
		Rating rating = new Rating();
		grid.add(rating, 0, 1);
		
		TextArea textArea = new TextArea();
		grid.add(textArea, 0, 2);
		
		Button rateButton = new Button("Rate agency");
		grid.add(rateButton, 0, 3);
		
		rateButton.setOnAction(e -> {
			int ratingValue = (int)rating.getRating();
			String comment = textArea.getText();
			db.executeUpdate("UPDATE Ratings ");
		});
		

		// Same function as in other scene. Closes the window.
		Button closeButton = new Button("Close the window");
		grid.add(closeButton, 1, 3);
		closeButton.setOnAction(e -> window.close());				// Closes the window.

		return grid;		
	}
}