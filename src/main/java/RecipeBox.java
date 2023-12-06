//package src.main.java;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.sound.sampled.*;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URI;

class PreviewDeails extends VBox{
	private Text displayType;
	private Text displayName;
	private Text displayIngredients;
	private Text displayDirections;
    private ImageView displayImageView;
    private InputBox inputBox;
	PreviewDeails(InputBox _inputBox){
		 inputBox=_inputBox;
		 displayType = new Text(inputBox.getType());
         displayType.setFont(Font.font("Arial", 14));
         
         displayIngredients = new Text(inputBox.getIngrediemts());
         displayIngredients.setWrappingWidth(450);
         displayIngredients.setFont(Font.font("Arial", 14));
         displayName = new Text();
         displayName.setFont(Font.font("Arial", 14));
         displayDirections = new Text();
         displayDirections.setWrappingWidth(450);
         displayDirections.setFont(Font.font("Arial", 14));
         displayImageView = new ImageView();
         displayImageView.setFitHeight(150);
         displayImageView.setFitWidth(150);
         this.getChildren().addAll(displayName,displayType,displayImageView,displayIngredients,displayDirections);
	}
	
	public Text getdisplayName() {
		return displayName;
	}
	public Text getdisplayType() {
		return displayType;
	}
	public Text getdisplayIngredients() {
		return displayIngredients;
	}
	public Text getdisplayDirections() {
		return displayDirections;
	}
	public ImageView getdisplayImage() {
		return displayImageView;
	}
}


class PreviewFrame extends BorderPane {
	private PreviewDeails prevDetails;
	private String recipeName;
	private String recipeType;
	private String recipeIngredients;
	private String recipeDirections;
	String imageLocation = "";
    String encodedImg = null;
    private Button saveButton;
	private Button refreshButton;
	private CreationFrame crFrame;
	private ArrayList<Recipe> allRecipes;
	private RecipeList recipeList;
	private RecipeDetails recipeDetails;
    private InputBox inputBox;
    private String[] recipeString;
    private String db_dir = "localDB/";
    PreviewFrame(RecipeList _recipelist, ArrayList<Recipe> _allRecipes, RecipeDetails _recipeDetails,InputBox _inputBox,String[] _recipeString) {
    	//vairable
    	allRecipes=_allRecipes;
    	recipeList=_recipelist;
    	recipeDetails=_recipeDetails;
    	inputBox=_inputBox;
    	recipeString=_recipeString;
    	recipeName = recipeString[0].strip();
        recipeType = inputBox.getType();
        recipeIngredients = inputBox.getIngrediemts();
        recipeDirections = recipeString[1];
    	prevDetails=new PreviewDeails(inputBox);
    	saveButton = new Button("Save"); // text displayed on add button
    	refreshButton = new Button("Refresh"); // text displayed on clear recipes button
    	
         setPreview(recipeName,recipeDirections);
    	HBox dialogButtons = new HBox(saveButton, refreshButton);
    	dialogButtons.setAlignment(Pos.CENTER);
    	this.setBottom(dialogButtons);
    	this.setCenter(prevDetails);
    	//this.getChildren().addAll(displayIngredients, displayDirections);
        addListeners();
    }

    
    private void addListeners() {
    	saveButton.setOnAction(e -> {
           recipeName=prevDetails.getdisplayName().getText();
           recipeDirections=prevDetails.getdisplayDirections().getText();
                Recipe recipe = new Recipe(recipeDetails);
                allRecipes.add(recipe);
                recipe.setRecipeName(recipeName);
                recipe.setRecipeType(recipeType);
                recipe.updateText();

                //! This is needed because we need to associate every single recipe
                //! with the arraylist of total recipes
                recipe.updateRecipeArray(allRecipes);
                recipe.updateCreationDateRank(allRecipes.size() + 1);
                recipeList.getChildren().add(0, recipe);

                // add recipe to MongoDB
                ObjectId id = MongoDB.addRecipe(recipeName, recipeType, recipeIngredients, recipeDirections, encodedImg);
                recipe.setRecipeID(id);
                recipe.setDate(RecipeList.convertToDate(id.toString()));

                recipeList.resortRecipes();
                recipe.toggleSelect();

                recipeDetails.showDetailsMongo(recipe.getRecipeID());


                Stage stageClose = (Stage) getScene().getWindow(); // Get the current stage
                stageClose.close(); // Close the window
        });
    	refreshButton.setOnAction(e -> {
    		String image = "images/" + recipeName + ".jpg";
            File imageFile = new File(image);
            imageFile.delete();
    		ChatGPT chatgpt=new ChatGPT();
            String[] recipeString = null;
	        try {
		        recipeString = chatgpt.generatedRecipe(inputBox.getType(), inputBox.getIngrediemts());
	        } catch (IOException e1) {
		        e1.printStackTrace();
	        } catch (InterruptedException e2) {
		        e2.printStackTrace();
	        }
	        recipeName = recipeString[0].strip();
	        recipeDirections = recipeString[1];
	        setPreview(recipeName,recipeDirections);         
  });
        
    }
    public void setDisplayImageView (String path) {
        Image image = new Image("file:" + path);
        prevDetails.getdisplayImage().setImage(image);
    }
    
