package behaviourTests.steps;

import behaviourTests.DtuApiService;
import behaviourTests.dtos.AccountDTO;
import behaviourTests.dtos.PaymentDTO;
import behaviourTests.dtos.TokenIdDTO;
import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentSteps {
    BankService bankService;
    DtuApiService dtuPayService;
    List<String> accountIds = new ArrayList<>();
    AccountDTO merchantAccountDTO; // AccountDTO received for registration
    AccountDTO customerAccountDTO; // AccountDTO received for registration
    private CompletableFuture<AccountDTO> merchantAccountWithId = new CompletableFuture<>();
    private CompletableFuture<AccountDTO> customerAccountId = new CompletableFuture<>();
    private CompletableFuture<TokenIdDTO> customerToken = new CompletableFuture<>();
    AccountDTO merchantAccount;
    AccountDTO customerAccount;
    private List<String> tokens;
    private Response paymentResponse;

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
                if (id.getUser().getFirstName().equals(firstName)) {
                    bankService.retireAccount(id.getAccountId());
                }
            }

            String bankAccountId = bankService.createAccountWithBalance(user, BigDecimal.valueOf(amount));
            accountIds.add(bankAccountId);
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
                if (id.getUser().getFirstName().equals(firstName)) {
                    bankService.retireAccount(id.getAccountId());
                }
            }

            String bankAccountId = bankService.createAccountWithBalance(user, BigDecimal.valueOf(amount));
            accountIds.add(bankAccountId);
            customerAccountDTO.setBankAccount(bankAccountId);
        } catch (BankServiceException_Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @When("the two accounts are registering at the same time")
    public void theTwoAccountsAreRegisteringAtTheSameTime() {
        var thread1 = new Thread(() -> {
            merchantAccountWithId.complete(dtuPayService.requestAccount(merchantAccountDTO));
        });
        var thread2 = new Thread(() -> {
            customerAccountId.complete(dtuPayService.requestAccount(customerAccountDTO));
        });
        thread1.start();
        thread2.start();
    }

    @Then("the customer and merchant has different id")
    public void theMerchantHasANonEmptyId() {
        merchantAccount = merchantAccountWithId.join();
        customerAccount = customerAccountId.join();
        assertEquals(customerAccountDTO.getFirstname(), customerAccount.getFirstname());
        assertEquals(merchantAccountDTO.getFirstname(), merchantAccount.getFirstname());
        assertNotNull(customerAccount.getAccountId());
        assertNotNull(merchantAccount.getAccountId());
        System.out.println("customer id: " + customerAccount.getAccountId());
        System.out.println("merchant id: " + merchantAccount.getAccountId());
        assertNotEquals(customerAccount.getAccountId(), merchantAccount.getAccountId());
    }

    @When("the customer {string} {string} has no tokens")
    public void theCustomerHasNoToken(String firstName, String lastName) {
        tokens = new ArrayList<>();
        assertEquals(0, tokens.size());
    }

    @When("the customer {string} {string} asks for a token")
    public void theCustomerAsksForAToken(String firstName, String lastName) {
        var thread1 = new Thread(() -> {
            customerToken.complete(dtuPayService.requestToken(customerAccount.getAccountId()));
        });
        thread1.start();
    }

    @Then("the customer {string} {string} receives {int} tokens")
    public void theCustomerReceives6Tokens(String firstName, String lastName, Integer numberOfTokens) {
        var tokenIdDTOReceived = customerToken.join();
        tokens = tokenIdDTOReceived.getTokenIdList();
        assertEquals(numberOfTokens, tokenIdDTOReceived.getTokenIdList().size());
    }

    @When("the merchant {string} {string} initializes a payment with the customer {string} {string} of {int} kr to the DTUPay")
    public void paymentInitialization(String merchantFirstName, String merchantLastName, String customerFirstName, String customerLastName, Integer amount) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setCustomerToken(tokens.get(0));
        paymentDTO.setMerchantId(merchantAccount.getAccountId());
        paymentDTO.setAmount(BigDecimal.valueOf(amount));
        paymentResponse = dtuPayService.requestPayment(paymentDTO);
    }

    @Then("the payment is successful")
    public void paymentSuccess() {
        assertEquals(201, paymentResponse.getStatus());
    }

    @After
    public void removeAccounts() {
        for (String accountId : accountIds) {
            try {
                bankService.retireAccount(accountId);
            } catch (BankServiceException_Exception e) {
                //TODO: handle exception
            }
        }
    }
}
