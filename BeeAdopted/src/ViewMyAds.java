import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewMyAds extends MemberPage{
	
	
	protected static void showMyAds(){
		
		
		Stage view = new Stage();
		BorderPane bp = new BorderPane();
		Scene scene = new Scene( bp, 300, 200);
		VBox vbox = new VBox();
		
		
		view.setScene(scene);
		view.show();
		
	}

}
