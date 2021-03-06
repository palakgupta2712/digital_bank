package mybank;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.grizzly.http.server.StaticHttpHandler;

import java.io.IOException;
import java.net.URI;

public class mainTransactions {
    public static final String BASE_URI = "http://localhost:8083/mybank/";
    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("mybank");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();

        server.getServerConfiguration().addHttpHandler(new StaticHttpHandler("C:\\bank_soa-1.8\\digitalbankwebsite\\"), "/");
        System.out.println(String.format(
                "Jersey app started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...",
                BASE_URI));

        // Open the URI in Chrome (Windows)
       
        System.in.read();
        server.shutdownNow();
    }
}
