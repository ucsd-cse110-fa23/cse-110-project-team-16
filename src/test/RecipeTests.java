package src.test;

import src.java.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.control.TextField;

public class RecipeTests {
	private RecipeBox recipeBox;
	
	@BeforeEach
	void setUp() {
		recipeBox = new RecipeBox();
	}
	
	@Test
    void testGetRecipeName() {
		recipeBox.setRecipeName("Onion Garlic Soup");
        assertEquals("Onion Garlic Soup", recipeBox.getRecipeName());
    }
 
	@Test
	void testGetIndredients() {
		recipeBox.setIngredients("Onion,Stock,Salt,Carrot,Cumin");
		assertEquals("Onion,Stock,Salt,Carrot,Cumin", recipeBox.getIngredients());
	}
}
