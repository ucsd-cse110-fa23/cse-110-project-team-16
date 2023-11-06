package src;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.control.TextField;

class MainTest {
	private recipeBox recipesCheck;
	 @BeforeEach
	    void setUp() {
	        recipesCheck = new recipeBox();
	    }
	 
	 @Test
	    void testGetRecipeName() {
		 TextField testName = null;
		 testName.setText("Onion Garlic Soup");
		 recipesCheck.setRecipeName(testName);
	        assertEquals("Onion Garlic Soup", recipesCheck.getRecipeName());
	    }
	 
	 @Test
	    void testGetIndredientsName() {
		 TextField testName = null;
		 testName.setText("Onion,Stock,Salt,Carrot,Cumin");
		 recipesCheck.setIngredientsName(testName);
	        assertEquals("Onion,Stock,Salt,Carrot,Cumin", recipesCheck.getIndredientsName());
	    }

}
