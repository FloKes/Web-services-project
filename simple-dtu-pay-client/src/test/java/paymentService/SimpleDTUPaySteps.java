package paymentService;
import Service.SimpleDTUPay;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import Domain.Payment;
import dtu.ws.fastmoney.User;

import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import Domain.UserRest;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/* Hint:
 * The step classes do not do the HTTP requests themselves.
 * Instead, the tests use the class HelloService, which encapsulates the
 * HTTP requests. This abstractions help to write easier and more understandable
 * test classes.
 */

public class SimpleDTUPaySteps {
    String cid, mid, customerAccountId;
    List<Payment> paymentList;
    SimpleDTUPay dtuPay = new SimpleDTUPay();
    Response response;
    UserRest customer, merchant;
    BankService bank = new BankServiceService().getBankServicePort();
    List<String> accountIds = new ArrayList<>();

    @Given("the customer {string} {string} with CPR {string} has a bank account with balance {int}")
    public void createCustomerAccount(String firstName, String lastName, String CPR, int balance) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCprNumber(CPR);
        try {
            customerAccountId = bank.createAccountWithBalance(user, BigDecimal.valueOf(balance));
            this.accountIds.add(customerAccountId);
            this.customer = new UserRest();
            this.customer.setFirstName(firstName);
            this.customer.setLastName(lastName);
            this.customer.setCprNumber(CPR);
            this.customer.setBankAccountId(customerAccountId);


        }
        catch (BankServiceException_Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Given("the customer {string} {string} with CPR {string} does not have a bank account")
    public void noCustomerAccount(String firstName, String lastName, String CPR) {
            this.customer = new UserRest();
            this.customer.setFirstName(firstName);
            this.customer.setLastName(lastName);
            this.customer.setCprNumber(CPR);
            this.customer.setBankAccountId("123456");
    }

    @When("that the customer is registered with DTU Pay")
    public void customerIsRegistered() {
        response = dtuPay.createUser(customer);

        if (response.getStatus() == 201) {
            String userId = dtuPay.getStringFromResponse(response);
            customer.setUserId(userId);
        }
    }

    @Then("the registration fails with {string}")
    public void itFails(String err) {
        assertEquals(response.readEntity(String.class), err);
    }

    @Then("the registration succeeds")
    public void itSucceeds() {
        assertEquals(response.getStatus(), 201);
    }

    @Given("the merchant {string} {string} with CPR {string} has a bank account with balance {int}")
    public void createMerchantAccount(String firstName, String lastName, String CPR, int balance) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCprNumber(CPR);
        try {
            customerAccountId = bank.createAccountWithBalance(user, BigDecimal.valueOf(balance));
            accountIds.add(customerAccountId);
            merchant = new UserRest();
            merchant.setFirstName(firstName);
            merchant.setLastName(lastName);
            merchant.setCprNumber(CPR);
            merchant.setBankAccountId(customerAccountId);
        }
        catch (BankServiceException_Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @When("that the merchant is registered with DTU Pay")
    public void merchantIsRegistered() {
        response = dtuPay.createUser(merchant);
        String userId = dtuPay.getStringFromResponse(response);
        merchant.setUserId(userId);
    }

    @Then("the balance of the customer at the bank is {int} kr")
    public void customerBalance(int balance){
        response = dtuPay.getBalance(customer.getUserId());
        if (response.getStatus() == 201) {
            String bal = dtuPay.getStringFromResponse(response);
            int actualBalance = Integer.parseInt(bal);
            assertEquals(balance, actualBalance);
        }
        else {
            System.out.println(response.getStatus());
        }
    }

    @Then("the balance of the merchant at the bank is {int} kr")
    public void merchantBalance(int balance){
        response = dtuPay.getBalance(merchant.getUserId());
        if (response.getStatus() == 201) {
            String bal = dtuPay.getStringFromResponse(response);
            int actualBalance = Integer.parseInt(bal);
            assertEquals(balance, actualBalance);
        }
        else {
            System.out.println(response.getStatus());
        }
    }

    @When("the merchant initiates a payment for {int} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) {
        response = dtuPay.conductPayment( customer, merchant, amount, "");
    }
    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        assertEquals(201, response.getStatus() );
    }

    @Then("the payment is unsuccessful with error {string}")
    public void thePaymentIsUnsuccessful(String err) {
        assertEquals( err, response.readEntity(String.class) );
    }

    @Given("a successful payment of {int} kr from customer {string} to merchant {string}")
    public void aSuccessPayment(Integer amount, UserRest cid, UserRest mid) {
        response = dtuPay.conductPayment(cid,mid,amount, "");
        System.out.print(response.getStatus());
        assertEquals(201, response.getStatus());
    }

    @When("the manager asks for a list of payments")
    public void theManagerAsksForList() {
        paymentList = dtuPay.getPayments();
    }

    @Then("the list contains a payments where customer {string} paid {int} kr to merchant {string}")
    public void checkPayment(String cid, Integer amount, String mid) {
        boolean result = false;
        for (Payment payment: paymentList){
            if (payment.getAmount() == amount && payment.getCid().equals(cid) && payment.getMid().equals(mid)){
                result = true;
                break;
            }
        }
        assertTrue(result);
    }

    @Then("the payment is not successful")
    public void checkValidity() {
        assertEquals( 400, response.getStatus() );
    }

    @Then("an error message is returned saying {string}")
    public void checkErrorMessage(String error) {
        assertEquals(response.readEntity(String.class), error);
    }

    @After
    public void closeResponse(){
        dtuPay.closeResponse(response);
    }


    @After
    public void removeAccounts() {
        for (String accountId : accountIds) {
            try {
                bank.retireAccount(accountId);
            } catch (BankServiceException_Exception e) {
                //TODO: handle exception
            }
        }
    }


}