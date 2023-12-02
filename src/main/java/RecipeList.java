//package src.main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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
        // String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        
        // actionsList = new ActionsList();
        // this.getChildren().add(actionsList);
        localRecipeDetails = details;
        allRecipes = recipeArray;
        filterType = "All";
        loadRecipesMongo();
        //this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
        //	changeRecipeSelect();
        //	event.consume();
        //});
    }
    
    //* Adds recipes from local database to recipeList*/
    public void loadRecipes() {
        Set<String> recipeFiles = listRecipeFiles(db_dir);
        // System.out.println("Current recipe files in db:");
        
        for (String file: recipeFiles) {            
            Recipe currRecipe = null;
            String currName = file.substring(0, file.length() - 4);
                        
            // System.out.println(currName);
            ArrayList<String> currMealParams = getDetails(currName);
            // System.out.println(currMealParams);

            currRecipe = new Recipe(localRecipeDetails);
            currRecipe.setRecipeName(currName);
            currRecipe.updateText();

            this.getChildren().add(currRecipe);
            allRecipes.add(currRecipe);
            currRecipe.updateRecipeArray(allRecipes);
        }
    }

    public void loadRecipesMongo() {
    	for (int i = 0; i < allRecipes.size(); i++) {
    		this.getChildren().remove(allRecipes.get(i));
    	}
    	allRecipes.clear();
    	
        Set<Document> recipes = listRecipesMongo();

        for (Document recipe: recipes) {
            Recipe currRecipe = null;
            String name = (String)recipe.get("name");
            String type = (String)recipe.get("type");

            if (!filterType.equals("All")) {
	            if (!(filterType.equals(type))) {
	            	continue;
	            }
            }

            currRecipe = new Recipe(localRecipeDetails);
            currRecipe.setRecipeName(name);
            currRecipe.setRecipeType(type);
            currRecipe.updateText();

            this.getChildren().add(currRecipe);
            allRecipes.add(currRecipe);
            currRecipe.updateRecipeArray(allRecipes);
        }
    }

    public Set<Document> listRecipesMongo() {
        try (MongoClient mongoClient = MongoClients.create(MongoDB.getURI())) {   	
    		MongoDatabase recipesDB = mongoClient.getDatabase("Recipes");
        	MongoCollection<Document> userCollection = recipesDB.getCollection(LoginFrame.getUser());
            FindIterable<Document> documentCursor = userCollection.find();
			
            Set<Document> recipeFiles = new HashSet<Document> ();

            // Iterate over collection
            for(Document doc : documentCursor)
                recipeFiles.add(doc);

            return recipeFiles;
		}
    }

    private Set<String> listRecipeFiles(String db_dir) {
        Set<String> recipeFiles = new HashSet<String> ();
        
        File recipeDir = new File(db_dir);
        String[] filesArray = recipeDir.list();
        
        for (String file: filesArray) {
            recipeFiles.add(file);
        }

        return recipeFiles;
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