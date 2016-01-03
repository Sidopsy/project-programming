package View;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.text.html.ImageView;

import Object.Ad;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Compare {

	public static StackPane compareAds(ObservableList<Ad> adList) {
		StackPane stack = new StackPane();
		stack.getStyleClass().add("hbox");
		
		GridPane grid = new GridPane();
		grid.getStyleClass().add("grid");
		
		for(int i = 0; i < adList.size() ; i++ ){
		//	ImageView picture = adList.get(i).getPicture();
			Label species = new Label(adList.get(i).getSpecies());
			Label type = new Label(adList.get(i).getType());
			Label age = new Label("" + adList.get(i).getAge());
			
			grid.add(species,i,0);
			grid.add(type, i, 1);
			grid.add(age, i, 2);
		}
		
		stack.getChildren().add(grid);
		return stack;
	}
}
