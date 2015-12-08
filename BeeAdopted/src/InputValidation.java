import java.util.GregorianCalendar;
import java.util.TimeZone;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * This class is used to gather all input validation methods in one place. All field that are able to be filled out inside the
 * application reference this class to ensure that values entered are within their accepted limits. Graphical changes to fields
 * that are not filled out correctly are also carries out in this class.
 * 
 * @since 2015-12-05
 * @author Måns Thörnvik
 */

public class InputValidation {
	
	/**
	 * Used to validate all input fields at once. This only checks that allowed valued have been entered into CBs and TFs.
	 * 
	 * @return boolean representing if all text inputs are correct and shows the program that it is OK to go ahead and send the
	 * information to the database.
	 */

	public static boolean validateAdInfo(TextField name, TextField age, ChoiceBox<Object> gender, ChoiceBox<Object> species, 
										ChoiceBox<Object> type,	TextField newSpecies, TextField newType, TextArea description) {
		validateChoiceBox(gender);
		validateInputAge(age);
		validateInputTextArea(description);
		if ((!validateChoiceBox(species)) &&			// Species and types have been chosen manually, both "new" TFs are validated
				(!validateChoiceBox(type))) {	

			return (validateInputTextFieldString(name)) && 
					(validateInputAge(age)) && 
					(validateChoiceBox(gender)) &&	
					(validateInputTextArea(description)) && 
					(validateInputTextFieldString(newSpecies)) && 
					(validateInputTextFieldString(newType)); 

		} else if (!validateChoiceBox(species)) {		// Species is in its original position, TF for new species is validated.

			return (validateInputTextFieldString(name)) && 
					(validateInputAge(age)) && 
					(validateChoiceBox(gender)) &&
					(validateInputTextArea(description)) &&
					(validateInputTextFieldString(newSpecies));

		} else if (!validateChoiceBox(type)) {			// Type is in its original position, TF for new type is validated.

			return (validateInputTextFieldString(name)) && 
					(validateInputAge(age)) && 
					(validateChoiceBox(gender)) &&
					(validateInputTextArea(description)) &&
					(validateInputTextFieldString(newType));

		} else {										// Nethier CBs were in their original states, none of the "new" TFs are validated.

			return (validateInputTextFieldString(name)) && 
					(validateInputAge(age)) && 
					(validateChoiceBox(gender)) &&
					(validateInputTextArea(description));
		}
	}
	
	/**
	 * Performs a check whether a choice box is in its default position, stating information about it's use.
	 *
	 * @param ChoiceBox for either a species, gender or type.
	 * @return boolean representing if parameter choice box is in its original position, true if it is NOT - since this is the
	 * not the desired position, another should be chosen. False means that the CB value has not been changed.
	 */

	public static boolean validateChoiceBox(ChoiceBox<Object> cb) {
		if ((cb.getValue().equals("Species")) || (cb.getValue().equals("...")) || (cb.getValue().equals("Type"))) {
			cb.setStyle("-fx-focus-color: red ; -fx-border-color: red;");
			return false;
		} else {
			cb.setStyle("-fx-box-border: teal;");
			return true;
		}
	}
	
	/**
	 * Ensures that text fields only contain alphabetical characters and or spaces, otherwise it will change the borde color to red.
	 * 
	 * @param TextField that should only contain letters, spaces also accepted.
	 * @return boolean representing if the input inside the TextField is within it's allowed limits. No more than 30 or less
	 * than one character(s), only letters and or spaces are allowed.
	 */

