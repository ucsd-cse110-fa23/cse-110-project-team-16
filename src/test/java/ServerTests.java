import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;

import javafx.stage.Stage;

public class ServerTests {
    private LoginFrame login = new LoginFrame(); 
    private SignUpFrame signUp = new SignUpFrame();
	
    @Test 
    void createAccountisValid() {
        String username = "CorrectUserName";
        String password = "CorrectPassword";

        
        assertEquals(true, signUp.CreateAccount(username, password));
        assertEquals(true, signUp.deleteAccount(username)); //returns true if delete is successful (i.e acc exists and deletes)

        
    }

    @Test 
    void createAccountisNotValidSameName() {
        String username = "ExistingUserName";
        String password = "ExistingPassword";
        signUp.CreateAccount(username, password);
        assertEquals(false, signUp.CreateAccount(username, password));
       
    }

	@Test 
	void userAccountLoginisValid() {
        String username = "Caitlin";
		String password = "password";
        signUp.CreateAccount(username, password);
        assertEquals(true, login.checkLogin(username, password));

	}

	@Test 
	void userAccountLoginisNotValidPassword() {
		String username = "Caitlin";
		String password = "password1";
        assertEquals(false, login.checkLogin(username, password));
	}
    @Test 
	void userAccountLoginisNotValid() {
		String username = "Caitlin1";
		String password = "password";
        assertEquals(false, login.checkLogin(username, password));
	}

    @Test 
	void userLoginPrefillTest() {
		String username = "PrefilledUser";
		String password = "PrefillUserPassword";
        assertEquals(false, login.checkLogin(username, password));
	}

}
