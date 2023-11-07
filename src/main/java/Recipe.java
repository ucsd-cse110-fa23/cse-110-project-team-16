package src.main.java;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Recipe extends HBox {

    private Label index;
    private String recipeName;
    private Text text;

    public Recipe() {
        this.setPrefSize(500, 50); // sets size of recipe
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of recipe

        /*
        index = new Label();
        index.setText(""); // create index label
        index.setPrefSize(40, 20); // set size of Index label
        index.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        index.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the recipe
        this.getChildren().add(index); // add index label to recipe
        */

        text = new Text(recipeName);
        //recipeName.setPrefSize(380, 20); // set size of text field
        text.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        
        //recipeName.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        this.getChildren().add(text); // add textlabel to recipe

    }

    public void setRecipeIndex(int num) {
        //this.recipeName.setPromptText("Recipe " + num);
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

}


class RecipeList extends VBox {
	
	private Button newRecipe;
	private Button editRecipe;

    RecipeList() {
    	this.setPadding(new Insets(10, 0, 10, 0)); 
        this.setSpacing(5); // sets spacing between recipes
        this.setPrefSize(150, 560);
        this.setStyle("-fx-background-color: #4B4B4B;");
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        
        // newRecipe button initialization
        newRecipe = new Button("+ New Recipe"); // text displayed on add button
        newRecipe.setStyle(defaultButtonStyle);
        this.getChildren().addAll(newRecipe);
        
        // editRecipe button initialization
        editRecipe = new Button("Edit Recipe"); // text displayed on add button
        editRecipe.setStyle(defaultButtonStyle);
        //this.getChildren().addAll(editRecipe);
        
        // Align buttons in top center
        this.setAlignment(Pos.TOP_CENTER);
    }
    
    public Button getNewRecipeButton() {
        return newRecipe;
    }
    
    public Button getEditRecipeButton() {
        return editRecipe;
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
}


class Header extends HBox {

    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #737373;");
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}