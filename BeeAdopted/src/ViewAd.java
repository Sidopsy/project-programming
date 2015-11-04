
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
	static Scene scAd, scAdopt;
	static BorderPane layoutAd, layoutAdopt;
	
	public static void display(String title, Ad ad, Agency agency){
		window = new Stage();
				
		// Sets Modality and window title.
		window.initModality(Modality.APPLICATION_MODAL);	// Application modal makes sure that input to the app is only
		window.setTitle(title);								// entered to the currently open window. This enum needs to be
		window.setMinWidth(250);							// set before anything is shown in this stage.
		
		// This is the Ad view of this Stage.
		layoutAd = new BorderPane();
		layoutAd.setTop(header());								// Show the iAdopt image.
		layoutAd.setCenter(showAd(ad, agency));							// Center of the BorderPane will show the Ad.
		scAd = new Scene(layoutAd,600,550);
				
		// This is the view of the Ad once adopt is pressed.
		layoutAdopt = new BorderPane();
		GridPane viewAdopt = adopted();
		layoutAdopt.setTop(header());							// Show the iAdopt image.
		layoutAdopt.setCenter(viewAdopt);					// Center of the BorderPane will show the Adopted layout.
		scAdopt = new Scene(layoutAdopt,600,550);
		
		// Ad scene will be shown first and the App awaits instructions.
		window.setScene(scAd);
		window.showAndWait();
	}

	/**
	 * Place an image in a stackpane and returns the stackpane
	 * @return head as stackpane
	 */
	private static StackPane header(){
		StackPane head = new StackPane();
		Image image = new Image("BeeAdoptedSmall.png");
		ImageView header = new ImageView();
		header.setImage(image);
		head.getChildren().add(header);
		return head;
	}
	
	/**
	 * This method returns a GridPane containing the information on a specific Ad that was clicked in a previous window.
	 * A button for adopting said item will be available as well as a close button that will close the entire window.
	 * 
	 * @param Ad
	 * @return GridPane
	 */
	
	private static GridPane getAd(Ad ad) {
		GridPane grid = new GridPane();							// Gridpane in which the Ad will be shown.
		grid.setHgap(10);										// Horizontal gaps between columns.
		grid.setVgap(10);										// Vertical gaps between columns.
		grid.setPadding(new Insets(10,10,10,10));				// Setting the padding around the content.
		
		// Labels for displaying information about said Ad.
		Label name = new Label("Name: " + ad.getName());
		grid.add(name,0,0);
		Label species = new Label("Species: " + ad.getSpecies());
		grid.add(species,1,0);
		Label type = new Label("Type: " + ad.getType());
		grid.add(type,0,1);
		Label gender = new Label("Gender: " + ad.getGender());
		grid.add(gender,1,1);
		Label age = new Label("Age: " + ad.getAge());
		grid.add(age,0,2);
		Label description = new Label("Description: /n" + ad.getDescription());
		grid.add(description,1,2);
		
		
		// Button for adopting the Ad, this sets the scene to Adopted.
		Button adoptButton = new Button("Adopt");
		grid.add(adoptButton, 0,3);
		adoptButton.setOnAction(e -> window.setScene(scAdopt));	// If button Adopt is pressed it takes the user to the
																// Adopted scene specified above.
		// Closes the window and takes you back to the Ad overview.
		Button closeButton = new Button("Close the window");
		grid.add(closeButton, 0, 4);
		closeButton.setOnAction(e -> window.close());			// Closes the window.
		
		return grid;
	}
	
	private static VBox getAgency(Agency agency){
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(10);
		
		String sqlStatement = "SELECT Agencies.ID,Name,Logo,AVG(Rating),Email,Phone,Street,ZIP,City FROM Agencies, Ratings, Addresses WHERE "
				+ "Agencies.ID = " + agency.getID() + ";";
		AgencyExtended agencyExtended = DatabaseCommunication.fetchAgencyExtended(sqlStatement).get(0);
		Label name = new Label("Name: " + agencyExtended.getName());
		Label rating = new Label("Rating: " + agencyExtended.getRating());
		Label email = new Label("Email: " + agencyExtended.getEmail());
		Label phone = new Label("Phone: " + agencyExtended.getPhone());
		Label street = new Label("Street: " + agencyExtended.getStreet());
		Label zip = new Label("ZIP: " + agencyExtended.getZip());
		Label city = new Label("City: " + agencyExtended.getCity());
		
		vbox.getChildren().addAll(name,rating,email,phone,street,zip,city);
		
		return vbox;
	}
	
	private static HBox showAd(Ad ad, Agency agency){
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(10));
		hbox.setSpacing(10);
		
		hbox.getChildren().addAll(getAd(ad),getAgency(agency));
		
		return hbox;
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
		Label label = new Label("CONGRATULATIONS MOTHERTRUCKER!");	// Friendly message shown when Adopt is pressed.
		grid.add(label, 0, 0);
		
		// Same function as in other scene. Closes the window.
		Button closeButton = new Button("Close the window");
		grid.add(closeButton, 4, 4);
		closeButton.setOnAction(e -> window.close());				// Closes the window.
		
		return grid;		
	}
}