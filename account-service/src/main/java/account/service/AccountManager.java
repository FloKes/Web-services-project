package account.service;

import java.util.HashMap;
import java.util.UUID;

public class AccountManager {
    private static AccountManager accountManager = null;
    HashMap<String,Account> accounts;

    private AccountManager() {
        accounts = new HashMap<String,Account>();
    }

    public static AccountManager getInstance() {
        if (accountManager == null)
            accountManager = new AccountManager();
        return accountManager;
    }

    public String createAccount(Account account){
        UUID id = UUID.randomUUID();
        String accountKey = id.toString();
        //account.setAccountId(accountKey);
        account.setAccountId("123");
        accounts.put(accountKey, account);
        return accountKey;
    }


    public Account getAccount(String id) {
        return accounts.get(id);
    }
}
