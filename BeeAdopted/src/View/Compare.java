package View;
import Object.Ad;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Class to show Ads in a scrollable GridPane.
 * @author Mattias Landkvist
 *
 */

public class Compare {

	public static ScrollPane compareAds(ObservableList<Ad> adList) {
		ScrollPane stack = new ScrollPane();
		stack.setVbarPolicy(ScrollBarPolicy.NEVER);
		stack.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		stack.getStyleClass().add("hbox");
		
		GridPane grid = new GridPane();
		grid.getStyleClass().add("grid");
		
		for(int i = 0; i < adList.size() ; i++ ){
		//	ImageView picture = adList.get(i).getPicture();
			ImageView picture = adList.get(i).getPicture();
			Label name = new Label(adList.get(i).getName());
			Label species = new Label(adList.get(i).getSpecies());
			Label type = new Label(adList.get(i).getType());
			Label age = new Label("" + adList.get(i).getAge());
			Label gender = new Label(adList.get(i).getGender());
			Label endDate = new Label(adList.get(i).getEndDate());
			
			picture.setFitHeight(100);
			picture.setFitWidth(100);
			
			grid.add(picture,i,0);
			grid.add(name, i, 1);
			grid.add(species, i, 2);
			grid.add(type, i, 3);
			grid.add(age, i, 4);
			grid.add(gender, i, 5);
			grid.add(endDate, i, 6);
		}
		
		stack.setContent(grid);
		return stack;
	}
}
