package dtuPayApi.service.adapter.rest;

import dtuPayApi.service.dtos.AccountDTO;
import dtuPayApi.service.dtos.TokenIdDTO;
import dtuPayApi.service.factories.AccountFactory;
import dtuPayApi.service.factories.TokenFactory;
import dtuPayApi.service.services.AccountService;
import dtuPayApi.service.services.TokenService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;

@Path("/customer")
public class CustomerResource {
    AccountService accountService = new AccountFactory().getService();
    TokenService tokenService = new TokenFactory().getService();

    @Path("/accounts")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDTO addAccount(AccountDTO accountDTO) throws URISyntaxException {
        var accountDTOProvided = accountService.requestAccount(accountDTO);
        return accountDTOProvided;
    }

    @Path("/tokens/{customerId}/tokens")
    @GET
    @Produces("application/json")
    public TokenIdDTO requestTokens(@PathParam("customerId") String customerId) throws URISyntaxException {
        System.out.println("Facade customer id: " + customerId);
        var tokenIdDTO = tokenService.requestTokenId(customerId);
        return tokenIdDTO;
    }
}
