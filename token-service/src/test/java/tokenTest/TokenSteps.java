package tokenTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import token.service.TokenService;
import domain.Token;

import java.util.HashMap;

public class TokenSteps {
    String customerID;
    String customerID2;
    String tokenID;
    HashMap tokenList;

    TokenService tokenService = new TokenService();

    @Given("The customerID is {string}")
    public void the_customer_id_is(String customerID) {
        this.customerID = customerID;
    }

    @Given("The customerID2 is {string}")
    public void the_customer_id2_is(String customerID2) {
        this.customerID2 = customerID2;
    }

    @When("the token is created")
    public void the_token_is_created() {
        this.tokenID = tokenService.createToken(customerID);
    }

    @Then("tokenID is {string}")
    public void token_id_is(String expectedTokenID) {
        Assert.assertEquals(expectedTokenID, this.tokenID);
    }

    @When("the token is added to tokenList")
    public void the_token_is_added() {
        this.tokenID = tokenService.createToken(customerID);
    }

    @When("the tokens are added to tokenList")
    public void the_tokens_are_added_to_token_list() {
        this.tokenID = tokenService.createToken(customerID);
        this.tokenID = tokenService.createToken(customerID2);
    }

    @Then("saved tokenID is {string}")
    public void saved_token_id_is(String expectedTokenID) {
        int size = tokenService.getTokenList().size();
        Assert.assertEquals(1, size);
    }

    @Then("tokenList size is {int}")
    public void token_list_size_is(Integer size) {
        Integer listSize = tokenService.getTokenList().size();
        Assert.assertEquals(size, listSize);
    }



}
