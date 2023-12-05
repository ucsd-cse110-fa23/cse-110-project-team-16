import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.time.*;

public class RecipeSortTests {
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;
	private ArrayList<Recipe> allRecipes;
	private RecipeDetails blankDetails;
	private RecipeListMock testRecipeList;
	
	@BeforeEach
	void setUp() {
		allRecipes = new ArrayList<Recipe>();
		
		// Created recipe for testing use
		recipe1 = new Recipe();
		recipe1.setRecipeName("Ham and Cheese Sandwich");
		recipe1.setDate(new Date(100));

		recipe2 = new Recipe();
		recipe2.setRecipeName("Breakfast Cereal");
		recipe2.setDate(new Date(2000));

		recipe3 = new Recipe();
		recipe3.setRecipeName("Steak Shawarma");
		recipe3.setDate(new Date(3000));

		recipe4 = new Recipe();
		recipe4.setRecipeName("Chicken Kabob");
		recipe4.setDate(new Date(4000));

		allRecipes.add(recipe1);
		allRecipes.add(recipe2);
		allRecipes.add(recipe3);
		allRecipes.add(recipe4);

		recipe1.updateRecipeArray(allRecipes);
		recipe2.updateRecipeArray(allRecipes);
		recipe3.updateRecipeArray(allRecipes);
		recipe4.updateRecipeArray(allRecipes);

		blankDetails = new RecipeDetails();
		testRecipeList = new RecipeListMock(blankDetails, allRecipes);
	}
	
	@Test
	void testCreateRecipe() {
		//Check recipe has been created
		String name0 = allRecipes.get(0).getRecipeName();
		String name1 = allRecipes.get(1).getRecipeName();
		String name2 = allRecipes.get(2).getRecipeName();		
		String name3 = allRecipes.get(3).getRecipeName();

		assertEquals("Ham and Cheese Sandwich", name0);
		assertEquals("Breakfast Cereal", name1);
		assertEquals("Steak Shawarma", name2);
		assertEquals("Chicken Kabob", name3);
	}
	
	@Test 
	void testSortA2Z() {
		
		testRecipeList.recipeSortA2Z();
		ArrayList<Recipe> sortedRecipes = testRecipeList.getAllRecipes();
		String name0 = sortedRecipes.get(0).getRecipeName();
		String name1 = sortedRecipes.get(1).getRecipeName();
		String name2 = sortedRecipes.get(2).getRecipeName();		
		String name3 = sortedRecipes.get(3).getRecipeName();

		assertEquals("Breakfast Cereal", name0);
		assertEquals("Chicken Kabob", name1);
		assertEquals("Ham and Cheese Sandwich", name2);		
		assertEquals("Steak Shawarma", name3);		
	}
	
	@Test 
	void testSortZ2A() {

		testRecipeList.recipeSortZ2A();
		ArrayList<Recipe> sortedRecipes = testRecipeList.getAllRecipes();
		String name0 = sortedRecipes.get(0).getRecipeName();
		String name1 = sortedRecipes.get(1).getRecipeName();
		String name2 = sortedRecipes.get(2).getRecipeName();		
		String name3 = sortedRecipes.get(3).getRecipeName();

		assertEquals("Steak Shawarma", name0);	
		assertEquals("Ham and Cheese Sandwich", name1);
		assertEquals("Chicken Kabob", name2);
		assertEquals("Breakfast Cereal", name3);

	}
	
	@Test
	void testSortNewToOld() {

		testRecipeList.recipeSortNewToOld();
		ArrayList<Recipe> sortedRecipes = testRecipeList.getAllRecipes();
		String name0 = sortedRecipes.get(0).getRecipeName();
		String name1 = sortedRecipes.get(1).getRecipeName();
		String name2 = sortedRecipes.get(2).getRecipeName();		
		String name3 = sortedRecipes.get(3).getRecipeName();
		
		
		assertEquals(recipe4.getDate(), sortedRecipes.get(0).getDate());
		assertEquals(recipe3.getDate(), sortedRecipes.get(1).getDate());
		assertEquals(recipe2.getDate(), sortedRecipes.get(2).getDate());
		assertEquals(recipe1.getDate(), sortedRecipes.get(3).getDate());
		assertEquals("Chicken Kabob", name0);
		assertEquals("Steak Shawarma", name1);	
		assertEquals("Breakfast Cereal", name2);
		assertEquals("Ham and Cheese Sandwich", name3);
	}

	@Test
	void testSortOldToNew() {

		testRecipeList.recipeSortOldToNew();
		ArrayList<Recipe> sortedRecipes = testRecipeList.getAllRecipes();
		String name0 = sortedRecipes.get(0).getRecipeName();
		String name1 = sortedRecipes.get(1).getRecipeName();
		String name2 = sortedRecipes.get(2).getRecipeName();		
		String name3 = sortedRecipes.get(3).getRecipeName();
		
		
		assertEquals(recipe1.getDate(), sortedRecipes.get(0).getDate());
		assertEquals(recipe2.getDate(), sortedRecipes.get(1).getDate());
		assertEquals(recipe3.getDate(), sortedRecipes.get(2).getDate());
		assertEquals(recipe4.getDate(), sortedRecipes.get(3).getDate());
		assertEquals("Ham and Cheese Sandwich", name0);
		assertEquals("Breakfast Cereal", name1);
		assertEquals("Steak Shawarma", name2);	
		assertEquals("Chicken Kabob", name3);

	}
}

