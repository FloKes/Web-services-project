package dtuPayApi.service.services;

import dtuPayApi.service.Token;
import messaging.Event;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;

public class TokenService {

    private MessageQueue queue;
    private CompletableFuture<Token> providedToken;

    public TokenService(MessageQueue q) {
        queue = q;
        queue.addHandler("TokenProvided", this::handleTokenProvided);
    }

    public Token requestToken(String customerId) {
        providedToken = new CompletableFuture<>();
        Event event = new Event("TokenRequested", new Object[] { customerId });
        queue.publish(event);
        return providedToken.join();
    }

    public void handleTokenProvided(Event e) {
        var token = e.getArgument(0, Token.class);
        providedToken.complete(token);
    }
}

