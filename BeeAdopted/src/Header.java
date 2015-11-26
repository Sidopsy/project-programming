import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Class for creating various headers for different windows/scenes in the application
 * 
 * @author MTs
 * @since 24-10-2015
 */

public class Header {
	
	private static StackPane header;
	private static Image image;
	private static Image imageBack;
	private static ImageView imageView;
	private static ImageView imageViewBack;
	private static Button btnBack;
	private static int toggleValue = 1;
	
	/**
	 * Creates the header for small windows in the application, this header makes no use of the back button.
	 * 
	 * @return completed header with a small image in the form of a StackPane.
	 */
	
	public static StackPane smallHeader() {
		header = new StackPane();
		
		image = new Image("BeeAdoptedSmall.png");
		imageView = new ImageView(image);
		header.getChildren().add(imageView);
		
		return header;
	}
	
	/**
	 * Creates the header for larger windows in the application, this header has the back button inactivated by default. Activate
	 * it by calling toggleBackButton().
	 * 
	 * @return completed header with a large image in the form of a StackPane.
	 */
	
	public static StackPane largeHeader() {
		header = new StackPane();
		
		image = new Image("BeeAdoptedLarge.png");
		imageView = new ImageView(image);
		
		imageBack = new Image("BackButton.png");
		imageViewBack = new ImageView(imageBack);
		
		btnBack = new Button();
		btnBack.setGraphic(imageViewBack);
		imageViewBack.setVisible(false);
		
		header.getChildren().addAll(imageView, imageViewBack);
		header.setAlignment(Pos.CENTER_LEFT);
		
		return header;
	}
	
	/**
	 * Toggles the back button on and off.
	 * 
	 */
	
	public static void toggleBackButton() {
		if (toggleValue == 1) {
			imageViewBack.setVisible(true);
			toggleValue = 0;
		} else {
			imageViewBack.setVisible(false);
			toggleValue = 1;
		}
		
	}
	
	/**
	 * Used to determine if the back button is shown or not, useful for hiding or showing it in places where is should be active/
	 * inactive. 
	 * 
	 * @return an integer representation of if the back button is currently activated.
	 */
	
	public static int getToggleValue() {
		return toggleValue;
	}
}
