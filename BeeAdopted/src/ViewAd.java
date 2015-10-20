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

public class ViewAd {
	static Stage window;
	static Scene sc1, sc2;
	static BorderPane layout1, layout2;
	
	public static void display(String title, int adID){
		window = new Stage();
		StackPane head = new StackPane();
		Image image = new Image("iAdopt.png");
		ImageView header = new ImageView();
		header.setImage(image);
		head.getChildren().add(header);
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		
		//Ad view
		layout1 = new BorderPane();
		GridPane adView = getAd(adID);
		layout1.setTop(head);
		layout1.setCenter(adView);
		sc1 = new Scene(layout1,600,400);
				
		//Adopt view
		layout2 = new BorderPane();
		GridPane adoptView = adopted();
		layout2.setTop(head);
		layout2.setCenter(adoptView);
		sc2 = new Scene(layout2,600,400);
		
		window.setScene(sc1);
		window.showAndWait();
	}

	
	private static GridPane getAd(int adID) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0,10,0,10));
		
		String[] adInfo = DatabaseConnection.getAd(adID);
		
		Button adoptButton = new Button("Adopt");
		grid.add(adoptButton, 1,0);
		adoptButton.setOnAction(e -> window.setScene(sc2));
		
		
		Button closeButton = new Button("Close the window");
		grid.add(closeButton, 4, 4);
		closeButton.setOnAction(e -> window.close());
		
		return grid;
	}
	
	private static GridPane adopted() {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0,10,0,10));
		
		Label label = new Label("CONGRATULATIONS!!!");
		grid.add(label, 0, 0);
		
		Button closeButton = new Button("Close the window");
		grid.add(closeButton, 4, 4);
		closeButton.setOnAction(e -> window.close());
		
		return grid;
	}

	
}
