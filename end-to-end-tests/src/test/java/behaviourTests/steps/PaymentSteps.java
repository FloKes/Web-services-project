package behaviourTests.steps;

import behaviourTests.DtuApiService;
import behaviourTests.dtos.AccountDTO;
import behaviourTests.dtos.PaymentDTO;
import behaviourTests.dtos.TokenIdDTO;
import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentSteps {
    BankService bankService;
    DtuApiService dtuPayService;
    List<String> bankAccountIds = new ArrayList<>();
    AccountDTO merchantAccountDTO; // AccountDTO received for registration
    AccountDTO customerAccountDTO; // AccountDTO received for registration
    private CompletableFuture<AccountDTO> merchantAccountCompletableFuture = new CompletableFuture<>();
    private CompletableFuture<AccountDTO> customerAccountCompletableFuture = new CompletableFuture<>();
    private CompletableFuture<TokenIdDTO> customerToken = new CompletableFuture<>();
    AccountDTO receivedMerchantAccountDTO;
    AccountDTO receivedCustomerAccountDTO;
    private List<String> tokens;
    private Response paymentResponse;
    private String accountId;
    List<String> accountIds = new ArrayList<>();

    public PaymentSteps() {
        bankService = new BankServiceService().getBankServicePort();
        dtuPayService = new DtuApiService();
    }

    @Given("merchant with name {string} {string} with CPR {string} has a bank account with {int} kr")
    public void merchantHasBankAccount(String firstName, String lastName, String cpr, Integer amount) {
        merchantAccountDTO = new AccountDTO();
        merchantAccountDTO.setFirstname(firstName);
        merchantAccountDTO.setLastname(lastName);
        merchantAccountDTO.setCpr(cpr);

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCprNumber(cpr);

        try {
            List<AccountInfo> ids = bankService.getAccounts();
            for (var id : ids) {
                if (id.getUser().getCprNumber().equals(cpr)) {
                    bankService.retireAccount(id.getAccountId());
                }
            }

            String bankAccountId = bankService.createAccountWithBalance(user, BigDecimal.valueOf(amount));
            bankAccountIds.add(bankAccountId);
            merchantAccountDTO.setBankAccount(bankAccountId);
        } catch (BankServiceException_Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Given("customer with name {string} {string} with CPR {string} has a bank account with {int} kr")
    public void customerHasBankAccount(String firstName, String lastName, String cpr, Integer amount) {
        customerAccountDTO = new AccountDTO();
        customerAccountDTO.setFirstname(firstName);
        customerAccountDTO.setLastname(lastName);
        customerAccountDTO.setCpr(cpr);

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCprNumber(cpr);

        try {
            List<AccountInfo> ids = bankService.getAccounts();
            for (var id : ids) {
                if (id.getUser().getCprNumber().equals(cpr)) {
                    bankService.retireAccount(id.getAccountId());
                }
            }

            String bankAccountId = bankService.createAccountWithBalance(user, BigDecimal.valueOf(amount));
            bankAccountIds.add(bankAccountId);
            customerAccountDTO.setBankAccount(bankAccountId);
        } catch (BankServiceException_Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Given("customer with name {string} {string} with CPR {string} has registered with wrong bank account")
    public void customerWithNameWithCPRHasRegisteredWithWrongBankAccount(String firstName, String lastName, String cpr) {
        customerAccountDTO = new AccountDTO();
        customerAccountDTO.setFirstname(firstName);
        customerAccountDTO.setLastname(lastName);
        customerAccountDTO.setCpr(cpr);
        customerAccountDTO.setAccountId("0142");
    }

    @Given("merchant with name {string} {string} with CPR {string} has registered with wrong bank account")
    public void merchantWithNameWithCPRHasRegisteredWithWrongBankAccount(String firstName, String lastName, String cpr) {
        merchantAccountDTO = new AccountDTO();
        merchantAccountDTO.setFirstname(firstName);
        merchantAccountDTO.setLastname(lastName);
        merchantAccountDTO.setCpr(cpr);
        merchantAccountDTO.setAccountId("01424");
    }

    @Given("merchant with name {string} {string} with CPR {string} is not registered with DTUPay")
    public void merchantWithNameWithCPRHasIsNotRegisteredWithDTUPay(String firstName, String lastName, String cpr) {
        merchantAccountDTO = new AccountDTO();
        merchantAccountDTO.setFirstname(firstName);
        merchantAccountDTO.setLastname(lastName);
        merchantAccountDTO.setCpr(cpr);
        merchantAccountDTO.setAccountId("01424");
        receivedMerchantAccountDTO = merchantAccountDTO;
    }
    @When("the customer registers")
    public void theCustomerRegisters() {
        var thread = new Thread(() -> {
            var response = dtuPayService.registerCustomerAccount(customerAccountDTO);
            if (response.getStatus()==201){
                var accountDTO = response.readEntity(AccountDTO.class);
                customerAccountCompletableFuture.complete(accountDTO);
            }
            else {
                response.close();
                customerAccountCompletableFuture.cancel(true);
                fail("Response code: " + response.getStatus());
            }
        });
        thread.start();
        receivedCustomerAccountDTO = customerAccountCompletableFuture.join();
        accountIds.add(receivedCustomerAccountDTO.getAccountId());
    }

    @When("the two accounts are registering at the same time")
    public void theTwoAccountsAreRegisteringAtTheSameTime() {
        var thread1 = new Thread(() -> {
            var response = dtuPayService.registerMerchantAccount(merchantAccountDTO);
            if (response.getStatus()==201){
                var accountDTO = response.readEntity(AccountDTO.class);
                merchantAccountCompletableFuture.complete(accountDTO);
            }
            else {
                response.close();
                merchantAccountCompletableFuture.cancel(true);
                fail("Response code: " + response.getStatus());
            }
        });
        var thread2 = new Thread(() -> {
            var response = dtuPayService.registerCustomerAccount(customerAccountDTO);
            if (response.getStatus()==201){
                var accountDTO = response.readEntity(AccountDTO.class);
                customerAccountCompletableFuture.complete(accountDTO);
            }
            else {
                response.close();
                customerAccountCompletableFuture.cancel(true);
                fail("Response code: " + response.getStatus());
            }
        });
        thread1.start();
        thread2.start();
    }

    @Then("the customer and merchant has different id")
    public void theMerchantHasANonEmptyId() {
        receivedMerchantAccountDTO = merchantAccountCompletableFuture.join();
        receivedCustomerAccountDTO = customerAccountCompletableFuture.join();
        assertEquals(customerAccountDTO.getFirstname(), receivedCustomerAccountDTO.getFirstname());
        assertEquals(merchantAccountDTO.getFirstname(), receivedMerchantAccountDTO.getFirstname());
        assertNotNull(receivedCustomerAccountDTO.getAccountId());
        assertNotNull(receivedMerchantAccountDTO.getAccountId());
        accountIds.add(receivedCustomerAccountDTO.getAccountId());
        accountIds.add(receivedMerchantAccountDTO.getAccountId());
        System.out.println("customer id: " + receivedCustomerAccountDTO.getAccountId());
        System.out.println("merchant id: " + receivedMerchantAccountDTO.getAccountId());
        assertNotEquals(receivedCustomerAccountDTO.getAccountId(), receivedMerchantAccountDTO.getAccountId());
    }

    @When("the customer has no tokens")
    public void theCustomerHasNoToken() {
        tokens = new ArrayList<>();
        assertEquals(0, tokens.size());
    }

    @When("the customer asks for a token")
    public void theCustomerAsksForAToken() {
        var thread1 = new Thread(() -> {
            customerToken.complete(dtuPayService.requestToken(receivedCustomerAccountDTO.getAccountId(), 6));
        });
        thread1.start();
    }

    @Then("the customer receives {int} tokens")
    public void theCustomerReceives6Tokens(Integer numberOfTokens) {
        var tokenIdDTOReceived = customerToken.join();
        tokens = tokenIdDTOReceived.getTokenIdList();
        assertEquals(numberOfTokens, tokens.size());
    }

    @When("the merchant {string} {string} initializes a payment with the customer {string} {string} of {int} kr to the DTUPay")
    public void paymentInitialization(String merchantFirstName, String merchantLastName, String customerFirstName, String customerLastName, Integer amount) {
        PaymentDTO paymentDTO = new PaymentDTO();
        if (tokens.size() > 0){
            paymentDTO.setCustomerToken(tokens.get(0));
            tokens.remove(0);
        }
        else paymentDTO.setCustomerToken("No tokens");
        paymentDTO.setMerchantId(receivedMerchantAccountDTO.getAccountId());
        paymentDTO.setAmount(BigDecimal.valueOf(amount));
        paymentResponse = dtuPayService.requestPayment(paymentDTO);
    }

    @When("the merchant initializes a payment with the customer of {int} kr to DTUPay")
    public void theMerchantInitializesAPaymentWithTheCustomerOfKrToDTUPay(Integer amount) {
        PaymentDTO paymentDTO = new PaymentDTO();
        if (tokens.size() > 0){
            paymentDTO.setCustomerToken(tokens.get(0));
            tokens.remove(0);
        }
        else paymentDTO.setCustomerToken("No tokens");
        paymentDTO.setMerchantId(receivedMerchantAccountDTO.getAccountId());
        paymentDTO.setAmount(BigDecimal.valueOf(amount));
        paymentResponse = dtuPayService.requestPayment(paymentDTO);
    }

    @Then("the customer has {int} kr in the bank")
    public void theCustomerHasKrInTheBank(Integer amount) throws BankServiceException_Exception {
        var balance = bankService.getAccount(customerAccountDTO.getBankAccount()).getBalance().intValue();
        assertEquals(amount, balance);
    }
    @Then("the merchant has {int} in bank")
    public void theMerchantBank(Integer amount) throws BankServiceException_Exception {
        var balance = bankService.getAccount(merchantAccountDTO.getBankAccount()).getBalance().intValue();
        assertEquals(amount, balance);
    }

    @When("the customer {string} {string} has invalid tokens")
    public void theCustomerHasInvalidTokens(String string, String string2) {
        tokens = new ArrayList<String>();
        tokens.add("This is invalid");
    }

    @Then("the payment is unsuccessful")
    public void thePaymentIsUnsuccessful() {
        assertEquals(400, paymentResponse.getStatus());
        paymentResponse.close();
    }

    @Then("the payment is unsuccessful with error {string}")
    public void thePaymentIsUnsuccessfulWithError(String errorDescription) {
        assertEquals(400, paymentResponse.getStatus());
        var errorDescriptionReceived = paymentResponse.readEntity(String.class);
        assertEquals(errorDescription, errorDescriptionReceived);
        paymentResponse.close();
    }

    @Then("the payment is successful")
    public void paymentSuccess() {
        assertEquals(201, paymentResponse.getStatus());
        paymentResponse.close();
    }

    @After
    public void removeAccounts() {
        for (String bankAccountId : bankAccountIds) {
            try {
                bankService.retireAccount(bankAccountId);
            } catch (BankServiceException_Exception e) {
                //TODO: handle exception
            }
        }
        for (var accountId : accountIds) {
            dtuPayService.deleteCustomerAccount(accountId);
        }
    }

//    @After
//    public void closeClient() {
//        dtuPayService.closeClient();
//    }
}
