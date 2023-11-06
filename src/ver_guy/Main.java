package src.ver_guy;
// Hello Guy Testing
// Hello Guy Variant Testing
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

class Task extends HBox {

    private Label index;
    private TextField taskName;
    //private Button doneButton;

    private boolean markedDone;

    Task() {
        this.setPrefSize(500, 50); // sets size of task
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of task
        markedDone = false;

        index = new Label();
        index.setText(""); // create index label
        index.setPrefSize(40, 20); // set size of Index label
        index.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        index.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the task
        this.getChildren().add(index); // add index label to task

        taskName = new TextField(); // create task name text field
        taskName.setPrefSize(380, 20); // set size of text field
        taskName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        index.setTextAlignment(TextAlignment.LEFT); // set alignment of text field
        taskName.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        this.getChildren().add(taskName); // add textlabel to task
        
//        doneButton = new Button("Done"); // creates a button for marking the task as done
//        doneButton.setPrefSize(100, 20);
//        doneButton.setPrefHeight(Double.MAX_VALUE);
//        doneButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // sets style of button
//
//        this.getChildren().add(doneButton);
    }

    public void setTaskIndex(int num) {
        //this.index.setText(num + ""); // num to String
        this.taskName.setPromptText("Recipe " + num);
    }

    public TextField getTaskName() {
        return this.taskName;
    }
    
    public void setTaskName(String name) {
        taskName.setText(name);
    }

//    public Button getDoneButton() {
//        return this.doneButton;
//    }

    public boolean isMarkedDone() {
        return this.markedDone;
    }

    public void toggleDone() {
        
        markedDone = true;
        this.setStyle("-fx-border-color: #000000; -fx-border-width: 0; -fx-font-weight: bold;"); // remove border of task
        for (int i = 0; i < this.getChildren().size(); i++) {
            this.getChildren().get(i).setStyle("-fx-background-color: #BCE29E; -fx-border-width: 0;"); // change color of task to green
        }
    }
}




class recipeBox extends VBox {
	
	 private TextField recipeName;
	 private TextField ingredientList;
	 private TextField instructions;
	 private Task ingredients;
    
	recipeBox() {
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(400, 560);
        //this.setStyle("-fx-background-color: #FFFF00;");
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
       
        //recipeName = new Task(); // create task name text field
//        recipeName.setPrefSize(500, 200); // set size of text field
//        recipeName.setStyle("-fx-background-color: #F0F8FF; -fx-border-width: 0;"); // set background color of texfield
//        recipeName.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        //this.getChildren().add(recipeName);
        
        recipeName = new TextField(); // create task name text field
        recipeName.setPrefSize(400, 20); // set size of text field
        recipeName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        recipeName.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        recipeName.setPromptText("Recipe Name");
        this.getChildren().add(recipeName); // add textlabel to task
        
        ingredientList = new TextField(); // create task name text field
        ingredientList.setPrefSize(400, 180); // set size of text field
        ingredientList.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        ingredientList.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        ingredientList.setPromptText("Ingredient List");
        this.getChildren().add(ingredientList); // add textlabel to task
        
        instructions = new TextField(); // create task name text field
        instructions.setPrefSize(400, 180); // set size of text field
        instructions.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        instructions.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        instructions.setPromptText("Instructions");
        this.getChildren().add(instructions); // add textlabel to task
        
        //ingredients = new Task(); // create task name text field
//        ingredients.setPrefSize(500, 200); // set size of text field
//        ingredients.setStyle("-fx-background-color: #F0F8FF; -fx-border-width: 0;"); // set background color of texfield
//        ingredients.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        //this.getChildren().add(recipeName);
        //this.getChildren().addAll(recipeName,ingredients);
    }
	
	public TextField getRecipeName() {
        return this.recipeName;
    }

    
}
class TaskList extends VBox {
	
	
	
