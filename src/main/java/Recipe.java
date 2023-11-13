package src.main.java;

import java.io.*;
import java.util.*;

import javafx.beans.binding.StringBinding;
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
import javafx.application.Platform;

public class Recipe extends HBox {

    private String recipeName;
    private Text text;
    private RecipeDetails recipeDetails;
    private ArrayList<Recipe> recipeArray;
    
    private boolean isSelected;    

    public Recipe(RecipeDetails _recipeDetails) {
    	recipeDetails = _recipeDetails;
    	
    	this.setPrefSize(500, 40); // sets size of recipe
        this.setStyle("-fx-background-color: #266024; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of recipe
        isSelected = false;

        text = new Text(); // create recipe name text field
        text.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        text.setStyle("-fx-background-color: #266024; -fx-border-width: 0;"); // set background color of texfield
        text.setTextAlignment(TextAlignment.CENTER); // set alignment of text field
        text.setFill(Color.WHITE);
        this.getChildren().add(text); // add textlabel to recipe
        
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
        	toggleSelect();
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

    	if (recipeName.length() > 20) {
            text.setText(recipeName.substring(0,20) + "...");
        } 
    	else {
            text.setText(recipeName);
        }
    }
    
    public boolean isSelected() {
        return this.isSelected;
    }

    public void toggleSelect() {
        
        if (!this.isSelected) {
            isSelected = true;

            this.setStyle("-fx-background-color: #66b3ff; -fx-border-width: 0; -fx-font-weight: bold;");

            // System.out.println("The current recpie array:");
            // System.out.println(recipeArray);
            for (int i = 0; i < recipeArray.size(); i++) {
            	if (recipeArray.get(i).isSelected() && recipeArray.get(i) != this) {
            		recipeArray.get(i).toggleSelect();
            	}
            }

            System.out.println("Current recipe name: " + this.getRecipeName());
            recipeDetails.showDetails(this.getRecipeName());

        } else {
            isSelected = false;

            this.setStyle("-fx-background-color: #266024; -fx-border-width: 0; -fx-font-weight: bold;"); 
            recipeDetails.defaultView();
        }
    }

    public void updateRecipeArray(ArrayList<Recipe> arry_input) {
        recipeArray = arry_input;
    }

    public String getDetails (String name, String whichDetail) {
		File file = new File("localDB/" + name + ".txt");
	    BufferedReader br = null;
	    try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	    
	    String mealName = "";
        String mealType = "";
        String mealIngred = "";
        String mealDirections = "";
        
	    try {			
			mealName = br.readLine();
            mealType = br.readLine();
            mealIngred = br.readLine();
            
            // empty space
            br.readLine();
            
            String line = br.readLine();;
            while (line != null) {
				mealDirections += line + "\n";
				line = br.readLine();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        
        try {
            br.close();
        } catch (IOException e) {
            
            System.out.println("Closing buffered Reader Failed: (RecipeList.getDetails)");
            e.printStackTrace();
        }
        
        if(whichDetail == "name")
        	return mealName;
        else if(whichDetail == "type")
        	return mealType;
        else if(whichDetail == "ingredients")
        	return mealIngred;
        else if(whichDetail == "directions")
        	return mealDirections;
        else
        	return "Unknown Detail";
        
	}
}

class ActionsList extends HBox {
    private Button newRecipeButton;
    private Button editRecipeButton;
    private Button deleteRecipeButton;
    
    ActionsList() {
        this.setPrefSize(300, 50);
        this.setSpacing(15);
        this.setStyle("-fx-background-color: #996600;");

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        newRecipeButton = new Button("New Recipe");
        newRecipeButton.setStyle(defaultButtonStyle);
        editRecipeButton = new Button("Edit Recipe");
        editRecipeButton.setStyle(defaultButtonStyle);
        deleteRecipeButton = new Button("Delete Recipe");
        deleteRecipeButton.setStyle(defaultButtonStyle);

        this.getChildren().setAll(newRecipeButton, editRecipeButton, deleteRecipeButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getNewRecipeButton() {
        return newRecipeButton;
    }
    public Button getEditRecipeButton() {
        return editRecipeButton;
    }
    public Button getDeleteRecipeButton() {
        return deleteRecipeButton;
    }
}




		

class RecipeDetails extends VBox {		

	private Text titleText;
	private Text displayType;
	private Text displayIngredients;
	private Text displayDirections;
    private String db_dir = "localDB/";
	
	
				

	public RecipeDetails (Optional<String> recipeName) {				
        String currDisplay = recipeName.orElse("Default");
        this.setPrefSize(900, 60);
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
		File file = new File(db_dir + recipeName + ".txt");
	 
	    BufferedReader br = null;
	    try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println("Opening bufferedReader failed: (RecipeDetails.showDetails)");
			e.printStackTrace();
		}
	 
	    try {

			String mealName = br.readLine();
            String mealType = br.readLine();
            String mealIngred = br.readLine();

			titleText.setText(mealName);
            titleText.setWrappingWidth(700);

			displayType.setText(mealType + "\n");
            displayType.setWrappingWidth(500);
			
			displayIngredients.setText(mealIngred + "\n");
            displayIngredients.setWrappingWidth(500);
            int c;
            StringBuilder parsedDirections= new StringBuilder();

            while ((c = br.read()) != -1) {
                parsedDirections.append( (char)c ) ;  
            }
            String directionString = parsedDirections.toString();
			displayDirections.setText(directionString);
            displayDirections.setWrappingWidth(500);
            this.setPadding(new Insets(10, 0, 10, 0));

            System.out.println("RecipeDetails texts are successfully set");
				
		} catch (IOException e) {
				
            System.out.println("bufferedReader read line failed: (RecipeDetails.showDetails)");
			e.printStackTrace();
		}

        try {
            br.close();
        } catch (IOException e) {
            
            System.out.println("Closing bufferedReader failed: (RecipeDetails.showDetails)");
			e.printStackTrace();
		}

        try {
            br.close();
        } catch (IOException e) {
            
            System.out.println("Closing bufferedReader failed: (RecipeDetails.showDetails)");
            e.printStackTrace();
        }
	}
	
	public void defaultView () {
		titleText.setText("Choose a recipe");
		displayType.setText(" ");
		displayIngredients.setText(" ");
		displayDirections.setText(" ");
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

    public void setDisplayType (String mealType) {
        titleText.setText(mealType);
	}
	
	public void setTitleText (String mealName) {
		titleText.setText(mealName);		
	}
	
	public void setDisplayIngredients (String mealIngred) {
		displayIngredients.setText(mealIngred);
	}
	
	public void setDisplayDirections (String mealDirection) {
		displayDirections.setText(mealDirection);
	}

}