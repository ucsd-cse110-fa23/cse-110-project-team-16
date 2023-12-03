import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
    private static String uri = "mongodb+srv://Wumboon:Cowperson10@cluster0.wpppozd.mongodb.net/?retryWrites=true&w=majority";
    private static MongoClient mongoClient;
    private static MongoDatabase recipesDB;
    private static String currUser;

    // connect to mongoDB
    public static void startMongoDB(String user) {
        try {
            mongoClient = MongoClients.create(MongoDB.getURI());
            recipesDB = mongoClient.getDatabase("Recipes");
            currUser = user;
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public static String getURI() {
        return uri;
    }

    // Add recipe to MongoDB
    public static ObjectId addRecipe(String name, String type, String ingredients, String directions, String image) {
        MongoCollection<Document> userCollection = recipesDB.getCollection(currUser);

        if(userCollection == null)
            return null;

        ObjectId id = new ObjectId();
        Document recipe = new Document("_id", id);
        recipe.append("name", name);
        recipe.append("type", type);
        recipe.append("ingredients", ingredients);
        recipe.append("directions", directions);
        recipe.append("image", image);
        userCollection.insertOne(recipe);

        return id;
    }

    // Retreive recipe from MongoDB
    public static List<String> getRecipe(ObjectId recipeID) {
        MongoCollection<Document> userCollection = recipesDB.getCollection(currUser);
        Document recipe = userCollection.find(new Document("_id", recipeID)).first();
        if (recipe != null) {
            String name = (String)recipe.get("name");
            String type = (String)recipe.get("type");
            String ingredients = (String)recipe.get("ingredients");
            String directions = (String)recipe.get("directions");
            String imageLocation = base64ToImg(name, (String)recipe.get("image"));

            List<String> recipeSet = new ArrayList<String>();
            recipeSet.add(name);
            recipeSet.add(type);
            recipeSet.add(ingredients);
            recipeSet.add(directions);
            recipeSet.add(imageLocation);

            return recipeSet;
        }
        else {
            return null;
        }
    }

    // Change recipe on MongoDB
    public static boolean editRecipe(ObjectId id, String name, String type, String ingredients, String directions) {
        MongoCollection<Document> userCollection = recipesDB.getCollection(currUser);
        Document existingRecipe = userCollection.find(new Document("_id", id)).first();

        if (existingRecipe != null) {
            Bson filter = eq("_id", id);
            Bson newName = set("name", name);
            Bson newType = set("type", type);
            Bson newIngredients = set("ingredients", ingredients);
            Bson newDirections = set("directions", directions);

            Bson updates = combine(newName, newType, newIngredients, newDirections);
            System.out.println(updates);
            userCollection.findOneAndUpdate(filter, updates);

            return true;
        }
        else {
            Document recipe = new Document("_id", new ObjectId());
            recipe.append("name", name);
            recipe.append("type", type);
            recipe.append("ingredients", ingredients);
            recipe.append("directions", directions);
            userCollection.insertOne(recipe);
            return false;
        }
    }

    // delete recipe on MongoDB
    public static boolean deleteRecipe(String name) {
        MongoCollection<Document> userCollection = recipesDB.getCollection(currUser);
        Document existingRecipe = userCollection.find(new Document("name", name)).first();

        if (existingRecipe != null) {
            Bson filter = eq("name", name);
            userCollection.deleteOne(filter);

            return false;
        }
        else {
            return false;            
        }
    }

    // get all recipes for current user
    public static Set<Document> listRecipes() {
        MongoCollection<Document> userCollection = recipesDB.getCollection(currUser);
        FindIterable<Document> documentCursor = userCollection.find();
        
        Set<Document> recipeFiles = new HashSet<Document> ();

        // Iterate over collection
        for(Document doc : documentCursor)
            recipeFiles.add(doc);

        return recipeFiles;
    }

    // Convert base64 string to jpg - helper method for retreiving recipe's image
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

        try (OutputStream oStream = new BufferedOutputStream(new FileOutputStream(file))){
            oStream.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }
}
