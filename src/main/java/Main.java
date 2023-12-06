//package src.main.java;

import java.util.ArrayList;

import java.util.Optional;
import java.util.*;


import org.bson.types.ObjectId;

import java.io.*;

import javafx.application.Application;
import javafx.application.Platform;
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


import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.net.InetAddress;
import java.net.Socket;
import org.bson.conversions.Bson;



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
        if (isServerAvailable("localhost", 8100)) {
            // Server is available, start the application
            launch(args);
        } else {
            showAlert("Server Not Available", "Please make sure the server is running.");
        }
    }
    
    private static boolean isServerAvailable(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
                return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
        
    
}

// Application - using JavaFX
class AppFrame extends BorderPane{

    private RecipeDetails recipeDetails;
    private RecipeList recipeList;
    private ArrayList<Recipe> allRecipes;
    private RecipeManager recipeManager;
    private Button newRecipeButton;
    private Button editRecipeButton;
    private Button deleteRecipeButton;

    private Button shareRecipeButton;


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
        recipeManager = new RecipeManager(allRecipes);

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

        shareRecipeButton = actionsList.getShareRecipeButton();

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
            Recipe selectedRecipe = recipeDetails.getCurrRecipe();
            if (selectedRecipe == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Choose a Recipe");
                alert.setHeaderText("Choose a Recipe");
                alert.showAndWait();
                return;
            }

            recipeManager.deleteRecipe(selectedRecipe);
            recipeList.getChildren().remove(selectedRecipe);
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

        shareRecipeButton.setOnAction(e -> {
            for (int i = 0; i < allRecipes.size(); i++) {
    			if (allRecipes.get(i).isSelected()) {
                    ShareLogic shareLogic = new ShareLogic(allRecipes.get(i).getRecipeName());
                    ShareFrame shareFrame = new ShareFrame(shareLogic);
                    Stage stage = new Stage();
                    stage.setTitle("Share Recipe");
                    stage.setScene(new Scene(shareFrame,400, 100));
                    stage.show();
                }
            }

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
    		// recipeList.loadRecipesMongo();
            recipeList.changeDisplayByType();
        });
    }
}
