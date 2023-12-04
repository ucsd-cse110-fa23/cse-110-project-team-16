//package src.main.java;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.io.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
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
            // Server is not available, display a notification and exit
            System.out.println("Server is not available. Please make sure the server is running.");
        }
    }
    
    private static boolean isServerAvailable(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
                return true;
        } catch (IOException e) {
            return false;
        }
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

                    String recipeName = allRecipes.get(i).getRecipeName();
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
                    deleteRecipeMongo(recipeName);
    			}
    		}
    		recipeDetails.defaultView();
        });
    	
        // Filter button functionality
    	filterBox.setOnAction(e -> {
            // Set Filter Type
    		recipeList.setFilterType(filterBox.getValue().toString());
    		recipeList.loadRecipesMongo();
        });
    	
    }

    private boolean deleteRecipeMongo(String name) {
        try (MongoClient mongoClient = MongoClients.create(MongoDB.getURI())) {
    		MongoDatabase recipesDB = mongoClient.getDatabase("Recipes");
        	MongoCollection<Document> userCollection = recipesDB.getCollection(LoginFrame.getUser());
			Document existingRecipe = userCollection.find(new Document("name", name)).first();

			if (existingRecipe != null) {
                Bson filter = eq("name", name);
                userCollection.deleteOne(filter);

        		return false;
        	}
        	else {
        		return false;            
    		}
		}
    }
}

