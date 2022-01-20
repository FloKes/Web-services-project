package behaviourTests;

import behaviourTests.dtos.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DtuApiService {

    WebTarget baseUrl;
    Client client;

    public DtuApiService() {
        client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/dtuPayApi");
    }

    public String getToken(String customerId) {
        //TODO: maybe delete?
        Response response = baseUrl.path("dtuPayApi/tokenManager/" + customerId + "/token")
                .request(MediaType.APPLICATION_JSON)
                .get();
        var token= response.readEntity(String.class);
        System.out.println(token);
        return token;
    }

    public Response requestAccount(AccountDTO accountDTO) {
//        Client client = ClientBuilder.newClient();
//        WebTarget r = client.target("http://localhost:8080/dtuPayApi");
//        var response = r.path("/customer/accounts").request().post(Entity.json(accountDTO), AccountDTO.class);
        var response = baseUrl.path("/customer/accounts").request().post(Entity.json(accountDTO));
        return response;
    }

    public TokenIdDTO requestToken(String customerId) {
//        Client client = ClientBuilder.newClient();
//        WebTarget r = client.target("http://localhost:8080/dtuPayApi");
        var response = baseUrl.path("/customer/tokens/" + customerId + "/tokens").request(MediaType.APPLICATION_JSON_TYPE).get(TokenIdDTO.class);
        return response;
    }

    public ReportDTO requestCustomerReport(String customerId) {
//        Client client = ClientBuilder.newClient();
//        WebTarget r = client.target("http://localhost:8080/dtuPayApi");
        var response = baseUrl.path("/customer/reports/" + customerId).request(MediaType.APPLICATION_JSON_TYPE).get(ReportDTO.class);
        return response;
    }

    public MerchantReportDTO requestMerchantReport(String customerId) {
//        Client client = ClientBuilder.newClient();
//        WebTarget r = client.target("http://localhost:8080/dtuPayApi");
        var response = baseUrl.path("/merchant/reports/" + customerId).request(MediaType.APPLICATION_JSON_TYPE).get(MerchantReportDTO.class);
        return response;
    }

    public ReportDTO requestManagerReport() {
//        Client client = ClientBuilder.newClient();
//        WebTarget r = client.target("http://localhost:8080/dtuPayApi");
        var response = baseUrl.path("/manager/reports/").request(MediaType.APPLICATION_JSON_TYPE).get(ReportDTO.class);
        return response;
    }

    public Response requestPayment(PaymentDTO paymentDTO) {
//        Client client = ClientBuilder.newClient();
//        WebTarget r = client.target("http://localhost:8080/dtuPayApi");
        var merchantId = paymentDTO.getMerchantId();
        var response = baseUrl.path("/merchant/payments/" + merchantId + "/payments").request().post(Entity.json(paymentDTO));
        return response;
    }

    public Response deleteCustomerAccount(String accountId) {
        Client client = ClientBuilder.newClient();
        WebTarget r = client.target("http://localhost:8080/dtuPayApi");
        var response = r.path("/customer/accounts/" + accountId ).request().delete();
        return response;
    }



    public Response deleteMerchantAccount(String accountId) {
        Client client = ClientBuilder.newClient();
        WebTarget r = client.target("http://localhost:8080/dtuPayApi");
        var response = r.path("/merchant/accounts/" + accountId ).request().delete();
        return response;
    }

    public String getStringFromResponse(Response response) {
        String uri = response.getLocation().toString();
        return uri.substring(uri.lastIndexOf('/') + 1).trim();
    }

    public void closeResponse(Response response) {
        response.close();
    }

    public  void closeClient() {client.close();}
}
