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

}


class RecipeList extends VBox {
	
	// private ActionsList actionsList;
    private String db_dir = "localDB/";
    private RecipeDetails localRecipeDetails;
    private ArrayList<Recipe> allRecipes;

    RecipeList(RecipeDetails details, ArrayList<Recipe> recipeArray) {
    	this.setSpacing(5); // sets spacing between recipes
        this.setPrefSize(300, 560);
        this.setStyle("-fx-background-color: #559952;");
        // String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        
        // actionsList = new ActionsList();
        // this.getChildren().add(actionsList);
        localRecipeDetails = details;
        allRecipes = recipeArray;
        loadRecipes();
        //this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
        //	changeRecipeSelect();
        //	event.consume();
        //});
    }
    
    //* Adds recipes from local database to recipeList*/
    public void loadRecipes() {
        Set<String> recipeFiles = listRecipeFiles(db_dir);
        // System.out.println("Current recipe files in db:");
        
        for (String file: recipeFiles) {            
            Recipe currRecipe = null;
            String currName = file.substring(0, file.length() - 4);
                        
            // System.out.println(currName);
            ArrayList<String> currMealParams = getDetails(currName);
            // System.out.println(currMealParams);

            currRecipe = new Recipe(localRecipeDetails);
            currRecipe.setRecipeName(currName);
            currRecipe.updateText();

            this.getChildren().add(currRecipe);
            allRecipes.add(currRecipe);
            currRecipe.updateRecipeArray(allRecipes);
        }
    }

    private Set<String> listRecipeFiles(String db_dir) {
        Set<String> recipeFiles = new HashSet<String> ();
        
        File recipeDir = new File(db_dir);
        String[] filesArray = recipeDir.list();
        
        for (String file: filesArray) {
            recipeFiles.add(file);
        }

        return recipeFiles;
    }

    public ArrayList<String> getDetails (String recipeName) {
		File file = new File(db_dir + recipeName + ".txt");
        ArrayList<String> recipeDetails = new ArrayList<String>();
	    BufferedReader br = null;
	    try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	 
	    try {			
			String mealName = br.readLine();
            String mealType = br.readLine();
            String mealIngred = br.readLine();
            String mealDirections = br.readLine();
            recipeDetails.add(mealName);
            recipeDetails.add(mealType);
            recipeDetails.add(mealIngred);
            recipeDetails.add(mealDirections);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        
        try {
            br.close();
        } catch (IOException e) {
            
            System.out.println("Closing buffered Reader Failed: (RecipeList.getDetails)");
            e.printStackTrace();
        }
        

        return recipeDetails;
	}

    public void changeRecipeSelect() {
            // Unselect other selected recipes
            for (int i = 0; i < allRecipes.size(); i++) {
            	if (allRecipes.get(i).isSelected()) {
            		allRecipes.get(i).toggleSelect();
            	}
            }
    }
    
    public ArrayList<Recipe> getAllRecipes () {
		return allRecipes;
	}

    // public Button getNewRecipeButton() {
    //     return actionsList.getNewRecipeButton();
    // }
    // public Button getEditRecipeButton() {
    //     return actionsList.getEditRecipeButton();
    // }
    // public Button getDeleteRecipeButton() {
    //     return actionsList.getDeleteRecipeButton();
    // }
}

class ActionsList extends HBox {
    private Button newRecipeButton;
    private Button editRecipeButton;
    private Button deleteRecipeButton;
    
    ActionsList() {
        this.setPrefSize(300, 50);
        this.setSpacing(15);
        this.setStyle("-fx-background-color: #2B6429;");

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
            String mealDirections = br.readLine();            

			titleText.setText(mealName);
            titleText.setWrappingWidth(600);

			displayType.setText(mealType + "\n");
            displayType.setWrappingWidth(400);
			
            displayIngredients.setText(mealIngred + "\n");
            displayIngredients.setWrappingWidth(400);
			
            displayDirections.setText(mealDirections);
            displayDirections.setWrappingWidth(400);
            this.setPadding(new Insets(10, 0, 10, 0));

            // Platform.runLater(() -> {
            //     titleText.setText(mealName);
			//     displayType.setText("Type: " + mealType);            
			//     displayIngredients.setText(mealIngred);
			//     displayDirections.setText(mealDirections);
            // });
            
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