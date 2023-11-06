package ver_yalu;
// Hello Guy Testing
// Hello Guy Variant Testing
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Action;

class Task extends HBox {

    private Label index;
    private TextField taskName;
    private Button selectButton;

    private boolean isSelected;

    Task() {
        this.setPrefSize(500, 20); // sets size of task
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of task
        isSelected = false;

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
        
        selectButton = new Button("Done"); // creates a button for marking the task as done
        selectButton.setPrefSize(100, 20);
        selectButton.setPrefHeight(Double.MAX_VALUE);
        selectButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // sets style of button

        this.getChildren().add(selectButton);
    }

    public void setTaskIndex(int num) {
        //this.index.setText(num + ""); // num to String
        this.taskName.setPromptText("Recipe " + num);
    }

    public TextField getTaskName() {
        return this.taskName;
    }

    public Button getSelectButton() {
        return this.selectButton;
    }

    public boolean isMarkedDone() {
        return this.isSelected;
    }

    public void toggleSelect() {
        
        if (!this.isSelected) {
            isSelected = true;
            this.setStyle("-fx-border-color: #000000; -fx-border-width: 0; -fx-font-weight: bold;"); // remove border of task
            for (int i = 0; i < this.getChildren().size(); i++) {
                this.getChildren().get(i).setStyle("-fx-background-color: #AD1453; -fx-border-width: 0;"); // change color of task to green
            }
        } else {
            isSelected = false;
            for (int i = 0; i < this.getChildren().size(); i++) {
                this.getChildren().get(i).setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");; // sets background color of task
            }        
        }
    }
}

class ActionsList extends HBox {
    private Button newRecipeButton;
    private Button editRecipeButton;
    
    ActionsList() {
        this.setPrefSize(300, 50);
        this.setSpacing(15);
        this.setStyle("-fx-background-color: #0099FF;");

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

class TaskList extends VBox {
			
	private ActionsList actionsList;	

    
    TaskList() {
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(300, 560);
        this.setStyle("-fx-background-color: #FFFF00;");
        // String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        
        actionsList = new ActionsList();
        this.getChildren().add(actionsList);      
        
    }
    public Button getNewRecipeButton() {
        return actionsList.getNewRecipeButton();
    }
    public Button getEditRecipeButton() {
        return actionsList.getEditRecipeButton();
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
        // hint 1: use try-catch block
        // hint 2: use FileWriter
        // hint 3: this.getChildren() gets the list of tasks

        System.out.println("savetasks() not implemented!");
    }

}

class Footer extends HBox {

    private Button addButton;
    private Button clearButton;
    // TODO: Add a button called "loadButton" to load tasks from file
    // TODO: Add a button called "saveButton" to save tasks to a file    

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("Add Task"); // text displayed on add button
        addButton.setStyle(defaultButtonStyle); // styling the button
        clearButton = new Button("Clear finished"); // text displayed on clear tasks button
        clearButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(addButton, clearButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        // TODO: Create loadButton, saveButton and sortButton to the footer
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getClearButton() {
        return clearButton;
    }

    // TODO: Add getters for loadButton, saveButton and sortButton
}

class RecipeDetails extends HBox {

    RecipeDetails() {
        this.setPrefSize(900, 60); // Size of the header
        this.setStyle("-fx-background-color: #BCE29E;");

        Text titleText = new Text("Recipe List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

class AppFrame extends BorderPane{

    private RecipeDetails recipeDetails;
   // private Footer footer;
    private TaskList taskList;
    private Button newRecipeButton;
    private Button addButton;
    private Button clearButton;
    private ScrollPane scrollPane;

    AppFrame()
    {
        // Initialise the header Object
        recipeDetails = new RecipeDetails();

        // Create a tasklist Object to hold the tasks
        taskList = new TaskList();
        scrollPane = new ScrollPane(taskList);
        
        // Initialise the Footer Object
       // footer = new Footer();                
        
        this.setRight(recipeDetails);

        // this.setLeft(taskList);

        this.setLeft(scrollPane);
       // this.setBottom(footer);
        
        // Initialise Button Variables through the getters in Footer
        //addButton = footer.getAddButton();
       // clearButton = footer.getClearButton();
        newRecipeButton=taskList.getNewRecipeButton();
        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {

        // Add button functionality
    	newRecipeButton.setOnAction(e -> {
            // Create a new task
            Task task = new Task();
            // Add task to tasklist
            taskList.getChildren().add(task);
            // Add selectButtonToggle to the Done button
            Button selectButton = task.getSelectButton();
            selectButton.setOnAction(e1 -> {
                // Call toggleDone on click
                task.toggleSelect();
            });
            // Update task indices
            taskList.updateTaskIndices();
        });
        
        // Clear finished tasks
//        clearButton.setOnAction(e -> {
//            taskList.removeCompletedTasks();
//        });
    }
}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the TaskList
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("App version 1");      
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 1200, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}