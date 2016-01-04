package Object;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Object for ads, appropriate variables for displaying in the application.
 * 
 * @author Maans Thoernvik 
 * @refactoredBy Mattias Landkvist * Added rating and picture inside the ad object.
 */

public class Ad {
	private final SimpleStringProperty name, gender, species, type, description, startDate, endDate, agencyName;
	private final SimpleIntegerProperty id, age, agencyID;
	private final ImageView picture;
	private final RatingExt rating;
	private final BooleanProperty check;
	
	public Ad(int id, Image picture, String name, String gender, String species, String type, 
				int age, String description, String start, String end, int agencyID, String agencyName, double rating) {
		this.id = new SimpleIntegerProperty(id);
		this.picture = new ImageView(picture);
		this.picture.setPreserveRatio(false);
		this.picture.setFitWidth(50);
		this.picture.setFitHeight(50);
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
		
		this.rating = new RatingExt(agencyID, rating);
		this.rating.setDisable(true);
		this.rating.setScaleX(0.5);
		this.rating.setScaleY(0.5);
		
		// BooleanProperty is initialized to false so that the checkboxes are unchecked by default.
		this.check = new SimpleBooleanProperty(false);
        this.check.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				check.set(newValue);
				System.out.println(newValue);
			}
        });          
	}
	
	/*
	 * Below are getters for all ad variables.
	 */
	
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
	
	public RatingExt getRating(){
		return rating;
	}
	
	public boolean getCheck() {
		return check.get();
	}

	public Ad getAd(){
		return this;
	}
	
	public BooleanProperty getCheckProperty(){
		return check;
	}
	
	public String toString() {
		return picture + " " + name + " " + gender + " " + species + " " + 
				type + " " + age + " " + description + " " + startDate + " " + endDate;
	}

}