    public void setPreview(String _displayName, String _displayDirections) {
    	prevDetails.getdisplayName().setText(_displayName);
    	prevDetails.getdisplayDirections().setText(_displayDirections);
    	String imageLocation = "";
        String encodedImgPreview = null;
        try {
            imageLocation = createImage(recipeName);
            File img = new File(imageLocation);
            encodedImgPreview = imgToBase64(img);
        } catch (IOException | InterruptedException | URISyntaxException e1) {
            e1.printStackTrace();
        }
        encodedImg=encodedImgPreview;
        setDisplayImageView(imageLocation);
    }
    
    private String base64ToImg(String name, String base64) {
        if (base64 == null)
            return null;

        String path = null;

        byte[] data = Base64.getDecoder().decode(base64);
        path = "images/" + name + ".jpg";
        File file = new File(path);

        try (OutputStream oStream = new BufferedOutputStream(new FileOutputStream(file))){
            oStream.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }

  private String imgToBase64(File file) {
	  String encoded = null;

	  try {
		  FileInputStream fileInputStream = new FileInputStream(file);
		  byte[] bytes = new byte[(int)file.length()];
		  fileInputStream.read(bytes);
		  encoded = Base64.getEncoder().encodeToString(bytes);
		  fileInputStream.close();
	  } catch (IOException e) {
      e.printStackTrace();
	  	}

	  return encoded;
    }

    private String createImage(String recipeName) throws IOException, InterruptedException, URISyntaxException {
    String url = DallE.generateImage(recipeName);
    //String url = "https://www.31daily.com/wp-content/uploads/2022/01/md-Chicken-Broccoli-8-1-of-1-840x480.jpg";
    String path = "images/" + recipeName + ".jpg";
    File file = new File(path);

    // if there is an image already in path, delete
    try {
        Files.deleteIfExists(file.toPath());
    } catch (IOException e) {
        e.printStackTrace();
    }

    try(
        InputStream in = new URI(url).toURL().openStream()
    )
        {
        Files.copy(in, Paths.get(path));
        }

        return path;
	}
}
class CreationFrame extends BorderPane {
    private Button saveButton;
	private Button cancelButton;
    private ArrayList<Recipe> allRecipes;
	private RecipeList recipeList;
	private RecipeDetails recipeDetails;
    private InputBox inputBox;


