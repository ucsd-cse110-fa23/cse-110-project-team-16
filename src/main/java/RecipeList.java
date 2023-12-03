//package src.main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;

import javafx.scene.layout.VBox;

public class RecipeList extends VBox {
	
	// private ActionsList actionsList;
    private String db_dir = "localDB/";
    private RecipeDetails localRecipeDetails;
    private ArrayList<Recipe> allRecipes;
    private String filterType;


    public RecipeList(RecipeDetails details, ArrayList<Recipe> recipeArray) {
    	this.setSpacing(5); // sets spacing between recipes
        this.setPrefSize(300, 560);
        this.setStyle("-fx-background-color: #559952;");
 
        localRecipeDetails = details;
        allRecipes = recipeArray;
        filterType = "All";

        // load recipes from mongoDB
        loadRecipesMongo();
    }

    public void loadRecipesMongo() {
    	for (int i = 0; i < allRecipes.size(); i++) {
    		this.getChildren().remove(allRecipes.get(i));
    	}
    	allRecipes.clear();
    	
        Set<Document> recipes = MongoDB.listRecipes();

        for (Document recipe: recipes) {
            Recipe currRecipe = null;
            String name = (String)recipe.get("name");
            String type = (String)recipe.get("type");
            ObjectId id = (ObjectId)recipe.get("_id");

            if (!filterType.equals("All")) {
	            if (!(filterType.equals(type))) {
	            	continue;
	            }
            }

            currRecipe = new Recipe(localRecipeDetails);
            currRecipe.setRecipeName(name);
            currRecipe.setRecipeType(type);
            currRecipe.setRecipeID(id);
            currRecipe.updateText();

            this.getChildren().add(currRecipe);
            allRecipes.add(currRecipe);
            currRecipe.updateRecipeArray(allRecipes);
        }
    }

    public ArrayList<String> getDetails (String recipeName) {
		File file = new File(db_dir + recipeName + ".txt");
        ArrayList<String> recipeDetails = new ArrayList<String>();
	    BufferedReader br = null;
	    try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	 
	    try {			
			String mealName = br.readLine();
            String mealType = br.readLine();
            String mealIngred = br.readLine();
            String mealDirections = br.readLine();
            recipeDetails.add(mealName);
            recipeDetails.add(mealType);
            recipeDetails.add(mealIngred);
            recipeDetails.add(mealDirections);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        
        try {
            br.close();
        } catch (IOException e) {
            
            System.out.println("Closing buffered Reader Failed: (RecipeList.getDetails)");
            e.printStackTrace();
        }
        
        return recipeDetails;
	}

    public void changeRecipeSelect() {
            // Unselect other selected recipes
            for (int i = 0; i < allRecipes.size(); i++) {
            	if (allRecipes.get(i).isSelected()) {
            		allRecipes.get(i).toggleSelect();
            	}
            }
    }
    
    public ArrayList<Recipe> getAllRecipes () {
		return allRecipes;
	}
    
    public void setFilterType (String _filterType) {
    	filterType = _filterType;
    }
}