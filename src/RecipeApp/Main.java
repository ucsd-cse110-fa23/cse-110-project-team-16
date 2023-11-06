package RecipeApp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Recipe extends HBox {

    private Label index;
    private TextField recipeName;
    private Button selectButton;

    private boolean isSelected;

    Recipe() {
        this.setPrefSize(500, 20); // sets size of task
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of task
        isSelected = false;

        index = new Label();
        index.setText(""); // create index label
        index.setPrefSize(40, 20); // set size of Index label
        index.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        index.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the task
        this.getChildren().add(index); // add index label to task

        recipeName = new TextField(); // create task name text field
        recipeName.setPrefSize(380, 20); // set size of text field
        recipeName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        index.setTextAlignment(TextAlignment.LEFT); // set alignment of text field
        recipeName.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        this.getChildren().add(recipeName); // add textlabel to task

        selectButton = new Button("Done"); // creates a button for marking the task as done
        selectButton.setPrefSize(100, 20);
        selectButton.setPrefHeight(Double.MAX_VALUE);
        selectButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // sets style of button

        this.getChildren().add(selectButton);
    }

    public void setRecipeIndex(int num) {
        this.index.setText(num + ""); // num to String
        this.recipeName.setPromptText("Task " + num);
    }

    public TextField getrecipeName() {
        return this.recipeName;
    }

    public Button getSelectButton() {
        return this.selectButton;
    }

    public boolean isisSelected() {
        return this.isSelected;
    }

    public void toggleSelect() {
        
        if (!this.isSelected) {
            isSelected = true;
            this.setStyle("-fx-border-color: #000000; -fx-border-width: 0; -fx-font-weight: bold;"); // remove border of task
            for (int i = 0; i < this.getChildren().size(); i++) {
                this.getChildren().get(i).setStyle("-fx-background-color: #BCE29E; -fx-border-width: 0;"); // change color of task to green
            }
        } else {
            isSelected = false;
            for (int i = 0; i < this.getChildren().size(); i++) {
                this.getChildren().get(i).setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");; // sets background color of task
            }        
        }
    }
}

class RecipeFunctions extends HBox {

    private Button addButton;    
    // TODO: Add a button called "loadButton" to load tasks from file
    // TODO: Add a button called "saveButton" to save tasks to a file
    // TODO: Add a button called "sortButton" to sort the tasks lexicographically
    private Button loadButton;
    private Button saveButton;
    private Button sortButton;

    RecipeFunctions() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("Add Recipe"); // text displayed on add button
        addButton.setStyle(defaultButtonStyle); // styling the button

        // TODO: Create loadButton, saveButton and sortButton to the recipeFunctions
        loadButton = new Button("Load Recipes");
        loadButton.setStyle(defaultButtonStyle);
        saveButton = new Button("Save Recipe");
        saveButton.setStyle(defaultButtonStyle);
        sortButton = new Button("Sort Recipes");
        sortButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(addButton, loadButton, saveButton, sortButton); // adding buttons to recipeFunctions
        this.setAlignment(Pos.CENTER); // aligning the buttons to center        
    }

    public Button getAddButton() {
        return addButton;
    }

    // TODO: Add getters for loadButton, saveButton and sortButton

    public Button getLoadButton() {
        return loadButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getSortButton() {
        return sortButton;
    }
}


class RecipeList extends VBox {

