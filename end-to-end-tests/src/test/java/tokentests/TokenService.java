package tokentests;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TokenService {
    public Token requestToken(String customerId) {
        Client client = ClientBuilder.newClient();
        WebTarget r = client.target("http://localhost:8080/dtuPayApi");
        var response = r.path("/tokens/"+ customerId+ "/token").request(MediaType.APPLICATION_JSON_TYPE).get(Token.class);
        return response;
    }
}
