package tokenTest;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
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
    Exception error;


    TokenService tokenService = TokenService.getInstance();

    @Given("The customerID is {string}")
    public void the_customer_id_is(String customerID) {
        this.customerID = customerID;
    }

    @When("the token is created")
    public void the_token_is_created() {
        try{
            List<String> tokens = tokenService.createToken(this.customerID);
            this.tokenID = tokens.get(0);
            Assert.assertNotNull(this.tokenID);
            tokenIDList.add( this.tokenID );
        }
        catch (Exception e){
            this.error = e;
            System.out.println(e.getMessage());
        }

    }

    @Then("tokenID is valid")
    public void token_id_is() {
        Assert.assertNotNull(this.tokenID);
        Assert.assertEquals("5e6050e9-319e-42ec-bc32-132f567452ba".length(), this.tokenID.length());
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

    @Given("he has {int} tokens already")
    public void he_has_tokens(Integer numberOfTokens) {
        for (int i = 0; i < numberOfTokens; i++)
            try{
                tokenService.createToken(this.customerID);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
    }

    @Then("the error message is {string}")
    public void the_message_is(String message) {
        Assert.assertEquals(message, error.getMessage());
    }



}
