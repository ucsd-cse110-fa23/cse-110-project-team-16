import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.bson.types.ObjectId;

public class RecipeManager {
    private ArrayList<Recipe> allRecipes;
    private String db_dir = "localDB/";

    public RecipeManager(ArrayList<Recipe> allRecipes) {
        this.allRecipes = allRecipes;
    }

    public void deleteRecipe(Recipe recipe) {
        if (recipe == null) return;

        ObjectId recipeID = recipe.getRecipeID();
        String recipeName = recipe.getRecipeName();

        // Delete the recipe file
        deleteFile(db_dir + recipeName + ".txt");

        // Delete the recipe image
        deleteFile("images/" + recipeName + ".jpg");

        // Remove from MongoDB
        MongoDB.deleteRecipe(recipeID);

        // Remove from the list
        allRecipes.remove(recipe);
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.delete()) {
            System.out.println("Deleted file: " + filePath);
        } else {
            System.out.println("Failed to delete file: " + filePath);
        }
    }
}

