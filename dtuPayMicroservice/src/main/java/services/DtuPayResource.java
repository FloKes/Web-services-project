package services;


import businessLogic.PayService;
import domain.Payment;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/payments")
public class DtuPayResource {

	PayService payService;

	public DtuPayResource() {
		payService = PayService.getInstance();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Payment> getPayments() {
		return payService.getPayments();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response pay(Payment payment) throws Exception {
		if( !payService.hasDTUPayaccount( payment.getCid() ) ){
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("customer with id " + payment.getCid() + " is unknown").build();
		}
		else if( !payService.hasDTUPayaccount( payment.getMid()) ){
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("merchant with id " + payment.getMid() + " is unknown").build();
		}

		int id = payService.getNewPaymentId();
		payment.setId(id);
		try{
			payService.addPayment(payment);
		} catch(Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(e.getMessage()).build();
		}

		return Response.created(new URI("/payments/" + id)).build();
	}

}
