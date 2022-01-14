package account.service;

import messaging.Event;
import messaging.MessageQueue;

public class AccountService {
    MessageQueue queue;

    public AccountService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("AccountRequested", this::handleAccountRequested);
    }

    public void handleAccountRequested(Event ev) {
        var account = ev.getArgument(0, Account.class);
        System.out.println(account);
        account.setAccountId("123");
        Event event = new Event("AccountProvided", new Object[] { account });
        queue.publish(event);
    }
}



