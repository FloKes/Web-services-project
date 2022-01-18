package token.service;

import DTOs.TokenIdDTO;
import domain.Token;
import messaging.Event;
import messaging.MessageQueue;

import static java.util.stream.Collectors.groupingBy;
import java.util.*;
import java.util.stream.Collectors;

public class TokenService {
    MessageQueue queue;
    TokenRepository repository;
    public static final String TOKEN_CHECK_REQUESTED = "TokenCheckRequested";
    public static final String TOKEN_CREATION_REQUESTED = "TokenRequested";
    public static final String TOKEN_PROVIDED = "TokenProvided";
    public static final String TOKEN_CHECK_PROVIDED = "TokenCheckProvided";
    private Map<String, Token> tokenList;
    private List<String> tempTokenIdList;

    public TokenService(MessageQueue q, TokenRepository repository) {
        this.queue = q;
        this.repository = repository;
        this.queue.addHandler(TOKEN_CREATION_REQUESTED, this::handleTokenCreationRequested);
        this.queue.addHandler(TOKEN_CHECK_REQUESTED, this::handleTokenCheckRequested);
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

    public void handleTokenCheckRequested(Event ev) {
        var tokenID = ev.getArgument(0, String.class);
//        System.out.println(tokenID);
        var checkValue = checkToken(tokenID);
        //TODO modify event name
        Event event = new Event(TOKEN_CHECK_PROVIDED, new Object[] { checkValue });
        queue.publish(event);
    }
}
