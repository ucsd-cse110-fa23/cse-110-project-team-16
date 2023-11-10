package src.main.java;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
// Test for Guy
public class ChatGPT extends Application {
	private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
	private static final String API_KEY = "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";
	private static final String MODEL = "text-davinci-003";
	private static String prompt="I want a recipe made from ";
	private static int maxTokens = 1000;
	private static String[] recipeDeconstructed={" ", " ", " "};
//	ChatGPT(String _prompt){
//		System.out.println(" IN CONSTRUCTOR");
//		prompt+=_prompt;
//	}
	public String[] generatedRecipe(String _prompt)  throws IOException, InterruptedException{
		prompt+=_prompt;
		JSONObject requestBody = new JSONObject();
		requestBody.put("model", MODEL);
		requestBody.put("prompt", prompt);
		requestBody.put("max_tokens", maxTokens);
		requestBody.put("temperature", 1.0);
		//System.out.println(prompt);
		HttpClient client = HttpClient.newHttpClient();
		// Create the request object
		URI myUri = URI.create(API_ENDPOINT);
		HttpRequest request = HttpRequest
		.newBuilder()
		.uri(myUri)
		.header("Content-Type", "application/json")
		.header("Authorization", String.format("Bearer %s", API_KEY))
		.POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))	
		.build();
		// Send the request and receive the response
		HttpResponse<String> response = client.send(
		request,
		HttpResponse.BodyHandlers.ofString()
		);
		
		// Process the response
	    String responseBody = response.body();
	    JSONObject responseJson = new JSONObject(responseBody);


	    JSONArray choices = responseJson.getJSONArray("choices");
	    String generatedText = choices.getJSONObject(0).getString("text");
	    recipeDeconstructed=parseRecipe(generatedText);
	    return recipeDeconstructed;
	}
	public static String[] parseRecipe(String generatedText) {
		String[] returnString= {" "," "," "};
		String[] arr=generatedText.split("Ingredients");
		 String[] arr2=arr[1].split("Instructions");
		 String title=arr[0];
		 String temp=arr2[0];
		 String ingredients=temp.replace(":","");
		 temp=arr2[1];
		 String instructions=temp.replace(":","");
		 returnString[0]=title;
		 returnString[1]=ingredients;
		 returnString[2]=instructions;
//		 System.out.println(title+" TITLE ");
//		 System.out.println(ingredients+" INGREDIENTS ");
//		 System.out.println(instructions+" INSTRUCTIONS ");
		return returnString;
	}

	public String[] getDecontructed() {
		return recipeDeconstructed;
	}
@Override
public void start(Stage arg0) throws Exception {
	// TODO Auto-generated method stub
	
}
}
