package src.test.java;

import src.main.java.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class RecipeTests {
	private Recipe recipe;
	private RecipeList recipeList;
	private ArrayList<Recipe> allRecipes;
	
	@BeforeEach
	void setUp() {
		allRecipes = new ArrayList<Recipe>();
		//recipeList = new RecipeList(null, allRecipes);
	}
	
	@Test
	void testCreateRecipe() {
		// CreateRecipe
		recipe = new Recipe(null);
		recipe.setRecipeName("Ham and Cheese Sandwich");
		recipe.updateRecipeArray(allRecipes);
		allRecipes.add(recipe);
		
		//Check recipe has been created
		String name = "";
		for (Recipe rec: allRecipes) {
			if(rec.getRecipeName() == recipe.getRecipeName())
				name = rec.getRecipeName();
		}
		assertEquals("Ham and Cheese Sandwich", name);
	}
	
}

