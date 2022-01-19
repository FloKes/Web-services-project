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

    @When("a {string} event for a customer with name {string}, surname {string}, cpr {string}, bank account {string} is received")
    public void anEventForAnCustomerAccountIsReceived(String eventName, String name, String surname,
                                                      String cpr, String bankAcc) throws Exception {
        customerAccount = new Account();
        customerAccount.setFirstname(name);
        customerAccount.setLastname(surname);
        customerAccount.setCpr(cpr);
        customerAccount.setBankAccount(bankAcc);
        assertNull(customerAccount.getAccountId());
        AccountDTO accountDTO = new AccountDTO();
        Mapper.mapAccountToDTO(customerAccount, accountDTO);
        correlationId = CorrelationId.randomId();
//        System.out.println(accountDTO);
        accountService.handleAccountRequested(new Event(eventName,new Object[] {accountDTO, correlationId}));
    }

    @Then("the account gets an account with id {string}")
    public void theAccountGetsId(String id) {
        customerAccount.setAccountId(id);
        assertNotNull(customerAccount.getAccountId());
    }

    @Then("the {string} event is sent")
    public void theEventIsSent(String eventName) {
        var accountDTO = new AccountDTO();
        Mapper.mapAccountToDTO(customerAccount, accountDTO);
        var event = new Event(eventName, new Object[] {accountDTO, correlationId});
        verify(queue).publish(event);
    }

    @Then("the account is returned")
    public void theAccountIsReturned() {
    }

    @Then("the {string} event is sent with error message {string}")
    public void the_event_is_sent_with_error_message(String eventType, String errorMsg) {
        AccountDTO accountDTO = new AccountDTO();
        Mapper.mapAccountToDTO(customerAccount, accountDTO);
//        accountDTO.setErrorMessage(errorMsg);
        System.out.println(accountDTO);
        System.out.println(correlationId);
        accountDTO.setErrorMessage(errorMsg);
        var event =  new Event(eventType, new Object[]{accountDTO, correlationId});
        verify(queue).publish(event);
    }
}