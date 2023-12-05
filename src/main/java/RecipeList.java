//package src.main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.bson.types.Symbol;

import javafx.scene.layout.VBox;

public class RecipeList extends VBox {
	
	// private ActionsList actionsList;
    private String db_dir = "localDB/";
    private RecipeDetails localRecipeDetails;
    private ArrayList<Recipe> allRecipes;
    private String filterType;
    private String sortType;


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
        int creationDateRank = 1;

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

            currRecipe.setDate(convertToDate(id.toString()));

            this.getChildren().add(currRecipe);
            allRecipes.add(currRecipe);
            currRecipe.updateRecipeArray(allRecipes);
        }
    }

    public static Date convertToDate(String objectId) {
        long date = Long.parseLong(objectId.substring(0, 8), 16) * 1000;
        return new Date(date);
    }

    public void setFilterType (String _filterType) {
    	filterType = _filterType;
    }

    public void setSortType (String _sortType) {
    	sortType = _sortType;
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

    public void sortDisplay(ArrayList<Recipe> sortedRecipes) {
        this.getChildren().setAll(sortedRecipes);
    }

    public void recipeSortA2Z() {
        Collections.sort(allRecipes, new AtoZComparator());
        sortDisplay(allRecipes);
    }

    public void recipeSortZ2A() {
        Collections.sort(allRecipes, new ZtoAComparator());
        sortDisplay(allRecipes);
    }

    public void recipeSortNewToOld() {
        Collections.sort(allRecipes, new NewToOldComparator(db_dir));
        sortDisplay(allRecipes);
    }

    public void recipeSortOldToNew() {
        Collections.sort(allRecipes, new OldToNewComparator(db_dir));
        sortDisplay(allRecipes);
    }

    // refreshes sort based on current sort type
    public void resortRecipes() {
        switch (sortType) {
            case "A - Z":
                recipeSortA2Z();
                break;
            
            case "Z - A":
                recipeSortZ2A();
                break;

            case "Newest to Oldest":
                recipeSortNewToOld();
                break;

            case "Oldest to Newest":
                recipeSortOldToNew();
                break;
        
            default:
                break;
        }
    }
}

class ZtoAComparator implements Comparator<Recipe> { 
  
    // override the compare() method 
    public int compare(Recipe r1, Recipe r2) 
    { 
        return r2.getRecipeName().compareTo(r1.getRecipeName());
    } 
}

class AtoZComparator implements Comparator<Recipe> { 
  
    // override the compare() method 
    public int compare(Recipe r1, Recipe r2) 
    { 
        return r1.getRecipeName().compareTo(r2.getRecipeName());
    } 
}

class NewToOldComparator implements Comparator<Recipe> { 
    String db_dir;
    NewToOldComparator(String db_dir){
        this.db_dir = db_dir;
    }
    // override the compare() method 
    public int compare(Recipe r1, Recipe r2) 
    {
        return r2.getDate().compareTo(r1.getDate());
    } 
}

class OldToNewComparator implements Comparator<Recipe> { 
    String db_dir;
    OldToNewComparator(String db_dir){
        this.db_dir = db_dir;
    }
    // override the compare() method 
    public int compare(Recipe r1, Recipe r2) 
    {
        return r1.getDate().compareTo(r2.getDate());
    } 

    
    
}
