import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Compare {

	public static GridPane compareAds(ObservableList<Ad> adList) {
		GridPane grid = new GridPane();
		grid.getStyleClass().add("grid");
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(5, 5, 5, 5));
		
		for(int i = 0; i < adList.size() ; i++ ){
			Label species = new Label(adList.get(i).getSpecies());
			Label type = new Label(adList.get(i).getType());
			Label age = new Label("" + adList.get(i).getAge());
			
			grid.add(species,i,0);
			grid.add(type, i, 1);
			grid.add(age, i, 2);
		}
		
		return grid;
	}
}
