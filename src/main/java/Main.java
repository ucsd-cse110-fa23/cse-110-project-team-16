//package src.main.java;

import java.util.ArrayList;

import java.util.Optional;
import java.util.*;


import org.bson.types.ObjectId;

import java.io.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
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
    private CheckMenuItem sortAtoZ;
    private CheckMenuItem sortZtoA;
    private CheckMenuItem sortNewToOld;
    private CheckMenuItem sortOldToNew;
    private ComboBox filterBox;
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
        filterBox = actionsList.getFilterBox();
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
            if (recipeDetails.getCurrRecipe() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Choose a Recipe");
                alert.setHeaderText("Choose a Recipe");
                alert.showAndWait();
            }
            else {
                // Edit a new recipe
                EditFrame root = new EditFrame(recipeList, recipeDetails, allRecipes, true);

                Stage stage = new Stage();
                stage.setTitle("Edit Recipe");
                stage.setScene(new Scene(root, 450, 450));
                stage.show();
            }
        });

    	deleteRecipeButton.setOnAction(e -> {
            if (recipeDetails.getCurrRecipe() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Choose a Recipe");
                alert.setHeaderText("Choose a Recipe");
                alert.showAndWait();
                return;
            }

            // Delete all toggled recipes
    		for (int i = 0; i < allRecipes.size(); i++) {
    			if (allRecipes.get(i).isSelected()) {
    				recipeList.getChildren().remove(allRecipes.get(i));
                    // delete txt file

                    String recipeName = allRecipes.get(i).getRecipeName();
                    ObjectId recipeID = allRecipes.get(i).getRecipeID();
                    String deletedFileName = db_dir + recipeName + ".txt";
                    File deletedFile = new File(deletedFileName);
                    deletedFile.delete();
                    System.out.println("Deleted this file: " + deletedFileName);

                    // delete jpg file
                    String image = "images/" + recipeName + ".jpg";
                    File imageFile = new File(image);
                    imageFile.delete();
                    System.out.println("Deleted this file: " + imageFile);
                    
    				allRecipes.remove(i);

                    // delete recipe on mongoDB
                    MongoDB.deleteRecipe(recipeID);
    			}
    		}
    		recipeDetails.defaultView();
        });
        
        sortAtoZ.setOnAction(e -> {
            String name = "A - Z";
            // sortMenuButton.setText(name);
            recipeList.setSortType(name);       
            
            actionsList.uncheckOtherItems(sortAtoZ);
            recipeList.recipeSortA2Z();
        });

        sortZtoA.setOnAction(e -> {
            String name = "Z - A";
            // sortMenuButton.setText(name);
            recipeList.setSortType(name);       
            
            actionsList.uncheckOtherItems(sortZtoA);
            recipeList.recipeSortZ2A();
        });

        sortNewToOld.setOnAction(e -> {
            String name = "Newest to Oldest";
            // sortMenuButton.setText(name);
            recipeList.setSortType(name);  

            actionsList.uncheckOtherItems(sortNewToOld);
            recipeList.recipeSortNewToOld();
        });

        sortOldToNew.setOnAction(e -> {
            String name = "Oldest to Newest";
            // sortMenuButton.setText(name);
            recipeList.setSortType(name);  

            actionsList.uncheckOtherItems(sortOldToNew);
            recipeList.recipeSortOldToNew();
        }); 
        
        // Filter button functionality
    	filterBox.setOnAction(e -> {
            // Set Filter Type
    		recipeList.setFilterType(filterBox.getValue().toString());
    		recipeList.loadRecipesMongo();
        });
    }
}
