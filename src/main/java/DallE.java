import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;


public class DallE {
	
	private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations";
	private static final String API_KEY = "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";
	private static final String MODEL = "dall-e-2";
	
	public static String generateImage(String prompt)
		throws IOException, InterruptedException, URISyntaxException {
		
		// Set request parameters
		//String prompt = "Ham and Cheese Sandwich";
		int n = 1;
		
		// Create a request body which you will pass into request object
		JSONObject requestBody = new JSONObject();
		requestBody.put("model", MODEL);
		requestBody.put("prompt", prompt);
		requestBody.put("n", n);
		requestBody.put("size", "256x256");
		
		// Create the HTTP client
		HttpClient client = HttpClient.newHttpClient();
		
		// Create the request object
		HttpRequest request = HttpRequest
			.newBuilder()
			.uri(URI.create(API_ENDPOINT))
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
        
        JSONArray data = responseJson.getJSONArray("data");
        String generatedImageURL = data.getJSONObject(0).getString("url");

		return generatedImageURL;

	}

	public static String generateImageMock(String prompt)
		throws IOException, InterruptedException, URISyntaxException {
		
		// Set request parameters
		//String prompt = "Ham and Cheese Sandwich";
		int n = 1;
		
		// Create a request body which you will pass into request object
		JSONObject requestBody = new JSONObject();
		requestBody.put("model", MODEL);
		requestBody.put("prompt", prompt);
		requestBody.put("n", n);
		requestBody.put("size", "256x256");
		
		// Create the HTTP client
		HttpClient client = HttpClient.newHttpClient();
		
		// Create the request object
		HttpRequest request = HttpRequest
			.newBuilder()
			.uri(URI.create(API_ENDPOINT))
			.header("Content-Type", "application/json")
			.header("Authorization", String.format("Bearer %s", API_KEY))
			.POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
			.build();

		
		//! Mock the responseBody for testing purposes
		// Process the response
		String responseBody = "{'data': }";

		String generatedImageURL = "https://testing.com";

		return generatedImageURL;
	}
}