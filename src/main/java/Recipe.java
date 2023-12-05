//package src.main.java;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import org.bson.types.ObjectId;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CheckMenuItem;
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

public class Recipe extends HBox {

    private String recipeName;
    private String recipeType;
    private ObjectId recipeID;
    private Text text;
    private Label label;
    private RecipeDetails recipeDetails;
    private ArrayList<Recipe> recipeArray;
    private int creationDateRank = 1;
    private Date creationDate;
    
    private boolean isSelected;

    // Default Constructor
    public Recipe(){}

    public Recipe(RecipeDetails _recipeDetails) {
    	recipeDetails = _recipeDetails;
    	
    	this.setPrefSize(500, 40); // sets size of recipe
        this.setStyle("-fx-background-color: #266024; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of recipe
        this.setSpacing(20);
        isSelected = false;

        text = new Text(); // create recipe name text field
        text.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        text.setStyle("-fx-background-color: #266024; -fx-border-width: 0;"); // set background color of texfield
        text.setTextAlignment(TextAlignment.CENTER); // set alignment of text field
        text.setFill(Color.WHITE);
        this.getChildren().add(text); // add textlabel to recipe

        label = new Label(); // create recipe name text field
        label.setText(recipeType);
        label.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        label.setStyle("-fx-background-color: #999999; -fx-border-width: 0;"); // set background color of texfield
        label.setTextAlignment(TextAlignment.RIGHT); // set alignment of text field
        this.getChildren().add(label); // add textlabel to recipe
        
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
        	toggleSelect();
        	event.consume();
        });

    }

    public Date getDate() {
        return this.creationDate;
    }

    public void setDate(Date date) {
        creationDate = date;
    }

    public String getRecipeName() {
        return this.recipeName;
    }
    
    public void setRecipeName(String name) {
        recipeName = name;
    }

    public ObjectId getRecipeID() {
        return this.recipeID;
    }
    
    public void setRecipeID(ObjectId id) {
        recipeID = id;
    }
    
    public String getRecipeType() {
        return this.recipeType;
    }
    
    public void setRecipeType(String type) {
        recipeType = type;
    }
    
    public void updateText() {
    	// Setting label and style based on recipeType
    	if (recipeType.equals("Breakfast")) {
    		label.setText("B");
    		label.setStyle("-fx-background-color: #3cb371; -fx-border-width: 0;"); // set background color of texfield
    	}
    	else if (recipeType.equals("Lunch")) {
    		label.setText("L");
    		label.setStyle("-fx-background-color: #ee82ee; -fx-border-width: 0;"); // set background color of texfield
    	}
    	else {
    		label.setText("D");
    		label.setStyle("-fx-background-color: #ff0000; -fx-border-width: 0;"); // set background color of texfield
    	}
 
    	// Setting recipe name on tabs
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
            recipeDetails.showDetailsMongo(this.getRecipeID());

        } else {
            isSelected = false;

            this.setStyle("-fx-background-color: #266024; -fx-border-width: 0; -fx-font-weight: bold;"); 
            recipeDetails.defaultView();
        }
    }

    public void updateRecipeArray(ArrayList<Recipe> arry_input) {
        recipeArray = arry_input;
    }

    public int getCreationDateRank() {
        return creationDateRank;
    }
    public void updateCreationDateRank(int newRank) {
        creationDateRank = newRank;
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
    private CheckMenuItem sortAtoZ;
    private CheckMenuItem sortZtoA;
    private CheckMenuItem sortNewToOld;
    private CheckMenuItem sortOldToNew;
    private ArrayList<CheckMenuItem> menuItemList;
    private ComboBox filterBox;
    
    ActionsList() {
        this.setPrefSize(300, 50);
        this.setSpacing(15);
        this.setStyle("-fx-background-color: #996600;");

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        
        // filter dropdown menu
        String[] recipeTypes = {"All", "Breakfast", "Lunch", "Dinner"};
        filterBox = new ComboBox(FXCollections.observableArrayList(recipeTypes));
        filterBox.getSelectionModel().selectFirst(); // set default value
        filterBox.setStyle(defaultButtonStyle);

        newRecipeButton = new Button("New Recipe");
        newRecipeButton.setStyle(defaultButtonStyle);
        editRecipeButton = new Button("Edit Recipe");
        editRecipeButton.setStyle(defaultButtonStyle);
        deleteRecipeButton = new Button("Delete Recipe");
        deleteRecipeButton.setStyle(defaultButtonStyle);

        sortMenuButton = new MenuButton("Sort Recipes");
        sortMenuButton.setMinWidth(125);
        sortAtoZ = new CheckMenuItem("A-Z");
        sortZtoA = new CheckMenuItem("Z-A");
        sortNewToOld = new CheckMenuItem("Newest to Oldest");
        sortOldToNew = new CheckMenuItem("Oldest to Newest");        
        sortMenuButton.getItems().addAll(sortAtoZ, sortZtoA, sortNewToOld, sortOldToNew);
        sortMenuButton.setStyle(defaultButtonStyle);

        menuItemList = new ArrayList<CheckMenuItem>();
        menuItemList.add(sortAtoZ);
        menuItemList.add(sortZtoA);
        menuItemList.add(sortNewToOld);
        menuItemList.add(sortOldToNew);

        this.getChildren().setAll(newRecipeButton, editRecipeButton, deleteRecipeButton, sortMenuButton, filterBox);
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
    public CheckMenuItem getSortAtoZ() {
        return sortAtoZ;
    }
    public CheckMenuItem getSortZtoA() {
        return sortZtoA;
    }
    public CheckMenuItem getSortNewToOld() {
        return sortNewToOld;
    }
    public CheckMenuItem getSortOldToNew() {
        return sortOldToNew;
    }
    public void uncheckOtherItems(CheckMenuItem checkedItem) {
        for (CheckMenuItem entries: menuItemList) {
            if (entries != checkedItem) {
                entries.setSelected(false);
            }
        }
    }
    public ComboBox getFilterBox() {
    	return filterBox;
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

    public boolean showDetailsMongo (ObjectId recipeID) {
        List<String> recipeSet = MongoDB.getRecipe(recipeID);
        if (recipeSet != null) {
            String name = recipeSet.get(0);
            String type = recipeSet.get(1);
            String ingredients = recipeSet.get(2);
            String directions = recipeSet.get(3);
            String image = recipeSet.get(4);

            // convert img and get path
            String imageLocation = base64ToImg(name, image);

            setTitleText(name);
            setDisplayType(type);
            setDisplayIngredients(ingredients);
            setDisplayDirections(directions);
            setDisplayImageView(imageLocation);

            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 0, 10, 200));

            return true;
        }
        else {
            return false;
        }
    }

    private static String base64ToImg(String name, String base64) {
        if (base64 == null)
            return null;

        String path = null;

        byte[] data = Base64.getDecoder().decode(base64);
        path = "images/" + name + ".jpg";
        File file = new File(path);

        // if there is an image already in path, delete
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (OutputStream oStream = new BufferedOutputStream(new FileOutputStream(file))) {
            oStream.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
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
        displayType.setText(mealType + "\n");
        displayType.setWrappingWidth(500);
	}
	
	public void setTitleText (String mealName) {
		titleText.setText(mealName);
        titleText.setWrappingWidth(700);
	}
	
	public void setDisplayIngredients (String mealIngred) {
		displayIngredients.setText(mealIngred + "\n");
        displayIngredients.setWrappingWidth(500);
	}
	
	public void setDisplayDirections (String mealDirections) {
		displayDirections.setText(mealDirections);
        displayDirections.setWrappingWidth(500);
	}

    public void setDisplayImageView (String path) {
        Image image = new Image("file:" + path);
        displayImageView.setImage(image);
    }

}
