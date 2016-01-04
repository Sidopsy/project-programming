package View;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import javax.xml.bind.DatatypeConverter;

import org.controlsfx.control.Rating;

import Control.InputValidation;
import Object.Ad;
import Object.AgencyExt;
import Object.Database;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Class for showing more information on a specific Ad. This window contains more information about said Ad and its corresponding Agency as well as an option
 * to press an button for adopting the animal and rating the agency. The button removes the Ad from being listed in the application and prompt the
 * user to input feedback on the transaction with a 1 to 5 star rating and a comment.
 * 
 * @author Mattias Landkvist & Yu Jet Hua
 */

public class AdView {
	static Stage window;
	static Scene sceneAd, sceneAdopt;
	static BorderPane bpLayoutAd, bpLayoutAdopt;
	public static ImageView animalPicture;
	private static Database db = new Database();

	
	/**
	 * This method returns a VBox with Ad and Agency information about the currently viewed Ad. 
	 * 
	 * @param Ad, int
	 * @return VBox containing Ad and Agency information
	 * 
	 * @author Mattias Landkvist 
	 */

	public static VBox showAd(Ad ad, int agencyID) throws IOException, SQLException {
		VBox vbox = new VBox();
		HBox hbox = new HBox();

		vbox.getStyleClass().add("outer-box");

		hbox.setPadding(new Insets(10));
		hbox.setSpacing(10);

		// Buttons for adopting or closing the ad.
		Button adoptButton = new Button("Adopt");

		// Associating actions with the buttons.
		adoptButton.setOnAction(e -> {
			hbox.getChildren().removeAll(hbox.getChildren());
			hbox.getChildren().add(showAgencyRater(agencyID));
			vbox.getChildren().remove(adoptButton);
			db.executeUpdate("UPDATE Ads SET EndDate = date('NOW', '-1 days') WHERE ID = " + ad.getID() + ";");
		});


		
		// Calling additional methods to retrieve the information.
		hbox.getChildren().addAll(getAd(ad),getAgency(agencyID));
		vbox.getChildren().addAll(hbox, adoptButton);
		
		return vbox;
	}

	/**
	 * This method returns a GridPane containing the information on a specific Ad that was clicked in a previous window.
	 * A button for adopting said item will be available as well as a close button that will close the entire window.
	 * 
	 * @param Ad
	 * @return VBox
	 * 
	 * @author Mattias Landkvist 
	 */

	private static VBox getAd(Ad ad) throws IOException, SQLException {
		VBox vbox = new VBox();
		vbox.getStyleClass().add("inner-box");
		vbox.setPrefSize(350, 400);
		vbox.setSpacing(10);										// Vertical gaps between columns.
		vbox.setPadding(new Insets(10));				// Setting the padding around the content.

		animalPicture = ad.getPicture();
		animalPicture.setPreserveRatio(false);
		animalPicture.setFitWidth(200);
		animalPicture.setFitHeight(200);

		// Labels for displaying information about said Ad.
		Label species = new Label("Species: " + ad.getSpecies());
		Text nameAgeGenderType= new Text(ad.getName() + " is a " + ad.getAge() + "year old " + ad.getGender().toLowerCase() + " " + ad.getType().toLowerCase());
		Text description = new Text("Description: " + ad.getDescription());
		description.autosize();

		vbox.getChildren().addAll(animalPicture,species,nameAgeGenderType, description);


		return vbox;
	}
	
	/**
	 * Returns a byte[] from a string input.
	 * 
	 * @param s
	 * @return byte[]
	 * 
	 * @author Yu Jet Hua
	 */
	public static byte[] toByteArray(String s) {
	    return DatatypeConverter.parseHexBinary(s);
	}

	/**
	 * This method returns a Vbox containing information about the Agency that has the currently viewed Ad. 
	 * 
	 * @param int
	 * @return Vbox with agency information
	 * 
	 * @author Mattias Landkvist & Yu Jet Hua
	 */

