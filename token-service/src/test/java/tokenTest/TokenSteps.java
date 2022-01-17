package tokenTest;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import token.service.TokenService;
import domain.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TokenSteps {
    String customerID;
    String customerID2;
    String tokenID;
    List<String > tokenIDList;
    boolean valid;


    TokenService tokenService = TokenService.getInstance();

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
        Assert.assertNotNull(this.tokenID);
        tokenIDList.add( tokenID );
    }

    @Then("tokenID is valid")
    public void token_id_is() {
        Assert.assertNotNull(this.tokenID);
        Assert.assertEquals("5e6050e9-319e-42ec-bc32-132f567452ba".length(), this.tokenID.length());
    }

    @When("the token is added to tokenList")
    public void the_token_is_added() {
        this.tokenID = tokenService.createToken(customerID);
    }

    @When("the tokens are added to tokenList")
    public void the_tokens_are_added_to_token_list() {
        this.tokenID = tokenService.createToken(customerID);
        tokenIDList.add( this.tokenID);
        this.tokenID = tokenService.createToken(customerID2);
        tokenIDList.add( this.tokenID);
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

    @After
    public void deleteUserIDsFromDTUPay(){
        for ( String ID : tokenIDList){
            tokenService.deleteToken( ID );
        }

    }

    @Before
    public void initList(){
        tokenIDList = new ArrayList<>();
        System.out.println(tokenIDList.size() );
    }


    @When("his token is being checked")
    public void his_token_is_being_checked() {
        this.valid = tokenService.checkToken(this.tokenID);
    }

    @Then("the validation is successful")
    public void the_result_is_true() {
        Assert.assertEquals(true, this.valid);
    }

    @When("his token is being deleted")
    public void his_token_is_being_deleted() {
        tokenService.deleteToken(this.tokenID);
    }

    @Then("the token is deleted")
    public void the_token_is_deleted() {
        Assert.assertEquals(false, tokenService.checkToken(this.tokenID));
    }

    @Given("he has {int} tokens")
    public void he_has_tokens(Integer numberOfTokens) {

    }

    @Then("the message is {string}")
    public void the_message_is(String message) {

    }



}
