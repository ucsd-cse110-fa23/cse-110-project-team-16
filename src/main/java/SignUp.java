//package src.main.java;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class SignUpFrame extends BorderPane{
	 private Button createAccountButton;
	 private SignUp signupinfo;
	 private signupButtons allSignUpButtons;
	 Stage primaryStage;
	 SignUpFrame(){
		 signupinfo=new SignUp();
		 allSignUpButtons = new signupButtons();
		 this.setCenter(signupinfo);
	     this.setBottom(allSignUpButtons);
	     createAccountButton=allSignUpButtons.getCreateAccountButton();
		 addListeners();
	 }
	 private void addListeners() {
		 createAccountButton.setOnAction(e -> {
			 //System.out.println("Hello");
	        });
	    }
	

}

class signupButtons extends HBox{
	 private Button createAccountButton;
	 signupButtons(){
		 this.setPrefSize(500, 60);
	     this.setStyle("-fx-background-color: #F0F8FF;");
	     this.setSpacing(15);
	     String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
	     createAccountButton = new Button("Create Account"); // text displayed on add button
	     createAccountButton.setStyle(defaultButtonStyle); // styling the button
	     
	     this.getChildren().addAll(createAccountButton); // adding buttons to footer
	     this.setAlignment(Pos.CENTER); 
	 }
	 
	    
	 public Button getCreateAccountButton() {
	        return createAccountButton;
	    }

}

public class SignUp extends VBox{
	private TextField newUserName;
	private TextField newPassword;
	private TextField confirmPassword;
	 SignUp(){
		 this.setSpacing(5); // sets spacing between tasks
	        this.setPrefSize(400, 560);
	        //this.setStyle("-fx-background-color: #FFFF00;");
	        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

	        newUserName = new TextField();
	        newUserName.setPrefSize(380, 20); // set size of text field
	        newUserName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield

	        newUserName.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
	        newUserName.setPromptText("Input username here");

	        newPassword = new TextField();
	        newPassword.setPrefSize(380, 20); // set size of text field
	        newPassword.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
	        
	        newPassword.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
	        newPassword.setPromptText("Input password here");
	        
	        confirmPassword = new TextField();
	        confirmPassword.setPrefSize(380, 20); // set size of text field
	        confirmPassword.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
	        
	        confirmPassword.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
	        confirmPassword.setPromptText("Confirm Password");
	        this.getChildren().addAll(newUserName,newPassword,confirmPassword);
	        this.setSpacing(15);
	        this.setAlignment(Pos.CENTER); 
	        
	 }
}
