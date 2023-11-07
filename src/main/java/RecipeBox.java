package src.main.java;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

class EditFrame extends BorderPane {
	private Button saveButton;
	private Button cancelButton;
	private Footer footer;
	private RecipeBox recipes;
   
	private RecipeList recipeList;
	
    EditFrame(RecipeList _recipelist)
    {
    	recipeList = _recipelist;
    	footer=new Footer();
    	recipes=new RecipeBox();
    	this.setCenter(recipes);
    	this.setBottom(footer);
    	saveButton=footer.getSaveButton();
    	cancelButton=footer.getCancelButton();
        addListeners();
    }

    void addListeners()
    {
    	// Add button functionality
    	saveButton.setOnAction(e -> {
            // Create a new recipe
            Recipe recipe = new Recipe();
            recipe.setRecipeName(recipes.getRecipeName());
            // Add recipe to recipelist
            recipeList.getChildren().add(recipe);
            // Update recipe indices
            recipeList.updateRecipeIndices();
        });
    }
}

public class RecipeBox extends HBox {
	
	 private TextField recipeName;
	 private TextField ingredients;
   
	 public RecipeBox() {
		this.setSpacing(5); // sets spacing between recipes
		this.setPrefSize(400, 560);
		//this.setStyle("-fx-background-color: #FFFF00;");
		String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
       
		recipeName = new TextField(); // create recipe name text field
       	recipeName.setPrefSize(380, 20); // set size of text field
       	recipeName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
       	recipeName.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
       	this.getChildren().add(recipeName); // add textlabel to recipe
       
       	ingredients = new TextField(); // create recipe name text field
       	ingredients.setPrefSize(500, 200); // set size of text field
       	ingredients.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
       	ingredients.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
       	this.getChildren().add(ingredients);
	}
	
	public String getRecipeName() {
       return this.recipeName.getText();
	}

	public void setRecipeName(String _recipeName) {
       this.recipeName.setText(_recipeName);
	}
	
	public String getIngredients() {
       return this.ingredients.getText();
	}
	
	public void setIngredients(String _ingredients) {
       this.ingredients.setText(_ingredients);
	}
}

// Footer of EditFrame - contains save and cancel buttons
class Footer extends HBox {
	
    private Button saveButton;
    private Button cancelButton;

    Footer() {
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