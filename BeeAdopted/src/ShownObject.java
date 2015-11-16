
import javafx.beans.property.SimpleStringProperty;

/**
 * The idea is to have one object for everything, send in as many variables as you want to show, up to 15.
 * 
 * @author MTs
 */

public class ShownObject {
	private SimpleStringProperty col1, col2, col3, col4, col5, col6, col7, col8, 
								 col9, col10, col11, col12, col13, col14, col15;
	
	public ShownObject(String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8,
					   String col9, String col10, String col11, String col12, String col13, String col14, String col15) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = new SimpleStringProperty(col4);
		this.col5 = new SimpleStringProperty(col5);
		this.col6 = new SimpleStringProperty(col6);
		this.col7 = new SimpleStringProperty(col7);
		this.col8 = new SimpleStringProperty(col8);
		this.col9 = new SimpleStringProperty(col9);
		this.col10 = new SimpleStringProperty(col10);
		this.col11 = new SimpleStringProperty(col11);
		this.col12 = new SimpleStringProperty(col12);
		this.col13 = new SimpleStringProperty(col13);
		this.col14 = new SimpleStringProperty(col14);
		this.col15 = new SimpleStringProperty(col15);
	}
	
	public ShownObject(String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8,
			String col9, String col10, String col11, String col12, String col13, String col14) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = new SimpleStringProperty(col4);
		this.col5 = new SimpleStringProperty(col5);
		this.col6 = new SimpleStringProperty(col6);
		this.col7 = new SimpleStringProperty(col7);
		this.col8 = new SimpleStringProperty(col8);
		this.col9 = new SimpleStringProperty(col9);
		this.col10 = new SimpleStringProperty(col10);
		this.col11 = new SimpleStringProperty(col11);
		this.col12 = new SimpleStringProperty(col12);
		this.col13 = new SimpleStringProperty(col13);
		this.col14 = new SimpleStringProperty(col14);
		this.col15 = null;
	}
	
	public ShownObject(String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8,
			String col9, String col10, String col11, String col12, String col13) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = new SimpleStringProperty(col4);
		this.col5 = new SimpleStringProperty(col5);
		this.col6 = new SimpleStringProperty(col6);
		this.col7 = new SimpleStringProperty(col7);
		this.col8 = new SimpleStringProperty(col8);
		this.col9 = new SimpleStringProperty(col9);
		this.col10 = new SimpleStringProperty(col10);
		this.col11 = new SimpleStringProperty(col11);
		this.col12 = new SimpleStringProperty(col12);
		this.col13 = new SimpleStringProperty(col13);
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject(String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8,
			String col9, String col10, String col11, String col12) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = new SimpleStringProperty(col4);
		this.col5 = new SimpleStringProperty(col5);
		this.col6 = new SimpleStringProperty(col6);
		this.col7 = new SimpleStringProperty(col7);
		this.col8 = new SimpleStringProperty(col8);
		this.col9 = new SimpleStringProperty(col9);
		this.col10 = new SimpleStringProperty(col10);
		this.col11 = new SimpleStringProperty(col11);
		this.col12 = new SimpleStringProperty(col12);
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject(String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8,
			String col9, String col10, String col11) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = new SimpleStringProperty(col4);
		this.col5 = new SimpleStringProperty(col5);
		this.col6 = new SimpleStringProperty(col6);
		this.col7 = new SimpleStringProperty(col7);
		this.col8 = new SimpleStringProperty(col8);
		this.col9 = new SimpleStringProperty(col9);
		this.col10 = new SimpleStringProperty(col10);
		this.col11 = new SimpleStringProperty(col11);
		this.col12 = null;
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject(String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8,
			String col9, String col10) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = new SimpleStringProperty(col4);
		this.col5 = new SimpleStringProperty(col5);
		this.col6 = new SimpleStringProperty(col6);
		this.col7 = new SimpleStringProperty(col7);
		this.col8 = new SimpleStringProperty(col8);
		this.col9 = new SimpleStringProperty(col9);
		this.col10 = new SimpleStringProperty(col10);
		this.col11 = null;
		this.col12 = null;
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject(String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8,
			String col9) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = new SimpleStringProperty(col4);
		this.col5 = new SimpleStringProperty(col5);
		this.col6 = new SimpleStringProperty(col6);
		this.col7 = new SimpleStringProperty(col7);
		this.col8 = new SimpleStringProperty(col8);
		this.col9 = new SimpleStringProperty(col9);
		this.col10 = null;
		this.col11 = null;
		this.col12 = null;
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject(String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = new SimpleStringProperty(col4);
		this.col5 = new SimpleStringProperty(col5);
		this.col6 = new SimpleStringProperty(col6);
		this.col7 = new SimpleStringProperty(col7);
		this.col8 = new SimpleStringProperty(col8);
		this.col9 = null;
		this.col10 = null;
		this.col11 = null;
		this.col12 = null;
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject(String col1, String col2, String col3, String col4, String col5, String col6, String col7) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = new SimpleStringProperty(col4);
		this.col5 = new SimpleStringProperty(col5);
		this.col6 = new SimpleStringProperty(col6);
		this.col7 = new SimpleStringProperty(col7);
		this.col8 = null;
		this.col9 = null;
		this.col10 = null;
		this.col11 = null;
		this.col12 = null;
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject(String col1, String col2, String col3, String col4, String col5, String col6) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = new SimpleStringProperty(col4);
		this.col5 = new SimpleStringProperty(col5);
		this.col6 = new SimpleStringProperty(col6);
		this.col7 = null;
		this.col8 = null;
		this.col9 = null;
		this.col10 = null;
		this.col11 = null;
		this.col12 = null;
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject(String col1, String col2, String col3, String col4, String col5) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = new SimpleStringProperty(col4);
		this.col5 = new SimpleStringProperty(col5);
		this.col6 = null;
		this.col7 = null;
		this.col8 = null;
		this.col9 = null;
		this.col10 = null;
		this.col11 = null;
		this.col12 = null;
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject(String col1, String col2, String col3, String col4) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = new SimpleStringProperty(col4);
		this.col5 = null;
		this.col6 = null;
		this.col7 = null;
		this.col8 = null;
		this.col9 = null;
		this.col10 = null;
		this.col11 = null;
		this.col12 = null;
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject(String col1, String col2, String col3) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
		this.col4 = null;
		this.col5 = null;
		this.col6 = null;
		this.col7 = null;
		this.col8 = null;
		this.col9 = null;
		this.col10 = null;
		this.col11 = null;
		this.col12 = null;
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject(String col1, String col2) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = null;
		this.col4 = null;
		this.col5 = null;
		this.col6 = null;
		this.col7 = null;
		this.col8 = null;
		this.col9 = null;
		this.col10 = null;
		this.col11 = null;
		this.col12 = null;
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject(String col1) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = null;
		this.col3 = null;
		this.col4 = null;
		this.col5 = null;
		this.col6 = null;
		this.col7 = null;
		this.col8 = null;
		this.col9 = null;
		this.col10 = null;
		this.col11 = null;
		this.col12 = null;
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public ShownObject() {
		this.col1 = null;
		this.col2 = null;
		this.col3 = null;
		this.col4 = null;
		this.col5 = null;
		this.col6 = null;
		this.col7 = null;
		this.col8 = null;
		this.col9 = null;
		this.col10 = null;
		this.col11 = null;
		this.col12 = null;
		this.col13 = null;
		this.col14 = null;
		this.col15 = null;
	}
	
	public void setCol1(String input) {
		col1.set(input);
	}
	
	public void setCol2(String input) {
		col2.set(input);
	}
	
	public void setCol3(String input) {
		col3.set(input);
	}
	
	public void setCol4(String input) {
		col4.set(input);
	}
	
	public void setCol5(String input) {
		col5.set(input);
	}
	
	public void setCol6(String input) {
		col6.set(input);
	}
	
	public void setCol7(String input) {
		col7.set(input);
	}
	
	public void setCol8(String input) {
		col8.set(input);
	}
	
	public void setCol9(String input) {
		col9.set(input);
	}
	
	public void setCol10(String input) {
		col10.set(input);
	}
	
	public void setCol11(String input) {
		col11.set(input);
	}
	
	public void setCol12(String input) {
		col12.set(input);
	}
	
	public void setCol13(String input) {
		col13.set(input);
	}
	
	public void setCol14(String input) {
		col14.set(input);
	}
	
	public void setCol15(String input) {
		col15.set(input);
	}
	
	public String toString() {
		return "" + col1 + col2 + col3 + col4 + col5 + col6 + col7 + col8 + col9 + col10 + col11 + col12 + col13 + col14 + col15;
	}
}
