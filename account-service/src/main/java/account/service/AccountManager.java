package account.service;

import account.service.domain.Account;

import java.util.HashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

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

    public String createAccount(Account account) throws Exception {
//        var existingAccount = accounts.values().stream()
//                .collect( groupingBy( Token::getUserID, Collectors.counting() ) ).get(customerId);
        var existingAccount = accounts.values().stream().filter(acc -> acc.getCpr().equals(account.getCpr())).findAny();
        if (!existingAccount.isEmpty()) {
            throw new Exception("Account already exists");
        }
        account.setAccountId(String.valueOf(accounts.size()));
        accounts.put(account.getAccountId(), account);
        return account.getAccountId();
    }


    public Account getAccount(String id) {
        return accounts.get(id);
    }
}
