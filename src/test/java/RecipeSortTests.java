import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class RecipeSortTests {
	private Recipe recipe;
	private ArrayList<Recipe> allRecipes;
	
	@BeforeEach
	void setUp() {
		allRecipes = new ArrayList<Recipe>();
		
		// Created recipe for testing use
		recipe = new Recipe();
		recipe.setRecipeName("Ham and Cheese Sandwich");
		recipe.updateRecipeArray(allRecipes);
		allRecipes.add(recipe);
	}
	
	@Test
	void testCreateRecipe() {
		//Check recipe has been created
		String name = "";
		for (Recipe rec: allRecipes) {
			if(rec.getRecipeName() == recipe.getRecipeName())
				name = rec.getRecipeName();
		}
		assertEquals("Ham and Cheese Sandwich", name);
	}
	
	@Test 
	void testSortA2Z() {
		// String type = recipe.getDetailsMOCK("Ham and Cheese Sandwich", "type");
		// assertEquals("Dinner", type);
	}
	
	@Test 
	void testSortZ2A() {
		// String ingredients = recipe.getDetailsMOCK("Ham and Cheese Sandwich", "ingredients");
		// assertEquals("Ham, bread, cheese, mayo, mustard, oil, vinegar.", ingredients);
	}
	
	@Test
	void testSortNewToOld() {
		
	}

	@Test
	void testSortOldToNew() {

	}
}