	public static boolean validateInputTextFieldString(TextField tf) {
		if ((tf.getLength() <= 30) && (tf.getLength() > 0)) {										// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!(Character.isAlphabetic(tf.getText().charAt(index))) && !(tf.getText().charAt(index) == ' ')) {
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;										// Loop is broken, a character that is not alphabetical was found
				} else {}
			}
			tf.setStyle("-fx-box-border: teal;");
			return true;
		} else {														// Number of chars too many
			tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}
	
	/**
	 * Ensures that text fields only contain digits, otherwise it will change the borde color to red.
	 * 
	 * @param TextField that should only contain numbers, spaces are not accepted.
	 * @return boolean representing if the input inside the TextField is within it's allowed limits. Not larger than 100 or
	 * less than 1, only digits are allowed.
	 */

	public static boolean validateInputAge(TextField tf) {
		if ((tf.getLength() <= 2) && (tf.getLength() > 0)) {	
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!Character.isDigit(tf.getText().charAt(index))) {		// "Not a digit" was encountered
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;											// Loop is broken, a character that is not numerical was found.
				} else {}
			}
			tf.setStyle("-fx-box-border: teal;");
			return true;
		} else {
			tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}

	/**
	 * Not currently in use as you may want to add a long text including characters such as !, ?, / and so on, including numbers.
	 * Does check for description length as the DB has a limit of 255 characters.
	 * 
	 * @param TextArea
	 * @return boolean representing if the input inside the TextArea is within it's allowed limits. No more than 255 or less
	 * than zero characters, any input is accepted.
	 */

	public static boolean validateInputTextArea(TextArea ta) {
		if ((ta.getLength() <= 255) && (ta.getLength() > 0)) {
			ta.setStyle("-fx-box-border: teal;");
			return true;
		} else {
			ta.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}
	
	/**
	 * Validate password PasswordField.
	 * 
	 * @param PasswordField
	 * @return boolean if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputPassword(PasswordField pf) {
		if ((pf.getLength() <= 20) && (pf.getLength() >= 5)) {			// Checking number of characters in TF
			for (int index = 0; index < pf.getLength(); index++)	{
				if (!(Character.isDigit(pf.getText().charAt(index))) && 
					!(Character.isAlphabetic(pf.getText().charAt(index)))) {
					pf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;										// Loop is broken, a character that is not alphabetical was found
				} else {}
			}
			pf.setStyle("-fx-box-border: teal;");
			return true;
		} else {														// Number of chars too many
			pf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}
	
	/**
	 * Validate name.
	 * 
	 * @param TextField
	 * @return boolean representing if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputName(TextField tf) {
		if ((tf.getLength() <= 30) && (tf.getLength() >= 1)) {			// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!(Character.isAlphabetic(tf.getText().charAt(index))) && 
					!(tf.getText().charAt(index) == ' ' && 
					!(tf.getText().charAt(index) == '\''))) {
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;										// Loop is broken, a character that is not alphabetical was found
				} else {}
			}
			tf.setStyle("-fx-box-border: teal;");
			return true;
		} else {														// Number of chars too many
			tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}
	
	/**
	 * Validate phone no.
	 * 
	 * @param TextField
	 * @return boolean representing if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputPhone(TextField tf) {
		if ((tf.getLength() <= 10) && (tf.getLength() >= 8)) {		// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!Character.isDigit(tf.getText().charAt(index))) {
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;									// Loop is broken, a character that is not alphabetical was found
				} else {}
			}
			tf.setStyle("-fx-box-border: teal;");
			return true;
		} else {														// Number of chars too many
			tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}
	
	/**
	 * Validate email TextField.
	 * 
	 * @param TextField
	 * @return boolean representing if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputEmail(TextField tf) {
		if ((tf.getLength() <= 50) && (tf.getLength() >= 8) && (tf.getText().contains("@"))) {	// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++) {
				if (!(Character.isDigit(tf.getText().charAt(index))) && 
					!(Character.isAlphabetic(tf.getText().charAt(index))) &&
					!(tf.getText().charAt(index) == '@') &&
					!(tf.getText().charAt(index) == '.')) {
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;
				} else {}
			}
			tf.setStyle("-fx-box-border: teal;");
			return true;
		} else {														// Number of chars too many
			tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}
	
	/**
	 * Validate street TextField.
	 * 
	 * @param TextField
	 * @return boolean representing if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputStreet(TextField tf) {
		if ((tf.getLength() <= 30) && (tf.getLength() >= 1)) {										// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!(Character.isDigit(tf.getText().charAt(index))) && 
					!(Character.isAlphabetic(tf.getText().charAt(index))) && 
					!(tf.getText().charAt(index) == ' ')) {
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;										// Loop is broken, a character that is not alphabetical was found
				} else {}
			}
			tf.setStyle("-fx-box-border: teal;");
			return true;
		} else {														// Number of chars too many
			tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}
	
	/**
	 * Validate zip TextField.
	 * 
	 * @param TextField
	 * @return boolean representing if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputZip(TextField tf) {
		if (tf.getLength() == 5) {										// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!Character.isDigit(tf.getText().charAt(index))) {
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;										// Loop is broken, a character that is not alphabetical was found
				} else {}
			}
			tf.setStyle("-fx-box-border: teal;");
			return true;
		} else {														// Number of chars too many
			tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}
	
	/**
	 * Validate city TextField.
	 * 
	 * @param TextField
	 * @return boolean representing if value entered was within its limits (true) or not.
	 */
	
	public static boolean validateInputCity(TextField tf) {
		if ((tf.getLength() < 30) && (tf.getLength() >= 1)) {										// Checking number of characters in TF
			for (int index = 0; index < tf.getLength(); index++)	{
				if (!(Character.isAlphabetic(tf.getText().charAt(index))) && 
					!(tf.getText().charAt(index) == ' ')) {
					tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
					return false;										// Loop is broken, a character that is not alphabetical was found
				} else {}
			}
			tf.setStyle("-fx-box-border: teal;");
			return true;
		} else {														// Number of chars too many
			tf.setStyle("-fx-focus-color: red ; -fx-text-inner-color: red ; -fx-text-box-border: red;");
			return false;
		}
	}

	/**
	 * Validates all member info input fields simultaneously.
	 */
	
	public static boolean validateMemberInfo(TextField name, TextField phone, TextField email, 
											TextField street, TextField zip, TextField city) {
		return (validateInputName(name)) && (validateInputPhone(phone)) && (validateInputEmail(email)) && 
				(validateInputStreet(street)) && (validateInputZip(zip)) && (validateInputCity(city));
		
	}
	
	/**
	 * Reads the endDate from the ad object and compares it to todays date.
	 * 
	 * @param Ad
	 * @return true if todays date is later than the compared ad's date.
	 */
	
	@SuppressWarnings("finally")
	public static boolean checkEndAfterToday(Ad ad) {
		String endDate = ad.getEndDate();
		GregorianCalendar todaysDate = new GregorianCalendar();
		todaysDate.setTimeZone(TimeZone.getTimeZone("CET"));
		
		try {
			int year = Integer.parseInt(endDate.substring(0, 4));
			int month = Integer.parseInt(endDate.substring(5, 7));
			int day = Integer.parseInt(endDate.substring(7));
			
			return todaysDate.after(new GregorianCalendar(year, month, day));
		} catch (Exception e) {
			System.err.println(">> Date not readable");
		} finally {
			return true;
		}
	}
}