package account.service;

import account.service.domain.Account;

import java.util.HashMap;

public class AccountManager {
    private static AccountManager accountManager = null;
    HashMap<String, Account> accounts;

    private AccountManager() {
        accounts = new HashMap<String,Account>();
    }

    public static AccountManager getInstance() {
        if (accountManager == null)
            accountManager = new AccountManager();
        return accountManager;
    }

    public String createAccount(Account account){
        account.setAccountId(String.valueOf(accounts.size()));
        accounts.put(account.getAccountId(), account);
        return account.getAccountId();
    }


    public Account getAccount(String id) {
        return accounts.get(id);
    }
}
