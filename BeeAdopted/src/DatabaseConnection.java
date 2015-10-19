import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseConnection {

	public static ObservableList<String> getListFromDatabase(String string) {
		ObservableList<String> result = 
			    FXCollections.observableArrayList(
			        "Option 1",
			        "Option 2",
			        "Option 3"
			    );
		return result;
	}
	
	

	public static String[] getAd(int adID) {
		// TODO Auto-generated method stub
		return null;
	}

}