	private Button newRecipe;
	private Button editRecipe;

    
    TaskList() {
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(150, 560);
        this.setStyle("-fx-background-color: #FFFF00;");
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        newRecipe = new Button("+ New Recipe"); // text displayed on add button
        newRecipe.setStyle(defaultButtonStyle);
        editRecipe = new Button("Edit Recipe"); // text displayed on add button
        editRecipe.setStyle(defaultButtonStyle);
        this.getChildren().addAll(newRecipe);
        this.getChildren().addAll(editRecipe);
        
    }
    public Button getNewRecipeButton() {
        return newRecipe;
    }
    public Button getEditRecipeButton() {
        return editRecipe;
    }
    public void updateTaskIndices() {
        int index = 1;
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Task) {
                ((Task) this.getChildren().get(i)).setTaskIndex(index);
                index++;
            }
        }
    }

    public void removeCompletedTasks() {
        this.getChildren().removeIf(task -> task instanceof Task && ((Task) task).isMarkedDone());
        this.updateTaskIndices();
    }

    // TODO: Complete this method
    /*
     * Load tasks from a file called "tasks.txt"
     * Add the tasks to the children of tasklist component
     */
    public void loadTasks() {
        // hint 1: use try-catch block
        // hint 2: use BufferedReader and FileReader
        // hint 3: task.getTaskName().setText() sets the text of the task

        System.out.println("loadtasks() not implemented!");
    }

    // TODO: Complete this method
    /*
     * Save tasks to a file called "tasks.txt"
     */
    public void saveTasks() {


        System.out.println("savetasks() not implemented!");
    }

    // TODO: Complete this method
    /*
     * Sort the tasks lexicographically
     */
    public void sortTasks() {


         System.out.println("sorttasks() not implemented!");
    }
}

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
        cancelButton = new Button("Cancel"); // text displayed on clear tasks button
        cancelButton.setStyle(defaultButtonStyle);
        

        this.getChildren().addAll(saveButton,cancelButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        // TODO: Create loadButton, saveButton and sortButton to the footer
    }


    
    public Button getSaveButton() {
        return saveButton;
    }
    
    public Button getCancelButton() {
        return cancelButton;
    }

    // TODO: Add getters for loadButton, saveButton and sortButton
}

class Header extends HBox {

    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #0000FF;");

        //Text titleText = new Text("To Do List"); // Text of the Header
        //titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        //this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

class AppFrame extends BorderPane{

    private Header header;
   // private Footer footer;
    private TaskList taskList;
    private Button newRecipeButton;
    private Button addButton;
    private Button clearButton;

    AppFrame()
    {
        // Initialise the header Object
        header = new Header();

        // Create a tasklist Object to hold the tasks
        taskList = new TaskList();
        

        this.setRight(header);
        // Add scroller to the centre of the BorderPane
        this.setLeft(taskList);
        newRecipeButton=taskList.getNewRecipeButton();
        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {

        // Add button functionality
    	newRecipeButton.setOnAction(e -> {
            // Create a new task
    		EditFrame root = new EditFrame(taskList);
            // Call toggleDone on click
        	//root = FXMLLoader.load(getClass().getClassLoader().getResource("C:\\Users\\wumbo\\OneDrive\\Desktop\\javafx-sdk-21.0.1\\lib"), resources);
            Stage stage = new Stage();
            stage.setTitle("Create New Recipe");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            
        });
        
    }
}

class EditFrame extends BorderPane{
	private Button saveButton;
	private Button cancelButton;
   private Footer footer;
   private recipeBox recipes;
   
   private TaskList taskList;
    EditFrame(TaskList _tasklist)
    {
    	taskList = _tasklist;
    	footer=new Footer();
    	recipes=new recipeBox();
    	this.setCenter(recipes);
    	this.setBottom(footer);
    	saveButton=footer.getSaveButton();
    	cancelButton=footer.getCancelButton();
        addListeners();
    }

    public void addListeners()
    {
    	// Add button functionality
    	saveButton.setOnAction(e -> {
            // Create a new task
            Task task = new Task();
            task.setTaskName(recipes.getRecipeName().getText());
            // Add task to tasklist
            taskList.getChildren().add(task);
            // Add selectButtonToggle to the Done button
            //Button selectButton = task.getSelectButton();
            //selectButton.setOnAction(e1 -> {
                // Call toggleDone on click
            //    task.toggleSelect();
            //});
            // Update task indices
            taskList.updateTaskIndices();
        });
    }
}
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the TaskList
        AppFrame root = new AppFrame();

        // Set the title of the app
      //  primaryStage.setTitle("To Do List");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 500, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}