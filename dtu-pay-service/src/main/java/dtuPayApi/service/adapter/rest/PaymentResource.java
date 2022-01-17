package dtuPayApi.service.adapter.rest;

import dtuPayApi.service.dtos.PaymentDTO;
import dtuPayApi.service.factories.PaymentFactory;
import dtuPayApi.service.services.PaymentService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/payments")
public class PaymentResource {

    PaymentService paymentService = new PaymentFactory().getService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPayment(PaymentDTO paymentDTO) throws URISyntaxException {
        PaymentDTO providedPaymentDTO = paymentService.addPayment(paymentDTO);
        return Response.created(new URI("/payments/" + providedPaymentDTO.getCorrelationId().toString())).build();
    }
}
