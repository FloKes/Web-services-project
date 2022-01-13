package services;


import businessLogic.FacadeService;
import domain.Payment;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/dtuPay")
public class DtuPayResource {

	FacadeService facadeService;

	public DtuPayResource() {
		facadeService = FacadeService.getInstance();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Payment> getPayments() {
		return facadeService.getPayments();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response pay(Payment payment) throws Exception {
		if( !facadeService.hasDTUPayaccount( payment.getCid() ) ){
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("customer with id " + payment.getCid() + " is unknown").build();
		}
		else if( !facadeService.hasDTUPayaccount( payment.getMid()) ){
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("merchant with id " + payment.getMid() + " is unknown").build();
		}

		int id = facadeService.getNewPaymentId();
		payment.setId(id);
		try{
			facadeService.addPayment(payment);
		} catch(Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(e.getMessage()).build();
		}

		return Response.created(new URI("/payments/" + id)).build();
	}

}
