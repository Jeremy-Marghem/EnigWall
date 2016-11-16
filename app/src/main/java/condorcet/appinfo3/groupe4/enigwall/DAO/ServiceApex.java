package condorcet.appinfo3.groupe4.enigwall.DAO;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;

public class ServiceApex {

        private static WebResource service;

        private ServiceApex() {
        }

        public static WebResource getInstance() {
            if (service != null) {
                return service;
            } else {
                Client client = Client.create();
                URI uri = UriBuilder.fromUri("https://apex.oracle.com/pls/apex/mathias_dumoulin_app/enigwall").build();
                service = client.resource(uri);

                return service;
            }
        }
}
