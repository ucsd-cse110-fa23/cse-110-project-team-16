import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

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

	}

	@Test 
	void userAccountLoginisNotValid() {
		String userName = "IncorrectUserName";
		String password = "IncorrectPassword";
	}
}
