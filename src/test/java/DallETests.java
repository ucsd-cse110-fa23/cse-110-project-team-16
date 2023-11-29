import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class DallETests {
    DallE testE = new DallE();

    @Test
	void testGenerateImage() {
        String testedURL = "wrong";
        try {
            testedURL = DallE.generateImageMock("whatever");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		assertEquals("https://testing.com", testedURL);
	}
}
