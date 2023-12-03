import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MongoRecipeTests {

    @BeforeAll
    static void setUpMongoDB() {
        MongoDB.startMongoDB("testing");
    }

    @Test
    void addRecipeMongoDB() {
        // add recipe to db
        ObjectId id = MongoDB.addRecipe("testName", "testType", "testIngred", "testDirec", "testImg");
        assertNotEquals(id, null);
    }

    @Test
    void getRecipeMongoDB() {
        // get recipe from db
        ObjectId id = MongoDB.addRecipe("testName", "testType", "testIngred", "testDirec", "testImg");
        List<String> recipe = MongoDB.getRecipe(id);
        assertNotEquals(recipe, null);
    }

}
