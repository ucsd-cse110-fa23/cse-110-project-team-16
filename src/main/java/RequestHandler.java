import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class RequestHandler implements HttpHandler {


 private RecipeDetails recipeDetails;


 public RequestHandler(RecipeDetails recipeDetails) {
   this.recipeDetails = recipeDetails;
 }

 public void handle(HttpExchange httpExchange) throws IOException {
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();
    try {
        if (method.equals("GET")) {
          response = handleGet(httpExchange);
        } else if (method.equals("POST")) {
          //response = handlePost(httpExchange);
        }
        else {
          throw new Exception("Not Valid Request Method");
        }
      } catch (Exception e) {
        System.out.println("An erroneous request");
        response = e.toString();
        e.printStackTrace();
      }
      //Sending back response to the client
    httpExchange.sendResponseHeaders(200, response.length());
    OutputStream outStream = httpExchange.getResponseBody();
    outStream.write(response.getBytes());
    outStream.close();
 }


private String handleGet(HttpExchange httpExchange) throws IOException {
  if ("GET".equals(httpExchange.getRequestMethod())) {
    // Extract query parameters from the request URI
    String query = httpExchange.getRequestURI().getQuery();
    String recipeName = recipeDetails.getTitleText().getText();

    // Build HTML response
    StringBuilder htmlBuilder = new StringBuilder();
    htmlBuilder
            .append("<html>")
            .append("<body>")
            .append("<h1>")
            .append("Hello ")
            .append(recipeName)
            .append("</h1>")
            .append("</body>")
            .append("</html>");

    // Encode HTML content
    String response = htmlBuilder.toString();

    // Set headers and send response
      httpExchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = httpExchange.getResponseBody()) {
          os.write(response.getBytes());
        }
        return "";
    } else {
    // Handle other HTTP methods if needed
      httpExchange.sendResponseHeaders(405, 0); // Method Not Allowed
      return "";
    }
}
  

  private String extractNameFromQuery(String query) {
  return null;
}

/* 
  private String handlePost(HttpExchange httpExchange) throws IOException {
    InputStream inStream = httpExchange.getRequestBody();
    Scanner scanner = new Scanner(inStream);
    String postData = scanner.nextLine();
    String language = postData.substring(
      0,
      postData.indexOf(",")
    ), year = postData.substring(postData.indexOf(",") + 1);
 
 
    // Store data in hashmap
    data.put(language, year);
 
 
    String response = "Posted entry {" + language + ", " + year + "}";
    System.out.println(response);
    scanner.close();
 
 
    return response;
  }
 
 */
 
}