	private static VBox getAgency(int agencyID){

		// The Vbox to be returned.
		VBox vbox = new VBox();
		vbox.getStyleClass().add("inner-box");
		vbox.setPrefSize(350, 400);
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(10);
		

		String sqlStatement = "SELECT Agencies.ID,Name,Logo,AVG(Rating),Email,Phone,Street,ZIP,City FROM "
				+ "Agencies, Ratings, Addresses WHERE "
				+ "Agencies.ID = Ratings.AgencyID and "
				+ "Agencies.ID = Addresses.AgencyID and "
				+ "Agencies.ID == " + agencyID + ";";

		// Saving all information about the Agency in extended format.
		AgencyExt agencyExtended = db.fetchAgencyExt(db.executeQuery(sqlStatement)).get(0);
		db.closeConnection();

		ImageView agencyLogo = new ImageView();
		Image logo;

		agencyLogo.setFitWidth(100);
		agencyLogo.setFitHeight(100);
		
		try{
			BufferedImage image = null;
			InputStream inputStream = null;
			inputStream = db.executeQuery("SELECT Logo FROM Agencies WHERE ID = " + agencyID + ";").getBinaryStream("Logo");
			db.closeConnection();
			image = javax.imageio.ImageIO.read(inputStream);
			if(image != null){
				logo = SwingFXUtils.toFXImage(image, null);
				agencyLogo.setImage(logo);
			} 
			inputStream.close();
		} catch (Exception e) {
			System.err.println(">> Error while loading image, or no image selected");
			logo = new Image("PlaceholderSmall.png");
			agencyLogo.setImage(logo);
		} finally {
			try {
				if (!db.getConnection().isClosed()) {db.closeConnection();}
			} catch (SQLException e) {System.err.println(">> Error when closing connection");}
		}
		
		// Transfering the Agency information into labels.
		Label name = new Label("Agency: " + agencyExtended.getName());
		Label rating = new Label("Rating (1-5): " + agencyExtended.getRating());
		Label email = new Label("Email: " + agencyExtended.getEmail());
		Label phone = new Label("Phone: " + agencyExtended.getPhone());
		Label street = new Label("Street: " + agencyExtended.getStreet());
		Label zip = new Label("ZIP: " + agencyExtended.getZip());
		Label city = new Label("City: " + agencyExtended.getCity());

		// Adding all Agency information to the Vbox.
		vbox.getChildren().addAll(agencyLogo, name, rating, email, phone, street, zip, city);

		
		return vbox;
	}

	/**
	 * This method returns a GridPane containing an appropriate message one should recieve when pressing the Adopt button 
	 * for a specific Ad. 
	 * 
	 * @return GridPane
	 * 
	 * @author Mattias Landkvist 
	 */

	private static GridPane showAgencyRater(int agencyID) {

		// GridPane in which the Adopted view will be shown.
		GridPane grid = new GridPane();
		grid.setHgap(10);											// Horizontal gaps between columns.
		grid.setVgap(10);											// Vertical gaps between columns.
		grid.setPadding(new Insets(0,10,0,10));	// Setting the padding around the content.

		
		
		// Label containing information to the shown to the user when pressing Adopt.
		Label label = new Label("Please rate the agency!");			// Friendly message shown when Adopt is pressed.
		grid.add(label, 0, 0);
		
		Rating rating = new Rating();
		rating.getStyleClass().add("borderless-button");
		grid.add(rating, 0, 1);
		
		TextArea textArea = new TextArea();
		grid.add(textArea, 0, 2);
		
		Button rateButton = new Button("Rate agency");
		grid.add(rateButton, 0, 3);
		
		rateButton.setOnAction(e -> {
			int agency = agencyID;
			String comment = "";
			int ratingValue = (int)rating.getRating();
			if(InputValidation.validateInputTextArea(textArea)){
				comment = ", '" + textArea.getText() + "'";
			} else {
				comment = ", 'No comment.'";
			}
			String insert = "INSERT INTO Ratings (AgencyID, Rating, Comment) ";
			String values = "VALUES (" + agency + ", " + ratingValue + comment + ");";		// Exchange 1 with the agencies ID.
			System.out.println(">> " + insert + "\n" + ">> " + values);
			db.executeUpdate(insert + values);
			Main.back();
		});
		
		return grid;		
	}
}