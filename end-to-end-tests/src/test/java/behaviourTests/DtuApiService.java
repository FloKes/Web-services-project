package behaviourTests;

import behaviourTests.accounts.AccountDTO;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DtuApiService {

    WebTarget baseUrl;

    public DtuApiService() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }

    public String getToken(String customerId) {
        Response response = baseUrl.path("dtuPayApi/tokenManager/" + customerId + "/token")
                .request(MediaType.APPLICATION_JSON)
                .get();
        var token= response.readEntity(String.class);
        System.out.println(token);
        return token;
    }

    public AccountDTO requestAccount(AccountDTO accountDTO) {
        Client client = ClientBuilder.newClient();
        WebTarget r = client.target("http://localhost:8080/dtuPayApi");
        var response = r.path("/accounts").request().post(Entity.json(accountDTO), AccountDTO.class);
        return response;
    }



//    public Response createUser(UserRest user){
//
//        Response response = baseUrl.path("users")
//                .request(MediaType.APPLICATION_JSON)
//                .accept(MediaType.TEXT_PLAIN)
//                .post(Entity.entity(user, MediaType.APPLICATION_JSON));
//        return response;
//    }
//
//    public Response getBalance(String userId) {
//        Response response = baseUrl.path("users/balance")
//                .request(MediaType.APPLICATION_JSON)
//                .accept(MediaType.TEXT_PLAIN)
//                .post(Entity.entity(userId, MediaType.APPLICATION_JSON));
//        return response;
//    }
//
//    public List<Payment> getPayments() {
//        Response response = baseUrl.path("payments")
//                .request()
//                .get();
//        List<Payment> paymentList = response.readEntity(new GenericType<List<Payment>>(){});
//        return paymentList;
//    }
//
//    public Response conductPayment(UserRest cid, UserRest mid, Integer amount, String description) {
//        Payment payment = new Payment();
//        payment.setCid(cid);
//        payment.setMid(mid);
//        payment.setAmount(amount);
//        payment.setDescription(description);
//        Response response = baseUrl.path("payments")
//                .request()
//                .post(Entity.entity(payment, MediaType.APPLICATION_JSON));
//        return response;
//    }

    public String getStringFromResponse(Response response) {
        String uri = response.getLocation().toString();
        return uri.substring(uri.lastIndexOf('/') + 1).trim();
    }

    public void closeResponse(Response response) {
        response.close();
    }
}