    CreationFrame(RecipeList _recipelist, ArrayList<Recipe> _allRecipes, RecipeDetails recipeDetails) {


        recipeList = _recipelist;
    	allRecipes = _allRecipes;
        this.recipeDetails = recipeDetails;
        inputBox = new InputBox();

        saveButton = new Button("Preview"); // text displayed on add button
        cancelButton = new Button("Cancel"); // text displayed on clear recipes button
        HBox dialogButtons = new HBox(saveButton, cancelButton);
        dialogButtons.setAlignment(Pos.CENTER);
        this.setCenter(inputBox);
        this.setBottom(dialogButtons);
        addListeners();
    }


    private void addListeners() {
        saveButton.setOnAction(e -> {
            if (inputBox.isValid()) {
            	ChatGPT chatgpt=new ChatGPT();
                String[] recipeString = null;
		        try {
			        recipeString = chatgpt.generatedRecipe(inputBox.getType(), inputBox.getIngrediemts());
		        } catch (IOException e1) {
			        e1.printStackTrace();
		        } catch (InterruptedException e2) {
			        e2.printStackTrace();
		        }
            	PreviewFrame root = new PreviewFrame(recipeList,allRecipes,recipeDetails,inputBox,recipeString);
                Stage stage = new Stage();
                stage.setTitle("Preview Recipe");
                stage.setScene(new Scene(root, 450, 550));
                stage.show();
                Stage stageClose = (Stage) getScene().getWindow(); // Get the current stage
                stageClose.close(); // Close the window
            }
            
            

        });
        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) getScene().getWindow(); // Get the current stage
            stage.close(); // Close the window

        });
    }
    

}


class InputBox extends VBox {
    private Label typeLabel;
    private Label typeInput;
    private Label ingredientsLabel;
    private Label ingredientsInput;
    private Button recordType; 
    private Button recordIngredients;
    private boolean validType = false;
    private boolean validIngredients = false;

    InputBox() {
        
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(400, 560);
        //this.setStyle("-fx-background-color: #FFFF00;");
                
        typeLabel = new Label("Recipe Type");
        typeInput = new Label("Please select your meal type: Breakfast, Lunch or Dinner");
        recordType = new Button("Record Voice Input");
        recordIngredients = new Button("Record Voice Input");
        ingredientsLabel = new Label("Recipe Ingredients");
        ingredientsInput = new Label("Please input all of the ingredients you have.");
        ingredientsInput.setWrapText(true);
        
        //typeInput.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        ingredientsInput.setStyle("-fx-border-color: black;");
        ingredientsInput.setPrefSize(300, 400);
        ingredientsInput.setAlignment(Pos.TOP_CENTER);

        typeLabel.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        typeInput.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        //recordType.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        //recordIngredients.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        ingredientsLabel.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        ingredientsInput.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
       
        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(typeLabel, typeInput, recordType, ingredientsLabel, ingredientsInput, recordIngredients);
        addListeners();

    }

    private void addListeners() {

        recordType.setOnAction(e -> {
            AudioRecordFrame audioRecorder = new AudioRecordFrame(this, false, true);
            audioRecorder.showAudioFrame();
        });
        recordIngredients.setOnAction(e -> {
            AudioRecordFrame audioRecorder = new AudioRecordFrame(this, true, false);
            audioRecorder.showAudioFrame();
        });

    }
    public void setType(String newTypeString) {
        if (newTypeString.contains("Breakfast") || newTypeString.contains("breakfast")) {
            validType = true;
            this.typeInput.setText("Breakfast");
        }
        else if (newTypeString.contains("Lunch") || newTypeString.contains("lunch")) {
            validType = true;
            this.typeInput.setText("Lunch");
        }
        else if (newTypeString.contains("Dinner") || newTypeString.contains("dinner")) {
            validType = true;
            this.typeInput.setText("Dinner");
        }
        else {
            this.typeInput.setText("Invalid Meal Type, please select from either Breakfast, Lunch, or Dinner");
            validType = false;
        }
        
    }
    public void setIngredients(String newIngredientString) {
        this.ingredientsInput.setText(newIngredientString);
        validIngredients = true;
    }
    public String getType() {
        return typeInput.getText();
    }
    public String getIngrediemts() {
        return ingredientsInput.getText();
    }
    public boolean isValid() {
        return validType && validIngredients;
    }
    


} 
class EditFrame extends BorderPane {
	private Button saveButton;
	private Button cancelButton;
	private DialogButtons dialogButtons;
	private RecipeBox recipes;
	private RecipeList recipeList;
	private RecipeDetails recipeDetails;
	private boolean editMode;
	
