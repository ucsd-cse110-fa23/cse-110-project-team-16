import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class RequestHandler implements HttpHandler {


 private final Map<String, String> data;


 public RequestHandler(Map<String, String> data) {
   this.data = data;
 }

 public void handle(HttpExchange httpExchange) throws IOException {
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();
    try {
        if (method.equals("GET")) {
          response = handleGet(httpExchange);
        } else if (method.equals("POST")) {
          response = handlePost(httpExchange);
        }
        else if (method.equals("/recording")) {
          response = handleRecording(httpExchange);
        }
        else if (method.equals("/whisper")) {
          response = handlePost(httpExchange);
        }
        else if (method.equals("/login")) {
          response = handlePost(httpExchange);
        }
        else if (method.equals("/chatGPT")) {
          response = handlePost(httpExchange);
        }
        else if (method.equals("/recordings")) {
          response = handlePost(httpExchange);
        }
        else if (method.equals("/recipes")) {
          response = handlePost(httpExchange);
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

 private String handleRecording(HttpExchange httpExchange) {
    String response = "Invalid GET request";
    return response;
}

private String handleGet(HttpExchange httpExchange) throws IOException {
    String response = "Invalid GET request";
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();
    if (query != null) {
      String value = query.substring(query.indexOf("=") + 1);
      String year = data.get(value); // Retrieve data from hashmap
      if (year != null) {
        response = year;
        System.out.println("Queried for " + value + " and found " + year);
      } else {
        response = "No data found for " + value;
      }
    }
    return response;
  }

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
 
 
 
}
