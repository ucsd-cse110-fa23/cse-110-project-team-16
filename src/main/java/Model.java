
import static com.mongodb.client.model.Filters.eq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.scene.image.Image;

import java.net.URI;


public class Model {
    public String performRequest(RecipeDetails recipeDetails) {


        try {
            String urlString = "http://localhost:8100/";
            String recipeName = recipeDetails.getTitleText().getText();
            String recipeType = recipeDetails.getDisplayType().getText();
            String recipeIngredients = recipeDetails.getDisplayIngredients().getText();
            String recipeDirections = recipeDetails.getDisplayDirections().getText();
            Image recipeImage = recipeDetails.getDisplayImageView().getImage();
            if (recipeName != null) {
                urlString += findID(recipeName);
            }
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    private String findID(String recipeName) {
        String recipeID = "";
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
                return recipeID;
            } else {
                return "Recipe not found.";
            }
		}
    }
}