package jevent;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import javax.ws.rs.client.Entity;
import org.glassfish.jersey.client.ClientConfig;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import javax.xml.ws.http.HTTPException;

import jdatabase.*;

public class jeventClient {
	private static String eventsyncURI = "http://localhost:8084/mybank";
	private static WebTarget service = null;
	private static String eventdestination;
	private static Client client;

	public jeventClient(String serviceName) {
		ClientConfig config = new ClientConfig();
		client = ClientBuilder.newClient(config);
		service = client.target(getBaseURI(eventsyncURI));
		eventdestination = serviceName;
	}

	public static void main(String args[]) {
		System.out.println("Event client is initialized sync events at: " + eventsyncURI);
	}

	public Response sendEvent(String data) {
		String output = "";

		try {
			data = data.replace("\n", "").replace("\\r", "").replace("\t", "");
			Object obj = new JSONParser().parse(data);
			JSONObject inputJsonObj = (JSONObject) obj;

			Response resp = service.path("event").path("sync").request(MediaType.APPLICATION_JSON)
					.post(Entity.json(inputJsonObj.toString()));

			output = resp.readEntity(String.class);
			System.out.println(output);
		} catch (Exception e) {
			System.out.println("Returning sendevent failure -");
			System.out.println("[ {'error':'" + e.toString() + "'}]");
			return sendjsonresponse("[ {'error':'" + e.toString() + "'}]");
		}
		return sendjsonresponse(output);

	}

	// syncevents are raised by eventsync. It should be processed by microservices
	// as needed
	// the component seeking to sync the event also passes the database into which
	// the event needs to be synced
	public String syncEvent(String eventdata, dbsql db) {
		// store the event in local database as per dbConfigFileName
		// return status 0, 1
		String query, jsonresult;
		String eventsource = "";

		// this block will get the eventsource
		try {
			eventdata = eventdata.replace("\n", "").replace("\\r", "").replace("\t", "");
			Object obj = new JSONParser().parse(eventdata);
			JSONObject jsonObj = (JSONObject) obj;
			eventsource = (String) jsonObj.get("source");
		} catch (Exception e) {
			System.out.println("Returning sendevent failure -");
			System.out.println("[ {'error':'" + e.toString() + "'}]");
		}

		// eventstatus = 0 means event is saved and needs to be processed
		// eventdirection = 2 since its a received event, waiting to be synched locally

		// json data needs the escape character to understand single quote contained
		// within query
		eventdata = eventdata.replace("'", "\\'");
		query = "INSERT INTO tevents (eventsource, eventdestination, eventdata, eventstatus, eventdirection)  VALUES ('"
				+ eventsource + "','" + eventdestination + "','" + eventdata + "', 0, 2);";
		System.out.println("Event query being fired: " + query);
		jsonresult = db.executequery(query); // return json result from the query
		System.out.println("jeventClient executequery result: " + jsonresult);
		return jsonresult;
	}

	public Response broadcastEvent(String data, String uri) {
		String output = "";

		try {
			System.out.println("Broadcasting to service at: " + uri);

			data = data.replace("\n", "").replace("\\r", "").replace("\t", "");
			Object obj = new JSONParser().parse(data);
			JSONObject inputJsonObj = (JSONObject) obj;

			WebTarget broadcasttoservice = client.target(uri);

			Response resp = broadcasttoservice.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(inputJsonObj.toString()));

			output = resp.readEntity(String.class);
			System.out.println(output);
		} catch (Exception e) {
			System.out.println("Returning broadcast event failure");
			System.out.println("[ {'error':'" + e.toString() + "'}]");
			return sendjsonresponse("[ {'error':'" + e.toString() + "'}]");
		}

		return sendjsonresponse(output);
	}

	private static URI getBaseURI(String uri) {
		return UriBuilder.fromUri(uri).build();
	}

	public Response sendjsonresponse(String output) {
		String data = "";
		try {
			data = output;
			return Response.status(200).entity(data).build();
		} catch (Exception e) {
			throw new HTTPException(400);
		}
	}
}
