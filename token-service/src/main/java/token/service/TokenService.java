package token.service;

import domain.Token;
import messaging.Event;
import messaging.MessageQueue;

import java.util.HashMap;

public class TokenService {
    MessageQueue queue;
    private static TokenService instance = new TokenService();

    private HashMap<String,Token> tokenList;

    public static TokenService getInstance() {return instance;}

    private TokenService(){
        this.tokenList = new HashMap<>();
    }
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




    //*************************************************************************************
    //This is new
    public String createToken(String userID){
        Token token = new Token(userID);
        tokenList.put(token.getTokenID(), token);
        return token.getTokenID();
    }

    public boolean checkToken(Token provided, Token stored){
        //Also inform error here?
        return true;
    }

    public void deleteToken(Token token){
    }

    public HashMap<String, Token> getTokenList() {
        return tokenList;
    }
}
