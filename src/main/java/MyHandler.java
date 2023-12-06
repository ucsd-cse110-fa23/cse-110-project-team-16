
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

            List<String> recipeSet = MongoDB.getRecipe(new ObjectId(id));

            String name = recipeSet.get(0);
            String type = recipeSet.get(1);
            String ingredients = recipeSet.get(2);
            String directions = recipeSet.get(3);
            String image = recipeSet.get(4);
            String imageLocation = base64ToImg(name, image);

            response = readHtmlFile("src/main/java/index.html");

            // Replace placeholders in the HTML with dynamic data
            response = response.replace("{{recipeName}}", name);
            response = response.replace("{{recipeType}}", type);
            response = response.replace("{{recipeIngredients}}", ingredients);
            response = response.replace("{{recipeDirections}}", directions);
            response = response.replace("{{imageLocation}}", image);
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

        try (OutputStream oStream = new BufferedOutputStream(new FileOutputStream(file))) {
            oStream.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }

}