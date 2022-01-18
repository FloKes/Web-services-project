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
    String tokenID;
    List<String> tokenIDList;
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
        System.out.println(this.tokenID);
        Assert.assertNotNull(this.tokenID);
        Assert.assertEquals("5e6050e9-319e-42ec-bc32-132f567452ba".length(), this.tokenID.length());
    }

    @After
    public void deleteUserIDsFromDTUPay(){
        for ( String ID : tokenIDList){
            try{
                tokenService.deleteToken(ID);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    @Before
    public void initList(){
        tokenIDList = new ArrayList<>();
        System.out.println("Initial list size: " + tokenIDList.size());
    }

    @When("his token is being checked")
    public void his_token_is_being_checked() {
        this.valid = tokenService.checkToken(this.tokenID);
    }

    @Then("the validation is {string}")
    public void the_result_is_true(String success) {
        String result;
        if (this.valid) result = "successful";
        else result = "unsuccessful";
        Assert.assertEquals(success, result);
    }

    @When("his token is being deleted")
    public void his_token_is_being_deleted() {
        try{
            tokenService.deleteToken(this.tokenID);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            this.error = e;
        }
    }

    @Then("the token is deleted")
    public void the_token_is_deleted() {
        Assert.assertEquals(false, tokenService.checkToken(this.tokenID));
        if (this.error != null) {
            Assert.assertEquals("Token not found", this.error.getMessage());
        }
    }

    @Given("he has {int} tokens already")
    public void he_has_tokens(Integer numberOfTokens) {
        try{
            List<String> tokens = tokenService.createToken(this.customerID);
            for (int i = 0; i < (6 - numberOfTokens); i++){
                tokenService.deleteToken(tokens.get(i));
            }
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
