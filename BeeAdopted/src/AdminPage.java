import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminPage {
	
		
	
	static void start(){
	
	
	Stage stage = new Stage();
	GridPane gridpane = new GridPane();
	
	Text text = new Text(10, 40, "Welcome Home, Admin.");
	text.setFont(new Font(20));
	Scene scene = new Scene(new Group(text));
	stage.setTitle("Admin Account"); 
    stage.setScene(scene); 
    stage.sizeToScene(); 
    stage.show(); 
		
		
	}
	
	public static void main(String[] args){
		
	}

}
