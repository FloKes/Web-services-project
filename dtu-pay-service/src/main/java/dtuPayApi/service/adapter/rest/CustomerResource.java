package dtuPayApi.service.adapter.rest;

import dtuPayApi.service.dtos.AccountDTO;
import dtuPayApi.service.factories.AccountFactory;
import dtuPayApi.service.services.AccountService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;

@Path("/customer")
public class CustomerResource {
    AccountService accountService = new AccountFactory().getService();

    @Path("/accounts")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDTO addAccount(AccountDTO accountDTO) throws URISyntaxException {
        var accountDTOProvided = accountService.requestAccount(accountDTO);
        return accountDTOProvided;
    }
}
