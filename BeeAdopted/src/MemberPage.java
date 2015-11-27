import java.awt.Event;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MemberPage extends ViewMaster{
	
	public static void memberPage(){
		
		//Still need to add header
		Stage MPStage = new Stage();
		MPStage.setTitle("Your Account");
		
		GridPane MPWindow = new GridPane();
		MPWindow.setPadding(new Insets(10,10,10,10));
		MPWindow.setVgap(8);
		MPWindow.setHgap(6);
	
		
		
		
		Label MPnameLabel = new Label("Username:");
        GridPane.setConstraints(MPnameLabel, 0, 0);
        //->Change prompt-texts from strings to current info
        //Name Input
        TextField MPnameInput = new TextField();
        (MPnameInput).setPromptText("Username");
        GridPane.setConstraints(MPnameInput, 1, 0);

        //Password Label
        Label MPpasswordLabel = new Label("Password:");
        GridPane.setConstraints(MPpasswordLabel, 0, 1);

        //Password Input
        PasswordField MPpasswordInput = new PasswordField();
        (MPpasswordInput).setPromptText("Password");
        GridPane.setConstraints(MPpasswordInput, 1, 1);
        
        //email
        Label MPemailLabel = new Label("Email:");
        GridPane.setConstraints(MPemailLabel, 0, 2);

        //Email INput
        TextField MPemailInput = new TextField();
        (MPemailInput).setPromptText("Email");
        GridPane.setConstraints(MPemailInput, 1, 2);
        
        //phone number
        Label MPphoneLabel = new Label("Phone Number:");
        GridPane.setConstraints(MPphoneLabel, 0, 3);

        //Email INput
        TextField MPphoneInput = new TextField();
        (MPphoneInput).setPromptText("Phone");
        GridPane.setConstraints(MPphoneInput, 1, 3);
        
        //Go to Input Page
        Button inputPage = new Button("Create a New Ad");
        inputPage.setOnAction(e -> InputPage.start());
        GridPane.setConstraints(inputPage, 3, 0);  
        
        //Go to Display my adds
        Button viewMyAds = new Button("View My Ads");
        viewMyAds.setOnAction(e -> ViewMyAds.showMyAds());
        GridPane.setConstraints(viewMyAds, 3, 1);
        
        
        
        //display adds
        
        //Save Changes Button
        Button Edit = new Button("Save Changes");
        GridPane.setConstraints(Edit, 1, 4);
		
      //Add everything to membership grid layout
        MPWindow.getChildren().addAll(MPnameLabel, MPnameInput, 
        		MPpasswordLabel, MPpasswordInput, MPphoneLabel, 
        		MPphoneInput, MPemailLabel, MPemailInput, Edit,
        		inputPage, viewMyAds);
        
       // Membership.verify(nameInput, passwordInput);
        Scene MPscene = new Scene(MPWindow, 500, 600);
        MPStage.setScene(MPscene);
        MPStage.show();
	}

	
}
