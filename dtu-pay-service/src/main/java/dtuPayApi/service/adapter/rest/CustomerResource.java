package dtuPayApi.service.adapter.rest;

import dtuPayApi.service.dtos.AccountDTO;
import dtuPayApi.service.dtos.PaymentDTO;
import dtuPayApi.service.dtos.TokenIdDTO;
import dtuPayApi.service.factories.AccountFactory;
import dtuPayApi.service.factories.TokenFactory;
import dtuPayApi.service.services.AccountService;
import dtuPayApi.service.services.TokenService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/customer")
public class CustomerResource {
    AccountService accountService = new AccountFactory().getService();
    TokenService tokenService = new TokenFactory().getService();

    @Path("/accounts")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAccount(AccountDTO accountDTO) throws URISyntaxException {
        var accountDTOProvided = accountService.requestAccount(accountDTO);
        return accountDTOProvided.getErrorMessage() == null ? Response.created(new URI("customer/accounts/" + accountDTO.getAccountId())).entity(accountDTOProvided).build()
                : Response.status(Response.Status.CONFLICT).entity(accountDTOProvided.getErrorMessage()).build();
    }

    @Path("/accounts/{accountId}")
    @DELETE
    public Response deleteAccount(@PathParam("accountId") String accountId) throws URISyntaxException {
        var accountIdProvided = accountService.deleteAccount(accountId);
        return accountIdProvided.equals(accountId) ? Response.noContent().build()
                : Response.status(Response.Status.CONFLICT).entity(accountId).build();
    }

    @Path("/tokens/{customerId}/tokens")
    @GET
    @Produces("application/json")
//    public TokenIdDTO requestTokens(@PathParam("customerId") String customerId, @QueryParam("amount") int amount) throws URISyntaxException {
//        System.out.println("Facade customer id: " + customerId + ", requested amount: " + amount);
    public Response requestTokens(@PathParam("customerId") String customerId) throws URISyntaxException {
        System.out.println("Facade customer id: " + customerId);
        var tokenIdDTO = tokenService.requestTokenId(customerId);
        return Response.status(Response.Status.OK)
                .entity(tokenIdDTO)
                .build();
//        return tokenIdDTO;
    }
}
