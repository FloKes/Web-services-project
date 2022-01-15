package account.service;

import messaging.Event;
import messaging.MessageQueue;

public class AccountService {
    AccountManager accountManager = AccountManager.getInstance();
    MessageQueue queue;

    public AccountService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("AccountRequested", this::handleAccountRequested);
        this.queue.addHandler("GetAccountRequested", this::handleGetAccountRequested);
    }

    public void handleAccountRequested(Event ev) {
        var account = ev.getArgument(0, Account.class);
        System.out.println(account);
        String id = accountManager.createAccount(account);
        account.setAccountId(id);
        Event event = new Event("AccountProvided", new Object[] { account });
        queue.publish(event);
    }

    public void handleGetAccountRequested(Event ev) {
        var accountId = ev.getArgument(0, String.class);
        Account account = accountManager.getAccount(accountId);
        Event event = new Event("GetAccountProvided", new Object[] { account });
        queue.publish(event);
    }
}



