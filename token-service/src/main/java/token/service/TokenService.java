package token.service;

import DTOs.TokenIdDTO;
import DTOs.TokenValidationDTO;
import domain.Token;
import messaging.Event;
import messaging.MessageQueue;

import static java.util.stream.Collectors.groupingBy;
import java.util.*;
import java.util.stream.Collectors;

public class TokenService {
    MessageQueue queue;
    TokenRepository repository;

    // Event received
    public static final String TOKEN_VALIDATION_REQUESTED = "TokenValidationRequested";
    public static final String TOKEN_CREATION_REQUESTED = "TokenRequested";

    // Event sent
    public static final String TOKEN_PROVIDED = "TokenProvided";
    public static final String TOKEN_VALIDATED = "TokenValidated";
    public static final String TOKEN_INVALID = "TokenInvalid";
    private Map<String, Token> tokenList;
    private List<String> tempTokenIdList;

    public TokenService(MessageQueue q, TokenRepository repository) {
        this.queue = q;
        this.repository = repository;
        this.queue.addHandler(TOKEN_CREATION_REQUESTED, this::handleTokenCreationRequested);
        this.queue.addHandler(TOKEN_VALIDATION_REQUESTED, this::handleTokenValidRequested);
        this.tokenList = new HashMap<>();
    }

    //*************************************************************************************
    //This is new

    //I think the client also has to do a digital signature on the token
    public TokenIdDTO createToken(String customerId) throws Exception {
        System.out.println("Token service customerId: " + customerId);
        var tokenIdList = repository.getTokenIdList(customerId);
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

    public void handleTokenCreationRequested(Event ev) {
        var customerId = ev.getArgument(0, String.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
//        System.out.println(customerId);
        TokenIdDTO tokenIdDTO = new TokenIdDTO();
        tokenIdDTO.setTokenIdList(new ArrayList<>());
        try {
            tokenIdDTO = createToken(customerId);
        }
        catch (Exception e){
            System.out.println(e);
        }
        Event event = new Event(TOKEN_PROVIDED, new Object[] { tokenIdDTO, correlationId });
        queue.publish(event);
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
}
