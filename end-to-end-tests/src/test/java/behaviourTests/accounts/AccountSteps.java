package behaviourTests.accounts;

import behaviourTests.DtuApiService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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
    private AccountDTO account;
    private CompletableFuture<AccountDTO> result = new CompletableFuture<>();
    private AccountDTO accountReceived;

    @Given("person with name {string} {string} with cpr {string}, bank accountId {string}")
    public void personWithNameWithCprBankAccountId(String firstName, String lastName, String cpr, String bankAccountId) {
        account = new AccountDTO();
        account.setFirstname(firstName);
        account.setLastname(lastName);
        account.setCpr(cpr);
        account.setBankAccount(bankAccountId);
        assertNull(account.getAccountId());
    }
    @When("the user is being registered")
    public void theUserIsBeingRegistered() {
        result.complete(service.requestAccount(account));
    }
    @Then("the user is registered")
    public void theUserIsRegistered() {
        accountReceived = result.join();
    }
    @Then("has a non empty id")
    public void hasANonEmptyId() {
        assertNotNull(accountReceived.getAccountId());
    }
}