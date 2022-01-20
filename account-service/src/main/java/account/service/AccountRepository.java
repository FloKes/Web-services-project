package account.service;

import account.service.domain.Account;

import java.util.HashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class AccountRepository {
    HashMap<String, Account> accounts;

    public AccountRepository() {
        accounts = new HashMap<String,Account>();
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

    public Account deleteAccount(String id){
        return accounts.remove(id);
    }
    public boolean checkAccountExists(String id){
        return accounts.containsKey(id);
    }

}