    EditFrame(RecipeList _recipelist, RecipeDetails _recipeDetails, ArrayList<Recipe> _allRecipes, boolean _editMode)
    {
    	recipeList = _recipelist;
    	recipeDetails = _recipeDetails;
    	editMode = _editMode;
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
                    recipeName = "New Recipe";
                }
                String recipeType = recipes.getRecipeType();
                String ingredients = recipes.getIngredients();
                String directions = recipes.getDirections();

                Recipe recipe = recipeDetails.getCurrRecipe();
                if (recipe == null)
                    return;
                
                // apply changes to mongoDB
                MongoDB.editRecipe(recipe.getRecipeID(), recipeName, recipeType, ingredients, directions);
                
                // System.out.println("This is the new recipe name added: " + recipeName);
                recipe.setRecipeName(recipeName);
                recipe.setRecipeType(recipeType);
                recipe.updateText();
                
                // show recipe details
                recipeDetails.showDetailsMongo(recipe.getRecipeID());

                // refresh sort method
                recipeList.resortRecipes();

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

class ShareFrame extends FlowPane {
    private TextField url;
    private ShareLogic shareLogic;

    ShareFrame(ShareLogic shareLogic) {
        this.shareLogic = shareLogic;
        url = new TextField(shareLogic.getURL());
        url.setEditable(false);
        url.setPrefWidth(300);
        this.getChildren().add(url);
    }
    
}

class ShareLogic {
    private String url;
    private String recipeID; private String recipeName;
    ShareLogic(String recipeName) {
        this.recipeName = recipeName;

        try (MongoClient mongoClient = MongoClients.create(MongoDB.getURI())) {
    		MongoDatabase recipesDB = mongoClient.getDatabase("Recipes");
        	MongoCollection<Document> userCollection = recipesDB.getCollection(LoginFrame.getUser());
			Document existingRecipe = userCollection.find(new Document("name", recipeName)).first();

            Bson filter = eq("name", recipeName);
            userCollection.find(filter).first();

             if (existingRecipe != null) {
                // Extract the _id field from the found document
                recipeID = existingRecipe.getObjectId("_id").toString();
                System.out.println("Recipe ID: " + recipeID);

                // Assuming you want to construct a URL based on the recipe ID
                this.url = "https://pantrypal-server-lh0e.onrender.com/id?=" + recipeID;
            } else {
                url = "Recipe not found.";
            }
		}
    }
    public String getURL() {
        return url;
    }

}


class RecipeBox extends VBox {
	
    private TextField recipeName;
    private TextField recipeType;
    private TextField ingredients;
    private TextField directions;
    private Button whisperButtonType; 
    private Button whisperButtonIngredients;
   
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

        whisperButtonType = new Button("Voice Input Meal Type");



       
        ingredients = new TextField(); // create task name text field
        ingredients.setPrefSize(380, 130); // set size of text field
        ingredients.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        ingredients.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        ingredients.setPromptText("Input Ingredients here");

        whisperButtonIngredients = new Button("Voice Input Ingredients");

    
        


        directions = new TextField(); // create task name text field
        directions.setPrefSize(380, 150); // set size of text field
        directions.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        directions.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        directions.setPromptText("Input Directions here");
        

        this.getChildren().addAll(recipeName, recipeType, whisperButtonType, ingredients, whisperButtonIngredients, directions);              
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
    public void setRecipeName(String newText) {
       this.recipeName.setText(newText);
    }

