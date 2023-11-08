package src.main.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class EditFrame extends BorderPane {
	private Button saveButton;
	private Button cancelButton;
	private DialogButtons dialogButtons;
	private RecipeBox recipes;
    private ArrayList<Recipe> allRecipes;
	private RecipeList recipeList;
	private RecipeDetails recipeDetails;
	
    EditFrame(RecipeList _recipelist, RecipeDetails _recipeDetails, ArrayList<Recipe> _allRecipes, boolean editMode)
    {
    	recipeList = _recipelist;
    	recipeDetails = _recipeDetails;
    	allRecipes = _allRecipes;
    	dialogButtons = new DialogButtons();
    	
    	if (!editMode)
    		recipes = new RecipeBox();
    	else
    		recipes = new RecipeBox(_recipeDetails);
    	
    	this.setCenter(recipes);
    	this.setBottom(dialogButtons);
    	
    	saveButton = dialogButtons.getSaveButton();
    	cancelButton = dialogButtons.getCancelButton();
    	
        addListeners();
    }

    void addListeners()
    {
    	saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String recipeName = recipes.getRecipeName();
                if (recipeName.length() == 0) {
                    recipeName = "New recipe added";
                }
                String recipeType = recipes.getRecipeType();
                String ingredients = recipes.getIngredients();
                String directions = recipes.getDirections();
                String filename = "localDB/" + recipeName + ".txt";
                
                Recipe recipe = null;
                boolean exists = false;
                
                for(int i = 0; i < allRecipes.size(); i++) {
                	if(allRecipes.get(i).getRecipeName().equals(recipeName)) {
                		exists = true;
                		recipe = allRecipes.get(i);
                	}
                }
                
                if (!exists) {
                	recipe = new Recipe(recipeDetails, allRecipes);
                	allRecipes.add(recipe);
                }
                
                // System.out.println("This is the new recipe name added: " + recipeName);
                recipe.setRecipeName(recipeName);
                recipe.updateText();
                if (!exists)
                	recipeList.getChildren().add(recipe);
            
                // writing to recipes text files
                try {
                    FileWriter writer = new FileWriter(filename);
                    writer.write(recipeName);
                    writer.write("\n");
                    writer.write(recipeType);
                    writer.write("\n");
                    writer.write(ingredients);
                    writer.write("\n");
                    writer.write(directions);

                    writer.close();

                } catch (IOException e) {
                    // TODO: handle exception
                    System.out.println("Error occured when writing to txt file");
                }
                
                recipeDetails.showDetails(recipeName);
                recipe.Select();

                Stage stage = (Stage) getScene().getWindow(); // Get the current stage
                stage.close(); // Close the window
            }
        });
    	
    	cancelButton.setOnAction(e -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.close();
        });
    }
}

class RecipeBox extends VBox {
	
    private TextField recipeName;
    private TextField recipeType;
    private TextField ingredients;
    private TextField directions;
   
    RecipeBox() {
        
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(400, 560);
        //this.setStyle("-fx-background-color: #FFFF00;");
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        recipeName = new TextField();
        recipeName.setPrefSize(380, 20); // set size of text field
        recipeName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        
        recipeName.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        recipeName.setPromptText("Input Recipe Name here");

        recipeType = new TextField();
        recipeType.setPrefSize(380, 20); // set size of text field
        recipeType.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        
        recipeType.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        recipeType.setPromptText("Input Recipe Type here: Breakfast / Lunch / Dinner");
       
        ingredients = new TextField(); // create task name text field
        ingredients.setPrefSize(380, 130); // set size of text field
        ingredients.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        ingredients.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        ingredients.setPromptText("Input Ingredients here");


        directions = new TextField(); // create task name text field
        directions.setPrefSize(380, 150); // set size of text field
        directions.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        directions.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        directions.setPromptText("Input Directions here");

        this.getChildren().addAll(recipeName, recipeType, ingredients, directions);              
    }
    
    RecipeBox(RecipeDetails recipeDetails) {
        
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(400, 560);
        //this.setStyle("-fx-background-color: #FFFF00;");
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        recipeName = new TextField(recipeDetails.getTitleText().getText());
        recipeName.setPrefSize(380, 20); // set size of text field
        recipeName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        
        recipeName.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        recipeName.setPromptText("Input Recipe Name here");

        recipeType = new TextField(recipeDetails.getDisplayType().getText());
        recipeType.setPrefSize(380, 20); // set size of text field
        recipeType.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        
        recipeType.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        recipeType.setPromptText("Input Recipe Type here: Breakfast / Lunch / Dinner");
       
        ingredients = new TextField(recipeDetails.getDisplayIngredients().getText()); // create task name text field
        ingredients.setPrefSize(380, 130); // set size of text field
        ingredients.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        ingredients.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        ingredients.setPromptText("Input Ingredients here");


        directions = new TextField(recipeDetails.getDisplayDirections().getText()); // create task name text field
        directions.setPrefSize(380, 150); // set size of text field
        directions.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        directions.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        directions.setPromptText("Input Directions here");

        this.getChildren().addAll(recipeName, recipeType, ingredients, directions);              
    }

    public String getRecipeName() {
        return this.recipeName.getText();
    }

    public String getRecipeType() {
        return this.recipeType.getText();
    }

    public String getIngredients() {
        return this.ingredients.getText();
    }

    public String getDirections() {
        return this.directions.getText();
    }

   
}

// Footer of EditFrame - contains save and cancel buttons
class DialogButtons extends HBox {
	
    private Button saveButton;
    private Button cancelButton;

    DialogButtons() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        saveButton = new Button("Save"); // text displayed on add button
        saveButton.setStyle(defaultButtonStyle); // styling the button
        cancelButton = new Button("Cancel"); // text displayed on clear recipes button
        cancelButton.setStyle(defaultButtonStyle);
        

        this.getChildren().addAll(saveButton,cancelButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getSaveButton() {
        return saveButton;
    }
    
    public Button getCancelButton() {
        return cancelButton;
    }
}