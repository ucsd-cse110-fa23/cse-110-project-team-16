package cse.project.team;

//import app.src.main.java.cse.project.*;

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
		String type = recipe.getDetailsMOCK("Ham and Cheese Sandwich", "type");
		assertEquals("Dinner", type);
	}
	
	@Test 
	void testReadRecipeIngredients() {
		String ingredients = recipe.getDetailsMOCK("Ham and Cheese Sandwich", "ingredients");
		assertEquals("Ham, bread, cheese, mayo, mustard, oil, vinegar.", ingredients);
	}
	
	@Test 
	void testReadRecipeDirections() {
		String directions = recipe.getDetailsMOCK("Ham and Cheese Sandwich", "directions");
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

