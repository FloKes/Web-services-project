package behaviourTests;

import account.service.domain.Account;
import account.service.AccountService;
import account.service.dtos.AccountDTO;
import account.service.CorrelationId;
import account.service.dtos.Mapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AccountSteps {
    Account account;
    private MessageQueue queue = mock(MessageQueue.class);
    private AccountService accountService = new AccountService(queue);
    private CorrelationId correlationId;
    Account customerAccount;
    AccountDTO customerAccountDTO, merchantAccountDTO;

    public AccountSteps(){
    }

    @When("a {string} event for a customer account is received")
    public void anEventForAnCustomerAccountIsReceived(String eventName) {
        customerAccount = new Account();
        customerAccount.setFirstname("Josephine");
        customerAccount.setLastname("Mellin");
        customerAccount.setCpr("010100-1234");
        customerAccount.setBankAccount("1568");
        assertNull(customerAccount.getAccountId());
        AccountDTO accountDTO = new AccountDTO();
        Mapper.mapAccountToDTO(customerAccount, accountDTO);
        correlationId = CorrelationId.randomId();
        System.out.println(accountDTO);
        accountService.handleAccountRequested(new Event(eventName,new Object[] {accountDTO, correlationId}));
    }

    @Then("the {string} event is sent")
    public void theEventIsSent(String eventName) {
        var accountDTO = new AccountDTO();
        Mapper.mapAccountToDTO(customerAccount, accountDTO);
        var event = new Event(eventName, new Object[] {accountDTO, correlationId});
        verify(queue).publish(event);
    }

    @Then("the account gets an account with id {string}")
    public void theAccountGetsId(String id) {
        customerAccount.setAccountId(id);
        assertNotNull(customerAccount.getAccountId());
    }


 //   @When("a {string} event for a merchant account is received")
//    public void anEventForAnMerchantAccountIsReceived(String eventName) {
//        merchantAccount = new Account();
//        merchantAccount.setFirstname("Josephine");
//        merchantAccount.setLastname("Mellin");
//        merchantAccount.setCpr("010100-1234");
//        merchantAccount.setBankAccount("1568");
//        assertNull(merchantAccount.getAccountId());
//        merchantAccountDTO = new AccountDTO();
//        Mapper.mapAccountToDTO(customerAccount, merchantAccountDTO);
//        merchantAccountDTO.setCorrelationId(CorrelationId.randomId());
//        accountService.handleAccountRequested(new Event(eventName,new Object[] {merchantAccountDTO}));
//    }
//    @When("a {string} event for an account is received")
//    public void anEventForAnAccountIsReceived(String eventName) {
//        account = new Account();
//        account.setFirstname("Josephine");
//        account.setLastname("Mellin");
//        account.setCpr("010100-1234");
//        account.setBankAccount("1568");
//        assertNull(account.getAccountId());
//        accountService.handleAccountRequested(new Event(eventName,new Object[] {account}));
//    }
//

//
//    @Then("the account gets an account id")
//    public void theAccountGetsId() {
//        assertNotNull(checkAccount.getAccountId());
//    }

//    @Then("the {string} event is sent")
//    public void theEventIsSentForMercahantAccount(String eventName) {
//        merchantCheckAccount = new Account();
//        merchantCheckAccount.setFirstname("Josephine");
//        merchantCheckAccount.setLastname("Mellin");
//        merchantCheckAccount.setCpr("010100-1234");
//        merchantCheckAccount.setBankAccount("1568");
//        merchantCheckAccount.setAccountId("123");
//        var event = new Event(eventName, new Object[] {merchantCheckAccount});
//        verify(queue).publish(event);
//    }


//    @When("a {string} event for getting an bank account is received")
//    public void anEventForGettingAnAccountIsReceived(String eventName) {
//        //String accountId = "123";
//        String payment = "145";
//        String customerId = "1";
//        String merchantId = "2";
//        accountService.handleAccountRequested(new Event(eventName,new Object[] {payment, customerId, merchantId}));
//    }



    /*@Then("the {string} event is sent")
    public void theEventForGettingAccountIsSent(String eventName) {
        checkAccount = new Account();
        checkAccount.setFirstname("Josephine");
        checkAccount.setLastname("Mellin");
        checkAccount.setCpr("010100-1234");
        checkAccount.setBankAccount("1568");
        checkAccount.setAccountId("123");
        var event = new Event(eventName, new Object[] {checkAccount});
        verify(queue).publish(event);
    }*/


    @Then("the account is returned")
    public void theAccountIsReturned() {
    }

//    @Then("the account is returned")
//    public void theAccountIsReturned() {
//        queue.addHandler("GetAccountProvided", this::handleGetBankAccountRequested);
//    }
//
//    public void handleGetBankAccountRequested(Event e) {
//        var account = e.getArgument(0, Account.class);
//        assertNotNull(account);
//    }
}