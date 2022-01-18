package behaviourTests;

import DTOs.TokenIdDTO;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import org.junit.Assert;
import token.service.CorrelationId;
import token.service.TokenRepository;
import token.service.TokenService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class TokenSteps {
    public static final String TOKEN_VALIDATION_REQUESTED = "TokenValidationRequested";
    public static final String TOKEN_CREATION_REQUESTED = "TokenCreationRequested";
    private CorrelationId correlationId;
    String customerID;
    String tokenID;
    List<String> tokenIDList;
    boolean valid;
    Exception error;

    private MessageQueue queue = mock(MessageQueue.class);
    private TokenRepository mockRepository = mock(TokenRepository.class);
    private TokenRepository repository = new TokenRepository();
    private TokenService service = new TokenService(queue, repository);
    private TokenService service2 = new TokenService(queue, mockRepository);

    @Given("The customerID is {string}")
    public void the_customer_id_is(String customerID) {
        this.customerID = customerID;
    }

    @When("the token is created")
    public void the_token_is_created() {
        try{
            var tokens = service.createToken(this.customerID);
            System.out.println("CustomerId: " + customerID + "\ntoken list size steps: " + tokens.getTokenIdList().size());
            this.tokenID = tokens.getTokenIdList().get(0);
            assertNotNull(this.tokenID);
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
        assertNotNull(this.tokenID);
        Assert.assertEquals("5e6050e9-319e-42ec-bc32-132f567452ba".length(), this.tokenID.length());
    }

    @After
    public void deleteUserIDsFromDTUPay(){
        for ( String ID : tokenIDList){
            try{
                service.deleteToken(ID);
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
        this.valid = service.checkToken(this.tokenID);
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
            service.deleteToken(this.tokenID);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            this.error = e;
        }
    }

    @Then("the token is deleted")
    public void the_token_is_deleted() {
        Assert.assertEquals(false, service.checkToken(this.tokenID));
        if (this.error != null) {
            Assert.assertEquals("Token not found", this.error.getMessage());
        }
    }

    @Given("he has {int} tokens already")
    public void he_has_tokens(Integer numberOfTokens) {
        try{
            List<String> tokens = service.createToken(this.customerID).getTokenIdList();
            for (int i = 0; i < (6 - numberOfTokens); i++){
                service.deleteToken(tokens.get(i));
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


    @When("a {string} event for a {string} is received")
    public void aEventForACustomerAccountIsReceived(String eventName, String customerId) throws Exception {
        List<String> mockList = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6"));
        correlationId = CorrelationId.randomId();
        when(mockRepository.getTokenIdList(customerId)).thenReturn(mockList);
        service2.handleTokenCreationRequested(new Event(eventName,new Object[] {customerId, correlationId}));
    }

    @Then("the token is created and its id is not null")
    public void theTokenIsCreatedAndItsIdIsNotNull() {
    }

    @Then("the {string} event is sent")
    public void theEventIsSent(String eventName) {
        List<String> expectedList = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6"));
        TokenIdDTO tokenIdDTO = new TokenIdDTO();
        tokenIdDTO.setTokenIdList(expectedList);
        var event = new Event(eventName, new Object[] {tokenIdDTO, correlationId});
        verify(queue).publish(event);
    }
}
