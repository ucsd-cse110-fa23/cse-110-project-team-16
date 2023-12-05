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

public class RecipeListMock extends VBox {
	
	// private ActionsList actionsList;
    private String db_dir = "localDB/";
    private RecipeDetails localRecipeDetails;
    private ArrayList<Recipe> allRecipes;
    private String filterType;
    private String sortType = "default";


    public RecipeListMock(RecipeDetails details, ArrayList<Recipe> recipeArray) {
    	this.setSpacing(5); // sets spacing between recipes
        this.setPrefSize(300, 560);
        this.setStyle("-fx-background-color: #559952;");
 
        localRecipeDetails = details;
        allRecipes = recipeArray;
        filterType = "All";        
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
