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
   // private TokenIdDTO tokenIdDTOReceived;
    private String customerId;
    private List<String> tokens;


    @Given("the customer with id {string} has no tokens")
    public void theCustomerWithIdHasNoToken(String customerId) {
        this.customerId = customerId;
        tokens = new ArrayList<>();
        assertEquals(0,tokens.size());
    }

    @Given("the customer with id {string} has {int} tokens")
    public void theCustomerWithIdHasTokens(String customerId, Integer tokenCount) {
        this.customerId = customerId;
        assertEquals(tokenCount, tokens.size());
    }

    @When("the customer asks for a token")
    public void theCustomerAsksForAToken() {
        result1.complete(service.requestToken(customerId));
    }

    @When("the customer asks again for a token")
    public void theCustomerAsksAgainForAToken() {
        result2.complete(service.requestToken(customerId));
    }

    @Then("the customer receives {int} tokens")
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

    @After
    public void closeClient() {
        service.closeClient();
    }
}