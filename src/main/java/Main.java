package src.main.java;

import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

// Main Method - Runs application
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the RecipeList
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("Recipes v1");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 1200, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// Application - using JavaFX
class AppFrame extends BorderPane{

    private RecipeDetails recipeDetails;
    private RecipeList recipeList;
    private ArrayList<Recipe> allRecipes;
    private Button newRecipeButton;
    private Button editRecipeButton;

    AppFrame()
    {
        // Initialise the header Object
    	recipeDetails = new RecipeDetails(Optional.empty());
    	
    	allRecipes = new ArrayList<Recipe>();

        // Create a recipelist Object to hold the recipes
        recipeList = new RecipeList();

        this.setRight(recipeDetails);
        // Add scroller to the centre of the BorderPane
        this.setLeft(recipeList);
        newRecipeButton = recipeList.getNewRecipeButton();
        editRecipeButton = recipeList.getEditRecipeButton();
        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {

        // Add button functionality
    	newRecipeButton.setOnAction(e -> {
            // Create a new recipe
    		EditFrame root = new EditFrame(recipeList, recipeDetails, allRecipes, false);

            Stage stage = new Stage();
            stage.setTitle("Create New Recipe");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            
        });
        
    	editRecipeButton.setOnAction(e -> {
            // Edit a new recipe
    		EditFrame root = new EditFrame(recipeList, recipeDetails, allRecipes, true);

            Stage stage = new Stage();
            stage.setTitle("Edit Recipe");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            
        });
    	
    }
}


