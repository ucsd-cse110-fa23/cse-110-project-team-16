
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import org.bson.types.ObjectId;

public class MyHandler implements HttpHandler {


    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        OutputStream outputStream = httpExchange.getResponseBody();

        // this line is a must
        httpExchange.sendResponseHeaders(200, response.length());

        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String id = query.substring(query.indexOf("=") + 1);

            // Read the content of the HTML file

            MongoDB.start();
            String recipe = MongoDB.getRecipe(new ObjectId(id)).get(0);
            System.out.println("Working Directory = " + System.getProperty("user.dir"));

            String htmlContent = readHtmlFile("src/main/java/index.html");

            // Replace placeholders in the HTML with dynamic data
            response = htmlContent.replace("{{recipeName}}", recipe);
        }
        return response;
    }

    private String readHtmlFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("File not found: " + filePath);
        }
    }

}