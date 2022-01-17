package account.service;

import messaging.Event;
import messaging.MessageQueue;

public class AccountService {
    AccountManager accountManager = AccountManager.getInstance();
    MessageQueue queue;

    public AccountService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("AccountRequested", this::handleAccountRequested);
        this.queue.addHandler("BankAccountRequested", this::handleGetBankAccountRequested);
    }

    public void handleAccountRequested(Event ev) {
        var account = ev.getArgument(0, Account.class);
        System.out.println(account);
        String id = accountManager.createAccount(account);
        account.setAccountId(id);
        Event event = new Event("AccountProvided", new Object[] { account });
        queue.publish(event);
    }

    public void handleGetBankAccountRequested(Event ev) {
        var paymentId = ev.getArgument(0, String.class);
        var customerId = ev.getArgument(1, String.class);
        var merchantId = ev.getArgument(2, String.class);
        Account customerAccount = accountManager.getAccount(customerId);
        Account merchantAccount = accountManager.getAccount(merchantId);
        Event event = new Event("BankAccountProvided", new Object[] {paymentId, customerAccount.getBankAccount(), merchantAccount.getBankAccount() });
        queue.publish(event);
    }
}



