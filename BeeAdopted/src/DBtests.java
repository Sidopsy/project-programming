
import java.util.ArrayList;
import java.util.Observable;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DBtests extends Application {
	public static Stage window;
	public static Scene one;
	public static BorderPane layout1;
	public static ChoiceBox<Object> cb1;
	
	public static void main(String[] args) {
		launch(args);
//		DBobject db = new DBobject();
//		String sql = "SELECT * FROM Ads;";
//		ArrayList<ArrayList<String>> results;
//		
//		// Testing out foreign key constraints through inserts and deletions:
//		sql = "DELETE FROM Agencies WHERE ID = 6;";
//
//		db.executeUpdate(sql);
		
	}
		
		
		
		// Tried making an ObservableList with a specific query, works! Remember to add "extends Application" in the class declaration if you wanna test.
		public void start(Stage primaryStage) throws SQLException {
		
			window = primaryStage;
			window.setTitle("Marketplace");	

			// Creating the window.
			insert();
			layout1 = new BorderPane();					// BorderPane layout is used.
			layout1.setCenter(cb1);						// Top element of the BorderPane is retrieved, which is the iAdopt image.
			one = new Scene(layout1, 800, 600);	
			
			// Setting the currently open window to show the scene created above.
			window.setScene(one);
			window.show();
		}
		
		
		// Tested making an observableList with a specific query, works!
		public void insert() throws SQLException {
			
			String sql = "SELECT Name, Age FROM Ads;";		
			DBobject db = new DBobject();
			
			db.resultSet = db.executeQuery(sql);
			
			ArrayList<ArrayList<String>> result = DBobject.fetchResult(db.resultSet);
			
			for (int i = 0; i < result.size(); i++) {
				System.out.println(result.get(i));
			}
			
			cb1 = new ChoiceBox<>(DBobject.createObservableList(result));
		}
		
		
		// Trying out the metaDataHandler
//		ResultSet rs = db.executeQuery(sql);
//		String[] names = CreateObjects.metaDataNames(rs);
//		String[] types = CreateObjects.metaDataTypes(rs);
//		
//		for (int i = 0; i < names.length; i++) {
//			System.out.println(names[i]);
//			System.out.println(types[i]);
//		}
//		db.closeConnection();

			
		// Trying out multiple >queries< in a row to see if it works with PreparedStatements
//		ResultSet rs = db.executeQuery(sql);
//		
//		while (rs.next()) {
//			int id = rs.getInt("ID");
//			System.out.println(id);
//		}
//		
//		sql = "SELECT * FROM Ads;";
//		
//		rs = db.executeQuery(sql);
//		
//		while (rs.next()) {
//			int id = rs.getInt("ID");
//			System.out.println(id);
//		}
		
		
		// Trying our paramererized queries for re-usability.
//		sql = "SELECT * FROM Addresses WHERE City = ?;";
//		String parameter = "Stockholm";
//		db.stmt = db.connect.prepareStatement(sql);
//		db.stmt.setString(1, parameter);
//		ResultSet rs = db.stmt.executeQuery();
//		
//		while (rs.next()) {
//			int ID = rs.getInt("AgencyID");
//			String street = rs.getString("Street");
//			String ZIP = rs.getString("ZIP");
//			String City = rs.getString("City");
//			System.out.println("" + ID + street + ZIP + City);
//		}
//		db.closeConnection();
		
		
		// This code determines if foreign keys have been activated for the current transaction.
//		db.foreignKeysOn();
//		db.closeConnection();
	}
