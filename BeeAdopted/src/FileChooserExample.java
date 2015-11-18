import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


public final class FileChooserExample {
	static Stage window;
	static Scene sc1, sc2;
	static BorderPane layout1, layout2;
	static ChoiceBox<Object> cb1, cb2, cb3, cb4, cb5;
	static TextField tf, age;
	static TextArea ta;
	static Button btn;
	static File file;
    private static Desktop desktop = Desktop.getDesktop();
 
    public static File display() {
    	window = new Stage();
        window.setTitle("Upload a picture");
 
        final FileChooser fileChooser = new FileChooser();

        final Button saveButton = new Button ("Save");
       
        final Button openButton = new Button("Open a Picture...");

 
        openButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    file = fileChooser.showOpenDialog(window);
                }
            });

 
 
        final GridPane inputGridPane = new GridPane();
 
        GridPane.setConstraints(openButton, 1, 5 );
        GridPane.setConstraints(saveButton, 5, 4);

        inputGridPane.setHgap(40);
        inputGridPane.setVgap(20);
        inputGridPane.getChildren().addAll(openButton);
        
        inputGridPane.add(saveButton, 3, 5 );
 
        final Pane rootGroup = new VBox(200);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(150, 200, 150, 200));
 
        window.setScene(new Scene(rootGroup));
        window.show();
        
        return file;
    }
 
    private static void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                FileChooserExample.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }
}