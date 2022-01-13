package dtuPayApi.service.adapter.rest;

import dtuPayApi.service.Token;
import dtuPayApi.service.TokenService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/tokens")
public class TokenResource {

	@Path("/{customerId}/token")
	@GET
	@Produces("application/json")
	public Token requestToken(String customerId) {
		TokenService service = new TokenFactory().getService();
		return service.requestToken(customerId);
	}
}
