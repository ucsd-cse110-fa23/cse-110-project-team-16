//package src.main.java;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

class LoginFrame extends BorderPane{
	 private Button loginButton;
	 private Button signUpButton;
	 private Login logininfo;
	 private loginButtons allLoginButtons;
	 String uri = "mongodb+srv://Wumboon:Cowperson10@cluster0.wpppozd.mongodb.net/?retryWrites=true&w=majority";
	 Stage primaryStage;
	 LoginFrame() {
		
	 }
	 LoginFrame(Stage _primaryStage){
		 primaryStage=_primaryStage;
		 logininfo=new Login();
		 allLoginButtons = new loginButtons();
		 this.setCenter(logininfo);
	     this.setBottom(allLoginButtons);
	     loginButton=allLoginButtons.getLoginButton();
	     signUpButton=allLoginButtons.getSignUpButton();
		 addListeners();
	 }
	 private void addListeners() {
		 signUpButton.setOnAction(e -> {
			 Stage stage = new Stage();
			 	SignUpFrame root = new SignUpFrame(stage);
	            stage.setTitle("New User Signup");
	            stage.setScene(new Scene(root, 450, 300));
	            stage.show(); // Close the window

	        });
		 loginButton.setOnAction(e -> {
			 if(checkLogin(logininfo.getUserName(),logininfo.getUserPassword())){ //checks login info returns true if user exists	 
			 System.out.println("Login Successful");
			 AppFrame root = new AppFrame();
			 Stage stage = new Stage();
	         stage.setTitle("PantryPal");
	         stage.setScene(new Scene(root, 1200, 600));
	         stage.show();// Close the window
	         primaryStage.close();
	       }  
	       else{
	        System.out.println("No Account Found");                      
	       		}
	        });
	    }
public boolean checkLogin(String username, String password) {
	try (MongoClient mongoClient = MongoClients.create(uri)) {
        MongoDatabase sampleTrainingDB = mongoClient.getDatabase("Accounts");
        MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("UserInfo");
        
        Document student1 = gradesCollection.find(new Document("username", username)).first();                
        if(student1!=null) {
        	if(student1.get("password").equals(password)) {
        		return true;
        	}
        	return false;
        }
        else
        	return false;            
        
    	}
	}
}



class loginButtons extends HBox{
	 private Button loginButton;
	 private Button signUpButton;
	 loginButtons(){
		 this.setPrefSize(500, 60);
	     this.setStyle("-fx-background-color: #F0F8FF;");
	     this.setSpacing(15);
	     String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
	     loginButton = new Button("Login"); // text displayed on add button
	     loginButton.setStyle(defaultButtonStyle); // styling the button
	     
	     signUpButton = new Button("Signup"); // text displayed on clear recipes button
	     signUpButton.setStyle(defaultButtonStyle);
	     
	     this.getChildren().addAll(loginButton,signUpButton); // adding buttons to footer
	     this.setAlignment(Pos.CENTER); 
	 }
	 
	 public Button getLoginButton() {
	        return loginButton;
	    }
	    
	 public Button getSignUpButton() {
	        return signUpButton;
	    }

}



public class Login extends VBox{
	 private TextField loginUsername;
	 private TextField loginPassword;
	 Login(){
		 this.setSpacing(5); // sets spacing between tasks
	        this.setPrefSize(400, 560);
	        //this.setStyle("-fx-background-color: #FFFF00;");
	        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

	        loginUsername = new TextField();
	        loginUsername.setPrefSize(380, 20); // set size of text field
	        loginUsername.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield

	        loginUsername.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
	        loginUsername.setPromptText("Input username here");

	        loginPassword = new TextField();
	        loginPassword.setPrefSize(380, 20); // set size of text field
	        loginPassword.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
	        
	        loginPassword.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
	        loginPassword.setPromptText("Input password here");
	        this.getChildren().addAll(loginUsername,loginPassword);
	        this.setSpacing(15);
	        this.setAlignment(Pos.CENTER); 
	        
	 }
	 public String getUserName() {
		 return loginUsername.getText();
	 }
	 public String getUserPassword() {
		 return loginPassword.getText();
	 }
}
