import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MongoRecipeTests {

    @BeforeAll
    static void setUp() {
        MongoDB.startWithUser("testing");
    }

    @Test
    void addRecipe() {
        // add recipe to db
        ObjectId id = MongoDB.addRecipe("testName", "testType", "testIngred", "testDirec", "testImg");
        assertNotEquals(null, id);
    }

    @Test
    void getRecipe() {
        // get recipe from db using known id
        ObjectId id = new ObjectId("656c2ca9cbb07e684c01a78d");
        List<String> recipe = MongoDB.getRecipe(id);
        assertNotEquals(null, recipe);
    }

    @Test
    void checkRecipeContents() {
        // get recipe from db using known id
        ObjectId id = new ObjectId("656c2ca9cbb07e684c01a78d");
        List<String> recipeSet = MongoDB.getRecipe(id);
        String name = recipeSet.get(0);
        String type = recipeSet.get(1);
        String ingredients = recipeSet.get(2);
        String directions = recipeSet.get(3);
        String imageLocation = recipeSet.get(4);

        // check contents
        assertEquals("testName", name);
        assertEquals("testType", type);
        assertEquals("testIngred", ingredients);
        assertEquals("testDirec", directions);
        assertEquals("testImg", imageLocation);
    }

    @Test
    void editRecipe() {
        // edit recipe on db using known id
        ObjectId id = new ObjectId("656c2d353e3f682bfd338e2f");
        boolean status = MongoDB.editRecipe(id, "testName", "testType", "testIngred", "testDirec");

        assertEquals(true, status);
    }

    @Test
    void checkEditedRecipeContents() {
        // edit recipe on db using known id
        ObjectId id = new ObjectId("656c2d353e3f682bfd338e2f");
        MongoDB.editRecipe(id, "newName", "newType", "newIngred", "newDirec");

        // get edited recipe
        List<String> recipeSet = MongoDB.getRecipe(id);
        String name = recipeSet.get(0);
        String type = recipeSet.get(1);
        String ingredients = recipeSet.get(2);
        String directions = recipeSet.get(3);

        // check updated contents
        assertEquals("newName", name);
        assertEquals("newType", type);
        assertEquals("newIngred", ingredients);
        assertEquals("newDirec", directions);
    }

    @Test
    void deleteRecipe() {
        // create recipe to be deleted
        ObjectId id = MongoDB.addRecipe("testName", "testType", "testIngred", "testDirec", "testImg");
        boolean status = MongoDB.deleteRecipe(id);

        assertEquals(true, status);
    }

    @Test
    void checkRecipeDeleted() {
        // create recipe to be deleted
        ObjectId id = MongoDB.addRecipe("testName", "testType", "testIngred", "testDirec", "testImg");
        MongoDB.deleteRecipe(id);

        // check recipe no longer exists
        List<String> recipe = MongoDB.getRecipe(id);
        assertEquals(null, recipe);
    }

}