    RecipeList() {
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void updateRecipeIndices() {
        int index = 1;
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Recipe) {
                ((Recipe) this.getChildren().get(i)).setRecipeIndex(index);
                index++;
            }
        }
    }

    public void removeCompletedTasks() {
        this.getChildren().removeIf(recipe -> recipe instanceof Recipe && ((Recipe) recipe).isisSelected());
        this.updateRecipeIndices();
    }

    public void loadRecipes() {
        // hint 1: use try-catch block
        // hint 2: use BufferedReader and FileReader
        // hint 3: task.getrecipeName().setText() sets the text of the task

        try (BufferedReader tasksReader = new BufferedReader(new FileReader("src/recipes.txt"))) {
            
            while (tasksReader.ready()) {
                Recipe recipe = new Recipe();
                recipe.getrecipeName().setText(tasksReader.readLine());
                // Add task to tasklist
                this.getChildren().add(recipe);
                // Add selectButtonToggle to the Done button
                Button selectButton = recipe.getSelectButton();
                selectButton.setOnAction(e1 -> {
                    // Call toggleDone on click
                    recipe.toggleSelect();
                });
                
            }
            
            // Update task indices
            this.updateRecipeIndices();
            System.out.println("loadRecipes() was successful!");

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Something went wrong with loadTasks()");
        }
    }

    public void saveRecipe() {

        try (FileWriter tasksWriter = new FileWriter("src/recipes.txt", false)){
            for (int i = 0; i < this.getChildren().size(); ++i){
                if (this.getChildren().get(i) instanceof Recipe) {
                    tasksWriter.write((((Recipe) this.getChildren().get(i)).getrecipeName()).getText()); 
                    tasksWriter.write("\n");
                }
            }
            System.out.println("saveRecipe() was successful!");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Something went wrong with saveRecipe()");
        }
    }

    // TODO: Complete this method
    /*
     * Sort the tasks lexicographically
     */
    public void sortRecipes() {
        // hint 1: this.getChildren() gets the list of tasks
        // hint 2: Collections.sort() can be used to sort the tasks
        // hint 3: task.getrecipeName().setText() sets the text of the task

        try {
            ArrayList<String> newlist = new ArrayList<String>();
            for (int i = 0; i < this.getChildren().size(); ++i){
                if (this.getChildren().get(i) instanceof Recipe) {
                    newlist.add(((Recipe) this.getChildren().get(i)).getrecipeName().getText());                    
                }
            }
            this.getChildren().removeIf(recipe -> recipe instanceof Recipe);
            

            Collections.sort(newlist);
            System.out.println("sortRecipes() is successful!");
            // System.out.println(newlist);

            for (int i = 0; i < newlist.size(); ++i) {
                Recipe recipe = new Recipe();
                recipe.getrecipeName().setText(newlist.get(i));
                // Add task to tasklist
                this.getChildren().add(recipe);
                // Add selectButtonToggle to the Done button
                Button selectButton = recipe.getSelectButton();
                selectButton.setOnAction(e1 -> {
                    // Call toggleDone on click
                    recipe.toggleSelect();
                });
                
            }

            this.updateRecipeIndices();

            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Something went wrong with sortTasks()");
        }
    }
}


class RecipeDetails extends HBox {

    RecipeDetails() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

class AppFrame extends BorderPane{

    private RecipeDetails recipeDetails;
    private RecipeFunctions recipeFunctions;
    private RecipeList recipeList;
    private ScrollPane scrollPane;

    private Button addButton;
    private Button loadButton;
    private Button saveButton;
    private Button sortButton;

    AppFrame()
    {
        // Initialise the header Object
        recipeDetails = new RecipeDetails();

        // Create a tasklist Object to hold the tasks
        recipeList = new RecipeList();
        
        // Initialise the recipeFunctions Object
        recipeFunctions = new RecipeFunctions();

        // TODO: Add a Scroller to the Task List
        // hint 1: ScrollPane() is the Pane Layout used to add a scroller - it will take the tasklist as a parameter
        // hint 2: setFitToWidth, and setFitToHeight attributes are used for setting width and height
        // hint 3: The center of the AppFrame layout should be the scroller window instead  of tasklist

        scrollPane = new ScrollPane(recipeList);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        // Add recipeFunctions to the bottom of the BorderPane
        this.setLeft(recipeList);

        // Add header to the top of the BorderPane
        this.setRight(recipeDetails);
        
        // Add scroller to the centre of the BorderPane
        // this.setLeft(scrollPane);
        

        // Initialise Button Variables through the getters in recipeFunctions
        addButton = recipeFunctions.getAddButton();
        loadButton = recipeFunctions.getLoadButton();
        saveButton = recipeFunctions.getSaveButton();
        sortButton = recipeFunctions.getSortButton();

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {

        // Add button functionality
        addButton.setOnAction(e -> {
            // Create a new task
            Recipe recipe = new Recipe();
            // Add task to tasklist
            recipeList.getChildren().add(recipe);
            // Add selectButtonToggle to the Done button
            Button selectButton = recipe.getSelectButton();
            selectButton.setOnAction(e1 -> {
                // Call toggleDone on click
                recipe.toggleSelect();
            });
            // Update task indices
            recipeList.updateRecipeIndices();
        });
        

        loadButton.setOnAction(e ->{
            recipeList.loadRecipes();
        });

        saveButton.setOnAction(e -> {
            recipeList.saveRecipe();
        });

        sortButton.setOnAction(e -> {
            recipeList.sortRecipes();
        });
    }
}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, recipeFunctions and the TaskList
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("Recipe Application");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 800, 600));
        // Make window non-resizable
        primaryStage.setResizable(true);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
