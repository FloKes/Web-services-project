package behaviourTests.steps;

import behaviourTests.DtuApiService;
import behaviourTests.dtos.AccountDTO;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;



/* Hint:
 * The step classes do not do the HTTP requests themselves.
 * Instead, the tests use the class HelloService, which encapsulates the
 * HTTP requests. This abstractions help to write easier and more understandable
 * test classes.
 */

public class AccountSteps {
    DtuApiService service = new DtuApiService();
    private AccountDTO account1;
    private CompletableFuture<AccountDTO> result1 = new CompletableFuture<>();
    private AccountDTO accountReceived1;
    private AccountDTO account2;
    private CompletableFuture<AccountDTO> result2 = new CompletableFuture<>();
    private AccountDTO accountReceived2;
    private Response response1;
    private Response response2;

    @Given("person with name {string} {string} with cpr {string}, bank accountId {string}")
    public void personWithNameWithCprBankAccountId(String firstName, String lastName, String cpr, String bankAccountId) {
        account1 = new AccountDTO();
        account1.setFirstname(firstName);
        account1.setLastname(lastName);
        account1.setCpr(cpr);
        account1.setBankAccount(bankAccountId);
        assertNull(account1.getAccountId());
    }

    @When("the user is being registered")
    public void theUserIsBeingRegistered() {
        response1 = service.requestAccount(account1);
    }

    @Then("the user is registered")
    public void theUserIsRegistered() {
        if (response1.getStatus()==201){
            var accountDTO = response1.readEntity(AccountDTO.class);
            result1.complete(accountDTO);
        }
        else {
            response1.close();
            fail("Response code: " + response1.getStatus());
        }
        accountReceived1 = result1.join();
    }

    @Then("an {string} error message is returned")
    public void anErrorMessageIsReturned(String errorMsg) {
        var receivedError = response1.readEntity(String.class);
        assertEquals(errorMsg, receivedError);
    }

    @Then("has a non empty id")
    public void hasANonEmptyId() {
        assertNotNull(accountReceived1.getAccountId());
    }

    @Given("second person with name {string} {string} with cpr {string}, bank accountId {string}")
    public void secondPersonWithNameWithCprBankAccountId(String firstName, String lastName, String cpr, String bankAccountId) {
        account2 = new AccountDTO();
        account2.setFirstname(firstName);
        account2.setLastname(lastName);
        account2.setCpr(cpr);
        account2.setBankAccount(bankAccountId);
        assertNull(account2.getAccountId());
    }

    @When("the two accounts are registered at the same time")
    public void theTwoAccountsAreRegisteredAtTheSameTime() {
        var thread1 = new Thread(() -> {
            var response = service.requestAccount(account1);
            if (response.getStatus()==201){
                var accountDTO = response.readEntity(AccountDTO.class);
                result1.complete(accountDTO);
            }
            else {
                response.close();
                fail("Response code: " + response.getStatus());
                return;
            }
        });
        var thread2 = new Thread(() -> {
            var response = service.requestAccount(account2);
            if (response.getStatus()==201){
                var accountDTO = response.readEntity(AccountDTO.class);
                result2.complete(accountDTO);
            }
            else {
                response.close();
                fail("Response code: " + response.getStatus());
            }
        });
        thread1.start();
        thread2.start();
    }

    @Then("the first account has a non empty id")
    public void theFirstAccountHasANonEmptyId() {
        var accountDTO = result1.join();
        assertEquals(account1.getFirstname(), accountDTO.getFirstname());
        assertNotNull(accountDTO.getAccountId());
    }

    @Then("the second account has a non empty id different from the first student")
    public void theSecondAccountHasANonEmptyIdDifferentFromTheFirstStudent() {
        var accountDTO1 = result1.join();
        var accountDTO2 = result2.join();
        assertEquals(account2.getFirstname(), accountDTO2.getFirstname());
        assertNotNull(accountDTO2.getAccountId());
        System.out.println("Accoutn 1 id: " + accountDTO1.getAccountId());
        System.out.println("Accoutn 2 id: " + accountDTO2.getAccountId());
        assertNotEquals(accountDTO1.getAccountId(), accountDTO2.getAccountId());
    }

    @After
    public void closeClient() {
        service.closeClient();
    }
}