    public void setRecipeType(String newText) {
    	 this.recipeType.setText(newText);
    }

    public void setIngredients(String newText) {
    	 this.ingredients.setText(newText);
    }

    public void setDirections(String newText) {
    	 this.directions.setText(newText);
    }
    public Button getWhisperTypeButton() {
        return this.whisperButtonType;
    }
    public Button getWhisperIngredientsButton() {
        return this.whisperButtonIngredients;
    }


   
}


// Footer of EditFrame - contains save and cancel buttons
class DialogButtons extends HBox {
	
    private Button saveButton;
    private Button cancelButton;
    private Button chatGPTButton;
    
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
        //chatGPTButton = new Button("ChatGPT"); // text displayed on clear recipes button
        //chatGPTButton.setStyle(defaultButtonStyle);
        

        this.getChildren().addAll(saveButton,cancelButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getSaveButton() {
        return saveButton;
    }
    
    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getChatGPTButton() {
        return chatGPTButton;
    }
}

class AudioRecordFrame extends FlowPane {
    private Button startButton;
    private Button stopButton;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    private boolean isRecording = false;
    private String audioString = "";
    private InputBox inputBox;
    private boolean ingredientsChange = false;
    private boolean typeChange = false;

    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    AudioRecordFrame(InputBox inputBox, boolean ingredientsChange, boolean typeChange) {
        this.ingredientsChange = ingredientsChange;
        this.typeChange = typeChange;
        this.inputBox = inputBox;
        // Set properties for the flowpane
        this.setPrefSize(370, 120);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setVgap(10);
        this.setHgap(10);
        this.setPrefWrapLength(170);

        // Add the buttons and text fields
        startButton = new Button("Start");
        startButton.setStyle(defaultButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);

        this.getChildren().addAll(startButton, stopButton, recordingLabel);

        // Get the audio format
        audioFormat = getAudioFormat();

        // Add the listeners to the buttons
        addListeners();
    }

    public void addListeners() {
        // Start Button
        startButton.setOnAction(e -> {
            startRecording();
        });

        // Stop Button
        stopButton.setOnAction(e -> {
            try {
                stopRecording();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }
    public void showAudioFrame() {
        Stage stage = new Stage();
            Scene addScene = new Scene(this, 370, 120);
            //stage.setAlignment(Pos.CENTER);
            stage.setTitle("Record Audio");
            stage.setScene(addScene);
            stage.show();
    }

    private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    private void startRecording() {
        if (isRecording == true) {
            return;
        }
        isRecording = true;
        Thread t = new Thread(
        new Runnable() {
          @Override
          public void run() {
        try {
            // the format of the TargetDataLine
            DataLine.Info dataLineInfo = new DataLine.Info(
                    TargetDataLine.class,
                    audioFormat);
            // the TargetDataLine used to capture audio data from the microphone
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            recordingLabel.setVisible(true);

            // the AudioInputStream that will be used to write the audio data to a file
            AudioInputStream audioInputStream = new AudioInputStream(
                    targetDataLine);

            // the file that will contain the audio data
            File audioFile = new File("recording.wav");
            AudioSystem.write(
                    audioInputStream,
                    AudioFileFormat.Type.WAVE,
                    audioFile);
            recordingLabel.setVisible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        }
    });
    t.start();
    }

    private void stopRecording() throws IOException, URISyntaxException {
        if (targetDataLine == null) {
            return;
        }
        isRecording = false;
        targetDataLine.stop();
        targetDataLine.close();
        Whisper whisper = new Whisper();
        whisper.generateString();
        audioString = whisper.getResponseString();
        if (ingredientsChange) {
            inputBox.setIngredients(audioString);
        }
        else {
            inputBox.setType(audioString);
        }
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
        
    }
    public String getAudioString() {
        return this.audioString;
    }
}
