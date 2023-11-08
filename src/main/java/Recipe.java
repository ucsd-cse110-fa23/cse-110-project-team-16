package src.main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color; 

public class Recipe extends HBox {

    private String recipeName;
    private Text text;
    private RecipeDetails recipeDetails;
    private ArrayList<Recipe> allRecipes;
    
    private boolean isSelected;

    public Recipe(RecipeDetails _recipeDetails, ArrayList<Recipe> _allRecipes) {
    	recipeDetails = _recipeDetails;
    	allRecipes = _allRecipes;
    	
    	this.setPrefSize(500, 40); // sets size of task
        this.setStyle("-fx-background-color: #266024; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of task
        isSelected = false;

        text = new Text(); // create task name text field
        text.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        text.setStyle("-fx-background-color: #266024; -fx-border-width: 0;"); // set background color of texfield
        text.setTextAlignment(TextAlignment.CENTER); // set alignment of text field
        text.setFill(Color.WHITE);
        this.getChildren().add(text); // add textlabel to task
        
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
        	Select();
        	event.consume();
        });

    }


    public String getRecipeName() {
        return this.recipeName;
    }
    
    public void setRecipeName(String name) {
        recipeName = name;
    }
    
    public void updateText() {
    	text.setText(recipeName);
    }
    
    public boolean isSelected() {
        return this.isSelected;
    }
    
    public void Select() {
    	for(Recipe _recipe: allRecipes)
    		_recipe.Select(false);
    	Select(true);
    }

    private void Select(boolean toSelect) {
        
        if (toSelect) {
            isSelected = true;
            this.setStyle("-fx-background-color: #3AA037; -fx-border-width: 0; -fx-font-weight: bold;");
            recipeDetails.showDetails(this.getRecipeName());

        } else {
            isSelected = false;
            this.setStyle("-fx-background-color: #266024; -fx-border-width: 0; -fx-font-weight: bold;");     
        }
    }

}


class RecipeList extends VBox {
	
	private ActionsList actionsList;	

    RecipeList() {
    	this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(300, 560);
        this.setStyle("-fx-background-color: #559952;");
        
        actionsList = new ActionsList();
        this.getChildren().add(actionsList);
    }
    
    public Button getNewRecipeButton() {
        return actionsList.getNewRecipeButton();
    }
    public Button getEditRecipeButton() {
        return actionsList.getEditRecipeButton();
    }
}

class ActionsList extends HBox {
    private Button newRecipeButton;
    private Button editRecipeButton;
    
    ActionsList() {
        this.setPrefSize(300, 50);
        this.setSpacing(15);
        this.setStyle("-fx-background-color: #2B6429;");

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        newRecipeButton = new Button("New Recipe");
        newRecipeButton.setStyle(defaultButtonStyle);
        editRecipeButton = new Button("Edit Recipe");
        editRecipeButton.setStyle(defaultButtonStyle);

        this.getChildren().setAll(newRecipeButton, editRecipeButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getNewRecipeButton() {
        return newRecipeButton;
    }
    public Button getEditRecipeButton() {
        return editRecipeButton;
    }
}


class RecipeDetails extends VBox {
	
	private Text titleText;
	private Text displayType;
	private Text displayIngredients;
	private Text displayDirections;
	
	
	public RecipeDetails (Optional<String> recipeName) {
        String currDisplay = recipeName.orElse("Default");
        this.setPrefSize(900, 60); // Size of the header
        this.setStyle("-fx-background-color: #BCE29E;");

        if (currDisplay == "Default") {
            titleText = new Text("Choose a recipe"); // Text of the Header
            titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
            this.getChildren().add(titleText);
            this.setAlignment(Pos.CENTER); // Align the text to the Center
            
            displayType = new Text(" ");
            displayType.setFont(Font.font("Arial", 14));

            displayIngredients = new Text(" ");
            displayIngredients.setFont(Font.font("Arial", 14));

            displayDirections = new Text(" ");
            displayDirections.setFont(Font.font("Arial", 14));

            this.getChildren().addAll(displayType, displayIngredients, displayDirections);
            this.setAlignment(Pos.CENTER); // Align the text to the Center
            
        } else {
            titleText = new Text(currDisplay); // Text of the Header
            titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
            this.getChildren().add(titleText);
            
            displayType = new Text(currDisplay);
            displayType.setFont(Font.font("Arial", 14));

            displayIngredients = new Text(currDisplay);
            displayIngredients.setFont(Font.font("Arial", 14));

            displayDirections = new Text(currDisplay);
            displayDirections.setFont(Font.font("Arial", 14));


            this.getChildren().addAll(displayType, displayIngredients, displayDirections);
            this.setAlignment(Pos.CENTER); // Align the text to the Center
        }
    }
	
	public void showDetails (String recipeName) {
		File file = new File("localDB/" + recipeName + ".txt");
	 
	    BufferedReader br = null;
	    try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	 
	    try {
			titleText.setText(br.readLine());
			displayType.setText(br.readLine());
			displayIngredients.setText(br.readLine());
			displayDirections.setText(br.readLine());
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Text getDisplayType () {
		return displayType;
	}
	
	public Text getTitleText () {
		return titleText;
	}
	
	public Text getDisplayIngredients () {
		return displayIngredients;
	}
	
	public Text getDisplayDirections () {
		return displayDirections;
	}
}