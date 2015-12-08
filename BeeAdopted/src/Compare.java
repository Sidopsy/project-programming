import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Compare {
	private static Stage window;
	private static Scene scene;
	private static BorderPane layout;
	public static void display(ObservableList<Ad> adList) {
		window = new Stage();

		// Sets Modality and window title.
		window.initModality(Modality.APPLICATION_MODAL);	// Application modal makes sure that input to the app is only
		window.setTitle("Compare Ads");								// entered to the currently open window. This enum needs to be
		window.setMinWidth(250);							// set before anything is shown in this stage.

		// This is the Ad view of this Stage.
		layout = new BorderPane();
		layout.setTop(header());						// Show the iAdopt image.
		layout.setCenter(compareAds(adList));			// Center of the BorderPane will show the Ad.
		scene = new Scene(layout,600,550);
		scene.getStylesheets().add("table.css");


		// Ad scene will be shown first and the App awaits instructions.
		window.setScene(scene);
		window.showAndWait();
	}
	
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
	
	private static GridPane compareAds(ObservableList<Ad> adList) {
		GridPane grid = new GridPane();
		
		for(int i = 0; i < adList.size() ; i++ ){
			grid.add(new Label(adList.get(i).toString()),i,0);
		}
		
		return grid;
	}
}
