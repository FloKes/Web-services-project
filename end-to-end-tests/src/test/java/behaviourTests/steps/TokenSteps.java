package behaviourTests.steps;

import behaviourTests.DtuApiService;
import behaviourTests.dtos.AccountDTO;
import behaviourTests.dtos.TokenIdDTO;
import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;



/* Hint:
 * The step classes do not do the HTTP requests themselves.
 * Instead, the tests use the class HelloService, which encapsulates the
 * HTTP requests. This abstractions help to write easier and more understandable
 * test classes.
 */

public class TokenSteps {
    DtuApiService service = new DtuApiService();
    TokenIdDTO tokenIdDTO;
    private CompletableFuture<TokenIdDTO> result1 = new CompletableFuture<>();
    private CompletableFuture<TokenIdDTO> result2 = new CompletableFuture<>();
    private CompletableFuture<AccountDTO> resultAccountDto = new CompletableFuture<>();

    // private TokenIdDTO tokenIdDTOReceived;
    private String customerId;
    private List<String> tokens;
    private List<String> accountIds = new ArrayList<>();
    private String accountId;


    @Given("the customer with id {string} has {int} tokens")
    public void theCustomerWithIdHasTokens(String customerId, Integer tokenCount) {
        this.customerId = customerId;
        assertEquals(tokenCount, tokens.size());
    }

    @When("the customer asks for {int} tokens")
    public void theCustomerAsksForAToken(int amount) {
        result1.complete(service.requestToken(accountId, amount));
    }

    @When("the customer asks again for {int} tokens")
    public void theCustomerAsksAgainForAToken(int amount) {
        result2.complete(service.requestToken(accountId, amount));
    }

    @Then("the customer receives {int} tokenss")
    public void theCustomerReceivesTokens(Integer numberOfTokens) {
        var tokenIdDTOReceived = result1.join();
        tokens = tokenIdDTOReceived.getTokenIdList();
        assertEquals(numberOfTokens, tokenIdDTOReceived.getTokenIdList().size());
    }


    @Then("the customer receives {int} tokens response")
    public void theCustomerReceivesTokensResponse(Integer numberOfTokens) {
        var tokenIdDTOReceived = result2.join();
        assertEquals(numberOfTokens, tokenIdDTOReceived.getTokenIdList().size());
    }

    @Given("person with name {string} {string} with cpr {string}, bank accountId {string} is registered")
    public void personWithNameWithCprBankAccountIdIsRegistered(String firstName, String lastName, String cpr, String bankAccountId) {
        var account1 = new AccountDTO();
        account1.setFirstname(firstName);
        account1.setLastname(lastName);
        account1.setCpr(cpr);
        account1.setBankAccount(bankAccountId);
        assertNull(account1.getAccountId());
        var response = service.registerCustomerAccount(account1);
        var accountDTO = response.readEntity(AccountDTO.class);
        assertNotNull(accountDTO.getAccountId());
        accountId = accountDTO.getAccountId();
        accountIds.add(accountId);
    }



    @When("account is deleted")
    public void accountIsDeleted() {
        var response = service.deleteCustomerAccount(accountId);
        assertEquals(204, response.getStatus());
        accountIds.remove(accountId);
    }
    @Then("a no account response is received")
    public void theThereAreNoTokensForTheAccount() {
    }

    @Given("person with id {string} is not registered")
    public void personWithIdIsNotRegistered(String customerId) {
        this.customerId = customerId;
    }

    @After
    public void deleteAccounts() {
        for (var accountId : accountIds) {
            service.deleteCustomerAccount(accountId);
        }
    }

    @After
    public void closeClient() {
        service.closeClient();
    }
}
