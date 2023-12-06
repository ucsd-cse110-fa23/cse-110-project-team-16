import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.time.*;

public class RecipeFilterTests {
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;
	private ArrayList<Recipe> allRecipes;
	private RecipeList testRecipeList;
	
	@BeforeEach
	void setUp() {
		allRecipes = new ArrayList<Recipe>();
		
		// Created recipe for testing use
		recipe1 = new Recipe();
		recipe1.setRecipeName("Ham and Cheese Sandwich");
		recipe1.setRecipeType("Breakfast");

		recipe2 = new Recipe();
		recipe2.setRecipeName("Breakfast Cereal");
		recipe2.setRecipeType("Breakfast");

		recipe3 = new Recipe();
		recipe3.setRecipeName("Steak Shawarma");
		recipe3.setRecipeType("Lunch");

		recipe4 = new Recipe();
		recipe4.setRecipeName("Chicken Kabob");
		recipe4.setRecipeType("Dinner");

		allRecipes.add(recipe1);
		allRecipes.add(recipe2);
		allRecipes.add(recipe3);
		allRecipes.add(recipe4);

		recipe1.updateRecipeArray(allRecipes);
		recipe2.updateRecipeArray(allRecipes);
		recipe3.updateRecipeArray(allRecipes);
		recipe4.updateRecipeArray(allRecipes);

		testRecipeList = new RecipeList(allRecipes);
	}
	
		
	@Test 
	void testFilterAll() {
		
		testRecipeList.setFilterType("All");
		testRecipeList.changeDisplayByType();
		
		int filtered_num = testRecipeList.getChildren().size();
		assertEquals(filtered_num, 4);	
	}
	
	@Test 
	void testFilterBreakfast() {

		testRecipeList.setFilterType("Breakfast");
		testRecipeList.changeDisplayByType();
		
		int filtered_num = testRecipeList.getChildren().size();
		assertEquals(filtered_num, 2);

	}
	
	@Test
	void testFilterLunch() {

		testRecipeList.setFilterType("Lunch");
		testRecipeList.changeDisplayByType();
		
		int filtered_num = testRecipeList.getChildren().size();
		assertEquals(filtered_num, 1);
	}

	@Test
	void testFilterDinner() {

		testRecipeList.setFilterType("Dinner");
		testRecipeList.changeDisplayByType();
		
		int filtered_num = testRecipeList.getChildren().size();
		assertEquals(filtered_num, 1);	

	}
}

