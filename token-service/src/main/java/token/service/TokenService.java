package token.service;

import DTOs.TokenIdDTO;
import DTOs.TokenValidationDTO;
import domain.Token;
import messaging.Event;
import messaging.MessageQueue;

import static java.util.stream.Collectors.groupingBy;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TokenService {
    private MessageQueue queue;
    private TokenRepository repository;

    // Event received
    public static final String TOKEN_VALIDATION_REQUESTED = "TokenValidationRequested";
    public static final String TOKEN_CREATION_REQUESTED = "TokenCreationRequested";
    public static final String ACCOUNT_DELETED = "AccountDeleted";
    public static final String ACCOUNT_CHECK_REQUESTED = "AccountCheckRequested";

    // Event sent
    public static final String TOKEN_PROVIDED = "TokenProvided";
    public static final String TOKEN_VALIDATED = "TokenValidated";
    public static final String TOKEN_INVALID = "TokenInvalid";
    public static final String ACCOUNT_CHECK_RESULT_PROVIDED = "AccountCheckResultProvided";

    private Map<String, Token> tokenList;
    private List<String> tempTokenIdList;
    private Map<CorrelationId, CompletableFuture<Boolean>> pendingAccountChecks; // payments waiting for token validation, <correlationId, payment>
    private CompletableFuture<Boolean> accountCheckResult;

    public TokenService(MessageQueue q, TokenRepository repository) {
        this.queue = q;
        this.repository = repository;
        this.queue.addHandler(TOKEN_CREATION_REQUESTED, this::handleTokenCreationRequested);
        this.queue.addHandler(TOKEN_VALIDATION_REQUESTED, this::handleTokenValidRequested);
        this.queue.addHandler(ACCOUNT_DELETED, this::handleAccountDeleted);
        this.queue.addHandler(ACCOUNT_CHECK_RESULT_PROVIDED, this::handleAccountCheckResultProvided);
        this.tokenList = new HashMap<>();
        this.pendingAccountChecks = new ConcurrentHashMap<>();
    }


    public TokenIdDTO createToken(String customerId) throws Exception {
        System.out.println("Token service customerId: " + customerId);
        var tokenIdList = repository.getTokenIdList(customerId);
        TokenIdDTO tokenIdDTO = new TokenIdDTO();
        tokenIdDTO.setTokenIdList(tokenIdList);
        System.out.println("Service list size: " + tokenIdList.size());
        return tokenIdDTO;
    }

    public TokenIdDTO createArbitraryToken(String customerId, int amountOfTokens) throws Exception {
        System.out.println("Token service customerId: " + customerId);
        var tokenIdList = repository.getArbitraryAmuntOfTokenIdList(customerId, amountOfTokens);
        TokenIdDTO tokenIdDTO = new TokenIdDTO();
        tokenIdDTO.setTokenIdList(tokenIdList);
        System.out.println("Service list size: " + tokenIdList.size());
        return tokenIdDTO;
    }

    public boolean checkToken(String providedTokenID){
        return repository.checkToken(providedTokenID);
    }

    public void deleteToken(String tokenID) throws Exception {
        repository.deleteToken(tokenID);
    }

    private String getCustomerIdByTokenId(String tokenId) throws Exception{
        return repository.getCustomerIdByTokenId(tokenId);
    }

    public Map<String, Token> getTokenList() {
        return tokenList;
    }

    public List<String> getTempTokenIdList(){
        return tempTokenIdList;
    }


//   //TODO make it unable to create new tokens if no such user is registered
    public void handleTokenCreationRequested(Event ev) {
        var customerId = ev.getArgument(0, String.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);

        var tokenAmount = 0;
        try {
            tokenAmount= ev.getArgument(2, int.class );
        } catch (Exception e) {
            System.out.println("no arbitrary token amount argument");
        }




        CompletableFuture<Boolean> accountCheckResult = new CompletableFuture<>();
        var correlationIdCheckAccount = CorrelationId.randomId();

        pendingAccountChecks.put(correlationIdCheckAccount, accountCheckResult);
        Event eventAccountCheck = new Event(ACCOUNT_CHECK_REQUESTED, new Object[]{customerId, correlationIdCheckAccount});

        queue.publish(eventAccountCheck);

        var result = accountCheckResult.join();


        TokenIdDTO tokenIdDTO = new TokenIdDTO();
        tokenIdDTO.setTokenIdList(new ArrayList<>());

        System.out.println("Customer id: " + customerId + ", exists: " + result);
        if (!result){
            tokenIdDTO.setTokenIdList(new ArrayList<>());
            Event eventTokenProvided = new Event(TOKEN_PROVIDED, new Object[] { tokenIdDTO, correlationId });
            queue.publish(eventTokenProvided);
            return;
        }

        try {
            if ( tokenAmount == 0 ) { // equivalent to default value 6
                tokenIdDTO = createToken(customerId);
            }else {
                tokenIdDTO = createArbitraryToken(customerId, tokenAmount);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        Event eventTokenProvided = new Event(TOKEN_PROVIDED, new Object[] { tokenIdDTO, correlationId });
        queue.publish(eventTokenProvided);
    }

    public void handleTokenValidRequested(Event ev) {
        var tokenValidationDTO = ev.getArgument(0, TokenValidationDTO.class);
        var tokenID = tokenValidationDTO.getCustomerToken();
        var correlationId = ev.getArgument(1, CorrelationId.class);
        var checkValue = checkToken(tokenID);
        if (checkValue) {
            try {
                tokenValidationDTO.setCustomerId(this.getCustomerIdByTokenId(tokenID)); // set the customerId and return it to payment service
                deleteToken(tokenID); // retire the token
                Event event = new Event(TOKEN_VALIDATED, new Object[] {tokenValidationDTO, correlationId});
                queue.publish(event);
            } catch (Exception e) {
                tokenValidationDTO.setErrorMessage(e.getMessage());
                Event event = new Event(TOKEN_INVALID, new Object[] {tokenValidationDTO, correlationId});
                queue.publish(event);
            }
        }
        else {
            tokenValidationDTO.setErrorMessage("TokenInvalid");
            Event event = new Event(TOKEN_INVALID, new Object[] {tokenValidationDTO, correlationId});
            queue.publish(event);
        }
    }

    /**
     *
     * @author Florian
     */
    public void handleAccountCheckResultProvided(Event ev){
        var result = ev.getArgument(0, Boolean.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        pendingAccountChecks.get(correlationId).complete(result);
    }

    /**
     * @author Florian
     */
    public void handleAccountDeleted(Event ev){
        var userId = ev.getArgument(0, String.class);
        repository.deleteUserTokens(userId);
    }

    /**
     * @author Florian
     */
    public int getNumberOfTokensForUser(String userId){
        return repository.getNumberOfTokensForUser(userId);
    }
}
