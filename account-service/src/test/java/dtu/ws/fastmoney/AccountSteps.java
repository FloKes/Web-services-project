package dtu.ws.fastmoney;

import account.service.Account;
import account.service.AccountService;
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
    Account checkAccount;
    Account customerAccount;
    Account merchantAccount;
    Account merchantCheckAccount;

    public AccountSteps(){
    }

    @When("a {string} event for an account is received")
    public void anEventForAnAccountIsReceived(String eventName) {
        account = new Account();
        account.setFirstname("Josephine");
        account.setLastname("Mellin");
        account.setCpr("010100-1234");
        account.setBankAccount("1568");
        assertNull(account.getAccountId());
        accountService.handleAccountRequested(new Event(eventName,new Object[] {account}));
    }

    @Then("the {string} event is sent")
    public void theEventIsSent(String eventName) {
        checkAccount = new Account();
        checkAccount.setFirstname("Josephine");
        checkAccount.setLastname("Mellin");
        checkAccount.setCpr("010100-1234");
        checkAccount.setBankAccount("1568");
        checkAccount.setAccountId("123");
        var event = new Event(eventName, new Object[] {checkAccount});
        verify(queue).publish(event);
    }

    @Then("the account gets an account id")
    public void theAccountGetsId() {
        assertNotNull(checkAccount.getAccountId());
    }


    @When("a {string} for an customer account is received")
    public void anEventForAnCustomerAccountIsReceived(String eventName) {
        customerAccount = new Account();
        customerAccount.setFirstname("Josephine");
        customerAccount.setLastname("Mellin");
        customerAccount.setCpr("010100-1234");
        customerAccount.setBankAccount("1568");
        assertNull(customerAccount.getAccountId());
        accountService.handleAccountRequested(new Event(eventName,new Object[] {customerAccount}));
    }


    @When("a {string} for an merchant account is received")
    public void anEventForAnMerchantAccountIsReceived(String eventName) {
        merchantAccount = new Account();
        merchantAccount.setFirstname("Josephine");
        merchantAccount.setLastname("Mellin");
        merchantAccount.setCpr("010100-1234");
        merchantAccount.setBankAccount("1568");
        assertNull(merchantAccount.getAccountId());
        accountService.handleAccountRequested(new Event(eventName,new Object[] {merchantAccount}));
    }

    @Then("the {string} event is sent")
    public void theEventIsSentForMercahantAccount(String eventName) {
        merchantCheckAccount = new Account();
        merchantCheckAccount.setFirstname("Josephine");
        merchantCheckAccount.setLastname("Mellin");
        merchantCheckAccount.setCpr("010100-1234");
        merchantCheckAccount.setBankAccount("1568");
        merchantCheckAccount.setAccountId("123");
        var event = new Event(eventName, new Object[] {merchantCheckAccount});
        verify(queue).publish(event);
    }


    @When("a {string} event for getting an bank account is received")
    public void anEventForGettingAnAccountIsReceived(String eventName) {
        //String accountId = "123";
        String payment = "145";
        String customerId = "1";
        String merchantId = "2";
        accountService.handleAccountRequested(new Event(eventName,new Object[] {payment, customerId, merchantId}));
    }



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
        queue.addHandler("GetAccountProvided", this::handleGetBankAccountRequested);
    }

    public void handleGetBankAccountRequested(Event e) {
        var account = e.getArgument(0, Account.class);
        assertNotNull(account);
    }
}