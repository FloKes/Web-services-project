package token.service;

import domain.Token;
import messaging.Event;
import messaging.MessageQueue;

import static java.util.stream.Collectors.groupingBy;
import java.util.*;
import java.util.stream.Collectors;

public class TokenService {
    MessageQueue queue;
    private static TokenService instance = new TokenService();
    private Map<String, Token> tokenList;


    public static TokenService getInstance() {return instance;}
    private TokenService(){
        this.tokenList = new HashMap<>() {
        };
    }


    //*************************************************************************************
    //This is new

    //I think the client also has to do a digital signature on the token
    public List<String> createToken(String userID) throws Exception {
        List<String> tokens = new ArrayList<>();
        int requiredNumber = 0;
        Long numberOfTokens = tokenList.values().stream()
                .collect( groupingBy( Token::getUserID, Collectors.counting() ) ).get(userID);
        if  (numberOfTokens == null) requiredNumber = 6;
        else requiredNumber = 6 - Math.toIntExact(numberOfTokens);
        if( numberOfTokens == null || numberOfTokens < 2) {
            for (int i = 0; i < requiredNumber; i++){
                Token token = new Token(userID);
                tokenList.put( token.getTokenID(), token );
                tokens.add(token.getTokenID());
            }
            return tokens;
        }
        else {
            throw new Exception("Too many tokens");
            }
    }

    public boolean checkToken(String providedTokenID){
        //Check if digital signature is correct?
        return tokenList.containsKey(providedTokenID);
    }

    public void deleteToken(String tokenID){
        if ( checkToken( tokenID ) ){
            tokenList.remove(tokenID);
            System.out.println("deleted: " + tokenID);
        }else {
            System.out.println(tokenID + " is not an existing element");
        }

    }

    public Map<String, Token> getTokenList() {
        return tokenList;
    }






    //********************************************************************************
    //This is working
    public TokenService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("TokenRequested", this::handleTokenRequested);
    }

    public void handleTokenRequested(Event ev) {
        var customerId = ev.getArgument(0, String.class);
        System.out.println(customerId);
        var token = new Token();
        token.setTokenID("4321");
        Event event = new Event("TokenProvided", new Object[] { token });
        queue.publish(event);
    }
}
