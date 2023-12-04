//package src.main.java;

import java.io.*;
import java.util.*;

import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    
    // getDetails using MOCK of bufferedReader
    public String getDetailsMOCK (String name, String whichDetail) {
	    String mealName = "";
        String mealType = "Dinner";
        String mealIngred = "Ham, bread, cheese, mayo, mustard, oil, vinegar.";
        String mealDirections = "1. Cut the bread into two medium-sized slices.\n"
        		+ "2. Put a thin layer of mustard on each slice of the bread.\n"
        		+ "3. Heat a pan on medium-high and add some oil.\n"
        		+ "4. On one side of the bread, place a few slices of ham and some slices of cheese.\n"
        		+ "5. Place the other piece of bread on top (mustard side down).\n"
        		+ "6. Cover the top and let it cook in the pan for a few minutes.\n"
        		+ "7. Flip the sandwich and let it cook for a few minutes on the other side.\n"
        		+ "8. Take the sandwich off the pan and spread mayo and vinegar on both sides.\n"
        		+ "9. Cut the sandwich in half and enjoy.\n";

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
    private MenuButton sortMenuButton;
    private MenuItem sortAtoZ;
    private MenuItem sortZtoA;
    private MenuItem sortNewToOld;
    private MenuItem sortOldToNew;
    
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

        sortMenuButton = new MenuButton("Sort Recipes");
        sortAtoZ = new MenuItem("A-Z");
        sortZtoA = new MenuItem("Z-A");
        sortNewToOld = new MenuItem("Newest to Oldest");
        sortOldToNew = new MenuItem("Oldest to Newest");
        sortMenuButton.getItems().addAll(sortAtoZ, sortZtoA, sortNewToOld, sortOldToNew);
        sortMenuButton.setStyle(defaultButtonStyle);

        this.getChildren().setAll(newRecipeButton, editRecipeButton, deleteRecipeButton, sortMenuButton);
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
    public MenuButton getSortMenuButton() {
        return sortMenuButton;
    }
    public MenuItem getSortAtoZ() {
        return sortAtoZ;
    }
    public MenuItem getSortZtoA() {
        return sortZtoA;
    }
    public MenuItem getSortNewToOld() {
        return sortNewToOld;
    }
    public MenuItem getSortOldToNew() {
        return sortOldToNew;
    }
}




		

class RecipeDetails extends VBox {		

	private Text titleText;
	private Text displayType;
	private Text displayIngredients;
	private Text displayDirections;
    private ImageView displayImageView;
    private String db_dir = "localDB/";
	
				

	public RecipeDetails () {
        this.setPrefSize(900, 60);
        this.setStyle("-fx-background-color: #BCE29E;");

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

        displayImageView = new ImageView();
        displayImageView.setFitHeight(150);
        displayImageView.setFitWidth(150);

        this.getChildren().addAll(displayImageView, displayType, displayIngredients, displayDirections);
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
            String imageLocation = br.readLine();
            String mealType = br.readLine();
            String mealIngred = br.readLine();

			titleText.setText(mealName);
            titleText.setWrappingWidth(700);

			displayType.setText(mealType + "\n");
            displayType.setWrappingWidth(500);
			
			displayIngredients.setText(mealIngred + "\n");
            displayIngredients.setWrappingWidth(500);

            setDisplayImageView(imageLocation);

            int c;
            StringBuilder parsedDirections= new StringBuilder();

            while ((c = br.read()) != -1) {
                parsedDirections.append( (char)c ) ;
            }
            String directionString = parsedDirections.toString();
			displayDirections.setText(directionString);
            displayDirections.setWrappingWidth(500);
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 0, 10, 200));

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
        this.setAlignment(Pos.CENTER);
		titleText.setText("Choose a recipe");
		displayType.setText(" ");
		displayIngredients.setText(" ");
		displayDirections.setText(" ");
        displayImageView.setImage(null);
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

    public ImageView getDisplayImageView () {
        return displayImageView;
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

    public void setDisplayImageView (String path) {
        Image image = new Image("file:" + path);
        displayImageView.setImage(image);
    }

}