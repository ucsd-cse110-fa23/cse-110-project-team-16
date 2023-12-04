import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


public class Server {


 // initialize server port and hostname
 private static final int SERVER_PORT = 8100;
 private static final String SERVER_HOSTNAME = "localhost";


 public static void main(String[] args) throws IOException {

    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    Map<String, String> data = new HashMap<>();


   // create a server
    HttpServer server = HttpServer.create(
     new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 0);

     HttpHandler handler = new RequestHandler(data);
     // create the context
     HttpContext context = server.createContext("/path", handler); // Replace "/path" with the desired context path

     // set the executor
     server.setExecutor(threadPoolExecutor);


     // start the server
     server.start();
    System.out.println("Server started on port " + SERVER_PORT);



 }
}
