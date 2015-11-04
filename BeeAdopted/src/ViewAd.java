
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
	
	public static void display(String title, Ad ad){
		window = new Stage();
				
		// Sets Modality and window title.
		window.initModality(Modality.APPLICATION_MODAL);	// Application modal makes sure that input to the app is only
		window.setTitle(title);								// entered to the currently open window. This enum needs to be
		window.setMinWidth(250);							// set before anything is shown in this stage.
		
		// This is the Ad view of this Stage.
		layoutAd = new BorderPane();
		GridPane viewAd = getAd(ad);						// Sends the input Ad into getAd() to be able to show it.
		layoutAd.setTop(header());								// Show the iAdopt image.
		layoutAd.setCenter(viewAd);							// Center of the BorderPane will show the Ad.
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
		
		// Gridpane in which the Ad will be shown.
		GridPane grid = new GridPane();
		grid.setHgap(10);										// Horizontal gaps between columns.
		grid.setVgap(10);										// Vertical gaps between columns.
		grid.setPadding(new Insets(0,10,0,10));					// Setting the padding around the content.
		
		// Labels for displaying information about said Ad.
		Label name = new Label();
		grid.add(name,1,0);
		Label species = new Label();
		grid.add(species, 2, 0);
		
		// Button for adopting the Ad, this sets the scene to Adopted.
		Button adoptButton = new Button("Adopt");
		grid.add(adoptButton, 0,3);
		adoptButton.setOnAction(e -> window.setScene(scAdopt));	// If button Adopt is pressed it takes the user to the
																// Adopted scene specified above.
		// Closes the window and takes you back to the Ad overview.
		Button closeButton = new Button("Close the window");
		grid.add(closeButton, 4, 4);
		closeButton.setOnAction(e -> window.close());			// Closes the window.
		
		return grid;
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