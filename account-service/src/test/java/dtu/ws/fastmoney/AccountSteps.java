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

    public AccountSteps(){

    }
/*
    @Given("the customer {string} {string} with CPR {string} has a bank account with bank account {string}")
    public void FirstStepsRegistration(String firstname, String lastname, String cpr, String bankAccount) {
        account.setFirstname(firstname);
        account.setLastname(lastname);
        account.setCpr(cpr);
        account.setBankAccount(bankAccount);
        throw new io.cucumber.java.PendingException();
    }*/


    @When("a {string} event for an account is received")
    public void aEventForAStudentIsReceived(String eventName) {
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
}