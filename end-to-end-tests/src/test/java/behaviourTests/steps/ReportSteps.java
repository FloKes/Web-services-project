package behaviourTests.steps;

import behaviourTests.DtuApiService;
import behaviourTests.domain.Payment;
import behaviourTests.dtos.AccountDTO;
import behaviourTests.dtos.PaymentDTO;
import behaviourTests.dtos.ReportDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ReportSteps {

    BankService bankService;
    DtuApiService dtuPayService;
    List<String> bankAccountIds = new ArrayList<>();
    AccountDTO merchantAccountDTO; // AccountDTO received for registration
    AccountDTO customerAccountDTO; // AccountDTO received for registration
    private CompletableFuture<AccountDTO> merchantAccountWithId = new CompletableFuture<>();
    private CompletableFuture<AccountDTO> customerAccountId = new CompletableFuture<>();
    private CompletableFuture<TokenIdDTO> customerToken = new CompletableFuture<>();
    private CompletableFuture<ReportDTO> customerReport = new CompletableFuture<>();
    List<String> accountIds = new ArrayList<>();
    AccountDTO merchantAccount;
    AccountDTO customerAccount;
    private List<String> tokens;

    public ReportSteps() {
        bankService = new BankServiceService().getBankServicePort();
        dtuPayService = new DtuApiService();
    }

    @Given("A merchant {string} {string} with CPR {string} has a bank account with balance {int} and is registered to DTU pay")
    public void aMerchantWithCPRHasABankAccountWithBalanceAndIsRegisteredToDTUPay(String firstName, String lastName, String cpr, Integer balance) {
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

            String bankAccountId = bankService.createAccountWithBalance(user, BigDecimal.valueOf(balance));
            bankAccountIds.add(bankAccountId);
            merchantAccountDTO.setBankAccount(bankAccountId);




        } catch (BankServiceException_Exception e) {
            System.out.println(e.getMessage());
        }
        var response = dtuPayService.requestAccount(merchantAccountDTO);
        if (response.getStatus()==201){
            var accountDTO = response.readEntity(AccountDTO.class);
            merchantAccountWithId.complete(accountDTO);
        }
        else {
            response.close();
            merchantAccountWithId.cancel(true);
            fail("Response code: " + response.getStatus());
        }
        merchantAccount = merchantAccountWithId.join();
    }
    @Given("a customer {string} {string} with CPR {string} has a bank account with balance {int} and is registered to DTU pay")
    public void aCustomerWithCPRHasABankAccountWithBalanceAndIsRegisteredToDTUPay(String firstName, String lastName, String cpr, Integer balance) {
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

            String bankAccountId = bankService.createAccountWithBalance(user, BigDecimal.valueOf(balance));
            bankAccountIds.add(bankAccountId);
            customerAccountDTO.setBankAccount(bankAccountId);
            var response = dtuPayService.requestAccount(customerAccountDTO);
            if (response.getStatus()==201){
                var accountDTO = response.readEntity(AccountDTO.class);
                customerAccountId.complete(accountDTO);
            }
            else {
                response.close();
                customerAccountId.cancel(true);
                fail("Response code: " + response.getStatus());
            }
            customerAccount = customerAccountId.join();
        } catch (BankServiceException_Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Given("the customer has requested tokens")
    public void theCustomerHasRequestedTokens() {
        customerToken.complete(dtuPayService.requestToken(customerAccount.getAccountId()));
        var tokenIdDTOReceived = customerToken.join();
        tokens = tokenIdDTOReceived.getTokenIdList();

    }
    @Given("one successful payment of {int} kr from customer to merchant has happened")
    public void oneSuccessfulPaymentFromCustomerToMerchantHasHappened(Integer amount) {
        PaymentDTO paymentDTO = new PaymentDTO();
        if (tokens.size() > 0){
            paymentDTO.setCustomerToken(tokens.get(0));
            tokens.remove(0);
        }
        else paymentDTO.setCustomerToken("No tokens");
        paymentDTO.setMerchantId(merchantAccount.getAccountId());
        paymentDTO.setAmount(BigDecimal.valueOf(amount));
        Response paymentResponse = dtuPayService.requestPayment(paymentDTO);
        assertEquals(201, paymentResponse.getStatus());
        paymentResponse.close();
    }
    @When("the customer request a report of the payments")
    public void theCustomerRequestAReportOfThePayments() {
        var thread1 = new Thread(() -> {
            customerReport.complete(dtuPayService.requestCustomerReport(customerAccount.getAccountId()));
        });
        thread1.start();
    }
    @Then("the customer receives a report with {int} payment")
    public void theCustomerReceivesAReportWithPayment(Integer numberOfPayments) {
        var customerReportDTUReceived = customerReport.join();
        List<Payment> report = customerReportDTUReceived.getReportList();
        assertEquals(numberOfPayments, report.size());
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
}
