//package src.main.java;


import java.util.ArrayList;
import java.util.Optional;
import java.util.*;
import java.io.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

// Main Method - Runs application
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the RecipeList
        //AppFrame root = new AppFrame();
        LoginFrame root = new LoginFrame(primaryStage);
        // Set the title of the app
        primaryStage.setTitle("Login");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 450, 200));
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
    private Button deleteRecipeButton;
    private MenuButton sortMenuButton;
    private MenuItem sortAtoZ;
    private MenuItem sortZtoA;
    private MenuItem sortNewToOld;
    private MenuItem sortOldToNew;
    private ScrollPane scrollPane;
    private ActionsList actionsList;
    private String db_dir = "localDB/";

    AppFrame()
    {
    	
    	allRecipes = new ArrayList<Recipe>();
    	
        // Initialise the header Object
    	recipeDetails = new RecipeDetails();

        // Create a recipelist Object to hold the recipes
        recipeList = new RecipeList(recipeDetails, allRecipes);

        actionsList = new ActionsList();
        scrollPane = new ScrollPane(recipeList);
        // scrollPane.setMaxHeight(500.0);
        scrollPane.setFitToHeight(true);


                
        this.setTop(actionsList);
                
        this.setRight(recipeDetails);
        // Add scroller to the centre of the BorderPane
        
        // this.setLeft(recipeList);
        this.setLeft(scrollPane);
        // newRecipeButton = recipeList.getNewRecipeButton();
        // editRecipeButton = recipeList.getEditRecipeButton();
        // deleteRecipeButton = recipeList.getDeleteRecipeButton();

        newRecipeButton = actionsList.getNewRecipeButton();
        editRecipeButton = actionsList.getEditRecipeButton();
        deleteRecipeButton = actionsList.getDeleteRecipeButton();
        sortMenuButton = actionsList.getSortMenuButton();
        sortAtoZ = actionsList.getSortAtoZ();
        sortZtoA = actionsList.getSortZtoA();
        sortNewToOld = actionsList.getSortNewToOld();
        sortOldToNew = actionsList.getSortOldToNew();
        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {

        // Add button functionality
    	newRecipeButton.setOnAction(e -> {
            // Create a new recipe
    		CreationFrame root = new CreationFrame(recipeList, allRecipes, recipeDetails);

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

    	deleteRecipeButton.setOnAction(e -> {
            // Delete all toggled recipes
    		for (int i = 0; i < allRecipes.size(); i++) {
    			if (allRecipes.get(i).isSelected()) {
    				recipeList.getChildren().remove(allRecipes.get(i));
                    // delete txt file
                    String deletedFileName = db_dir + allRecipes.get(i).getRecipeName() + ".txt";
                    File deletedFile = new File(deletedFileName);
                    deletedFile.delete();
                    System.out.println("Deleted this file: " + deletedFileName);

                    // delete jpg file
                    String image = "images/" + allRecipes.get(i).getRecipeName() + ".jpg";
                    File imageFile = new File(image);
                    imageFile.delete();
                    System.out.println("Deleted this file: " + imageFile);
                    
    				allRecipes.remove(i);
    			}
    		}
    		recipeDetails.defaultView();
        });
        
        sortAtoZ.setOnAction(e -> {
            // System.out.println("Sorting A to Z is called");
            // sortMenuButton.setText("A-Z");
            
            Collections.sort(allRecipes, new AtoZComparator());
            recipeList.sortDisplay(allRecipes);
            
            // System.out.println(allRecipes);
            // System.out.println("----------");
        });

        sortZtoA.setOnAction(e -> {
            // System.out.println("Sorting Z to A is called");
            // sortMenuButton.setText("Z-A");

            Collections.sort(allRecipes, new ZtoAComparator());
            recipeList.sortDisplay(allRecipes);
            
            // System.out.println(allRecipes);
            // System.out.println("----------");
        });

        sortNewToOld.setOnAction(e -> {
            // System.out.println("Sorting Newest to Oldest is called");
            // sortMenuButton.setText("Newest to Oldest");

            Collections.sort(allRecipes, new NewToOldComparator(db_dir));
            recipeList.sortDisplay(allRecipes);
        });

        sortOldToNew.setOnAction(e -> {
            // System.out.println("Sorting Oldest to Newest is called");
            // sortMenuButton.setText("Oldest to Newest");
            Collections.sort(allRecipes, new OldToNewComparator(db_dir));
            recipeList.sortDisplay(allRecipes);
        });    	
    }
}

class AtoZComparator implements Comparator<Recipe> { 
  
    // override the compare() method 
    public int compare(Recipe r1, Recipe r2) 
    { 
        return r2.getRecipeName().compareTo(r1.getRecipeName());
    } 
}

class ZtoAComparator implements Comparator<Recipe> { 
  
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
        File file1 = new File(db_dir + r1.getRecipeName() + ".txt");
        File file2 = new File(db_dir + r2.getRecipeName() + ".txt");
        if (file1.lastModified() < file2.lastModified()) {
            return 1;
        }

        return -1;
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
        File file1 = new File(db_dir + r1.getRecipeName() + ".txt");
        File file2 = new File(db_dir + r2.getRecipeName() + ".txt");
        if (file1.lastModified() < file2.lastModified()) {
            return -1;
        }

        return 1;
    } 
}