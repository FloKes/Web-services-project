package dtuPayApi.service.adapter.rest;

import dtuPayApi.service.Token;
import dtuPayApi.service.services.TokenService;
import dtuPayApi.service.factories.TokenFactory;

import javax.ws.rs.*;

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
