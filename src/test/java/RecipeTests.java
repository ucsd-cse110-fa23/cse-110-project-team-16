package src.test.java;

import src.main.java.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class RecipeTests {
	private Recipe recipe;
	private ArrayList<Recipe> allRecipes;
	
	@BeforeEach
	void setUp() {
		allRecipes = new ArrayList<Recipe>();
		
		// Created recipe for testing use
		recipe = new Recipe(null);
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
	void testReadRecipeType() {
		recipe = new Recipe(null);
		recipe.setRecipeName("Ham and Cheese Sandwich");
		
		String type = recipe.getDetails("type");
		assertEquals("Dinner", type);
	}
	
	@Test 
	void testReadRecipeIngredients() {
		recipe = new Recipe(null);
		recipe.setRecipeName("Ham and Cheese Sandwich");
		
		String ingredients = recipe.getDetails("ingredients");
		assertEquals("Ham, bread, cheese, mayo, mustard, oil, vinegar.", ingredients);
	}
	
	@Test 
	void testReadRecipeDirections() {
		recipe = new Recipe(null);
		recipe.setRecipeName("Ham and Cheese Sandwich");
		
		String directions = recipe.getDetails("directions");
		assertEquals("1. Cut the bread into two medium-sized slices.\n"
				+ "2. Put a thin layer of mustard on each slice of the bread.\n"
				+ "3. Heat a pan on medium-high and add some oil.\n"
				+ "4. On one side of the bread, place a few slices of ham and some slices of cheese.\n"
				+ "5. Place the other piece of bread on top (mustard side down).\n"
				+ "6. Cover the top and let it cook in the pan for a few minutes.\n"
				+ "7. Flip the sandwich and let it cook for a few minutes on the other side.\n"
				+ "8. Take the sandwich off the pan and spread mayo and vinegar on both sides.\n"
				+ "9. Cut the sandwich in half and enjoy.\n", directions);
	}
}

