package dtuPayApi.service.adapter.rest;

import dtuPayApi.service.dtos.AccountDTO;
import dtuPayApi.service.dtos.PaymentDTO;
import dtuPayApi.service.factories.AccountFactory;
import dtuPayApi.service.factories.PaymentFactory;
import dtuPayApi.service.services.AccountService;
import dtuPayApi.service.services.PaymentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/merchant")
public class MerchantResource {
    AccountService accountService = new AccountFactory().getService();
    PaymentService paymentService = new PaymentFactory().getService();

    @Path("/accounts")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAccount(AccountDTO accountDTO) throws URISyntaxException {
        var accountDTOProvided = accountService.requestAccount(accountDTO);
        return accountDTOProvided.getErrorMessage() == null ? Response.created(new URI("customer/accounts/" + accountDTO.getAccountId())).entity(accountDTOProvided).build()
                : Response.status(Response.Status.CONFLICT).entity(accountDTOProvided.getErrorMessage()).build();
    }

    @Path("/payments/{merchantId}/payments")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPayment(@PathParam("merchantId") String merchantId, PaymentDTO paymentDTO) throws URISyntaxException {
        PaymentDTO returnedPaymentDTO = paymentService.addPayment(paymentDTO); // successful payment returns a paymentDTO with id and description

        return returnedPaymentDTO.getErrorDescription() == null ? Response.created(new URI("/payments/" + returnedPaymentDTO.getPaymentId())).build()
                : Response.status(Response.Status.BAD_REQUEST).entity(returnedPaymentDTO.getErrorDescription()).build();
    }
}
