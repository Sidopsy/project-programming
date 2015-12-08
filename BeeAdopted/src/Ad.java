
import java.sql.Blob;

import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Object for ads, appropriate variables for displaying in the application.
 * 
 * @author MTs
 * @since 10-26-15
 */

public class Ad {
	private final SimpleStringProperty name, gender, species, type, description, startDate, endDate, agencyName;
	private final SimpleIntegerProperty id, age, agencyID;
	private final ImageView picture;
	private final RatingObject rating;
	private final SimpleBooleanProperty check;
	
	public Ad(int id, Image picture, String name, String gender, String species, String type, 
				int age, String description, String start, String end, int agencyID, String agencyName, double rating, boolean check) {
		this.id = new SimpleIntegerProperty(id);
		this.picture = new ImageView(picture);
		this.name = new SimpleStringProperty(name);
		this.gender = new SimpleStringProperty(gender);
		this.species = new SimpleStringProperty(species);
		this.type = new SimpleStringProperty(type);
		this.age = new SimpleIntegerProperty(age);
		this.description = new SimpleStringProperty(description);
		this.startDate = new SimpleStringProperty(start);
		this.endDate = new SimpleStringProperty(end);
		this.agencyID = new SimpleIntegerProperty(agencyID);
		this.agencyName = new SimpleStringProperty(agencyName);
		this.rating = new RatingObject(agencyID, rating);

		this.rating.setDisable(true);
		this.rating.setScaleX(0.5);
		this.rating.setScaleY(0.5);
		this.check = new SimpleBooleanProperty(check);
		 
        
		 
        this.check.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				
			}

        });          

	}
	
	public int getID(){
		return id.get();
	}
	
	public ImageView getPicture(){
		return picture;
	}
	
	public String getName(){
		return name.get();
	}
	
	public String getGender(){
		return gender.get();
	}
	
	public String getSpecies(){
		return species.get();
	}
	
	public String getType(){
		return type.get();
	}
	
	public int getAge(){
		return age.get();
	}
	
	public String getDescription(){
		return description.get();
	}
	
	public String getStartDate(){
		return startDate.get();
	}
	
	public String getEndDate(){
		return endDate.get();
	}
	
	public int getAgencyID(){
		return agencyID.get();
	}
	
	public String getAgencyName(){
		return agencyName.get();
	}
	
	public RatingObject getRating(){
		return rating;
	}
	
	
	public String toString() {
		return picture + " " + name + " " + gender + " " + species + " " + 
				type + " " + age + " " + description + " " + startDate + " " + endDate;
	}

	public CheckBox getCheck() {
		// TODO Auto-generated method stub
		return new CheckBox();
	}
	
	public Ad getAd(){
		return this;
	}
}