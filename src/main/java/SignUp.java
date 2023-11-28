//package src.main.java;

import java.util.Random;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

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
	 String uri = "mongodb+srv://Wumboon:Cowperson10@cluster0.wpppozd.mongodb.net/?retryWrites=true&w=majority";
	 private Button createAccountButton;
	 private SignUp signupinfo;
	 private signupButtons allSignUpButtons;
	 Stage currStage;
	 SignUpFrame(Stage stage){
     	 currStage=stage;
		 signupinfo=new SignUp();
		 allSignUpButtons = new signupButtons();
		 this.setCenter(signupinfo);
	     this.setBottom(allSignUpButtons);
	     createAccountButton=allSignUpButtons.getCreateAccountButton();
		 addListeners();
	 }
	 SignUpFrame() {
		
	 }
	 private void addListeners() {
		 createAccountButton.setOnAction(e -> {
			 if(signupinfo.getNewPassword().equals(signupinfo.getConfirmedPassword())) {
				if (CreateAccount(signupinfo.getNewUsername(),signupinfo.getNewPassword())) {
					currStage.close();
					System.out.println("Account Successfuly Made");
				}
				else {
					System.out.println("Username already exists");
				}
			 }
			 else {
				 System.out.println("Passwords does not match confirmed password");
			 }
	        });
	    }
	
	public boolean CreateAccount(String username,String password) {
		try (MongoClient mongoClient = MongoClients.create(uri)) {   	
    		MongoDatabase sampleTrainingDB = mongoClient.getDatabase("Accounts");
        	MongoCollection<Document> userInfo = sampleTrainingDB.getCollection("UserInfo");
			Document existingStudent = userInfo.find(new Document("username", username)).first();
			if (existingStudent!=null) {
        		return false;
        	}
        	else {
				Document student = new Document("_id", new ObjectId());
        		student.append("username", username);
        		student.append("password", password);
        		userInfo.insertOne(student);
        		return true;            
    		}
		}
        	
    }
	

	public boolean deleteAccount(String username) {
		try (MongoClient mongoClient = MongoClients.create(uri)) {
        MongoDatabase sampleTrainingDB = mongoClient.getDatabase("Accounts");
        MongoCollection<Document> userInfo = sampleTrainingDB.getCollection("UserInfo");
        
        Document student1 = userInfo.find(new Document("username", username)).first();                
        if(student1!=null) {
        	userInfo.deleteOne(student1);
			return true;
        }
        else
        	return false;            
        
    	}
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
	 public String getNewUsername() {
		 return newUserName.getText();
	 }
	 public String getNewPassword() {
		 return newPassword.getText();
	 }
	 public String getConfirmedPassword() {
		 return confirmPassword.getText();
	 }
}
