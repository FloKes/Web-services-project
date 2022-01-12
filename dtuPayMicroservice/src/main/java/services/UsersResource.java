package services;

import businessLogic.PayService;
import domain.Payment;
import domain.UserRest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/users")
public class UsersResource {
    PayService payService;

    public UsersResource() {
        payService = PayService.getInstance();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(UserRest user) throws Exception {
        String userId = payService.createUser(user);
        if (userId.equals("User does not have a bank account")) {
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("User does not have a bank account").build());
        }
        user.setUserId(userId);
        payService.addUser(user);
        return Response.created(new URI("/users/" + userId)).build();
    }

    @POST
    @Path("/balance")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getBalance(String userId) throws Exception {
        String balance = payService.getBalance(userId);
        if (balance.equals("User does not have a bank account")) {
            //TODO update to actual exception message
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("User does not have a bank account").build());
        }
        return Response.created(new URI("/users/" + balance)).build();
    }
}
