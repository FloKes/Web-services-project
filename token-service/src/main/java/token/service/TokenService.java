package token.service;

import messaging.Event;
import messaging.MessageQueue;

public class TokenService {
    MessageQueue queue;

    public TokenService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("TokenRequested", this::handleTokenRequested);
    }

    public void handleTokenRequested(Event ev) {
        var customerId = ev.getArgument(0, String.class);
        System.out.println(customerId);
        var token = new Token();
        token.setToken("4321");
        Event event = new Event("TokenProvided", new Object[] { token });
        queue.publish(event);
    }
}
