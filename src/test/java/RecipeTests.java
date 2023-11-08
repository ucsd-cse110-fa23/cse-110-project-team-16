package src.test.java;

import src.main.java.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeTests {
	private Recipe recipe;
	
	@BeforeEach
	void setUp() {
		recipe = new Recipe();
	}
	
	@Test
	void testGetRecipeName() {
		recipe.setRecipeName("Onion Garlic Soup");
		assertEquals("Onion Garlic Soup", recipe.getRecipeName());
	}
	
	@Test
	void testGetRecipeName2() {
		recipe.setRecipeName("Lemon Chicken");
		assertEquals("Lemon Chicken", recipe.getRecipeName());
	}
	
	/*
	@Test
	void testGetIndredients() {
		recipe.setIngredients("Onion,Stock,Salt,Carrot,Cumin");
		assertEquals("Onion,Stock,Salt,Carrot,Cumin", recipe.getIngredients());
		assertEquals("test", "test");
	}
	*/
}
